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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Named(value = "notesBean")
@SessionScoped
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
    private NotesEntity entity;


    /**
     * Save Note Entity !
     *
     * @param entity NotesEntity
     */
    protected void save(NotesEntity entity) {

        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(entity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (entity.getContactsByIdContacts() != null) {
            try {
                checkEntities.checkContact(entity.getContactsByIdContacts());
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (entity.getCompaniesByIdCompanies() != null) {
            try {
                checkEntities.checkCompany(entity.getCompaniesByIdCompanies());
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
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
    protected NotesEntity findById(int id) {
        if (id == 0) {
            log.error("Note ID is null");
            return null;
        }

        EntityManager em = EMF.getEM();
        Optional<NotesEntity> notesEntity = Optional.ofNullable(dao.findById(em, id));
        em.clear();
        em.close();
        return notesEntity.orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune Note avec l'ID = " + id + " n a ete trouvee dans la BDD",
                        ErrorCodes.NOTE_NOT_FOUND
                ));
    }

    /**
     * Find Notes entities by id Contact
     *
     * @param id Contact
     * @return List NotesEntity
     */
    protected List<NotesEntity> findNotesEntityByContactsByIdContacts(int id) {
        if (id == 0) {
            log.error("Contact ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<NotesEntity> notesEntities = dao.findNotesEntityByContactsByIdContacts(em, id);

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
    protected List<NotesEntity> findNotesEntityByCompaniesByIdCompanies(int id) {
        if (id == 0) {
            log.error("Contact ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<NotesEntity> notesEntities = dao.findNotesEntityByCompaniesByIdCompanies(em, id);

        em.clear();
        em.close();

        return notesEntities;
    }

    /**
     * Find All Notes Entities
     *
     * @return List NotesEntity
     */
    protected List<NotesEntity> findAll() {
        return dao.findAll();
    }

    /**
     * delete note by id
     *
     * @param id note
     */
    protected void delete(int id) {
        if (id == 0) {
            log.error("Note ID is null");
            return;
        }

        NotesEntity notesEntity;

        try {
            notesEntity = findById(id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
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

    protected void update(NotesEntity entity) {

        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        try {
            NotesEntity notesEntity = findById(entity.getId());
            entity.setCreationDate(notesEntity.getCreationDate());
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }
        CheckEntities checkEntities = new CheckEntities();

        checkEntities.checkUser(entity.getUsersByIdUsers());
        checkEntities.checkContact(entity.getContactsByIdContacts());
        checkEntities.checkCompany(entity.getCompaniesByIdCompanies());

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
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
     * Validate Note !
     *
     * @param entity NotesEntity
     */
    private void validateNote(NotesEntity entity) {
        List<String> errors = NotesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Note is not valide {}", entity);
            log.info(errors.toString());
            throw new InvalidEntityException("La note n est pas valide", ErrorCodes.NOTE_NOT_VALID, errors);
        }
    }

}
