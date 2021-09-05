package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.NotesDao;
import be.atc.salesmanagercrm.dao.impl.NotesDaoImpl;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.entities.NotesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.*;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.NotesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Younes Arifi
 */
@Slf4j
@Named(value = "notesBean")
@ViewScoped
public class NotesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;


    @Getter
    @Setter
    private NotesDao dao = new NotesDaoImpl();

    @Getter
    @Setter
    private NotesEntity notesEntity;
    @Getter
    @Setter
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    @Getter
    @Setter
    private List<NotesEntity> notesEntities;
    @Getter
    @Setter
    private List<NotesEntity> notesEntitiesFiltered;
    @Getter
    @Setter
    private String paramType = "";

    @Inject
    private ContactsBean contactsBean;
    @Inject
    private CompaniesBean companiesBean;
    @Inject
    private UsersBean usersBean;
    @Inject
    private AccessControlBean accessControlBean;

    /**
     * Use this method for save entity note
     */
    public void saveEntity() {
        log.info("NotesBean => method : saveEntity()");

        log.info("message : " + notesEntity.getMessage());

        try {
            notesEntity.setUsersByIdUsers(usersBean.getUsersEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (getParamType().equalsIgnoreCase("displayByContact")) {
            notesEntity.setContactsByIdContacts(contactsBean.getContactsEntity());
        } else if (getParamType().equalsIgnoreCase("displayByCompany")) {
            notesEntity.setCompaniesByIdCompanies(companiesBean.getCompaniesEntity());
        } else {
            return;
        }

        save(notesEntity);

        createNewEntity();

        checkTypeParamAndFindNotesEntitiesByContactOrByCompany();
    }

    /**
     * Find all entities for Contacts
     */
    public void listEntitiesContacts() {
        log.info("NotesBean => method : listEntitiesContacts()");
        notesEntities = findNotesEntityByContactsByIdContacts(contactsBean.getContactsEntity(), usersBean.getUsersEntity());
    }

    /**
     * Find all entities for Companies
     */
    public void listEntitiesCompanies() {
        log.info("NotesBean => method : listEntitiesCompanies()");
        notesEntities = findNotesEntityByCompaniesByIdCompanies(companiesBean.getCompaniesEntity(), usersBean.getUsersEntity());
    }

    /**
     * Method to show modal in create note
     */
    public void showModalCreate() {
        log.info("NotesBean => method : showModalCreate()");
        notesEntity = new NotesEntity();
        checkTypeParamAndSetContactOrCompanyInNotesEntity();
    }

    /**
     * Check type Param on create Notes and set Contact or Company entity in NotesEntity !!
     */
    public void checkTypeParamAndSetContactOrCompanyInNotesEntity() {
        if (this.paramType.equalsIgnoreCase("displayByContact")) {
            setContactEntityInNotesEntity();
        } else if (this.paramType.equalsIgnoreCase("displayByCompany")) {
            setCompanyEntityInNotesEntity();
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Check type Param for find list Notes by contact or by company !!
     */
    public void checkTypeParamAndFindNotesEntitiesByContactOrByCompany() {
        if (this.paramType.equalsIgnoreCase("displayByContact")) {
            listEntitiesContacts();
        } else if (this.paramType.equalsIgnoreCase("displayByCompany")) {
            listEntitiesCompanies();
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Create new instance for objects
     */
    public void createNewEntity() {
        log.info("NotesBean => method : createNewEntity()");
        notesEntity = new NotesEntity();
        notesEntities = new ArrayList<>();
    }

    /**
     * Method for update
     *
     * @param event RowEditEvent<NotesEntity>
     */
    public void onRowEdit(RowEditEvent<NotesEntity> event) {
        log.info("NotesBean => method : onRowEdit(RowEditEvent<NotesEntity> event)");

        int idNote;
        FacesMessage msg;
        try {
            idNote = event.getObject().getId();
        } catch (NumberFormatException exception) {
            log.info(exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "notes.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        NotesEntity notesEntityToUpdate;

        try {
            notesEntityToUpdate = findById(idNote, usersBean.getUsersEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "notes.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        try {
            notesEntityToUpdate.setMessage(event.getObject().getMessage());
        } catch (InvalidOperationException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "notes.validator.message"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        update(notesEntityToUpdate);

        checkTypeParamAndFindNotesEntitiesByContactOrByCompany();

    }


    /**
     * On cancel delete
     */
    public void onRowCancel(RowEditEvent<NotesEntity> event) {
        log.info("NotesBean => method : onRowCancel(RowEditEvent<NotesEntity> event)");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "canceled"), null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Method for delete entity
     */
    public void deleteEntity() {
        log.info("NotesBean => method : deleteEntity()");

        try {
            int idEntity = Integer.parseInt(getParam("idEntity"));
            delete(idEntity, usersBean.getUsersEntity());
        } catch (NumberFormatException exception) {
            log.warn(exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        checkTypeParamAndFindNotesEntitiesByContactOrByCompany();
    }

    /**
     * Set ContactEntity in NotesEntity
     */
    public void setContactEntityInNotesEntity() {
        log.info("NotesBean => method : setContactEntityInNotesEntity()");
        try {
            this.notesEntity.setContactsByIdContacts(contactsBean.getContactsEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Set ContactEntity in NotesEntity
     */
    public void setCompanyEntityInNotesEntity() {
        log.info("NotesBean => method : setCompanyEntityInNotesEntity()");
        try {
            this.notesEntity.setCompaniesByIdCompanies(companiesBean.getCompaniesEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Save Note Entity !
     *
     * @param entity NotesEntity
     */
    protected void save(NotesEntity entity) {
        log.info("NotesBean => method : save(NotesEntity entity)");

        try {
            accessControlBean.checkPermission("addNotes");
        } catch (AccessDeniedException exception) {
            log.info(exception.getMessage());
            accessControlBean.hasNotPermission();
            return;
        }

        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage msg;
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        entity.setCreationDate(LocalDateTime.now());

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.save(em, entity);
            tx.commit();
            log.info("Persist ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "notes.saved"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * Find Note by ID
     *
     * @param id NotesEntity
     * @return Notes Entity
     */
    protected NotesEntity findById(int id, UsersEntity usersEntity) {
        log.info("NotesBean => method : findById(int id, int idUser)");

        FacesMessage msg;

        if (id == 0) {
            log.error("Note ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "note.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new EntityNotFoundException(
                    "L ID de la note est incorrect",
                    ErrorCodes.NOTE_NOT_FOUND
            );
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new EntityNotFoundException(
                    "Aucun utilisateur n a ete trouve", ErrorCodes.USER_NOT_FOUND
            );
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findById(em, id, usersEntity.getId());
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune Note avec l ID " + id + " et l ID User " + usersEntity.getId() + " n a ete trouvee dans la BDD",
                    ErrorCodes.NOTE_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find Notes entities by id Contact
     *
     * @param contactsEntity ContactsEntity
     * @return List NotesEntity
     */
    protected List<NotesEntity> findNotesEntityByContactsByIdContacts(ContactsEntity contactsEntity, UsersEntity usersEntity) {
        log.info("NotesBean => method : findNotesEntityByContactsByIdContacts(int id, int idUser)");
        FacesMessage msg;
        if (contactsEntity == null) {
            log.error("Contact Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<NotesEntity> notesEntities = dao.findNotesEntityByContactsByIdContacts(em, contactsEntity.getId(), usersEntity.getId());

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * Find Notes Entities by Id Company
     *
     * @param companiesEntity CompaniesEntity
     * @return List NotesEntity
     */
    protected List<NotesEntity> findNotesEntityByCompaniesByIdCompanies(CompaniesEntity companiesEntity, UsersEntity usersEntity) {
        log.info("NotesBean => method : findNotesEntityByCompaniesByIdCompanies(int id, int idUser)");
        FacesMessage msg;
        if (companiesEntity == null) {
            log.error("Company Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<NotesEntity> notesEntities = dao.findNotesEntityByCompaniesByIdCompanies(em, companiesEntity.getId(), usersEntity.getId());

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * Find All Notes Entities
     *
     * @return List NotesEntity
     */
    protected List<NotesEntity> findAll(UsersEntity usersEntity) {
        log.info("NotesBean => method : findAll(int idUser)");
        FacesMessage msg;
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<NotesEntity> notesEntities = dao.findAll(em, usersEntity.getId());

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * delete note by id
     *
     * @param id note
     */
    protected void delete(int id, UsersEntity usersEntity) {
        log.info("NotesBean => method : delete(int id, int idUser)");

        try {
            accessControlBean.checkPermission("deleteNotes");
        } catch (AccessDeniedException exception) {
            log.info(exception.getMessage());
            accessControlBean.hasNotPermission();
            return;
        }

        FacesMessage msg;
        if (id == 0) {
            log.error("Note ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "note.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        NotesEntity notesEntity;

        try {
            notesEntity = findById(id, usersEntity);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "note.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        EntityManager em = EMF.getEM();
        em.getTransaction();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.delete(em, notesEntity);
            tx.commit();
            log.info("Delete ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "notes.deleted"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.error("Delete Error");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Update NoteEntity
     *
     * @param entity NoteEntity
     */
    protected void update(NotesEntity entity) {
        log.info("NotesBean => method : update(NotesEntity entity)");

        try {
            accessControlBean.checkPermission("updateNotes");
        } catch (AccessDeniedException exception) {
            log.info(exception.getMessage());
            accessControlBean.hasNotPermission();
            return;
        }

        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage msg;
        try {
            NotesEntity notesEntity = findById(entity.getId(), entity.getUsersByIdUsers());
            entity.setCreationDate(notesEntity.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "notes.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            tx.commit();
            log.info("Update ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "notes.updated"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }

    }

    /**
     * Validate Note !
     *
     * @param entity NotesEntity
     */
    private void validateNote(NotesEntity entity) {
        log.info("NotesBean => method : validateNote(NotesEntity entity)");
        List<String> errors = NotesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Note is not valide {}", entity);
            throw new InvalidEntityException("La note n est pas valide", ErrorCodes.NOTE_NOT_VALID, errors);
        }
    }
}
