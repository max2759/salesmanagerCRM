package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TasksEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface TasksDao {

    void save(EntityManager em, TasksEntity entity);

    TasksEntity findById(EntityManager em, int id);

    List<TasksEntity> findTasksEntityByContactsByIdContacts(EntityManager em, int id);

    List<TasksEntity> findTasksEntityByCompaniesByIdCompanies(EntityManager em, int id);

    List<TasksEntity> findAll();

    void delete(EntityManager em, TasksEntity entity);

    void update(EntityManager em, TasksEntity entity);
}
