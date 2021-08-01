package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompaniesDao;
import be.atc.salesmanagercrm.dao.impl.CompaniesDaoImpl;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.entities.CompanyTypesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.CompanyValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "companiesBean")
@ViewScoped
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
    private UsersEntity usersEntity = new UsersEntity();

    @Getter
    @Setter
    private List<CompaniesEntity> companiesEntityList;

    @Getter
    @Setter
    private String sendType = "";

    @Getter
    private final LocalDateTime now = LocalDateTime.now();

    @Inject
    private BranchActivitiesBean branchActivitiesBean;

    @Inject
    private CompanyTypesBean companyTypesBean;


    /**
     * public method that call save
     */
    public void saveCompany() {
        save(companiesEntity);

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
        usersEntity.setId(1);
        companiesEntity.setUsersByIdUsers(usersEntity);

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
     * Method that call findAll and fill companiesEntityList
     */
    public void findAllActiveCompanies() {

        //TODO: à modifier plus tard
        usersEntity.setId(1);
        companiesEntityList = findActiveCompanies(usersEntity.getId());
    }

    /**
     * Auto Complete form creation
     *
     * @param query String
     * @return List Contacts Entities
     */
    public List<CompaniesEntity> completeCompaniesContains(String query) {
        String queryLowerCase = query.toLowerCase();

        // TODO : à modifier
        List<CompaniesEntity> companiesEntitiesForm = findCompaniesEntityByIdUser(1);

        return companiesEntitiesForm.stream().filter(t -> t.getLabel().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }

    /**
     * Sort Contacts by group in form
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
     * @param idUser idUser
     * @return List CompaniesEntities
     */
    protected List<CompaniesEntity> findAll(int idUser) {

        log.info("Start method findAll");

        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<CompaniesEntity> companiesEntities = companiesDao.findAll(em, idUser);

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find all active companies
     *
     * @param idUser idUser
     * @return List Active CompaniesEntity
     */
    protected List<CompaniesEntity> findActiveCompanies(int idUser) {

        log.info("Start method findActiveCompanies");

        FacesMessage facesMessage;

        if (idUser == 0) {
            log.error("User ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<CompaniesEntity> companiesEntities = companiesDao.findActiveCompany(em, idUser);

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find Companies entities by id User
     *
     * @param idUser UsersEntity
     * @return List CompaniesEntity
     */
    public List<CompaniesEntity> findCompaniesEntityByIdUser(int idUser) {

        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<CompaniesEntity> companiesEntities = companiesDao.findCompaniesEntityByIdUser(em, idUser);

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
    public CompaniesEntity findByIdCompanyAndByIdUser(int id, int idUser) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Company ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return companiesDao.findByIdCompanyAndByIdUser(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune compagny avec l ID " + id + " et l ID User " + idUser + " n a ete trouvee dans la BDD",
                    ErrorCodes.CONTACT_NOT_FOUND
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
     * Method for checking sendType and open good modal
     */
    public void checkSendType() {
        sendType = getParam("sendType");
        log.info("type envoyé : " + sendType);
        if (sendType.equalsIgnoreCase("edit")) {
            companiesEntity = null;
            log.info("jobtitles : " + companiesEntity.getId());
        } else if (sendType.equalsIgnoreCase("add")) {
            companiesEntity = new CompaniesEntity();
        }
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
     * Auto complete for branchActivivites
     *
     * @param search String
     * @return list of BranchActivitiesEntity
     */
    public List<BranchActivitiesEntity> completeBranchActivities(String search) {

        String searchLowerCase = search.toLowerCase();

        List<BranchActivitiesEntity> companiesEntitiesDropDown = branchActivitiesBean.findBranchActvivitiesList();

        return companiesEntitiesDropDown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());

    }

    /**
     * Auto complete for CompanyTypes
     *
     * @param search String
     * @return list of CompanyTypesEntity
     */
    public List<CompanyTypesEntity> completeCompanyTypes(String search) {

        String searchLowerCase = search.toLowerCase();

        List<CompanyTypesEntity> companyTypesEntitiesDropDown = companyTypesBean.findCompanyTypesList();

        return companyTypesEntitiesDropDown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());

    }


    /**
     * Get company by it's ID
     */
    public void displayOneCompany() {

        FacesMessage facesMessage;

        log.info("Début méthode displayOneCompany");

        int idCompany = Integer.parseInt(getParam("companyID"));

        log.info("Id de la société : " + idCompany);

        if (idCompany == 0) {
            log.error("Company ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "company.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            throw new InvalidEntityException("L'Id de l'entreprise n'existe pas", ErrorCodes.COMPANY_NOT_VALID);
        }

        EntityManager em = EMF.getEM();

        companiesEntity = companiesDao.findById(em, idCompany);

    }


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
     * Delete company by id
     *
     * @param id id
     */
    protected void delete(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Task ID is null");
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
            log.info("Delete ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "company.deleted"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Delete échec");
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

        delete(Integer.parseInt(getParam("companiesID")));

        findAllActiveCompanies();
    }

}
