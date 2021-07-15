package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.TasksDao;
import be.atc.salesmanagercrm.dao.impl.TasksDaoImpl;
import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.TasksValidator;
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
import java.util.stream.Collectors;

/**
 * @author Younes Arifi
 */
@Slf4j
@Named(value = "tasksBean")
@ViewScoped
public class TasksBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 8865671023396118126L;

    @Getter
    @Setter
    private TasksDao dao = new TasksDaoImpl();
    // Todo : A modifier
    @Getter
    @Setter
    private UsersEntity usersEntity = new UsersEntity();
    @Getter
    @Setter
    private ContactsEntity contactsEntity = new ContactsEntity();
    @Getter
    @Setter
    private CompaniesEntity companiesEntity = new CompaniesEntity();

    @Getter
    private final LocalDateTime now = LocalDateTime.now();
    @Getter
    @Setter
    private TasksEntity tasksEntity;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntities;
    @Getter
    @Setter
    private TaskTypesEntity taskTypesEntity = new TaskTypesEntity();
    @Inject
    @Getter
    @Setter
    private ContactsBean contactsBean;
    @Inject
    @Getter
    @Setter
    private CompaniesBean companiesBean;

    /**
     * Auto Complete form creation
     *
     * @param query String
     * @return List Contacts Entities
     */
    public List<ContactsEntity> completeContactsContains(String query) {
        String queryLowerCase = query.toLowerCase();

        // TODO : à modifier
        List<ContactsEntity> contactsEntitiesForm = contactsBean.findContactsEntityByIdUser(1);

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
     * Auto Complete form creation
     *
     * @param query String
     * @return List Contacts Entities
     */
    public List<CompaniesEntity> completeCompaniesContains(String query) {
        String queryLowerCase = query.toLowerCase();

        // TODO : à modifier
        List<CompaniesEntity> companiesEntitiesForm = companiesBean.findCompaniesEntityByIdUser(1);

        return companiesEntitiesForm.stream().filter(t -> t.getLabel().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }

    /**
     * Sort Contacts by group in form
     *
     * @param entityGroup
     * @return
     */
    public char getCompaniesEntityGroup(CompaniesEntity entityGroup) {
        return entityGroup.getLabel().charAt(0);
    }

    /**
     * Save entity form
     */
    public void saveEntity() {
        log.info("method : saveEntity()");

        log.info("TaskEntity = : " + tasksEntity);
        usersEntity.setId(1);

        tasksEntity.setUsersByIdUsers(usersEntity);


        save(tasksEntity);

        // TODO : Corriger ça
        createNewEntity();
//        listEntitiesContacts();
        listEntities();

//        listEntitiesCompanies();
    }

    /**
     * Find all entities for Contacts
     */
    public void listEntitiesContacts() {
        log.info("method : listEntitiesContacts()");
        // TODO : A modifier par l'user
        usersEntity.setId(1);
        contactsEntity.setId(1);

        tasksEntities = findTasksEntityByContactsByIdContacts(contactsEntity.getId(), usersEntity.getId());
    }

    /**
     * Find all entities for Users
     */
    public void listEntities() {
        log.info("method : listEntities()");
        // TODO : A modifier par l'user
        usersEntity.setId(1);

        tasksEntities = findAll(usersEntity.getId());
    }

    /**
     * Find all entities for Companies
     */
    public void listEntitiesCompanies() {
        log.info("method : listEntitiesCompanies()");
        usersEntity.setId(1);
        companiesEntity.setId(1);

        tasksEntities = findTasksEntityByCompaniesByIdCompanies(companiesEntity.getId(), usersEntity.getId());
    }


    /**
     * Create new instance for objects
     */
    public void createNewEntity() {
        log.info("method : createNewEntity()");
        this.tasksEntity = new TasksEntity();
        this.tasksEntities = new ArrayList<>();
    }

    /**
     * Method for update
     *
     * @param event RowEditEvent<NotesEntity>
     */
    public void onRowEdit(RowEditEvent<TasksEntity> event) {
        // TODO : Corriger le idUser
        TasksEntity tasksEntityToUpdate = findById(event.getObject().getId(), 1);

        log.info("event : " + event);
        update(event.getObject());


        // TODO : Corriger ça
//        listEntitiesCompanies();
        listEntitiesContacts();
    }

    /**
     * On cancel delete
     */
    public void onRowCancel() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "canceled"), null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Method for delete entity
     */
    public void deleteEntity() {

        // TODO : Corriger l idUser
        delete(Integer.parseInt(getParam("idEntity")), 1);


        // TODO : Corriger ça
        listEntitiesContacts();
//        listEntitiesCompanies();
    }

    /**
     * Save Task Entity
     *
     * @param entity TasksEntity
     */
    protected void save(TasksEntity entity) {

        try {
            validateTask(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        entity.setCreationDate(LocalDateTime.now());

        FacesMessage msg;

        if (entity.getEndDate() != null) {
            try {
                validateTaskDateEnd(entity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "task.validator.dateEnd"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
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

        if (entity.getTaskTypesByIdTaskTypes() != null) {
            try {
                checkEntities.checkTaskType(entity.getTaskTypesByIdTaskTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "taskTypeNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        entity.setStatus(true);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.save(em, entity);
            tx.commit();
            log.info("Persist ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "tasks.saved"), null);
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
     * Find Task by ID
     *
     * @param id TasksEntity
     * @return Task Entity
     */
    protected TasksEntity findById(int id, int idUser) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Task ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
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
            return dao.findById(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune tâche avec l ID " + id + " et l ID User " + idUser + " n a ete trouve dans la BDD",
                    ErrorCodes.TASK_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find tasks entities by id contact
     *
     * @param id Contact
     * @return List TasksEntities
     */
    protected List<TasksEntity> findTasksEntityByContactsByIdContacts(int id, int idUser) {
        FacesMessage msg;
        if (id == 0) {
            log.error("Contact ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();

        List<TasksEntity> tasksEntities = dao.findTasksEntityByContactsByIdContacts(em, id, idUser);

        em.clear();
        em.close();

        return tasksEntities;
    }

    /**
     * Find tasks entities by id company
     *
     * @param id Company
     * @return List TasksEntities
     */
    protected List<TasksEntity> findTasksEntityByCompaniesByIdCompanies(int id, int idUser) {
        FacesMessage msg;
        if (id == 0) {
            log.error("Company ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<TasksEntity> tasksEntities = dao.findTasksEntityByCompaniesByIdCompanies(em, id, idUser);

        em.clear();
        em.close();

        return tasksEntities;
    }


    /**
     * Find All tasks Entities
     *
     * @return List TasksEntity
     */
    protected List<TasksEntity> findAll(int idUser) {
        FacesMessage msg;
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<TasksEntity> tasksEntities = dao.findAll(em, idUser);

        em.clear();
        em.close();

        return tasksEntities;
    }


    /**
     * delete task by id
     *
     * @param id Task
     */
    protected void delete(int id, int idUser) {
        FacesMessage msg;
        if (id == 0) {
            log.error("Task ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        TasksEntity tasksEntityToDelete;

        try {
            tasksEntityToDelete = findById(id, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        EntityManager em = EMF.getEM();
        em.getTransaction();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.delete(em, tasksEntityToDelete);
            tx.commit();
            log.info("Delete ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "tasks.deleted"), null);
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
     * Update TasksEntity
     *
     * @param entity TasksEntity
     */
    protected void update(TasksEntity entity) {

        try {
            validateTask(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage msg;
        try {
            TasksEntity tasksEntityToFind = findById(entity.getId(), entity.getUsersByIdUsers().getId());
            entity.setCreationDate(tasksEntityToFind.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (entity.getEndDate() != null) {
            try {
                validateTaskDateEnd(entity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "task.validator.dateEnd"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
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

        if (entity.getTaskTypesByIdTaskTypes() != null) {
            try {
                checkEntities.checkTaskType(entity.getTaskTypesByIdTaskTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
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
     * Validate Task !
     *
     * @param entity TasksEntity
     */
    private void validateTask(TasksEntity entity) {
        List<String> errors = TasksValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Task is not valide {}", entity);
            throw new InvalidEntityException("La tache n est pas valide", ErrorCodes.TASK_NOT_VALID, errors);
        }
    }

    private void validateTaskDateEnd(TasksEntity entity) {
        if (entity.getEndDate() != null) {
            if (entity.getEndDate().isBefore(entity.getCreationDate())) {
                log.error("Task end date in not valide {}", entity);
                throw new InvalidEntityException("La date de fin de tâche doit etre superieur à la date de creation", ErrorCodes.TASK_NOT_VALID);
            }
        }
    }

}
