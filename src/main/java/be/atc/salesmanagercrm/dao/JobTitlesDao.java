package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.JobTitlesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
public interface JobTitlesDao {

    void add(EntityManager em, JobTitlesEntity jobTitlesEntity);

    List<JobTitlesEntity> findAll();

    void update(EntityManager em, JobTitlesEntity jobTitlesEntity);

    JobTitlesEntity findById(EntityManager em, int id);

    List<JobTitlesEntity> findByLabel(EntityManager em, String label);

}
