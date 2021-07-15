package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
public interface BranchActivitiesDao {

    void add(EntityManager em, BranchActivitiesEntity branchActivitiesEntity);

    List<BranchActivitiesEntity> findAll();

    void update(EntityManager em, BranchActivitiesEntity branchActivitiesEntity);

    BranchActivitiesEntity findById(EntityManager em, int id);

    List<BranchActivitiesEntity> findByLabel(EntityManager em, String label);

    BranchActivitiesEntity findByLabelEntity(EntityManager em, String label);
}
