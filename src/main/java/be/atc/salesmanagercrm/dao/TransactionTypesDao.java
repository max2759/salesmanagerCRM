package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionTypesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public interface TransactionTypesDao {

    TransactionTypesEntity findById(EntityManager em, int id);

}
