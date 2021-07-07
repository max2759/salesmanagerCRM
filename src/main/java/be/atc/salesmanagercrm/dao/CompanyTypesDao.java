package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CompanyTypesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
public interface CompanyTypesDao {

    List<CompanyTypesEntity> findAll();

    CompanyTypesEntity findById(EntityManager em, int id);
}
