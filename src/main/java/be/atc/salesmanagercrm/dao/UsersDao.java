package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.UsersEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface UsersDao {

    UsersEntity findById(EntityManager em, int id);

    UsersEntity findNUserByUsernameAndPassword(EntityManager em, String username, String password);

    UsersEntity findByUsername(String username);

    void register(EntityManager em, UsersEntity usersEntity);

    List<UsersEntity> findAllUsers(EntityManager em);
}
