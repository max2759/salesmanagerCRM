package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.UsersEntity;

import javax.persistence.EntityManager;

public interface UsersDao {

    UsersEntity findById(EntityManager em, int id);
}
