package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CompaniesEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface CompaniesDao {

    void add(EntityManager em, CompaniesEntity companiesEntity);

    CompaniesEntity findById(EntityManager em, int id);

    List<CompaniesEntity> findAll(EntityManager em, int idUser);

    void update(EntityManager em, CompaniesEntity companiesEntity);

    void delete(EntityManager em, CompaniesEntity companiesEntity);

    CompaniesEntity findByIdCompanyAndByIdUser(EntityManager em, int id, int idUser);

    List<CompaniesEntity> findCompaniesEntityByIdUser(EntityManager em, int idUser);
}
