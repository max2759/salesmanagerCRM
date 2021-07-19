package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TransactionsBeanTest {

    private TransactionsBean transactionsBean;

    @BeforeEach
    public void init() {
        log.info("Appel avant chaque test");
        transactionsBean = new TransactionsBean();
    }

    @AfterEach
    public void after() {
        log.info("Appel après chaque test");
        transactionsBean = null;
    }

    @Test
    void save() {

        UsersEntity usersEntity = new UsersEntity();
        CompaniesEntity companiesEntity = new CompaniesEntity();
        ContactsEntity contactsEntity = new ContactsEntity();
        TransactionTypesEntity transactionTypesEntity = new TransactionTypesEntity();
        TransactionPhasesEntity transactionPhasesEntity = new TransactionPhasesEntity();
        double amount = 2000;

        usersEntity.setId(1);
        companiesEntity.setId(1);
        contactsEntity.setId(1);
        transactionTypesEntity.setId(1);
        transactionPhasesEntity.setId(1);

        TransactionsEntity entity = new TransactionsEntity();

        entity.setUsersByIdUsers(usersEntity);
        entity.setCompaniesByIdCompanies(companiesEntity);
        entity.setContactsByIdContacts(contactsEntity);
        entity.setTransactionPhasesByIdTransactionPhases(transactionPhasesEntity);
        entity.setTransactionTypesByIdTransactionTypes(transactionTypesEntity);
        entity.setTitle("Transaction 2 Test");
        entity.setAmount(amount);
        entity.setEndDate(LocalDateTime.now().plusMonths(1));

        transactionsBean.save(entity);
    }


    @Test
    void findByIdShouldReturnFalse() {

        // Mettre un id incorrect
        int idTransaction = 2;
        int idUser = 1;
        TransactionsEntity transactionsEntity = null;

        try {
            transactionsEntity = transactionsBean.findById(idTransaction, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = transactionsEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(false);

    }


    @Test
    void findByIdShouldReturnTrue() {

        // Mettre un id correct
        int idTransaction = 1;
        int idUser = 1;
        TransactionsEntity transactionsEntity = null;

        try {
            transactionsEntity = transactionsBean.findById(idTransaction, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = transactionsEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(true);

    }

    @Test
    void findTransactionsEntityByContactsByIdContactsShouldReturnTrue() {

        // Mettre un id correct
        int idContact = 1;
        int idUser = 1;

        List<TransactionsEntity> transactionsEntities = transactionsBean.findTransactionsEntityByContactsByIdContacts(idContact, idUser);


        boolean test = !transactionsEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findTransactionsEntityByContactsByIdContactsShouldReturnFalse() {

        // Mettre un id incorrect
        int idContact = 1;

        int idUser = 10;

        List<TransactionsEntity> transactionsEntities = transactionsBean.findTransactionsEntityByContactsByIdContacts(idContact, idUser);


        boolean test = !transactionsEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }

    @Test
    void findTransactionsEntityByCompaniesByIdCompaniesShouldReturnTrue() {

        // Mettre un id correct
        int idCompany = 1;
        int idUser = 1;

        List<TransactionsEntity> transactionsEntities = transactionsBean.findTransactionsEntityByCompaniesByIdCompanies(idCompany, idUser);


        boolean test = !transactionsEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findTransactionsEntityByCompaniesByIdCompaniesShouldReturnFalse() {

        // Mettre un id incorrect
        int idCompany = 1;
        int idUser = 10;

        List<TransactionsEntity> transactionsEntities = transactionsBean.findTransactionsEntityByCompaniesByIdCompanies(idCompany, idUser);


        boolean test = !transactionsEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }

    @Test
    void findAll() {
        int idUser = 1;
        List<TransactionsEntity> transactionsEntities = transactionsBean.findAll(idUser);


        boolean test = !transactionsEntities.isEmpty();

        log.info("Le test vaut : " + test + ". La liste contient : " + (long) transactionsEntities.size() + " transactions");

        assertThat(test).isEqualTo(true);
    }

    @Test
    void update() {
        UsersEntity usersEntity = new UsersEntity();
        CompaniesEntity companiesEntity = new CompaniesEntity();
        ContactsEntity contactsEntity = new ContactsEntity();
        TransactionTypesEntity transactionTypesEntity = new TransactionTypesEntity();
        TransactionPhasesEntity transactionPhasesEntity = new TransactionPhasesEntity();
        double amount = -1500.5;

        usersEntity.setId(1);
        companiesEntity.setId(1);
        contactsEntity.setId(1);
        transactionTypesEntity.setId(2);
        transactionPhasesEntity.setId(2);

        TransactionsEntity entity = new TransactionsEntity();

        entity.setId(2);

//        entity.setUsersByIdUsers(usersEntity);
        entity.setCompaniesByIdCompanies(companiesEntity);
        entity.setContactsByIdContacts(contactsEntity);
        entity.setTransactionPhasesByIdTransactionPhases(transactionPhasesEntity);
        entity.setTransactionPhasesByIdTransactionPhases(transactionPhasesEntity);
//        entity.setTitle("Transaction Modifié");
        entity.setAmount(amount);
        entity.setEndDate(LocalDateTime.now().plusMonths(1));
        entity.setActive(true);

        transactionsBean.update(entity);
    }

    @Test
    void delete() {

        int idUser = 1;
        TransactionsEntity entity = new TransactionsEntity();

        entity.setId(2);
        transactionsBean.delete(entity, idUser);

    }

    @Test
    void findAllByPhase() {
        int idUser = 1;
        String phaseTransaction = "Prospection";
        List<TransactionsEntity> transactionsEntities = transactionsBean.findAllByPhase(idUser, phaseTransaction);


        boolean test = !transactionsEntities.isEmpty();

        log.info("Le test vaut : " + test + ". La liste contient : " + (long) transactionsEntities.size() + " transactions");

        assertThat(test).isEqualTo(true);
    }

    @Test
    void entitiesToFind() {

        List<TransactionsEntity> transactionsEntities = transactionsBean.findAll(1);

        List<TransactionsEntity> transactionsEntitiesProspection = transactionsEntities.stream()
                .filter(t -> t.getTransactionPhasesByIdTransactionPhases().getLabel().equalsIgnoreCase("Prospection"))
                .collect(Collectors.toList());

        List<TransactionsEntity> transactionsEntitiesQualification = transactionsEntities.stream()
                .filter(t -> t.getTransactionPhasesByIdTransactionPhases().getLabel().equalsIgnoreCase("Qualification"))
                .collect(Collectors.toList());

        List<TransactionsEntity> transactionsEntitiesProposition = transactionsEntities.stream()
                .filter(t -> t.getTransactionPhasesByIdTransactionPhases().getLabel().equalsIgnoreCase("Proposition"))
                .collect(Collectors.toList());

        List<TransactionsEntity> transactionsEntitiesNegociation = transactionsEntities.stream()
                .filter(t -> t.getTransactionPhasesByIdTransactionPhases().getLabel().equalsIgnoreCase("Négociation"))
                .collect(Collectors.toList());

        List<TransactionsEntity> transactionsEntitiesConclue = transactionsEntities.stream()
                .filter(t -> t.getTransactionPhasesByIdTransactionPhases().getLabel().equalsIgnoreCase("Conclue"))
                .collect(Collectors.toList());

        List<TransactionsEntity> transactionsEntitiesAnnulee = transactionsEntities.stream()
                .filter(t -> t.getTransactionPhasesByIdTransactionPhases().getLabel().equalsIgnoreCase("Annulé"))
                .collect(Collectors.toList());


        log.info("La liste contient : " + (long) transactionsEntities.size() + " transactions");
        log.info("La liste contient : " + (long) transactionsEntitiesProspection.size() + " transactions Prospection");
        log.info("La liste contient : " + (long) transactionsEntitiesQualification.size() + " transactions Qualification");
        log.info("La liste contient : " + (long) transactionsEntitiesProposition.size() + " transactions Proposition");
        log.info("La liste contient : " + (long) transactionsEntitiesNegociation.size() + " transactions Négociation");
        log.info("La liste contient : " + (long) transactionsEntitiesConclue.size() + " transactions Conclues");
        log.info("La liste contient : " + (long) transactionsEntitiesAnnulee.size() + " transactions Annulées");

    }
}