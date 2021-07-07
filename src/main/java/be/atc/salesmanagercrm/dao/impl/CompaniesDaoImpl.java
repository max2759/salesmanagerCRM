package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.CompaniesDao;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

public class CompaniesDaoImpl extends EntityFinderImpl<CompaniesEntity> implements CompaniesDao {

    @Override
    public void add(EntityManager em, CompaniesEntity companiesEntity) {
        em.persist(companiesEntity);
    }

    @Override
    public CompaniesEntity findById(EntityManager em, int id) {
        return em.find(CompaniesEntity.class, id);
    }

    @Override
    public List<CompaniesEntity> findAll(EntityManager em, int idUser) {
        return this.findByNamedQuery("Companies.findAll", new CompaniesEntity());
    }

    @Override
    public void update(EntityManager em, CompaniesEntity companiesEntity) {
        em.merge(companiesEntity);
    }

    @Override
    public void delete(EntityManager em, CompaniesEntity companiesEntity) {
        em.remove(em.merge(companiesEntity));
    }
}
