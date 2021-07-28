package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.VoucherHistoriesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface VoucherHistoriesDao {

    void save(EntityManager em, VoucherHistoriesEntity entity);

    List<VoucherHistoriesEntity> findAllByIdUserAndByIdVoucher(EntityManager em, int idVoucher, int idUser);
}
