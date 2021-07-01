package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TasksEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface TasksDao {

    void save(EntityManager em, TasksEntity entity);

    TasksEntity findById(EntityManager em, int id, int idUser);

    List<TasksEntity> findTasksEntityByContactsByIdContacts(EntityManager em, int id, int idUser);

    List<TasksEntity> findTasksEntityByCompaniesByIdCompanies(EntityManager em, int id, int idUser);

    List<TasksEntity> findAll(EntityManager em, int idUser);

    void delete(EntityManager em, TasksEntity entity);

    void update(EntityManager em, TasksEntity entity);
}
