package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.utils.EMF;

import javax.persistence.EntityManager;

/**
 * @author Larché Marie-Élise
 */
public class RolesDaoImpl implements RolesDao {


    @Override
    public RolesEntity findById(int id) {
        EntityManager em = EMF.getEM();
        return em.find(RolesEntity.class, id);
    }


}
