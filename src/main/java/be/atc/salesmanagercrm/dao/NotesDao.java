package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.NotesEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Younes Arifi
 */
public interface NotesDao {

    void save(EntityManager em, NotesEntity entity);

    Optional<NotesEntity> findById(EntityManager em, int id, int idUser);

    List<NotesEntity> findNotesEntityByContactsByIdContacts(EntityManager em, int id, int idUser);

    List<NotesEntity> findNotesEntityByCompaniesByIdCompanies(EntityManager em, int id, int idUser);

    List<NotesEntity> findAll(EntityManager em, int idUser);

    void delete(EntityManager em, NotesEntity entity);

    void update(EntityManager em, NotesEntity entity);

}
