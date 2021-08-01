package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.ConversationsEntity;

import javax.persistence.EntityManager;
import java.util.List;

public interface ConversationsDao {

    ConversationsEntity findById(EntityManager em, int id);

    void add(EntityManager em, ConversationsEntity conversationsEntity);

    List<ConversationsEntity> findAll(EntityManager em);

    List<ConversationsEntity> find10Last(EntityManager em);

    void update(EntityManager em, ConversationsEntity entity);


}
