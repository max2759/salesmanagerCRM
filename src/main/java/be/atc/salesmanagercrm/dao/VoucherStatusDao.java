package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.VoucherStatusEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface VoucherStatusDao {

    VoucherStatusEntity findById(EntityManager em, int id);

    List<VoucherStatusEntity> findAll();

    VoucherStatusEntity findByLabel(EntityManager em, String label);
}
