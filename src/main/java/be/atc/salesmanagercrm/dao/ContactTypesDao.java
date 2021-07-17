package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.ContactTypesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
public interface ContactTypesDao {

    ContactTypesEntity findById(EntityManager em, int id);

    List<ContactTypesEntity> findAll();
}
