package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.utils.EMF;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author Larché Marie-Élise
 */
public class UsersDaoImpl implements UsersDao {


    @Override
    public UsersEntity findById(EntityManager em, int id) {
        return em.find(UsersEntity.class, id);
    }

    @Override
    public UsersEntity findNUserByUsernameAndPassword(EntityManager em, String username, String password) {
        return em.createNamedQuery("Users.findNUserByUsernameAndPassword",
                UsersEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }

    @Override
    public UsersEntity findByUsername(String username) {
        EntityManager em = EMF.getEM();
        try {
            return em.createNamedQuery("Users.findByUsername",
                    UsersEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }


    }

    public void register(EntityManager em, UsersEntity usersEntity) {
        em.persist(usersEntity);
    }

    @Override
    public List<UsersEntity> findAllUsers(EntityManager em) {
        return em.createNamedQuery("Users.findAllUsers",
                UsersEntity.class)
                .getResultList();
    }

}
