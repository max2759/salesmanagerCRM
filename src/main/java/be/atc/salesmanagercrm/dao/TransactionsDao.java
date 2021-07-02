package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionsEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface TransactionsDao {

    void save(EntityManager em, TransactionsEntity entity);

    TransactionsEntity findById(EntityManager em, int id, int idUser);

    List<TransactionsEntity> findTransactionsEntityByContactsByIdContacts(EntityManager em, int id, int idUser);

    List<TransactionsEntity> findTransactionsEntityByCompaniesByIdCompanies(EntityManager em, int id, int idUser);

    List<TransactionsEntity> findAll(EntityManager em, int idUser);

    void update(EntityManager em, TransactionsEntity entity);
}
