package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.ConversationsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface ConversationsDao {

    ConversationsEntity findById(EntityManager em, int id);

    void add(EntityManager em, ConversationsEntity usersEntity);

    List<ConversationsEntity> findAll(EntityManager em);
}
