package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionTypesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface TransactionTypesDao {

    TransactionTypesEntity findById(EntityManager em, int id);

    List<TransactionTypesEntity> findAll();

    TransactionTypesEntity findByLabel(EntityManager em, String label);
}
