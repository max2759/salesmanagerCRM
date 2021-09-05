package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CompaniesEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface CompaniesDao {

    void add(EntityManager em, CompaniesEntity companiesEntity);

    CompaniesEntity findById(EntityManager em, int id);

    List<CompaniesEntity> findAllCompaniesByIdUser(EntityManager em, int idUser);

    void update(EntityManager em, CompaniesEntity companiesEntity);

    CompaniesEntity findByIdCompanyAndByIdUser(EntityManager em, int id, int idUser);

    List<CompaniesEntity> findCompaniesEntityByIdUser(EntityManager em, int idUser);

    List<CompaniesEntity> findByIdCompaniAndByIdUser(EntityManager em, int id, int idUser);

    List<CompaniesEntity> findActiveCompany(EntityManager em, int idUser);

    List<CompaniesEntity> findDisableCompany(EntityManager em, int idUser);
}
