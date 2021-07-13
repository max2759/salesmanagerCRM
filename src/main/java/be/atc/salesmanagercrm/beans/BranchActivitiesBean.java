package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.BranchActivitiesDao;
import be.atc.salesmanagercrm.dao.impl.BranchActivitiesDaoImpl;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.BranchActivitiesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Getter
    @Setter
    private List<BranchActivitiesEntity> branchActivitiesEntityList;

    @Getter
    @Setter
    private String sendType = "";

    /**
     * Public method that call either addBranchActivities if sendType=add or updateBranchActivitiesLabel if sendType=edit
     */
    public void saveBranchActivities() {

        if (sendType.equalsIgnoreCase("add")) {
            addBranchActivities(branchActivitiesEntity);
        } else if (sendType.equalsIgnoreCase("edit")) {
            updateBranchActivitiesLabel(branchActivitiesEntity);
        }
    }


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

        FacesMessage facesMessage;
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkBranchActivitiesLabel(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "branchActivities.labelExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "branchActivities.save"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * public method that call findAll
     */
    public List<BranchActivitiesEntity> findAllBranchActivities() {
        return branchActivitiesEntityList = findAll();
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

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Branch_Activities ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "branchActivities.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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

        FacesMessage facesMessage;

        try {
            findById(branchActivitiesEntity.getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "branchActivities.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkBranchActivitiesLabel(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "branchActivities.labelExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "branchActivities.updated"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * Method for checking sendType and open good modal
     */
    public void checkSendType() {
        sendType = getParam("sendType");
        log.info("type envoyé : " + sendType);
        if (sendType.equalsIgnoreCase("edit")) {
            branchActivitiesEntity = findById(Integer.parseInt(getParam("id")));
            log.info("jobtitles : " + branchActivitiesEntity.getId());
        } else if (sendType.equalsIgnoreCase("add")) {
            branchActivitiesEntity = new BranchActivitiesEntity();
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

    /**
     * Get param
     *
     * @param name String
     * @return String
     */
    protected String getParam(String name) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        return params.get(name);
    }

}
