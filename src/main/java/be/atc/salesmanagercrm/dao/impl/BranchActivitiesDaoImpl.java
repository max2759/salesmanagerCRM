package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.BranchActivitiesDao;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class BranchActivitiesDaoImpl extends EntityFinderImpl<BranchActivitiesEntity> implements BranchActivitiesDao {

    @Override
    public void add(EntityManager em, BranchActivitiesEntity branchActivitiesEntity) {
        em.persist(branchActivitiesEntity);
    }

    @Override
    public List<BranchActivitiesEntity> findAll() {
        return this.findByNamedQuery("BranchActivities.findAll", new BranchActivitiesEntity());
    }

    @Override
    public void update(EntityManager em, BranchActivitiesEntity branchActivitiesEntity) {
        em.merge(branchActivitiesEntity);
    }

    @Override
    public BranchActivitiesEntity findById(EntityManager em, int id) {
        return em.find(BranchActivitiesEntity.class, id);
    }

    @Override
    public boolean findByLabel(EntityManager em, String label) {
        try {
            em.createNamedQuery("BranchActivities.findByLabel",
                    BranchActivitiesEntity.class)
                    .setParameter("label", label)
                    .getSingleResult();
            return true;
        } catch (Exception ex) {
            log.info("label n'existe pas");
            return false;
        }
    }

    @Override
    public BranchActivitiesEntity findByLabelEntity(EntityManager em, String label) {
        return em.createNamedQuery("BranchActivities.findByLabel",
                BranchActivitiesEntity.class)
                .setParameter("label", label)
                .getSingleResult();
    }
}
