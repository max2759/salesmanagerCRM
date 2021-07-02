package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionPhasesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public interface TransactionPhasesDao {

    TransactionPhasesEntity findById(EntityManager em, int id);

}
