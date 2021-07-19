package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.PermissionsDao;
import be.atc.salesmanagercrm.entities.PermissionsEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Larché Marie-Élise
 */
public class PermissionsDaoImpl implements PermissionsDao {


    @Override
    public PermissionsEntity findById(EntityManager em, int id) {
        return em.find(PermissionsEntity.class, id);
    }

    @Override
    public List<PermissionsEntity> findAllPermissions(EntityManager em) {
        return em.createNamedQuery("Permissions.findAllPermissions",
                PermissionsEntity.class)
                .getResultList();
    }


    @Override
    public void register(EntityManager em, PermissionsEntity permissionsEntity) {
        em.persist(permissionsEntity);
    }


}
