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
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDate;
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
    @Getter
    private final LocalDateTime now = LocalDateTime.now().plusSeconds(10);
    @Getter
    @Setter
    private TasksEntity tasksEntity;
    @Getter
    @Setter
    private TasksEntity selectedTaskEntity;
    @Getter
    @Setter
    private ScheduleModel eventModel;

    @Getter
    @Setter
    private List<TasksEntity> tasksEntities;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesFiltered;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesDueToday;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesDueTodayFiltered;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesToLate;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesToLateFiltered;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesToCome;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesToComeFiltered;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesFinished;
    @Getter
    @Setter
    private List<TasksEntity> tasksEntitiesFinishedFiltered;
    @Getter
    @Setter
    private TaskTypesEntity taskTypesEntity = new TaskTypesEntity();
    @Getter
    @Setter
    private String paramType = "";

    @Inject
    private ContactsBean contactsBean;
    @Inject
    private CompaniesBean companiesBean;
    @Inject
    private UsersBean usersBean;

    /**
     * Save entity form
     */
    public void saveEntity() {
        log.info("TasksBean => method : saveEntity()");

        log.info("TaskEntity = : " + tasksEntity);

        tasksEntity.setUsersByIdUsers(usersBean.getUsersEntity());

        save(tasksEntity);

        createNewEntity();

        loadListEntities(getParamType());
    }


    /**
     * Find all entities for Users
     */
    public void listEntities() {
        log.info("TasksBean => method : listEntities()");
        loadListEntities("all");
    }

    /**
     * Find all entities for Contacts
     */
    public void listEntitiesContacts() {
        log.info("TasksBean => method : listEntitiesContacts()");
        loadListEntities("displayByContact");
    }

    /**
     * Find all entities for Companies
     */
    public void listEntitiesCompanies() {
        log.info("TasksBean => method : listEntitiesCompanies()");
        loadListEntities("displayByCompany");
    }

    /**
     * Create new instance for objects
     */
    public void createNewEntity() {
        log.info("TasksBean => method : createNewEntity()");
        this.tasksEntity = new TasksEntity();
        this.tasksEntities = new ArrayList<>();

        checkTypeParamAndSetContactOrCompanyInTasksEntity();
        if (!(paramType.equalsIgnoreCase("displayByContact") || paramType.equalsIgnoreCase("displayByCompany"))) {
            this.tasksEntitiesDueToday = new ArrayList<>();
            this.tasksEntitiesToLate = new ArrayList<>();
            this.tasksEntitiesToCome = new ArrayList<>();
            this.tasksEntitiesFinished = new ArrayList<>();
        }
    }

    /**
     * Check type Param on create Tasks and set Contact or Company entity in NotesEntity !!
     */
    public void checkTypeParamAndSetContactOrCompanyInTasksEntity() {
        if (this.paramType.equalsIgnoreCase("displayByContact")) {
            setContactEntityInTasksEntity();
        } else if (this.paramType.equalsIgnoreCase("displayByCompany")) {
            setCompanyEntityInTasksEntity();
        }
    }

    /**
     * Set ContactEntity in TasksEntity
     */
    public void setContactEntityInTasksEntity() {
        log.info("NotesBean => method : setContactEntityInTasksEntity()");
        try {
            this.tasksEntity.setContactsByIdContacts(contactsBean.getContactsEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Set ContactEntity in NotesEntity
     */
    public void setCompanyEntityInTasksEntity() {
        log.info("NotesBean => method : setCompanyEntityInTasksEntity()");
        try {
            this.tasksEntity.setCompaniesByIdCompanies(companiesBean.getCompaniesEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Method for update
     *
     * @param event RowEditEvent<NotesEntity>
     */
    public void onRowEdit(RowEditEvent<TasksEntity> event) {
        log.info("TasksBean => method : onRowEdit(RowEditEvent<TasksEntity> event)");

        FacesMessage msg;
        try {
            findById(event.getObject().getId(), usersBean.getUsersEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        log.info("event : " + event);
        update(event.getObject());

        loadListEntities(getParamType());
    }

    /**
     * On cancel delete
     */
    public void onRowCancel(RowEditEvent<NotesEntity> event) {
        log.info("TasksBean => method : onRowCancel()");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "canceled"), null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Method for delete entity
     */
    public void deleteEntity() {
        log.info("TasksBean => method : deleteEntity()");

        try {
            int idEntity = Integer.parseInt(getParam("idEntity"));
            log.info("ID Task : " + idEntity);
            delete(idEntity, usersBean.getUsersEntity());
        } catch (NumberFormatException exception) {
            log.warn(exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        loadListEntities(getParamType());
    }

    /**
     * Method to show modal in update task
     */
    public void showModalUpdate() {
        log.info("TasksBean => method : showModalUpdate()");
        try {
            int idEntity = Integer.parseInt(getParam("idEntity"));
            tasksEntity = findById(idEntity, usersBean.getUsersEntity());
        } catch (NumberFormatException exception) {
            log.warn(exception.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Method to show modal in create task
     */
    public void showModalCreate() {
        log.info("TasksBean => method : showModalCreate()");
        tasksEntity = new TasksEntity();
        checkTypeParamAndSetContactOrCompanyInTasksEntity();
    }

    /**
     * Update Entity form
     */
    public void updateEntity() {
        log.info("method : updateEntity()");
        log.info("TaskEntity = : " + tasksEntity);

        update(tasksEntity);

        loadListEntities(getParam("typeEntities"));
    }

    /**
     * Method to change status of task
     *
     * @param event
     */
    public void updateStatus(AjaxBehaviorEvent event) {
        log.info("method : updateStatus()");

        TasksEntity tasksEntityToUpdate;
        FacesMessage msg;
        try {
            tasksEntityToUpdate = findById((Integer) event.getComponent().getAttributes().get("idTask"), usersBean.getUsersEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        tasksEntityToUpdate.setStatus(!tasksEntityToUpdate.isStatus());
        update(tasksEntityToUpdate);

        loadListEntities("all");

    }

    /**
     * method to know which entity to reload
     *
     * @param typeLoad String
     */
    public void loadListEntities(String typeLoad) {

        if (typeLoad.equalsIgnoreCase("displayByCompany")) {
            if (companiesBean.getCompaniesEntity() == null) {
                return;
            }
            log.info("id company : " + companiesBean.getCompaniesEntity().getId());
            log.info("id User : " + usersBean.getUsersEntity().getId());

            tasksEntities = findTasksEntityByCompaniesByIdCompanies(companiesBean.getCompaniesEntity(), usersBean.getUsersEntity());
        } else if (typeLoad.equalsIgnoreCase("displayByContact")) {
            if (contactsBean.getContactsEntity() == null) {
                return;
            }
            log.info("id contact : " + contactsBean.getContactsEntity().getId());
            log.info("id User : " + usersBean.getUsersEntity().getId());

            tasksEntities = findTasksEntityByContactsByIdContacts(contactsBean.getContactsEntity(), usersBean.getUsersEntity());
        } else if (typeLoad.equalsIgnoreCase("all")) {

            LocalDateTime dateTime = LocalDate.now().atTime(0, 0, 0, 0).plusDays(1);

            tasksEntities = findAll(usersBean.getUsersEntity());

            this.tasksEntitiesToCome = tasksEntities.stream().filter(t -> t.getEndDate() != null && t.getEndDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            this.tasksEntitiesDueToday = tasksEntities.stream().filter(t -> t.getEndDate() != null && t.getEndDate().isBefore(dateTime) && t.getEndDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            this.tasksEntitiesToLate = tasksEntities.stream().filter(t -> t.getEndDate() != null && t.getEndDate().isBefore(LocalDateTime.now())).collect(Collectors.toList());

            tasksEntitiesFinished = findTasksFinished(usersBean.getUsersEntity());
        }
    }

    /**
     * Fill the calendar with tasks
     */
    public void setCalendar() {
        eventModel = new DefaultScheduleModel();

        for (TasksEntity t : tasksEntities) {
            if (t.getEndDate() != null && t.isStatus() == false) {
                DefaultScheduleEvent event = DefaultScheduleEvent.builder()
                        .title(t.getTitle())
                        .startDate(t.getCreationDate())
                        .endDate(t.getEndDate())
                        .description(t.getDescription())
                        .overlapAllowed(true)
                        .build();
                eventModel.addEvent(event);
            }
        }
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

        entity.setStatus(false);

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
    protected TasksEntity findById(int id, UsersEntity usersEntity) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Task ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if (usersEntity == null) {
            log.error("User is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findById(em, id, usersEntity.getId());
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune tâche avec l ID " + id + " et l ID User " + usersEntity.getId() + " n a ete trouve dans la BDD",
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
     * @param contactsEntity ContactsEntity
     * @return List TasksEntities
     */
    protected List<TasksEntity> findTasksEntityByContactsByIdContacts(ContactsEntity contactsEntity, UsersEntity usersEntity) {
        FacesMessage msg;
        if (contactsEntity == null) {
            log.error("Contact Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();

        List<TasksEntity> tasksEntities = dao.findTasksEntityByContactsByIdContacts(em, contactsEntity.getId(), usersEntity.getId());

        em.clear();
        em.close();

        return tasksEntities;
    }

    /**
     * Find tasks entities by id company
     *
     * @param companiesEntity CompaniesEntity
     * @return List TasksEntities
     */
    protected List<TasksEntity> findTasksEntityByCompaniesByIdCompanies(CompaniesEntity companiesEntity, UsersEntity usersEntity) {
        FacesMessage msg;
        if (companiesEntity == null) {
            log.error("Company Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<TasksEntity> tasksEntities = dao.findTasksEntityByCompaniesByIdCompanies(em, companiesEntity.getId(), usersEntity.getId());

        em.clear();
        em.close();

        return tasksEntities;
    }


    /**
     * Find All tasks Entities
     *
     * @return List TasksEntity
     */
    protected List<TasksEntity> findAll(UsersEntity usersEntity) {
        FacesMessage msg;
        if (usersEntity == null) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<TasksEntity> tasksEntities = dao.findAll(em, usersEntity.getId());

        em.clear();
        em.close();

        return tasksEntities;
    }

    /**
     * Find all tasks finished
     */
    protected List<TasksEntity> findTasksFinished(UsersEntity usersEntity) {
        FacesMessage msg;
        if (usersEntity == null) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<TasksEntity> tasksEntities = dao.findTasksFinished(em, usersEntity.getId());

        em.clear();
        em.close();

        return tasksEntities;
    }

    /**
     * delete task by id
     *
     * @param id Task
     */
    protected void delete(int id, UsersEntity usersEntity) {
        FacesMessage msg;

        if (id == 0) {
            log.error("Task ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if (usersEntity == null) {
            log.error("User is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        TasksEntity tasksEntityToDelete;

        try {
            tasksEntityToDelete = findById(id, usersEntity);
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
            TasksEntity tasksEntityToFind = findById(entity.getId(), entity.getUsersByIdUsers());
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
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "taskTypeNotExist"), null);
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "tasks.updated"), null);
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

    /**
     * Valide Task date End
     *
     * @param entity TasksEntity
     */
    private void validateTaskDateEnd(TasksEntity entity) {
        if (entity.getEndDate() != null) {
            if (entity.getEndDate().isBefore(entity.getCreationDate())) {
                log.error("Task end date in not valide {}", entity);
                throw new InvalidEntityException("La date de fin de tâche doit etre superieur à la date de creation", ErrorCodes.TASK_NOT_VALID);
            }
        }
    }
}
