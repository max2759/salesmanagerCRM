package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionPhasesEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Younes Arifi
 */
public interface TransactionPhasesDao {

    TransactionPhasesEntity findById(EntityManager em, int id);


    List<TransactionPhasesEntity> findAll();

    Optional<TransactionPhasesEntity> findByLabel(EntityManager em, String label);
}
