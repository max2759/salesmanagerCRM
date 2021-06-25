package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.entities.UsersEntity;

import javax.persistence.EntityManager;

public class UsersDaoImpl implements UsersDao {
    @Override
    public UsersEntity findById(EntityManager em, int id) {
        return em.find(UsersEntity.class, id);
    }
}
