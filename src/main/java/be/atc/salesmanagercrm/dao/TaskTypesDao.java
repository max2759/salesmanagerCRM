package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TaskTypesEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface TaskTypesDao {

    void save(EntityManager em, TaskTypesEntity entity);

    TaskTypesEntity findById(EntityManager em, int id);

    List<TaskTypesEntity> findAll();

    void update(EntityManager em, TaskTypesEntity entity);
}
