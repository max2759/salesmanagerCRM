package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.VoucherStatusDao;
import be.atc.salesmanagercrm.entities.VoucherStatusEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public class VoucherStatusDaoImpl extends EntityFinderImpl<VoucherStatusEntity> implements VoucherStatusDao {
    @Override
    public VoucherStatusEntity findById(EntityManager em, int id) {
        return em.find(VoucherStatusEntity.class, id);
    }

    @Override
    public List<VoucherStatusEntity> findAll() {
        return this.findByNamedQuery("VoucherStatus.findAll", new VoucherStatusEntity());
    }

    @Override
    public VoucherStatusEntity findByLabel(EntityManager em, String label) {
        return em.createNamedQuery("VoucherStatus.findVoucherStatusEntityByLabel",
                        VoucherStatusEntity.class)
                .setParameter("label", label)
                .getSingleResult();
    }
}
