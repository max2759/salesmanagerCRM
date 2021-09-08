package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.TransactionHistoriesDao;
import be.atc.salesmanagercrm.dao.TransactionsDao;
import be.atc.salesmanagercrm.dao.impl.TransactionHistoriesDaoImpl;
import be.atc.salesmanagercrm.dao.impl.TransactionsDaoImpl;
import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.AccessDeniedException;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.TransactionsValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
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
@ViewScoped
public class TransactionsBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = -5437009720221374165L;

    @Getter
    @Setter
    private TransactionsDao dao = new TransactionsDaoImpl();

    @Getter
    @Setter
    private TransactionsEntity transactionsEntity;
    @Getter
    @Setter
    private TransactionsEntity selectedTransactionEntity;

    @Getter
    @Setter
    private List<TransactionsEntity> transactionsEntities;
    @Getter
    private final LocalDateTime now = LocalDateTime.now().plusSeconds(10);
    @Getter
    @Setter
    private List<TransactionsEntity> transactionsEntitiesFiltered;

    @Inject
    private TransactionHistoriesBean transactionHistoriesBean;
    @Inject
    private UsersBean usersBean;
    @Inject
    private AccessControlBean accessControlBean;

    /**
     * Save entity form
     */
    public void saveEntity() {
        log.info("TransactionsBean => method : saveEntity()");

        log.info("TransactionsEntity = : " + transactionsEntity);

        transactionsEntity.setUsersByIdUsers(usersBean.getUsersEntity());

        save(transactionsEntity);
        findAllEntitiesAndFilter();

    }

    /**
     * Update Entity form
     */
    public void updateEntity() {
        log.info("TransactionsBean => method : updateEntity()");

        log.info("transactionsEntity = : " + this.transactionsEntity);

        update(this.transactionsEntity);

        findAllEntitiesAndFilter();
    }

    /**
     * Method for delete entity
     */
    public void deleteEntity() {
        log.info("TransactionsBean => method : deleteEntity()");

        log.info("Param : " + getParam("idTransaction"));
        int idTransaction;
        FacesMessage msg;
        try {
            idTransaction = Integer.parseInt(getParam("idTransaction"));
        } catch (NumberFormatException exception) {
            log.info(exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        delete(idTransaction, usersBean.getUsersEntity());

        findAllEntitiesAndFilter();
    }

    /**
     * Update entity form
     */
    public void showModalUpdate() {
        log.info("TransactionsBean => method : showModalUpdate()");

        log.info("param : " + getParam("idEntity"));

        int idTransaction;
        FacesMessage msg;
        try {
            idTransaction = Integer.parseInt(getParam("idEntity"));
        } catch (NumberFormatException exception) {
            log.info(exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        try {
            transactionsEntity = findById(idTransaction, usersBean.getUsersEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Create New instance
     */
    public void createNewEntity() {
        log.info("TransactionsBean => method : createNewEntity()");

        transactionsEntity = new TransactionsEntity();
    }

    /**
     * Find All Transactions and filter
     */
    public void findAllEntitiesAndFilter() {
        log.info("TransactionsBean => method : findAllEntitiesAndFilter()");

        this.transactionsEntities = findAll(usersBean.getUsersEntity());
    }

    /**
     * Call this method in transactionDetails
     */
    public void displayOneTransaction() {
        log.info("TransactionsBean => method : displayOneTransaction()");

        log.info("Param : " + getParam("idTransaction"));
        int idTransaction;
        FacesMessage msg;

        try {
            idTransaction = Integer.parseInt(getParam("idTransaction"));
        } catch (NumberFormatException exception) {
            log.info(exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        try {
            transactionsEntity = findById(idTransaction, usersBean.getUsersEntity());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        transactionHistoriesBean.findAllEntities(idTransaction, usersBean.getUsersEntity());
    }

    /**
     * Method to show modal in create transaction
     */
    public void showModalCreate() {
        log.info("TransactionsBean => method : showModalCreate()");
        transactionsEntity = new TransactionsEntity();
    }

    /**
     * Save Transaction Entity
     *
     * @param entity TransactionsEntity
     */
    protected void save(TransactionsEntity entity) {
        log.info("TrasnactionsBean => method : save(TransactionsEntity entity)");

        try {
            accessControlBean.checkPermission("addTransactions");
        } catch (AccessDeniedException exception) {
            log.info(exception.getMessage());
            accessControlBean.hasNotPermission();
            return;
        }

        try {
            validateTransaction(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        entity.setCreationDate(LocalDateTime.now());

        FacesMessage msg;

        if (entity.getEndDate() != null) {
            try {
                validateTransactionDateEnd(entity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.validator.dateEnd"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (entity.getTransactionTypesByIdTransactionTypes() != null) {
            try {
                checkEntities.checkTransactionTypes(entity.getTransactionTypesByIdTransactionTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactionTypeNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getTransactionPhasesByIdTransactionPhases() != null) {
            try {
                checkEntities.checkTransactionPhases(entity.getTransactionPhasesByIdTransactionPhases());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactionPhaseNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "transactions.saved"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }
    }


    /**
     * Find Transaction entity by id
     *
     * @param id          Transaction
     * @param usersEntity UsersEntity
     * @return TransactionsEntity
     */
    protected TransactionsEntity findById(int id, UsersEntity usersEntity) {
        log.info("TransactionsBean => method : findById(int id, UsersEntity usersEntity)");

        FacesMessage msg;

        if (id == 0) {
            log.error("Transaction ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new EntityNotFoundException(
                    "L ID de la tansaction est incorrect",
                    ErrorCodes.TRANSACTION_NOT_FOUND
            );
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new EntityNotFoundException(
                    "Aucun utilisateur n a ete trouve", ErrorCodes.USER_NOT_FOUND
            );
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findById(em, id, usersEntity.getId());
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune transaction avec l ID " + id + " et l ID User " + usersEntity.getId() + " n a ete trouvee dans la BDD",
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
     * @param contactsEntity ContactsEntity
     * @param usersEntity    UsersEntity
     * @return List TransactionsEntities
     */
    protected List<TransactionsEntity> findTransactionsEntityByContactsByIdContacts(ContactsEntity contactsEntity, UsersEntity usersEntity) {
        log.info("TransactionsBean => method : findTransactionsEntityByContactsByIdContacts(ContactsEntity contactsEntity, UsersEntity usersEntity)");

        FacesMessage msg;
        if (contactsEntity == null) {
            log.error("Contact Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();

        List<TransactionsEntity> transactionsEntities;
        try {
            transactionsEntities = dao.findTransactionsEntityByContactsByIdContacts(em, contactsEntity.getId(), usersEntity.getId());
        } finally {
            em.clear();
            em.close();
        }

        return transactionsEntities;
    }


    /**
     * Find Transactions entities by id company
     *
     * @param companiesEntity CompaniesEntity
     * @return List TransactionsEntities
     */
    protected List<TransactionsEntity> findTransactionsEntityByCompaniesByIdCompanies(CompaniesEntity companiesEntity, UsersEntity usersEntity) {
        log.info("TransactionsBean => method : findTransactionsEntityByCompaniesByIdCompanies(CompaniesEntity companiesEntity, UsersEntity usersEntity)");

        FacesMessage msg;
        if (companiesEntity == null) {
            log.error("Company Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<TransactionsEntity> transactionsEntities;
        try {
            transactionsEntities = dao.findTransactionsEntityByCompaniesByIdCompanies(em, companiesEntity.getId(), usersEntity.getId());
        } finally {
            em.clear();
            em.close();
        }

        return transactionsEntities;
    }

    /**
     * Find All Transactions Entities
     *
     * @return List TransactionsEntity
     */
    protected List<TransactionsEntity> findAll(UsersEntity usersEntity) {
        log.info("TransactionsBean => method : findAll(UsersEntity usersEntity)");

        FacesMessage msg;
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<TransactionsEntity> transactionsEntities;
        try {
            transactionsEntities = dao.findAll(em, usersEntity.getId());
        } finally {
            em.clear();
            em.close();
        }

        return transactionsEntities;
    }

    /**
     * Find All Transactions Entities by Phase
     *
     * @return List TransactionsEntity
     */
    protected List<TransactionsEntity> findAllByPhase(UsersEntity usersEntity, String phaseTransaction) {
        log.info("TransactionsBean => method : findAllByPhase(UsersEntity usersEntity, String phaseTransaction)");

        FacesMessage msg;
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<TransactionsEntity> transactionsEntitiesToFind;
        try {
            transactionsEntitiesToFind = dao.findAllByPhase(em, usersEntity.getId(), phaseTransaction);
        } finally {
            em.clear();
            em.close();
        }

        return transactionsEntitiesToFind;
    }


    /**
     * Update TransactionsEntity
     *
     * @param entity TransactionsEntity
     */
    protected void update(TransactionsEntity entity) {
        log.info("TransactionsBean => method : update(TransactionsEntity entity)");

        try {
            accessControlBean.checkPermission("updateTransactions");
        } catch (AccessDeniedException exception) {
            log.info(exception.getMessage());
            accessControlBean.hasNotPermission();
            return;
        }

        try {
            validateTransaction(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage msg;
        try {
            TransactionsEntity transactionsEntityToFind = findById(entity.getId(), entity.getUsersByIdUsers());
            entity.setCreationDate(transactionsEntityToFind.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (entity.getEndDate() != null) {
            try {
                validateTransactionDateEnd(entity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.validator.dateEnd"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (entity.getTransactionTypesByIdTransactionTypes() != null) {
            try {
                checkEntities.checkTransactionTypes(entity.getTransactionTypesByIdTransactionTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactionTypeNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getTransactionPhasesByIdTransactionPhases() != null) {
            try {
                checkEntities.checkTransactionPhases(entity.getTransactionPhasesByIdTransactionPhases());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactionPhaseNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "transactions.updated"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }

    }


    /**
     * Logical Delete transaction
     *
     * @param id          int
     * @param usersEntity UsersEntity
     */
    protected void delete(int id, UsersEntity usersEntity) {
        log.info("TransactionsBean => method : delete(int id, TransactionsEntity entity)");

        try {
            accessControlBean.checkPermission("deleteTransactions");
        } catch (AccessDeniedException exception) {
            log.info(exception.getMessage());
            accessControlBean.hasNotPermission();
            return;
        }

        FacesMessage msg;
        if (id == 0) {
            log.error("Transaction ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if (usersEntity == null) {
            log.error("User Entity is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        TransactionsEntity transactionsEntityToDelete;
        try {
            transactionsEntityToDelete = findById(id, usersEntity);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "transactions.deleted"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Logical delete echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
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
        log.info("TransactionsBean => method : validateTransaction(TransactionsEntity entity)");

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
        log.info("TransactionsBean => method : validateTransactionDateEnd(TransactionsEntity entity)");

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
        log.info("TransactionsBean => method : saveTransactionHistories(TransactionsEntity entity)");

        TransactionHistoriesEntity transactionHistoriesEntity = new TransactionHistoriesEntity();

        transactionHistoriesEntity.setTransactionsByIdTransactions(entity);
        transactionHistoriesEntity.setTransactionPhasesByIdTransactionPhases(entity.getTransactionPhasesByIdTransactionPhases());
        transactionHistoriesEntity.setSaveDate(LocalDateTime.now());

        return transactionHistoriesEntity;
    }
}
