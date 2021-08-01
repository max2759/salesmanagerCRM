package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.AddressesDao;
import be.atc.salesmanagercrm.entities.AddressesEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@Slf4j
public class AddressesDaoImpl extends EntityFinderImpl<AddressesEntity> implements AddressesDao {

    @Override
    public AddressesEntity findByIdCompanies(EntityManager em, int id) {
        return em.createNamedQuery("Addresses.findByIdCompanies",
                AddressesEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void update(EntityManager em, AddressesEntity addressesEntity) {
        em.merge(addressesEntity);
    }
}
