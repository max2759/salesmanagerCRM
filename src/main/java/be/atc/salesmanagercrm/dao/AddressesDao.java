package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.AddressesEntity;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @author Maximilien Zabbara
 */
public interface AddressesDao {

    Optional<AddressesEntity> findByIdCompanies(EntityManager em, int id);

    Optional<AddressesEntity> findByIdContacts(EntityManager em, int id);

    void update(EntityManager em, AddressesEntity addressesEntity);
}
