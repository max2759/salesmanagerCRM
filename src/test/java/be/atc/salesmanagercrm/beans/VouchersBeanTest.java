package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class VouchersBeanTest {

    private VouchersBean vouchersBean;

    @BeforeEach
    public void initNotes() {
        log.info("Appel avant chaque test");
        vouchersBean = new VouchersBean();
    }

    @AfterEach
    public void undefOrders() {
        log.info("Appel après chaque test");
        vouchersBean = null;
    }

    @Test
    void save() {

        UsersEntity usersEntity = new UsersEntity();
        CompaniesEntity companiesEntity = new CompaniesEntity();
        ContactsEntity contactsEntity = new ContactsEntity();
        VoucherStatusEntity voucherStatusEntity = new VoucherStatusEntity();

        usersEntity.setId(1);
        companiesEntity.setId(1);
        contactsEntity.setId(1);
        voucherStatusEntity.setId(1);

        VouchersEntity entity = new VouchersEntity();

        entity.setUsersByIdUsers(usersEntity);
//        entity.setPriority(EnumPriority.ELEVEE);
//        entity.setEndDate(LocalDateTime.now().plusMonths(1));
        entity.setTitle("Voucher Test");
        entity.setVoucherStatusByIdVoucherStatus(voucherStatusEntity);

        entity.setCompaniesByIdCompanies(companiesEntity);
        entity.setContactsByIdContacts(contactsEntity);
        vouchersBean.save(entity);
    }


    @Test
    void findByIdShouldReturnFalse() {

        // Mettre un id incorrect
        int id = 10;
        int idUser = 1;
        VouchersEntity vouchersEntity = null;

        try {
            vouchersEntity = vouchersBean.findById(id, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = vouchersEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(false);

    }


    @Test
    void findByIdShouldReturnTrue() {

        // Mettre un id correct
        int id = 1;
        int idUser = 1;
        VouchersEntity vouchersEntity = null;

        try {
            vouchersEntity = vouchersBean.findById(id, idUser);
        } catch (EntityNotFoundException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
        }

        boolean test = vouchersEntity != null;

        log.info("Le test vaut : " + test);
        assertThat(test).isEqualTo(true);

    }

    @Test
    void findVouchersEntityByContactsByIdContactsShouldReturnTrue() {

        // Mettre un id correct
        int id = 1;
        int idUser = 1;

        List<VouchersEntity> vouchersEntities = vouchersBean.findVouchersEntityByContactsByIdContacts(id, idUser);


        boolean test = !vouchersEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findVouchersEntityByContactsByIdContactsShouldReturnFalse() {

        // Mettre un id incorrect
        int id = 10;

        int idUser = 1;

        List<VouchersEntity> vouchersEntities = vouchersBean.findVouchersEntityByContactsByIdContacts(id, idUser);


        boolean test = !vouchersEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }

    @Test
    void findVouchersEntityByCompaniesByIdCompaniesShouldReturnTrue() {

        // Mettre un id correct
        int id = 1;
        int idUser = 1;

        List<VouchersEntity> vouchersEntities = vouchersBean.findVouchersEntityByCompaniesByIdCompanies(id, idUser);


        boolean test = !vouchersEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findVouchersEntityByCompaniesByIdCompaniesShouldReturnFalse() {

        // Mettre un id incorrect
        int id = 195;
        int idUser = 1;

        List<VouchersEntity> vouchersEntities = vouchersBean.findVouchersEntityByCompaniesByIdCompanies(id, idUser);


        boolean test = !vouchersEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }

    @Test
    void findAll() {
        int idUser = 1;
        List<VouchersEntity> vouchersEntities = vouchersBean.findAll(idUser);


        boolean test = !vouchersEntities.isEmpty();

        log.info("Le test vaut : " + test + ". La liste contient : " + (long) vouchersEntities.size() + " taches");

        assertThat(test).isEqualTo(true);
    }

    @Test
    void update() {
        UsersEntity usersEntity = new UsersEntity();
        VoucherStatusEntity voucherStatusEntity = new VoucherStatusEntity();
        voucherStatusEntity.setId(3);
        usersEntity.setId(1);

        VouchersEntity vouchersEntity = new VouchersEntity();
        vouchersEntity.setId(3);
        vouchersEntity.setTitle("Titre modifié");
        vouchersEntity.setDescription("Description Modifié");
        vouchersEntity.setVoucherStatusByIdVoucherStatus(voucherStatusEntity);

        vouchersEntity.setUsersByIdUsers(usersEntity);

        vouchersBean.update(vouchersEntity);
    }
}