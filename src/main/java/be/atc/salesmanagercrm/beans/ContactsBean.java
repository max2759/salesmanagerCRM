package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.ContactsDao;
import be.atc.salesmanagercrm.dao.impl.ContactsDaoImpl;
import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.utils.ObjectActivity;
import be.atc.salesmanagercrm.validators.ContactValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.TabChangeEvent;

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
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "contactsBean")
@ViewScoped
public class ContactsBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 848519777793777451L;

    @Getter
    @Setter
    private ContactsEntity contactsEntity;

    @Getter
    @Setter
    private List<ContactsEntity> contactsEntityList;

    @Getter
    @Setter
    private UsersEntity usersEntity = new UsersEntity();

    @Getter
    @Setter
    private ContactsDao contactsDao = new ContactsDaoImpl();

    @Getter
    @Setter
    private ContactsEntity selectedContactsEntity;

    @Getter
    @Setter
    private List<ContactsEntity> contactEntitiesFiltered;
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
    private AddressesBean addressesBean;
    @Inject
    private CompaniesContactsBean companiesContactsBean;
    @Inject
    private UsersBean usersBean;

    @Getter
    @Setter
    private Map<LocalDateTime, Object> listActivity;


    /**
     * this method is used in activity page
     */
    public void activityThread() {
        log.info("ContactsBean : activityThread");

        this.listActivity = new TreeMap<>(Collections.reverseOrder());

        for (NotesEntity n : notesBean.getNotesEntities()) {
            ObjectActivity objectActivity = new ObjectActivity(n.getClass().getName(), n);
            listActivity.put(n.getCreationDate(), objectActivity);
        }

        for (TasksEntity t : tasksBean.getTasksEntities()) {
            ObjectActivity objectActivity = new ObjectActivity(t.getClass().getName(), t);
            listActivity.put(t.getCreationDate(), objectActivity);
        }

        List<TransactionsEntity> transactionsEntities = transactionsBean.findTransactionsEntityByContactsByIdContacts(contactsEntity.getId(), usersBean.getUsersEntity().getId());
        for (TransactionsEntity t : transactionsEntities) {
            ObjectActivity objectActivity = new ObjectActivity(t.getClass().getName(), t);
            listActivity.put(t.getCreationDate(), objectActivity);

            // TODO Modifier USER
            List<TransactionHistoriesEntity> transactionHistoriesEntities = transactionHistoriesBean.findAllByIdUserAndByIdTransaction(t.getId(), usersBean.getUsersEntity().getId());
            for (TransactionHistoriesEntity tH : transactionHistoriesEntities) {
                ObjectActivity objectActivity1 = new ObjectActivity(tH.getClass().getName(), tH);
                listActivity.put(tH.getSaveDate(), objectActivity1);
            }
        }

        List<VouchersEntity> vouchersEntities = vouchersBean.findVouchersEntityByContactsByIdContacts(contactsEntity.getId(), usersBean.getUsersEntity().getId());
        for (VouchersEntity v : vouchersEntities) {
            ObjectActivity objectActivity = new ObjectActivity(v.getClass().getName(), v);
            listActivity.put(v.getCreationDate(), objectActivity);

            // TODO Modifier USER
            List<VoucherHistoriesEntity> voucherHistoriesEntities = voucherHistoriesBean.findAllByIdUserAndByIdVoucher(v.getId(), usersBean.getUsersEntity().getId());
            for (VoucherHistoriesEntity vH : voucherHistoriesEntities) {
                ObjectActivity objectActivity1 = new ObjectActivity(vH.getClass().getName(), vH);
                listActivity.put(vH.getSaveDate(), objectActivity1);
            }
        }

        log.info("Liste : " + listActivity);
    }

    public void findAllActiveContacts() {
        loadListEntities("displayActiveContacts");
    }

    /**
     * Find Contacts entities by id User
     *
     * @param idUser UsersEntity
     * @return List ContactsEntity
     */
    public List<ContactsEntity> findContactsEntityByIdUser(int idUser) {

        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<ContactsEntity> contactsEntities = contactsDao.findContactsEntityByIdUser(em, idUser);

        em.clear();
        em.close();

        return contactsEntities;
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
     * method to know which entity to reload
     *
     * @param typeLoad String
     */
    public void loadListEntities(String typeLoad) {

        usersEntity.setId(1);

        if (typeLoad.equalsIgnoreCase("displayActiveContacts")) {
            contactsEntityList = findContactsEntityByIdUser(usersEntity.getId());
        } else if (typeLoad.equalsIgnoreCase("displayDisableContacts")) {
            contactsEntityList = findDisableContacts(usersEntity.getId());
        }
    }

    /**
     * Find all ContactTypes entities
     *
     * @return List of Contact Types
     */
    public List<ContactsEntity> findAll() {

        EntityManager em = EMF.getEM();

        List<ContactsEntity> contactsEntityList1 = contactsDao.findAll();

        em.clear();
        em.close();

        return contactsEntityList1;
    }

    /**
     * Find all disable Contacts
     *
     * @param idUser idUser
     * @return List disable ContactsEntity
     */
    protected List<ContactsEntity> findDisableContacts(int idUser) {

        log.info("Start method findDisableContacts");

        FacesMessage facesMessage;

        if (idUser == 0) {
            log.error("User ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<ContactsEntity> contactsEntities = contactsDao.findDisableContactsEntityByIdUser(em, idUser);

        em.clear();
        em.close();

        return contactsEntities;
    }

    /**
     * Find Contact by ID
     *
     * @param id ContactsEntity
     * @return Contacts Entity
     */
    public ContactsEntity findByIdContactAndByIdUser(int id, int idUser) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Contact ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
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
            return contactsDao.findByIdContactAndByIdUser(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun contact avec l ID " + id + " et l ID User " + idUser + " n a ete trouvee dans la BDD",
                    ErrorCodes.CONTACT_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * public method that call displayOneContact
     */
    public void getDisplayOneContact() {
        displayOneContact();
    }

    /**
     * display one contact by CompanyId
     */
    protected void displayOneContact() {

        FacesMessage facesMessage;

        log.info("Début méthode displayOneContact");
        log.info("Param : " + getParam("contactID"));

        int idContact;

        try {
            idContact = Integer.parseInt(getParam("contactID"));
        } catch (NumberFormatException exception) {
            log.info(exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "contact.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        try {
            contactsEntity = findById(idContact);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "contact.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        addressesBean.getAddressByIdContacts();
    }

    protected ContactsEntity findById(int id) {
        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Contact ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contact.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            throw new InvalidEntityException("L'Id du contact n'existe pas", ErrorCodes.CONTACT_NOT_VALID);
        }

        EntityManager em = EMF.getEM();

        try {
            return contactsDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun contact avec l ID " + id + " n'a été trouvé dans la DB",
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
        contactsEntity = new ContactsEntity();
    }

    /**
     * Public method that call save() method
     */
    public void addContact() {
        save(contactsEntity);
        findAllActiveContacts();
    }

    /**
     * Auto Complete form creation
     *
     * @param query String
     * @return List Contacts Entities
     */
    public List<ContactsEntity> completeContactsContains(String query) {
        String queryLowerCase = query.toLowerCase();

        // TODO : à modifier
        List<ContactsEntity> contactsEntitiesForm = findContactsEntityByIdUser(1);

        return contactsEntitiesForm.stream().filter(t -> (t.getFirstname().toLowerCase().contains(queryLowerCase)) || (t.getLastname().toLowerCase().contains(queryLowerCase))).collect(Collectors.toList());
    }

    /**
     * Sort Contacts by group in form
     *
     * @param entityGroup ContactsEntity
     * @return list of firstname from ContactsEntity
     */

    public char getContactsEntityGroup(ContactsEntity entityGroup) {
        return entityGroup.getFirstname().charAt(0);
    }

    /**
     * Save contact entity
     *
     * @param contactsEntity ContactsEntity
     */
    protected void save(ContactsEntity contactsEntity) {

        contactsEntity.setRegisterDate(LocalDateTime.now());
        contactsEntity.setActive(true);
        usersEntity.setId(1);
        contactsEntity.setUsersByIdUsers(usersEntity);

        try {
            validateContacts(contactsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage facesMessage;
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(contactsEntity.getUsersByIdUsers());
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

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            contactsDao.add(em, contactsEntity);
            tx.commit();
            log.info("Persist ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "contacts.save"), null);
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
     * Public method that call update
     */
    public void updateContact() {
        update(contactsEntity);
    }

    /**
     * Update contacts entity
     *
     * @param contactsEntity
     */
    protected void update(ContactsEntity contactsEntity) {

        FacesMessage facesMessage;

        CheckEntities checkEntities = new CheckEntities();


        try {
            validateContacts(contactsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            checkEntities.checkUser(contactsEntity.getUsersByIdUsers());
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

        try {
            checkEntities.checkContactType(contactsEntity.getContactTypesByIdContactTypes());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contact"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        if (contactsEntity.getBranchActivitiesByIdBranchActivities() != null) {
            try {
                checkEntities.checkBranchActivities(contactsEntity.getBranchActivitiesByIdBranchActivities());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactTypes.NotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
        }

        if (contactsEntity.getJobTitlesByIdJobTitles() != null) {
            try {
                checkEntities.checkJobTitles(contactsEntity.getJobTitlesByIdJobTitles());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "jobTitles.notExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            contactsDao.update(em, contactsEntity);
            addressesBean.getAddressesEntity().setContactsByIdContacts(contactsEntity);
            addressesBean.updateEntity();
            companiesContactsBean.getCompaniesContactsEntity().setContactsByIdContacts(contactsEntity);
            companiesContactsBean.createCompaniesContacts();
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
     * Validate Contacts !
     *
     * @param entity ContactsEntity
     */
    private void validateContacts(ContactsEntity entity) {
        List<String> errors = ContactValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Contacts is not valid {}", entity);
            throw new InvalidEntityException("Les données du contact ne sont pas valide", ErrorCodes.CONTACT_NOT_VALID, errors);
        }
    }
}
