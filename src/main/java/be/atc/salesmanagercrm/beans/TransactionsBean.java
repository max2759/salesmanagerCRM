package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.TransactionHistoriesDao;
import be.atc.salesmanagercrm.dao.TransactionsDao;
import be.atc.salesmanagercrm.dao.impl.TransactionHistoriesDaoImpl;
import be.atc.salesmanagercrm.dao.impl.TransactionsDaoImpl;
import be.atc.salesmanagercrm.entities.TransactionHistoriesEntity;
import be.atc.salesmanagercrm.entities.TransactionsEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.TransactionsValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Younes Arifi
 */
@Slf4j
@Named(value = "transactionsBean")
@RequestScoped
public class TransactionsBean implements Serializable {

    private static final long serialVersionUID = -5437009720221374165L;

    @Getter
    @Setter
    private TransactionsDao dao = new TransactionsDaoImpl();

    @Getter
    @Setter
    private TransactionsEntity transactionsEntity;


    /**
     * Save Transaction Entity
     *
     * @param entity TransactionsEntity
     */
    protected void save(TransactionsEntity entity) {

        try {
            validateTransaction(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        entity.setCreationDate(LocalDateTime.now());

        if (entity.getEndDate() != null) {
            try {
                validateTransactionDateEnd(entity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getTransactionTypesByIdTransactionTypes() != null) {
            try {
                checkEntities.checkTransactionTypes(entity.getTransactionTypesByIdTransactionTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getTransactionPhasesByIdTransactionPhases() != null) {
            try {
                checkEntities.checkTransactionPhases(entity.getTransactionPhasesByIdTransactionPhases());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        entity.setActive(true);

        TransactionHistoriesEntity transactionHistoriesEntity = saveTransactionHistories(entity);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.save(em, entity);
            TransactionHistoriesDao transactionHistoriesDao = new TransactionHistoriesDaoImpl();
            transactionHistoriesDao.save(em, transactionHistoriesEntity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }


    /**
     * Find Transaction entity by id
     *
     * @param id     Transaction
     * @param idUser User
     * @return TransactionsEntity
     */
    protected TransactionsEntity findById(int id, int idUser) {
        if (id == 0) {
            log.error("Transaction ID is null");
            return null;
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findById(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune transaction avec l ID " + id + " et l ID User " + idUser + " n a ete trouvee dans la BDD",
                    ErrorCodes.TRANSACTION_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find Transactions entities by id contact
     *
     * @param id Contact
     * @return List TransactionsEntities
     */
    protected List<TransactionsEntity> findTransactionsEntityByContactsByIdContacts(int id, int idUser) {
        if (id == 0) {
            log.error("Contact ID is null");
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();

        List<TransactionsEntity> transactionsEntities = dao.findTransactionsEntityByContactsByIdContacts(em, id, idUser);

        em.clear();
        em.close();

        return transactionsEntities;
    }


    /**
     * Find Transactions entities by id company
     *
     * @param id Company
     * @return List TransactionsEntities
     */
    protected List<TransactionsEntity> findTransactionsEntityByCompaniesByIdCompanies(int id, int idUser) {
        if (id == 0) {
            log.error("Company ID is null");
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<TransactionsEntity> transactionsEntities = dao.findTransactionsEntityByCompaniesByIdCompanies(em, id, idUser);

        em.clear();
        em.close();

        return transactionsEntities;
    }

    /**
     * Find All Transactions Entities
     *
     * @return List TransactionsEntity
     */
    protected List<TransactionsEntity> findAll(int idUser) {
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<TransactionsEntity> transactionsEntities = dao.findAll(em, idUser);

        em.clear();
        em.close();

        return transactionsEntities;
    }


    /**
     * Update TransactionsEntity
     *
     * @param entity TransactionsEntity
     */
    protected void update(TransactionsEntity entity) {

        try {
            validateTransaction(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            TransactionsEntity transactionsEntityToFind = findById(entity.getId(), entity.getUsersByIdUsers().getId());
            entity.setCreationDate(transactionsEntityToFind.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getEndDate() != null) {
            try {
                validateTransactionDateEnd(entity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getTransactionTypesByIdTransactionTypes() != null) {
            try {
                checkEntities.checkTransactionTypes(entity.getTransactionTypesByIdTransactionTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getTransactionPhasesByIdTransactionPhases() != null) {
            try {
                checkEntities.checkTransactionPhases(entity.getTransactionPhasesByIdTransactionPhases());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        TransactionHistoriesEntity transactionHistoriesEntity = saveTransactionHistories(entity);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            TransactionHistoriesDao transactionHistoriesDao = new TransactionHistoriesDaoImpl();
            transactionHistoriesDao.save(em, transactionHistoriesEntity);
            tx.commit();
            log.info("Update ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
        } finally {
            em.clear();
            em.clear();
        }

    }


    /**
     * Logical Delete transaction
     *
     * @param transactionsEntityToDelete TransactionsEntity
     */
    protected void delete(TransactionsEntity transactionsEntityToDelete, int idUser) {
        if (transactionsEntityToDelete == null) {
            log.error("Transaction ID is null");
            return;
        }

        try {
            transactionsEntityToDelete = findById(transactionsEntityToDelete.getId(), idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        transactionsEntityToDelete.setActive(false);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, transactionsEntityToDelete);
            tx.commit();
            log.info("Logical delete ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Logical delete echec");
        } finally {
            em.clear();
            em.clear();
        }

    }


    /**
     * Validate Transaction !
     *
     * @param entity TransactionsEntity
     */
    private void validateTransaction(TransactionsEntity entity) {
        List<String> errors = TransactionsValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Transaction is not valide {}", entity);
            throw new InvalidEntityException("La transaction n est pas valide", ErrorCodes.TRANSACTION_NOT_VALID, errors);
        }
    }

    /**
     * Validate Transaction Date End
     *
     * @param entity TransactionsEntity
     */
    private void validateTransactionDateEnd(TransactionsEntity entity) {
        if (entity.getEndDate() != null) {
            if (entity.getEndDate().isBefore(entity.getCreationDate())) {
                log.error("Transaction end date in not valide {}", entity);
                throw new InvalidEntityException("La date de fin de la transaction doit etre superieur à la date de creation", ErrorCodes.TRANSACTION_NOT_VALID);
            }
        }
    }

    /**
     * Save Transaction Histories ! Use after create or update
     *
     * @param entity TransactionsEntity
     * @return TransactionHistoriesEntity
     */
    private TransactionHistoriesEntity saveTransactionHistories(TransactionsEntity entity) {
        TransactionHistoriesEntity transactionHistoriesEntity = new TransactionHistoriesEntity();

        transactionHistoriesEntity.setTransactionsByIdTransactions(entity);
        transactionHistoriesEntity.setTransactionPhasesByIdTransactionPhases(entity.getTransactionPhasesByIdTransactionPhases());
        transactionHistoriesEntity.setSaveDate(LocalDateTime.now());

        return transactionHistoriesEntity;
    }

}
