package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.NotesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface NotesDao {

    void save(EntityManager em, NotesEntity entity);

    NotesEntity findById(EntityManager em, int id);

    List<NotesEntity> findNotesEntityByContactsByIdContacts(EntityManager em, int id);

    List<NotesEntity> findNotesEntityByCompaniesByIdCompanies(EntityManager em, int id);

    List<NotesEntity> findAll();

    void delete(EntityManager em, NotesEntity entity);

    void update(EntityManager em, NotesEntity entity);

}
