package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.entities.TaskTypesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


@Slf4j
class CheckEntitiesTest {

    @Test
    void checkContact() {

        ContactsEntity entity = new ContactsEntity();

        entity.setId(2);

        CheckEntities checkEntities = new CheckEntities();

        log.info("Test de départ");
        try {
            checkEntities.checkContact(entity);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        log.info("test de fin");

    }

    @Test
    void checkCompany() {

        CompaniesEntity entity = new CompaniesEntity();

        entity.setId(2);

        CheckEntities checkEntities = new CheckEntities();
        log.info("Test de départ");

        try {
            checkEntities.checkCompany(entity);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        log.info("test de fin");
    }

    @Test
    void checkUser() {

        UsersEntity entity = new UsersEntity();

        entity.setId(3);

        CheckEntities checkEntities = new CheckEntities();
        log.info("Test de départ");

        try {
            checkEntities.checkUser(entity);
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        log.info("test de fin");
    }

    @Test
    void checkTaskType() {
        TaskTypesEntity entity = new TaskTypesEntity();

        entity.setId(20);

        CheckEntities checkEntities = new CheckEntities();
        log.info("Test de départ");

        try {
            checkEntities.checkTaskType(entity);
        } catch (EntityNotFoundException exception) {
            log.info("test on est dans le catch");
            return;
        }

        log.info("test de fin");
    }
}