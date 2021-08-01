package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CompaniesContactsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface CompaniesContactsDao {

    void add(EntityManager em, CompaniesContactsEntity companiesContactsEntity);

    void update(EntityManager em, CompaniesContactsEntity companiesContactsEntity);

    List<CompaniesContactsEntity> findAll(EntityManager em);
}
