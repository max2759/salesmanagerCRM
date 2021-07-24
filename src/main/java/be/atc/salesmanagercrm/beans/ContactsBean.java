package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.ContactsDao;
import be.atc.salesmanagercrm.dao.impl.ContactsDaoImpl;
import be.atc.salesmanagercrm.entities.ContactTypesEntity;
import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.ContactValidator;
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

    @Inject
    private JobTitlesBean jobTitlesBean;

    @Inject
    private ContactTypesBean contactTypesBean;

    public void findAllContacts() {
        contactsEntityList = findContactsEntityByIdUser(1);
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
        findAllContacts();
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
     * @param entityGroup
     * @return
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
     * Auto complete for JobTitlesEntity
     *
     * @param search String
     * @return list of JobTitlesEntity
     */
    public List<JobTitlesEntity> completeJobTitles(String search) {

        String searchLowerCase = search.toLowerCase();

        List<JobTitlesEntity> jobTitlesEntitiesDropdown = jobTitlesBean.findAll();

        return jobTitlesEntitiesDropdown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());

    }

    /**
     * Auto complete for ContactTypesEntity
     *
     * @param search String
     * @return list of ContactTypesEntity
     */
    public List<ContactTypesEntity> completeContactType(String search) {

        String searchLowerCase = search.toLowerCase();

        List<ContactTypesEntity> contactTypesDropdown = contactTypesBean.findAll();

        return contactTypesDropdown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());
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
