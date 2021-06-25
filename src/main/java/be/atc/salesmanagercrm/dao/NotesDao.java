package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.NotesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface NotesDao {

    void save(NotesEntity entity, EntityManager em);

    NotesEntity findById(int id);

    List<NotesEntity> findNotesEntityByContactsByIdContacts(int id);

    List<NotesEntity> findNotesEntityByCompagniesByIdCompagnies(int id);

    List<NotesEntity> findAll();

    void delete(int id);

    void update(NotesEntity entity);

}
