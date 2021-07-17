package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.ContactsEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ContactsBeanTest {

    private ContactsBean contactsBean;

    @BeforeEach
    public void init() {
        log.info("Appel avant chaque test");
        contactsBean = new ContactsBean();
    }

    @AfterEach
    public void after() {
        log.info("Appel après chaque test");
        contactsBean = null;
    }

    @Test
    void findContactsEntityByIdUser() {

        // Mettre un id correct
        int idUser = 1;

        List<ContactsEntity> contactsEntities = contactsBean.findContactsEntityByIdUser(idUser);

        boolean test = !contactsEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);

    }
}