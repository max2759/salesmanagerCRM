package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.TaskTypesDao;
import be.atc.salesmanagercrm.entities.TaskTypesEntity;

import javax.persistence.EntityManager;

public class TaskTypesDaoImpl implements TaskTypesDao {
    @Override
    public TaskTypesEntity findById(EntityManager em, int id) {
        return em.find(TaskTypesEntity.class, id);
    }
}
