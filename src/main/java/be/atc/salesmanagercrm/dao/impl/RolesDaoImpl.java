package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.entities.RolesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Larché Marie-Élise
 */
public class RolesDaoImpl implements RolesDao {


    @Override
    public RolesEntity findById(EntityManager em, int id) {
        return em.find(RolesEntity.class, id);
    }

    @Override
    public List<RolesEntity> findAllRoles(EntityManager em) {
        return em.createNamedQuery("Roles.findAllRoles",
                RolesEntity.class)
                .getResultList();
    }


}
