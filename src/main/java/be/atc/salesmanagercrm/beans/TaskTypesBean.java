package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.TaskTypesDao;
import be.atc.salesmanagercrm.dao.impl.TaskTypesDaoImpl;
import be.atc.salesmanagercrm.entities.TaskTypesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.TaskTypesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Named(value = "taskTypesBean")
@RequestScoped
public class TaskTypesBean implements Serializable {


    private static final long serialVersionUID = 6295899008521191899L;

    @Getter
    @Setter
    private TaskTypesDao dao = new TaskTypesDaoImpl();

    @Getter
    @Setter
    private TaskTypesEntity taskTypesEntity;


    /**
     * Save TaskType Entity
     *
     * @param entity TaskTypesEntity
     */
    protected void save(TaskTypesEntity entity) {
        try {
            validateTaskType(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.save(em, entity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }


    /**
     * Find Tasktype by ID
     *
     * @param id TaskTypesEntity
     * @return TaskType Entity
     */
    protected TaskTypesEntity findById(int id) {
        if (id == 0) {
            log.error("TaskType ID is null");
            return null;
        }

        EntityManager em = EMF.getEM();
        Optional<TaskTypesEntity> optionalTasksEntity = Optional.ofNullable(dao.findById(em, id));
        em.clear();
        em.close();
        return optionalTasksEntity.orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun type de tâche avec l'ID " + id + " n a ete trouve dans la BDD",
                        ErrorCodes.TASKTYPE_NOT_FOUND
                ));
    }


    /**
     * Update TasksEntity
     *
     * @param entity TasksEntity
     */
    protected void update(TaskTypesEntity entity) {

        try {
            validateTaskType(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        try {
            findById(entity.getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            tx.commit();
            log.info("Update ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
        } finally {
            em.clear();
            em.clear();
        }

    }


    /**
     * Find All task types Entities
     *
     * @return List TaskTypesEntity
     */
    protected List<TaskTypesEntity> findAll() {
        return dao.findAll();
    }

    /**
     * Validate TaskTypes !
     *
     * @param entity TaskTypesEntity
     */
    private void validateTaskType(TaskTypesEntity entity) {
        List<String> errors = TaskTypesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("TaskType is not valide {}", entity);
            throw new InvalidEntityException("Le type de tache n est pas valide", ErrorCodes.TASKTYPE_NOT_VALID, errors);
        }
    }
}
