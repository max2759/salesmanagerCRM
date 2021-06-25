package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TaskTypesEntity;

import javax.persistence.EntityManager;

public interface TaskTypesDao {

    TaskTypesEntity findById(EntityManager em, int id);
}
