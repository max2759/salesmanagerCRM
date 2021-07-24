package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.RolesPermissionsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface RolesPermissionsDao {

    RolesPermissionsEntity findById(EntityManager em, int id);

    List<RolesPermissionsEntity> findAllRolesPermissions(EntityManager em);

    List<RolesPermissionsEntity> findAllRolesPermissionsWithIdRole(EntityManager em, int idRole);

    void addRolePermissions(EntityManager em, RolesPermissionsEntity rolesPermissionsEntity);

    void update(EntityManager em, RolesPermissionsEntity rolesPermissionsEntity);

    RolesPermissionsEntity getComboRolePerm(EntityManager em, int idRole, int idPerm);

    void delete(EntityManager em, RolesPermissionsEntity rolesPermissionsEntity);

    void deleteRolesPermissions(EntityManager em, int idRole);

    List<RolesPermissionsEntity> findPermissionWithRole(EntityManager em, int idRole);


}
