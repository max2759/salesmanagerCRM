package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.VoucherStatusDao;
import be.atc.salesmanagercrm.entities.VoucherStatusEntity;

import javax.persistence.EntityManager;


/**
 * @author Younes Arifi
 */
public class VoucherStatusDaoImpl implements VoucherStatusDao {
    @Override
    public VoucherStatusEntity findById(EntityManager em, int id) {
        return em.find(VoucherStatusEntity.class, id);
    }
}
