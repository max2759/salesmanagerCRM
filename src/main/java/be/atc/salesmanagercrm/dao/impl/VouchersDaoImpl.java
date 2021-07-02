package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.VouchersDao;
import be.atc.salesmanagercrm.entities.VouchersEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public class VouchersDaoImpl extends EntityFinderImpl<VouchersEntity> implements VouchersDao {
    @Override
    public void save(EntityManager em, VouchersEntity entity) {
        em.persist(entity);
    }

    @Override
    public VouchersEntity findById(EntityManager em, int id, int idUser) {
        return em.createNamedQuery("Vouchers.findById",
                VouchersEntity.class)
                .setParameter("id", id)
                .setParameter("idUser", idUser)
                .getSingleResult();
    }

    @Override
    public List<VouchersEntity> findVouchersEntityByContactsByIdContacts(EntityManager em, int id, int idUser) {

        return em.createNamedQuery("Vouchers.findVouchersEntityByContactsByIdContacts",
                VouchersEntity.class)
                .setParameter("id", id)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public List<VouchersEntity> findVouchersEntityByCompaniesByIdCompanies(EntityManager em, int id, int idUser) {

        return em.createNamedQuery("Vouchers.findVouchersEntityByCompaniesByIdCompanies",
                VouchersEntity.class)
                .setParameter("id", id)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public List<VouchersEntity> findAll(EntityManager em, int idUser) {

        return em.createNamedQuery("Vouchers.findAll",
                VouchersEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public void update(EntityManager em, VouchersEntity entity) {
        em.merge(entity);
    }
}
