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

    boolean findByLabel(EntityManager em, String label);
}
