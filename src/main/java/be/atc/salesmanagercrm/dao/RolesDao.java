package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.RolesEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface RolesDao {

    RolesEntity findById(EntityManager em, int id);

    List<RolesEntity> findAllRoles(EntityManager em);

}
