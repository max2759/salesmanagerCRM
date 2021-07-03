package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.JobTitlesDao;
import be.atc.salesmanagercrm.dao.impl.JobTitlesDaoImpl;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
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

    public void saveJobTitles() {
        addJobTitles(jobTitlesEntity);
    }

    protected void addJobTitles(JobTitlesEntity jobTitlesEntity) {

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
        }catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }

    public void findAll() {
        jobTitlesEntities = jobTitlesDao.findAll();
    }


}
