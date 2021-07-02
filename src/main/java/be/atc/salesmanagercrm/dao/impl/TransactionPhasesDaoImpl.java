package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.TransactionPhasesDao;
import be.atc.salesmanagercrm.entities.TransactionPhasesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public class TransactionPhasesDaoImpl implements TransactionPhasesDao {
    @Override
    public TransactionPhasesEntity findById(EntityManager em, int id) {
        return em.find(TransactionPhasesEntity.class, id);
    }
}
