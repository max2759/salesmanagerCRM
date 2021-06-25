package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.ContactsEntity;

import javax.persistence.EntityManager;

public interface ContactsDao {

    ContactsEntity findById(EntityManager em, int id);
}
