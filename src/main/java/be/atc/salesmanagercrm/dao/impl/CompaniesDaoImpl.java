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
        return em.createNamedQuery("Companies.findById",
                        CompaniesEntity.class)
                .setParameter("id", id)
                .getSingleResult();
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
    public CompaniesEntity findByIdCompanyAndByIdUser(EntityManager em, int id, int idUser) {
        return em.createNamedQuery("Companies.findByIdCompanyAndByIdUser",
                CompaniesEntity.class)
                .setParameter("id", id)
                .setParameter("idUser", idUser)
                .getSingleResult();
    }

    @Override
    public List<CompaniesEntity> findCompaniesEntityByIdUser(EntityManager em, int idUser) {

        return em.createNamedQuery("Companies.findCompaniesEntityByIdUser",
                CompaniesEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public List<CompaniesEntity> findActiveCompany(EntityManager em, int idUser) {
        return em.createNamedQuery("Companies.findActiveCompany",
                CompaniesEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }

    @Override
    public List<CompaniesEntity> findDisableCompany(EntityManager em, int idUser) {
        return em.createNamedQuery("Companies.findDisableCompany",
                CompaniesEntity.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }
}
