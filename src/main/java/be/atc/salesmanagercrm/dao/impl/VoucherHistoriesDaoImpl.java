package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.VoucherHistoriesDao;
import be.atc.salesmanagercrm.entities.VoucherHistoriesEntity;

import javax.persistence.EntityManager;

/**
 * @author Younes Arifi
 */
public class VoucherHistoriesDaoImpl implements VoucherHistoriesDao {
    @Override
    public void save(EntityManager em, VoucherHistoriesEntity entity) {
        em.persist(entity);
    }
}
