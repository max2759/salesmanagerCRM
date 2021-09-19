package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.PermissionsEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface PermissionsDao {

    PermissionsEntity findById(EntityManager em, int id);

    List<PermissionsEntity> findAllPermissions(EntityManager em);

    void register(EntityManager em, PermissionsEntity permissionsEntity);

    Optional<PermissionsEntity> findByLabel(EntityManager em, String label);


}
