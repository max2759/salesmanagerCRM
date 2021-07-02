package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.VoucherHistoriesEntity;

import javax.persistence.EntityManager;

public interface VoucherHistoriesDao {

    void save(EntityManager em, VoucherHistoriesEntity entity);

}
