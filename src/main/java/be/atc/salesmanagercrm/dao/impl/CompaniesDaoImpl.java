package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.CompaniesDao;
import be.atc.salesmanagercrm.entities.CompaniesEntity;

import javax.persistence.EntityManager;

public class CompaniesDaoImpl implements CompaniesDao {
    @Override
    public CompaniesEntity findById(EntityManager em, int id) {
        return em.find(CompaniesEntity.class, id);
    }
}
