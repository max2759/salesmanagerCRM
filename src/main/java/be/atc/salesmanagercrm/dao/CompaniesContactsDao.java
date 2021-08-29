package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CompaniesContactsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface CompaniesContactsDao {

    void update(EntityManager em, CompaniesContactsEntity companiesContactsEntity);

    List<CompaniesContactsEntity> findByIdContacts(EntityManager em, int idContacts);

    CompaniesContactsEntity findById(EntityManager em, int id);

    void delete(EntityManager em, CompaniesContactsEntity companiesContactsEntity);

}
