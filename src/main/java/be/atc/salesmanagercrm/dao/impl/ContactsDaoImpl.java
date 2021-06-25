package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.ContactsDao;
import be.atc.salesmanagercrm.entities.ContactsEntity;

import javax.persistence.EntityManager;

public class ContactsDaoImpl implements ContactsDao {
    @Override
    public ContactsEntity findById(EntityManager em, int id) {
        return em.find(ContactsEntity.class, id);
    }
}
