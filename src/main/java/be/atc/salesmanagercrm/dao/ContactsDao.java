package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.ContactsEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface ContactsDao {

    void add(EntityManager em, ContactsEntity contactsEntity);

    Optional<ContactsEntity> findById(EntityManager em, int id);

    Optional<ContactsEntity> findByIdContactAndByIdUser(EntityManager em, int id, int idUser);

    List<ContactsEntity> findContactsEntityByIdUser(EntityManager em, int idUser);

    List<ContactsEntity> findDisableContactsEntityByIdUser(EntityManager em, int idUser);

    List<ContactsEntity> findAllContactsEntityByIdUser(EntityManager em, int idUser);

    List<ContactsEntity> findAll();

    void update(EntityManager em, ContactsEntity contactsEntity);

    Long countActiveContacts(EntityManager em, int idUser);
}
