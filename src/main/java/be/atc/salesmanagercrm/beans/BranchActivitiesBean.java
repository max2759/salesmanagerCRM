package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.BranchActivitiesDao;
import be.atc.salesmanagercrm.dao.impl.BranchActivitiesDaoImpl;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.BranchActivitiesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "branchActivitiesBean")
@ViewScoped
public class BranchActivitiesBean implements Serializable {

    @Getter
    @Setter
    private BranchActivitiesDao branchActivitiesDao = new BranchActivitiesDaoImpl();

    @Getter
    @Setter
    private BranchActivitiesEntity branchActivitiesEntity = new BranchActivitiesEntity();

    @Getter
    @Setter
    private List<BranchActivitiesEntity> branchActivitiesEntityList;

    /**
     * Save branch activities title
     *
     * @param branchActivitiesEntity BranchActitivities
     */
    protected void addBranchActivities(BranchActivitiesEntity branchActivitiesEntity) {

        try {
            validateBranchActivities(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkBranchActivitiesLabel(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            branchActivitiesDao.add(em, branchActivitiesEntity);
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
     * public method that call findAll
     */
    public void findAllBranchActivities() {
        branchActivitiesEntityList = findAll();
    }

    /**
     * Find all Branch activities
     *
     * @return List of BranchActivities
     */
    protected List<BranchActivitiesEntity> findAll() {
        EntityManager em = EMF.getEM();
        List<BranchActivitiesEntity> branchActivitiesEntities = branchActivitiesDao.findAll();
        em.clear();
        em.close();
        return branchActivitiesEntities;
    }

    /**
     * Find Branch Activity by label
     *
     * @param id BranchActivities
     * @return BranchActivitiesEntity
     */
    protected BranchActivitiesEntity findById(int id) {
        if (id == 0) {
            log.error("Branch_Activities ID is null");
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return branchActivitiesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun secteur d'activié avec l ID " + id + " n'a été trouve dans la DB",
                    ErrorCodes.BRANCHACTIVITIES_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Update BranchActivities
     *
     * @param branchActivitiesEntity BranchActivities
     */
    protected void updateBranchActivitiesLabel(BranchActivitiesEntity branchActivitiesEntity) {


        try {
            validateBranchActivities(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            findById(branchActivitiesEntity.getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkBranchActivitiesLabel(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            branchActivitiesDao.update(em, branchActivitiesEntity);
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
     * Validate BranchActivities !
     *
     * @param entity BranchActivities
     */
    private void validateBranchActivities(BranchActivitiesEntity entity) {
        List<String> errors = BranchActivitiesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("BranchActivities is not valid {}", entity);
            throw new InvalidEntityException("Le secteur d'activité n'est pas valide", ErrorCodes.BRANCHACTIVITIESLABEL_NOT_VALID, errors);
        }
    }

}
