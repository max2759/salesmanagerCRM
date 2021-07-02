package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.NotesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NotesBeanTest {

    private NotesBean notesBean;

    @BeforeEach
    public void init() {
        log.info("Appel avant chaque test");
        notesBean = new NotesBean();
    }

    @AfterEach
    public void after() {
        log.info("Appel après chaque test");
        notesBean = null;
    }

    @Test
    void save() {

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId(1);

        NotesEntity entity = new NotesEntity();

        entity.setUsersByIdUsers(usersEntity);
        entity.setMessage("test 4");

        notesBean.save(entity);
    }

    @Test
    void findByIdShouldReturnFalse() {
        int id = 1;
        int idUser = 1014;

        NotesEntity notesEntity = null;
        try {
            notesEntity = notesBean.findById(id, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = notesEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(false);
    }

    @Test
    void findByIdShouldReturnTrue() {
        int id = 1;
        int idUser = 1;

        NotesEntity notesEntity = null;
        try {
            notesEntity = notesBean.findById(id, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = notesEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(true);
    }
    @Test
    void findEntityNotesEntityByContactsByIdContactsShouldReturnTrue() {

        // Mettre un id correct
        int id = 1;
        int idUser = 1;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByContactsByIdContacts(id, idUser);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findEntityNotesEntityByContactsByIdContactsShouldReturnFalse() {

        // Mettre un id incorrect
        int id = 1;

        int idUser = 11;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByContactsByIdContacts(id, idUser);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }


    @Test
    void findEntityNotesEntityByCompaniesByIdCompaniesShouldReturnTrue() {

        // Mettre un id correct
        int id = 1;
        int idUser = 1;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByCompaniesByIdCompanies(id, idUser);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findEntityNotesEntityByCompaniesByIdCompaniesShouldReturnFalse() {

        // Mettre un id incorrect
        int id = 195;
        int idUser = 1;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByCompaniesByIdCompanies(id, idUser);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }

    @Test
    void findAll() {
        int idUser = 1;
        List<NotesEntity> notesEntities = notesBean.findAll(idUser);


        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test + ". La liste contient : " + (long) notesEntities.size() + " notes");

        assertThat(test).isEqualTo(true);
    }

    @Test
    void delete() {
        int id = 12;
        int idUser = 1;

        notesBean.delete(id, idUser);

    }

    @Test
    void update() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(10);

        NotesEntity notesEntity = new NotesEntity();
        notesEntity.setId(1);
        notesEntity.setMessage("Message modifié");
        notesEntity.setUsersByIdUsers(usersEntity);

        notesBean.update(notesEntity);
    }
}