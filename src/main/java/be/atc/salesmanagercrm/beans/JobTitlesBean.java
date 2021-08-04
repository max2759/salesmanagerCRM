package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.JobTitlesDao;
import be.atc.salesmanagercrm.dao.impl.JobTitlesDaoImpl;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.JobTitlesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "jobtitlesBean")
@ViewScoped
public class JobTitlesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 7299153922194330761L;
    @Getter
    @Setter
    private JobTitlesDao jobTitlesDao = new JobTitlesDaoImpl();

    @Getter
    @Setter
    private JobTitlesEntity jobTitlesEntity = new JobTitlesEntity();

    @Getter
    @Setter
    private List<JobTitlesEntity> jobTitlesEntityList;

    @Getter
    @Setter
    private JobTitlesEntity selectedJobTitlesEntity;

    @Getter
    @Setter
    private List<JobTitlesEntity> jobTitlesEntitiesFiltered;

    @Getter
    @Setter
    private String sendType = "";

    @Getter
    @Setter
    private boolean disable = true;

    /**
     * Public method that call either addJobTitle if sendType=add or update if sendType=edit
     */
    public void saveJobTitles() {

        if (sendType.equalsIgnoreCase("add")) {
            addJobTitle(jobTitlesEntity);
            this.disable = true;
        } else if (sendType.equalsIgnoreCase("edit")) {
            update(jobTitlesEntity);
            this.disable = true;
        }

        findAllJobTitles();
    }

    /**
     * Save job title
     *
     * @param jobTitlesEntity JobTitlesEntity
     */
    protected void addJobTitle(JobTitlesEntity jobTitlesEntity) {

        try {
            validateJobTitles(jobTitlesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage facesMessage;
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkJobTitlesLabel(jobTitlesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "jobTitles.labelExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            jobTitlesDao.add(em, jobTitlesEntity);
            tx.commit();
            log.info("Persist ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "jobTitles.save"), null);
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
     * Find job title by id
     *
     * @param id JobTitles
     * @return JobTitlesEntity
     */
    public JobTitlesEntity findById(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Job_Titles ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "jobTitles.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return jobTitlesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun intitulé de poste avec l ID " + id + " n'a ete trouve dans la DB",
                    ErrorCodes.JOBTITLES_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Method that call findAll and fill jobTitlesEntityList
     */
    public void findAllJobTitles() {
        jobTitlesEntityList = findAll();
    }


    /**
     * Find all jobtitles entities
     *
     * @return List of JobTitles Entity
     */
    protected List<JobTitlesEntity> findAll() {

        EntityManager em = EMF.getEM();
        List<JobTitlesEntity> jobTitlesEntities = jobTitlesDao.findAll();

        em.clear();
        em.close();

        return jobTitlesEntities;
    }

    /**
     * Method for checking sendType and open good modal
     */
    public void checkSendType() {
        sendType = getParam("sendType");
        log.info("type envoyé : " + sendType);
        if (sendType.equalsIgnoreCase("edit")) {
            jobTitlesEntity = findById(Integer.parseInt(getParam("id")));
            log.info("jobtitles : " + jobTitlesEntity.getId());
        } else if (sendType.equalsIgnoreCase("add")) {
            jobTitlesEntity = new JobTitlesEntity();
        }
    }

    /**
     * Update JobTitles
     *
     * @param jobTitlesEntity JobTitlesEntity
     */
    protected void update(JobTitlesEntity jobTitlesEntity) {

        try {
            validateJobTitles(jobTitlesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        FacesMessage facesMessage;

        try {
            findById(jobTitlesEntity.getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "jobTitles.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkJobTitlesLabel(jobTitlesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "jobTitles.labelExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            jobTitlesDao.update(em, jobTitlesEntity);
            tx.commit();
            log.info("Update ok");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "jobTitles.updated"), null);
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
     * Validate JobTitles !
     *
     * @param entity JobTitles
     */
    private void validateJobTitles(JobTitlesEntity entity) {
        List<String> errors = JobTitlesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Job title is not valid {}", entity);
            throw new InvalidEntityException("L'intitulé du poste n'est pas valide", ErrorCodes.JOBTITLES_NOT_VALID, errors);
        }
    }

    /**
     * Auto complete for JobTitlesEntity
     *
     * @param search String
     * @return list of JobTitlesEntity
     */
    public List<JobTitlesEntity> completeJobTitles(String search) {

        String searchLowerCase = search.toLowerCase();

        List<JobTitlesEntity> jobTitlesEntitiesDropdown = jobTitlesDao.findAll();

        return jobTitlesEntitiesDropdown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());
    }


    /**
     * return true if value null and false if value not null
     *
     * @param event event
     */
    public void makeDisable(AjaxBehaviorEvent event) {
        log.info("Start of makeDisable");
        log.info("label : " + ((UIInput) event.getSource()).getValue());

        this.disable = ((UIInput) event.getSource()).getValue() == null || ((UIInput) event.getSource()).getValue() == "";
    }


}
