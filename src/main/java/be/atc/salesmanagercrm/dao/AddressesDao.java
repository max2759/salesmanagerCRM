package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.AddressesEntity;

import javax.persistence.EntityManager;

/**
 * @author Maximilien Zabbara
 */
public interface AddressesDao {

    AddressesEntity findByIdCompanies(EntityManager em, int id);

    void update(EntityManager em, AddressesEntity addressesEntity);
}
