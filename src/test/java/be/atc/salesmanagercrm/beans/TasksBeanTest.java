package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.enums.EnumPriority;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TasksBeanTest {

    private TasksBean tasksBean;

    @BeforeEach
    public void initNotes() {
        log.info("Appel avant chaque test");
        tasksBean = new TasksBean();
    }

    @AfterEach
    public void undefOrders() {
        log.info("Appel après chaque test");
        tasksBean = null;
    }

    @Test
    void save() {

        UsersEntity usersEntity = new UsersEntity();
        CompaniesEntity companiesEntity = new CompaniesEntity();
        ContactsEntity contactsEntity = new ContactsEntity();
        TaskTypesEntity taskTypesEntity = new TaskTypesEntity();

        usersEntity.setId(1);
        companiesEntity.setId(100);
        contactsEntity.setId(100);
        taskTypesEntity.setId(1);

        TasksEntity entity = new TasksEntity();

        entity.setUsersByIdUsers(usersEntity);
        entity.setPriority(EnumPriority.ELEVEE);
        entity.setEndDate(LocalDateTime.now().plusMonths(1));
        entity.setTitle("Task Test");

//        entity.setCompaniesByIdCompanies(companiesEntity);
//        entity.setContactsByIdContacts(contactsEntity);
        entity.setTaskTypesByIdTaskTypes(taskTypesEntity);
        tasksBean.save(entity);
    }

    @Test
    void findByIdShouldReturnFalse() {
        int id = 14574;
        TasksEntity tasksEntity = null;

        try {
            tasksEntity = tasksBean.findById(id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = tasksEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(false);

    }


    @Test
    void findByIdShouldReturnTrue() {
        int id = 12;
        TasksEntity tasksEntity = null;

        try {
            tasksEntity = tasksBean.findById(id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = tasksEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(true);

    }
}