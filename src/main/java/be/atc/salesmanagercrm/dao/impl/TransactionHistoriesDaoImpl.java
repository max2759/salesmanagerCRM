package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.TransactionHistoriesDao;
import be.atc.salesmanagercrm.entities.TransactionHistoriesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public class TransactionHistoriesDaoImpl implements TransactionHistoriesDao {
    @Override
    public void save(EntityManager em, TransactionHistoriesEntity entity) {
        em.persist(entity);
    }
}
