package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.JobTitlesDao;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

public class JobTitlesDaoImpl extends EntityFinderImpl<JobTitlesEntity> implements JobTitlesDao {

    @Override
    public void add(EntityManager em, JobTitlesEntity jobTitlesEntity) {
        em.persist(jobTitlesEntity);
    }

    @Override
    public List<JobTitlesEntity> findAll() {
        return this.findByNamedQuery("JobTitles.findAll", new JobTitlesEntity());
    }

    @Override
    public void update(EntityManager em, JobTitlesEntity jobTitlesEntity) {
        em.merge(jobTitlesEntity);
    }

    @Override
    public JobTitlesEntity findById(EntityManager em, int id) {
        return em.find(JobTitlesEntity.class, id);
    }
}
