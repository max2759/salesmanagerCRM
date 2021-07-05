package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.JobTitlesDao;
import be.atc.salesmanagercrm.dao.impl.JobTitlesDaoImpl;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
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
@Named(value = "jobtitlesBean")
@ViewScoped
public class JobTitlesBean implements Serializable {

    @Getter
    @Setter
    private JobTitlesDao jobTitlesDao = new JobTitlesDaoImpl();

    @Getter
    @Setter
    private List<JobTitlesEntity> jobTitlesEntities;

    @Getter
    @Setter
    private JobTitlesEntity jobTitlesEntity = new JobTitlesEntity();

    /**
     * public method that call addJobTitle
     */
    public void saveJobTitles() {
        addJobTitle(jobTitlesEntity);
    }

    /**
     * Save job title
     *
     * @param jobTitlesEntity
     */
    protected void addJobTitle(JobTitlesEntity jobTitlesEntity) {

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkJobTitlesLabel(jobTitlesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
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
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
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
    protected JobTitlesEntity findById(int id) {
        if (id == 0) {
            log.error("Job_Titles ID is null");
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
     * Update JobTitles
     *
     * @param jobTitlesEntity
     */
    protected void update(JobTitlesEntity jobTitlesEntity) {

        try {
            findById(jobTitlesEntity.getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkJobTitlesLabel(jobTitlesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
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
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
        } finally {
            em.clear();
            em.clear();
        }

    }


}
