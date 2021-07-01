package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.NotesDao;
import be.atc.salesmanagercrm.dao.impl.NotesDaoImpl;
import be.atc.salesmanagercrm.entities.NotesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.NotesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Named(value = "notesBean")
@RequestScoped
public class NotesBean implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @Getter
    @Setter
    private NotesDao dao = new NotesDaoImpl();
    @Getter
    @Setter
    private UsersEntity usersEntity = new UsersEntity();
    @Getter
    @Setter
    private NotesEntity notesEntity;


    /**
     * Save Note Entity !
     *
     * @param entity NotesEntity
     */
    protected void save(NotesEntity entity) {

        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        entity.setCreationDate(LocalDateTime.now());

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.save(em, entity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }

    /**
     * Find Note by ID
     *
     * @param id NotesEntity
     * @return Notes Entity
     */
    protected NotesEntity findById(int id, int idUser) {
        if (id == 0) {
            log.error("Task ID is null");
            return null;
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findById(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune Note avec l ID " + id + " et l ID User " + idUser + " n a ete trouvee dans la BDD",
                    ErrorCodes.NOTE_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find Notes entities by id Contact
     *
     * @param id Contact
     * @return List NotesEntity
     */
    protected List<NotesEntity> findNotesEntityByContactsByIdContacts(int id, int idUser) {
        if (id == 0) {
            log.error("Contact ID is null");
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<NotesEntity> notesEntities = dao.findNotesEntityByContactsByIdContacts(em, id, idUser);

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * Find Notes Entities by Id Company
     *
     * @param id Company
     * @return List NotesEntity
     */
    protected List<NotesEntity> findNotesEntityByCompaniesByIdCompanies(int id, int idUser) {
        if (id == 0) {
            log.error("Contact ID is null");
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<NotesEntity> notesEntities = dao.findNotesEntityByCompaniesByIdCompanies(em, id, idUser);

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * Find All Notes Entities
     *
     * @return List NotesEntity
     */
    protected List<NotesEntity> findAll(int idUser) {
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }
        EntityManager em = EMF.getEM();
        List<NotesEntity> notesEntities = dao.findAll(em, idUser);

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * delete note by id
     *
     * @param id note
     */
    protected void delete(int id, int idUser) {
        if (id == 0) {
            log.error("Task ID is null");
            return;
        }
        if (idUser == 0) {
            log.error("User ID is null");
            return;
        }

        NotesEntity notesEntity;

        try {
            notesEntity = findById(id, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        EntityManager em = EMF.getEM();
        em.getTransaction();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.delete(em, notesEntity);
            tx.commit();
            log.info("Delete ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.error("Delete Error");
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Update NoteEntity
     *
     * @param entity NoteEntity
     */
    protected void update(NotesEntity entity) {

        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        try {
            NotesEntity notesEntity = findById(entity.getId(), entity.getUsersByIdUsers().getId());
            entity.setCreationDate(notesEntity.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }
        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            tx.commit();
            log.info("Update ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
        } finally {
            em.clear();
            em.clear();
        }

    }

    /**
     * Validate Note !
     *
     * @param entity NotesEntity
     */
    private void validateNote(NotesEntity entity) {
        List<String> errors = NotesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Note is not valide {}", entity);
            throw new InvalidEntityException("La note n est pas valide", ErrorCodes.NOTE_NOT_VALID, errors);
        }
    }

}
