package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.PermissionsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface PermissionsDao {

    PermissionsEntity findById(EntityManager em, int id);

    List<PermissionsEntity> findAllPermissions(EntityManager em);

    void register(EntityManager em, PermissionsEntity permissionsEntity);


}
