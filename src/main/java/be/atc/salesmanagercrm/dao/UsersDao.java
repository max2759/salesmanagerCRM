package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.UsersEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface UsersDao {

    UsersEntity findById(EntityManager em, int id);

    UsersEntity findNUserByUsernameAndPassword(EntityManager em, String username, String password);

    UsersEntity findByUsername(EntityManager em, String username);

    void register(EntityManager em, UsersEntity usersEntity);

    void update(EntityManager em, UsersEntity usersEntity);

    List<UsersEntity> findAllUsers(EntityManager em);

    UsersEntity findPassword(EntityManager em, int id);

    UsersEntity findUsername(EntityManager em, int id);

    List<UsersEntity> findActiveUsers(EntityManager em);

    List<UsersEntity> findDisableUsers(EntityManager em);
}
