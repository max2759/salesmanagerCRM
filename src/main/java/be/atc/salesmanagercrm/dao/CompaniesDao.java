package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CompaniesEntity;

import javax.persistence.EntityManager;

public interface CompaniesDao {

    CompaniesEntity findById(EntityManager em, int id);
}
