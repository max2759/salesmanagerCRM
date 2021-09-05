package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompaniesDao;
import be.atc.salesmanagercrm.dao.impl.CompaniesDaoImpl;
import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.CompanyValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.TabChangeEvent;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "companiesBean")
@SessionScoped
public class CompaniesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 5861943957551000529L;

    @Getter
    @Setter
    private CompaniesDao companiesDao = new CompaniesDaoImpl();

    @Getter
    @Setter
    private CompaniesEntity companiesEntity;

    @Getter
    @Setter
    private List<CompaniesEntity> companiesEntityList;

    @Getter
    @Setter
    private List<CompaniesEntity> companiesEntitiesDisableList;

    @Getter
    @Setter
    private String sendType = "";

    @Getter
    private final LocalDateTime now = LocalDateTime.now();

    @Getter
    @Setter
    private CompaniesEntity selectedCompaniesEntity;

    @Getter
    @Setter
    private List<CompaniesEntity> companiesEntitiesFiltered;

    @Inject
    private AddressesBean addressesBean;
    @Inject
    private NotesBean notesBean;
    @Inject
    private TasksBean tasksBean;
    @Inject
    private VouchersBean vouchersBean;
    @Inject
    private TransactionsBean transactionsBean;
    @Inject
    private TransactionHistoriesBean transactionHistoriesBean;
    @Inject
    private VoucherHistoriesBean voucherHistoriesBean;

    @Inject
    private UsersBean usersBean;

    @Getter
    @Setter
    private Map<LocalDateTime, Object> listActivity;


    /**
     * this method is used in activity page
     */
    public void activityThread() {
        log.info("CompaniesBean : activityThread");
        this.listActivity = new TreeMap<>(Collections.reverseOrder());

        for (NotesEntity n : notesBean.getNotesEntities()) {
            ObjectActivity objectActivity = new ObjectActivity(n.getClass().getName(), n);
            listActivity.put(n.getCreationDate(), objectActivity);
        }

        for (TasksEntity t : tasksBean.getTasksEntities()) {
            ObjectActivity objectActivity = new ObjectActivity(t.getClass().getName(), t);
            listActivity.put(t.getCreationDate(), objectActivity);
        }

        List<TransactionsEntity> transactionsEntities = transactionsBean.findTransactionsEntityByCompaniesByIdCompanies(companiesEntity, usersBean.getUsersEntity());
        for (TransactionsEntity t : transactionsEntities) {
            ObjectActivity objectActivity = new ObjectActivity(t.getClass().getName(), t);
            listActivity.put(t.getCreationDate(), objectActivity);

            List<TransactionHistoriesEntity> transactionHistoriesEntities = transactionHistoriesBean.findAllByIdUserAndByIdTransaction(t.getId(), usersBean.getUsersEntity());
            for (TransactionHistoriesEntity tH : transactionHistoriesEntities) {
                ObjectActivity objectActivity1 = new ObjectActivity(tH.getClass().getName(), tH);
                listActivity.put(tH.getSaveDate(), objectActivity1);
            }
        }

        List<VouchersEntity> vouchersEntities = vouchersBean.findVouchersEntityByCompaniesByIdCompanies(companiesEntity, usersBean.getUsersEntity());
        for (VouchersEntity v : vouchersEntities) {
            ObjectActivity objectActivity = new ObjectActivity(v.getClass().getName(), v);
            listActivity.put(v.getCreationDate(), objectActivity);

            List<VoucherHistoriesEntity> voucherHistoriesEntities = voucherHistoriesBean.findAllByIdUserAndByIdVoucher(v.getId(), usersBean.getUsersEntity());
            for (VoucherHistoriesEntity vH : voucherHistoriesEntities) {
                ObjectActivity objectActivity1 = new ObjectActivity(vH.getClass().getName(), vH);
                listActivity.put(vH.getSaveDate(), objectActivity1);
            }
        }

        log.info("Liste : " + listActivity);
    }

    /**
     * public method that call save
     */
    public void saveCompany() {
        save(companiesEntity);
        createNewEntity();
        findAllActiveCompanies();
    }

    /**
     * Save Companies Entity
     *
     * @param companiesEntity CompaniesEntity
     */
    protected void save(CompaniesEntity companiesEntity) {

        companiesEntity.setRegisterDate(LocalDateTime.now());
        companiesEntity.setActive(true);

        companiesEntity.setUsersByIdUsers(usersBean.getUsersEntity());

        try {
            validateCompanies(companiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage facesMessage;
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(companiesEntity.getUsersByIdUsers());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        if (companiesEntity.getBranchActivitiesByIdBranchActivities() != null) {
            try {
                checkEntities.checkBranchActivities(companiesEntity.getBranchActivitiesByIdBranchActivities());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "branchActivities.error"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
        }

        if (companiesEntity.getCompanyTypesByIdCompanyTypes() != null) {
            try {
                checkEntities.checkCompanyTypes(companiesEntity.getCompanyTypesByIdCompanyTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyTypes.error"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            companiesDao.add(em, companiesEntity);
            tx.commit();
            log.info("Persist ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "company.saved"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * Method that call findActiveCompanies and fill companiesEntityList
     */
    public void findAllActiveCompanies() {
        loadListEntities("displayActiveCompany");
    }

    /**
     * method to know which entity to reload
     *
     * @param typeLoad String
     */
    public void loadListEntities(String typeLoad) {

        if (typeLoad.equalsIgnoreCase("displayActiveCompany")) {
            companiesEntityList = findActiveCompanies(usersBean.getUsersEntity());
        } else if (typeLoad.equalsIgnoreCase("displayDisableCompany")) {
            companiesEntitiesDisableList = findDisableCompanies(usersBean.getUsersEntity());
        }
    }

    /**
     * Load loadListEntities when Tab Change !
     *
     * @param event TabChangeEvent
     */
    public void onTabChange(TabChangeEvent event) {
        log.info("method : onTabChange()");
        log.info("event : " + event.getTab().getId());

        loadListEntities(event.getTab().getId());
    }

    /**
     * Auto Complete for companies in form
     *
     * @param query String
     * @return List Contacts Entities
     */
    public List<CompaniesEntity> completeCompaniesContains(String query) {
        String queryLowerCase = query.toLowerCase();

        List<CompaniesEntity> companiesEntitiesForm = findCompaniesEntityByIdUser(usersBean.getUsersEntity());

        return companiesEntitiesForm.stream().filter(t -> t.getLabel().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }

    /**
     * Sort Companies by group in form
     *
     * @param entityGroup CompaniesEntity
     * @return entity label
     */
    public char getCompaniesEntityGroup(CompaniesEntity entityGroup) {
        return entityGroup.getLabel().charAt(0);
    }


    /**
     * Find all Companies entities by userID
     *
     * @param usersEntity UsersEntity
     * @return List CompaniesEntities
     */
    protected List<CompaniesEntity> findAll(UsersEntity usersEntity) {

        log.info("Start method findAll");

        if (usersEntity == null) {
            log.error("User Entity is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<CompaniesEntity> companiesEntities = companiesDao.findAll(em, usersEntity.getId());

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find all active companies
     *
     * @param usersEntity UsersEntity
     * @return List Active CompaniesEntity
     */
    protected List<CompaniesEntity> findActiveCompanies(UsersEntity usersEntity) {

        log.info("Start method findActiveCompanies");

        FacesMessage facesMessage;

        if (usersEntity == null) {
            log.error("User Entity is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<CompaniesEntity> companiesEntities = companiesDao.findActiveCompany(em, usersEntity.getId());

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find all disable companies
     *
     * @param usersEntity UsersEntity
     * @return List disable CompaniesEntity
     */
    protected List<CompaniesEntity> findDisableCompanies(UsersEntity usersEntity) {

        log.info("Start method findDisableCompanies");

        FacesMessage facesMessage;

        if (usersEntity == null) {
            log.error("User Entity is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<CompaniesEntity> companiesEntities = companiesDao.findDisableCompany(em, usersEntity.getId());

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find Companies entities by id User
     *
     * @param usersEntity UsersEntity
     * @return List CompaniesEntity
     */
    public List<CompaniesEntity> findCompaniesEntityByIdUser(UsersEntity usersEntity) {

        if (usersEntity == null) {
            log.error("User Entity is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<CompaniesEntity> companiesEntities = companiesDao.findCompaniesEntityByIdUser(em, usersEntity.getId());

        em.clear();
        em.close();

        return companiesEntities;
    }


    /**
     * Find Company by ID
     *
     * @param id CompaniesEntity
     * @return Companies Entity
     */
    public CompaniesEntity findByIdCompanyAndByIdUser(int id, UsersEntity usersEntity) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Company ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return companiesDao.findByIdCompanyAndByIdUser(em, id, usersEntity.getId());
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune entreprise avec l ID " + id + " et l ID User " + usersEntity.getId() + " n a ete trouvee dans la BDD",
                    ErrorCodes.COMPANY_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    public List<CompaniesEntity> callFindByIdCompaniAndByIdUser(int id) {
        return findByIdCompaniAndByIdUser(id, usersBean.getUsersEntity());
    }

    /**
     * Return list of Companies by its id and idUser
     *
     * @param id          id Companies
     * @param usersEntity UsersEntity
     * @return CompaniesEntity
     */
    private List<CompaniesEntity> findByIdCompaniAndByIdUser(int id, UsersEntity usersEntity) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Company ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return companiesDao.findByIdCompaniAndByIdUser(em, id, usersEntity.getId());
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune entreprise avec l ID " + id + " et l ID User " + usersEntity.getId() + " n a ete trouvee dans la BDD",
                    ErrorCodes.COMPANY_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Create new instance for objects
     */
    public void createNewEntity() {
        log.info("method : createNewEntity()");
        companiesEntity = new CompaniesEntity();
    }

    /**
     * Validate Companies !
     *
     * @param entity CompaniesEntity
     */
    private void validateCompanies(CompaniesEntity entity) {
        List<String> errors = CompanyValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Company is not valid {}", entity);
            throw new InvalidEntityException("Les données de l'entreprise ne sont pas valide", ErrorCodes.COMPANY_NOT_VALID, errors);
        }
    }

    /**
     * Public method to display company
     */
    public void displayCompanyDetails() {
        displayOneCompany();

    }

    /**
     * Get company by it's ID
     */
    protected void displayOneCompany() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
        FacesMessage facesMessage;

        log.info("Début méthode displayOneCompany");
        log.info("Param : " + getParam("companyID"));

        if (getParam("companyID") != null) {
            int idCompany;

            try {
                idCompany = Integer.parseInt(getParam("companyID"));
            } catch (NumberFormatException exception) {
                log.info(exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "company.error"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                nav.performNavigation("/company.xhtml");
                return;
            }

            // TODO: faire une méthode findbyid dans bean
            try {
                companiesEntity = findById(idCompany);
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "company.error"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                nav.performNavigation("/company.xhtml");
                return;
            }
        }
        addressesBean.getAddressByIdCompany();
    }

    /**
     * Find Companies by id
     *
     * @param id Companies
     * @return CompaniesEntity
     */
    protected CompaniesEntity findById(int id) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Companies ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "company.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return companiesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune entreprise avec l ID " + id + " n a ete trouvee dans la BDD",
                    ErrorCodes.COMPANY_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Public method that call update company
     */
    public void updateCompanies() {
        update(companiesEntity);
    }

    /**
     * Update companies
     *
     * @param companiesEntity CompaniesEntity
     */
    protected void update(CompaniesEntity companiesEntity) {

        FacesMessage facesMessage;

        companiesEntity.setModificationDate(LocalDateTime.now());
        CheckEntities checkEntities = new CheckEntities();


        try {
            validateCompanies(companiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            checkEntities.checkUser(companiesEntity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        if (companiesEntity.getBranchActivitiesByIdBranchActivities() != null) {
            try {
                checkEntities.checkBranchActivities(companiesEntity.getBranchActivitiesByIdBranchActivities());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "branchActivities.error"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
        }

        if (companiesEntity.getCompanyTypesByIdCompanyTypes() != null) {
            try {
                checkEntities.checkCompanyTypes(companiesEntity.getCompanyTypesByIdCompanyTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyTypes.error"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
        }


        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            companiesDao.update(em, companiesEntity);
            addressesBean.getAddressesEntity().setCompaniesByIdCompanies(companiesEntity);
            addressesBean.updateEntity();
            tx.commit();
            log.info("Update ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "company.updated"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update échec");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }

    }

    /**
     * Public method for activeCompany
     */
    public void activateCompanyByIdCompany() {

        log.info("method : activateCompany()");
        log.info("Id de companies = " + getParam("companiesID"));

        activeCompany(Integer.parseInt(getParam("companiesID")));

        loadListEntities("displayDisableCompany");

    }

    /**
     * Re activate Company by its id
     *
     * @param id id Company
     */
    protected void activeCompany(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Companies ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        CompaniesEntity companiesEntityToActivate;
        EntityManager em = EMF.getEM();
        em.getTransaction();
        EntityTransaction tx = null;

        try {
            companiesEntityToActivate = companiesDao.findById(em, id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        companiesEntityToActivate.setActive(true);

        try {
            tx = em.getTransaction();
            tx.begin();
            companiesDao.update(em, companiesEntityToActivate);
            tx.commit();
            log.info("Company enable ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "company.activated"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Soft delete failed");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * Soft delete for company by id
     *
     * @param id id Company
     */
    protected void delete(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Companies ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        CompaniesEntity companiesEntityToDelete;
        EntityManager em = EMF.getEM();
        em.getTransaction();
        EntityTransaction tx = null;

        try {
            companiesEntityToDelete = companiesDao.findById(em, id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        companiesEntityToDelete.setActive(false);

        try {
            tx = em.getTransaction();
            tx.begin();
            companiesDao.update(em, companiesEntityToDelete);
            tx.commit();
            log.info("Soft delete ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "company.deleted"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Soft delete échec");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * Method for delete
     */
    public void deleteCompany() {
        log.info("method : deleteCompany()");
        log.info("Id de companies = " + getParam("companiesID"));

        delete(Integer.parseInt(getParam("companiesID")));

        loadListEntities("displayActiveCompany");
    }


    /**
     * Auto complete for CompaniesEntity
     *
     * @param search search
     * @return List of label Companies Entity
     */
    public List<CompaniesEntity> completeCompanies(String search) {
        String searchLowerCase = search.toLowerCase();

        List<CompaniesEntity> companiesEntitiesDropdown = findActiveCompanies(usersBean.getUsersEntity());

        return companiesEntitiesDropdown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());
    }
}
