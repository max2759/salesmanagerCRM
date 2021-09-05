package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.ContactsDao;
import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

public class ContactsDaoImpl extends EntityFinderImpl<ContactsEntity> implements ContactsDao {

    @Override
    public void add(EntityManager em, ContactsEntity contactsEntity) {
        em.persist(contactsEntity);
    }

    @Override
    public ContactsEntity findById(EntityManager em, int id) {
        return em.createNamedQuery("Contacts.findById",
                        ContactsEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public ContactsEntity findByIdContactAndByIdUser(EntityManager em, int id, int idUser) {
        return em.createNamedQuery("Contacts.findByIdContactAndByIdUser",
                ContactsEntity.class)
                .setParameter("id", id)
                .setParameter("idUser", idUser)
                .getSingleResult();
    }

    @Override
    public List<ContactsEntity> findContactsEntityByIdUser(EntityManager em, int idUser) {
        return em.createNamedQuery("Contacts.findContactsEntityByIdUser",
                        ContactsEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public List<ContactsEntity> findDisableContactsEntityByIdUser(EntityManager em, int idUser) {
        return em.createNamedQuery("Contacts.findDisableContactsEntityByIdUser",
                        ContactsEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public List<ContactsEntity> findAllContactsEntityByIdUser(EntityManager em, int idUser) {
        return em.createNamedQuery("Contacts.findAllContactsEntityByIdUser",
                        ContactsEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }


    @Override
    public List<ContactsEntity> findAll() {
        return this.findByNamedQuery("Contacts.findAll", new ContactsEntity());
    }

    @Override
    public void update(EntityManager em, ContactsEntity contactsEntity) {
        em.merge(contactsEntity);
    }
}
