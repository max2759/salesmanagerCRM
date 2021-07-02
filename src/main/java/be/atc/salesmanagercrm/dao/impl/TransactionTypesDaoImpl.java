package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.TransactionTypesDao;
import be.atc.salesmanagercrm.entities.TransactionTypesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public class TransactionTypesDaoImpl implements TransactionTypesDao {
    @Override
    public TransactionTypesEntity findById(EntityManager em, int id) {
        return em.find(TransactionTypesEntity.class, id);
    }
}
