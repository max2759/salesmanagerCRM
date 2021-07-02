package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.VoucherStatusEntity;

import javax.persistence.EntityManager;

public interface VoucherStatusDao {

    VoucherStatusEntity findById(EntityManager em, int id);

}
