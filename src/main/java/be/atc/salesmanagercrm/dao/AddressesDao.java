package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.AddressesEntity;

import javax.persistence.EntityManager;

/**
 * @author Maximilien Zabbara
 */
public interface AddressesDao {

    void add(EntityManager em, AddressesEntity addressesEntity);

    AddressesEntity findByIdCompanies(EntityManager em, int id);

    void deleteByIdCompanies(EntityManager em, AddressesEntity addressesEntity);

    void update(EntityManager em, AddressesEntity addressesEntity);
}
