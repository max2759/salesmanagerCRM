package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.VoucherHistoriesDao;
import be.atc.salesmanagercrm.dao.VouchersDao;
import be.atc.salesmanagercrm.dao.impl.VoucherHistoriesDaoImpl;
import be.atc.salesmanagercrm.dao.impl.VouchersDaoImpl;
import be.atc.salesmanagercrm.entities.VoucherHistoriesEntity;
import be.atc.salesmanagercrm.entities.VouchersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.VouchersValidator;
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
@Named(value = "vouchersBean")
@RequestScoped
public class VouchersBean implements Serializable {

    private static final long serialVersionUID = -8603892740414606326L;

    @Getter
    @Setter
    private VouchersDao dao = new VouchersDaoImpl();

    @Getter
    @Setter
    private VouchersEntity vouchersEntity;

    /**
     * Save Voucher Entity
     *
     * @param entity VouchersEntity
     */
    protected void save(VouchersEntity entity) {

        try {
            validateVoucher(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        entity.setCreationDate(LocalDateTime.now());

        if (entity.getEndDate() != null) {
            try {
                validateVoucherDateEnd(entity);
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

        try {
            checkEntities.checkVoucherStatus(entity.getVoucherStatusByIdVoucherStatus());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
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

        VoucherHistoriesEntity voucherHistoriesEntity = saveVoucherHistories(entity);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.save(em, entity);
            VoucherHistoriesDao voucherHistoriesDao = new VoucherHistoriesDaoImpl();
            voucherHistoriesDao.save(em, voucherHistoriesEntity);
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
     * Find Voucher entity by id
     *
     * @param id     Voucher
     * @param idUser User
     * @return VouchersEntity
     */
    protected VouchersEntity findById(int id, int idUser) {
        if (id == 0) {
            log.error("Voucher ID is null");
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
                    "Aucun ticket avec l ID " + id + " et l ID User " + idUser + " n a ete trouve dans la BDD",
                    ErrorCodes.VOUCHER_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find vouchers entities by id contact
     *
     * @param id Contact
     * @return List VouchersEntities
     */
    protected List<VouchersEntity> findVouchersEntityByContactsByIdContacts(int id, int idUser) {
        if (id == 0) {
            log.error("Contact ID is null");
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();

        List<VouchersEntity> vouchersEntities = dao.findVouchersEntityByContactsByIdContacts(em, id, idUser);

        em.clear();
        em.close();

        return vouchersEntities;
    }


    /**
     * Find vouchers entities by id company
     *
     * @param id Company
     * @return List VouchersEntities
     */
    protected List<VouchersEntity> findVouchersEntityByCompaniesByIdCompanies(int id, int idUser) {
        if (id == 0) {
            log.error("Company ID is null");
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<VouchersEntity> vouchersEntities = dao.findVouchersEntityByCompaniesByIdCompanies(em, id, idUser);

        em.clear();
        em.close();

        return vouchersEntities;
    }

    /**
     * Find All vouchers Entities
     *
     * @return List VouchersEntity
     */
    protected List<VouchersEntity> findAll(int idUser) {
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<VouchersEntity> vouchersEntities = dao.findAll(em, idUser);

        em.clear();
        em.close();

        return vouchersEntities;
    }


    /**
     * Update VouchersEntity
     *
     * @param entity VouchersEntity
     */
    protected void update(VouchersEntity entity) {

        try {
            validateVoucher(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            VouchersEntity vouchersEntityToFind = findById(entity.getId(), entity.getUsersByIdUsers().getId());
            entity.setCreationDate(vouchersEntityToFind.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getEndDate() != null) {
            try {
                validateVoucherDateEnd(entity);
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

        try {
            checkEntities.checkVoucherStatus(entity.getVoucherStatusByIdVoucherStatus());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
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

        VoucherHistoriesEntity voucherHistoriesEntity = saveVoucherHistories(entity);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            VoucherHistoriesDao voucherHistoriesDao = new VoucherHistoriesDaoImpl();
            voucherHistoriesDao.save(em, voucherHistoriesEntity);
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
     * Validate Voucher !
     *
     * @param entity VouchersEntity
     */
    private void validateVoucher(VouchersEntity entity) {
        List<String> errors = VouchersValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Voucher is not valide {}", entity);
            throw new InvalidEntityException("Le ticket n est pas valide", ErrorCodes.VOUCHER_NOT_VALID, errors);
        }
    }

    /**
     * Validate Voucher Date End
     *
     * @param entity VouchersEntity
     */
    private void validateVoucherDateEnd(VouchersEntity entity) {
        if (entity.getEndDate() != null) {
            if (entity.getEndDate().isBefore(entity.getCreationDate())) {
                log.error("Voucher end date in not valide {}", entity);
                throw new InvalidEntityException("La date de fin du ticket doit etre superieur à la date de creation", ErrorCodes.VOUCHER_NOT_VALID);
            }
        }
    }

    /**
     * Save Voucher Histories ! Use after create or update
     *
     * @param entity VouchersEntity
     * @return VoucherHistoriesEntity
     */
    private VoucherHistoriesEntity saveVoucherHistories(VouchersEntity entity) {
        VoucherHistoriesEntity voucherHistoriesEntity = new VoucherHistoriesEntity();

        voucherHistoriesEntity.setVouchersByIdVouchers(entity);
        voucherHistoriesEntity.setVoucherStatusByIdVoucherStatus(entity.getVoucherStatusByIdVoucherStatus());
        voucherHistoriesEntity.setSaveDate(LocalDateTime.now());

        return voucherHistoriesEntity;
    }


}
