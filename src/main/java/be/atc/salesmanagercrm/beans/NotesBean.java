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
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Named(value = "notesBean")
@SessionScoped
public class NotesBean implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;
    UsersEntity usersEntity = new UsersEntity();
    @Getter
    @Setter
    private NotesEntity entity;
    @Inject
    private CheckEntities checkEntities;

    public void save() {

        usersEntity.setId(1);

//        NotesEntity entity = new NotesEntity();

        entity.setCreationDate(LocalDateTime.now());
        entity.setUsersByIdUsers(usersEntity);
//        entity.setMessage("test");


        try {
            validateNote(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

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
            NotesDao dao = new NotesDaoImpl();
            dao.save(entity, em);
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

    private void validateNote(NotesEntity entity) {
        List<String> errors = NotesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Note is not valide {}", entity);
            log.info(errors.toString());
            throw new InvalidEntityException("La note n est pas valide", ErrorCodes.NOTE_NOT_VALID, errors);
        }
    }

    public void createNewEntity() {
        entity = new NotesEntity();
    }

}
