package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionHistoriesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface TransactionHistoriesDao {

    void save(EntityManager em, TransactionHistoriesEntity entity);

    List<TransactionHistoriesEntity> findAllByIdUserAndByIdTransaction(EntityManager em, int idTransaction, int idUser);
}
