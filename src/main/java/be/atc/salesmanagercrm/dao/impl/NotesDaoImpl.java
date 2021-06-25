package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.NotesDao;
import be.atc.salesmanagercrm.entities.NotesEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public class NotesDaoImpl implements NotesDao {
    @Override
    public void save(NotesEntity entity, EntityManager em) {
        em.persist(entity);
    }

    @Override
    public NotesEntity findById(int id) {
        return null;
    }

    @Override
    public List<NotesEntity> findNotesEntityByContactsByIdContacts(int id) {
        return null;
    }

    @Override
    public List<NotesEntity> findNotesEntityByCompagniesByIdCompagnies(int id) {
        return null;
    }

    @Override
    public List<NotesEntity> findAll() {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(NotesEntity entity) {

    }
}
