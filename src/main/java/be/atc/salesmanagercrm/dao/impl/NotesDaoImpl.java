package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.NotesDao;
import be.atc.salesmanagercrm.entities.NotesEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public class NotesDaoImpl extends EntityFinderImpl<NotesEntity> implements NotesDao {
    @Override
    public void save(EntityManager em, NotesEntity entity) {
        em.persist(entity);
    }

    @Override
    public NotesEntity findById(EntityManager em, int id) {
        return em.find(NotesEntity.class, id);
    }

    @Override
    public List<NotesEntity> findNotesEntityByContactsByIdContacts(EntityManager em, int id) {

        return em.createNamedQuery("Notes.findNotesEntityByContactsByIdContacts",
                NotesEntity.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<NotesEntity> findNotesEntityByCompaniesByIdCompanies(EntityManager em, int id) {

        return em.createNamedQuery("Notes.findNotesEntityByCompaniesByIdCompanies",
                NotesEntity.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<NotesEntity> findAll() {
        return this.findByNamedQuery("Notes.findAll", new NotesEntity());
    }

    @Override
    public void delete(EntityManager em, NotesEntity entity) {
        em.remove(em.merge(entity));
    }

    @Override
    public void update(EntityManager em, NotesEntity entity) {
        em.merge(entity);
    }
}
