package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.CivilitiesEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface CivilitiesDao {

    List<CivilitiesEntity> findAll();

    CivilitiesEntity findById(EntityManager em, int id);
}
