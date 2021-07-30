package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CitiesEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface CitiesDao {

    List<CitiesEntity> findAll();

    CitiesEntity findById(EntityManager em, int id);
}
