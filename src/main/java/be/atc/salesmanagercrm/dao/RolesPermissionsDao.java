package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.RolesPermissionsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface RolesPermissionsDao {

    RolesPermissionsEntity findById(EntityManager em, int id);

    List<RolesPermissionsEntity> findAllRolesPermissions(EntityManager em);

    void addRolePermissions(EntityManager em, RolesPermissionsEntity rolesPermissionsEntity);

    void update(EntityManager em, RolesPermissionsEntity rolesPermissionsEntity);


}
