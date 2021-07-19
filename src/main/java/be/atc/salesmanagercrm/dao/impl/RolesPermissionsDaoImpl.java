package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.RolesPermissionsDao;
import be.atc.salesmanagercrm.entities.RolesPermissionsEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Larché Marie-Élise
 */
public class RolesPermissionsDaoImpl implements RolesPermissionsDao {


    @Override
    public RolesPermissionsEntity findById(EntityManager em, int id) {
        return em.find(RolesPermissionsEntity.class, id);
    }

    @Override
    public List<RolesPermissionsEntity> findAllRolesPermissions(EntityManager em) {
        return em.createNamedQuery("RolesPermissions.findAllRolesPermissions",
                RolesPermissionsEntity.class)
                .getResultList();
    }


    @Override
    public void update(EntityManager em, RolesPermissionsEntity rolesPermissionsPermissionsEntity) {
        em.merge(rolesPermissionsPermissionsEntity);
    }

    @Override
    public void addRolePermissions(EntityManager em, RolesPermissionsEntity rolesPermissionsPermissionsEntity) {

    }


}
