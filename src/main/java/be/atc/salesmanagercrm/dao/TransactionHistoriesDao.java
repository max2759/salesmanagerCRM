package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionHistoriesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public interface TransactionHistoriesDao {

    void save(EntityManager em, TransactionHistoriesEntity entity);

}
