package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.TransactionsEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Younes Arifi
 */
public interface TransactionsDao {

    void save(EntityManager em, TransactionsEntity entity);

    Optional<TransactionsEntity> findById(EntityManager em, int id, int idUser);

    List<TransactionsEntity> findTransactionsEntityByContactsByIdContacts(EntityManager em, int id, int idUser);

    List<TransactionsEntity> findTransactionsEntityByCompaniesByIdCompanies(EntityManager em, int id, int idUser);

    List<TransactionsEntity> findAll(EntityManager em, int idUser);

    List<TransactionsEntity> findAllByPhase(EntityManager em, int idUser, String phaseTransaction);

    void update(EntityManager em, TransactionsEntity entity);

    Long countActiveTransactions(EntityManager em, int idUser);
}
