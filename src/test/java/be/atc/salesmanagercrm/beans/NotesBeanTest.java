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
    public void initNotes() {
        log.info("Appel avant chaque test");
        notesBean = new NotesBean();
    }

    @AfterEach
    public void undefOrders() {
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
    void findbyId() {
        int id = 1;
        NotesEntity notesEntity = null;
        try {
            notesEntity = notesBean.findById(id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = notesEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(true);
    }

    @Test
    void findEntityNotesEntityByContactsByIdContactsShouldReturnTrue() {
        int id = 1;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByContactsByIdContacts(id);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findEntityNotesEntityByContactsByIdContactsShouldReturnFalse() {
        int id = 15111;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByContactsByIdContacts(id);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }


    @Test
    void findEntityNotesEntityByCompaniesByIdCompaniesShouldReturnTrue() {
        int id = 1;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByCompaniesByIdCompanies(id);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findEntityNotesEntityByCompaniesByIdCompaniesShouldReturnFalse() {
        int id = 15111;

        List<NotesEntity> notesEntities = notesBean.findNotesEntityByCompaniesByIdCompanies(id);

        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }

    @Test
    void findAll() {
        List<NotesEntity> notesEntities = notesBean.findAll();


        boolean test = !notesEntities.isEmpty();

        log.info("Le test vaut : " + test + ". La liste contient : " + notesEntities.stream().count() + " notes");

        assertThat(test).isEqualTo(true);
    }

    @Test
    void delete() {
        int id = 4;

        notesBean.delete(id);

    }

    @Test
    void update() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(1);

        NotesEntity notesEntity = new NotesEntity();
        notesEntity.setId(10);
        notesEntity.setMessage("Message modifié");
        notesEntity.setUsersByIdUsers(usersEntity);

        notesBean.update(notesEntity);
    }
}