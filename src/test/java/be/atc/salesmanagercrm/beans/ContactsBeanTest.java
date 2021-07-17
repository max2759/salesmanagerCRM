package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.ContactTypesEntity;
import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
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

    @Test
    void save() {

        ContactsEntity contactsEntity = new ContactsEntity();
        JobTitlesEntity jobTitlesEntity = new JobTitlesEntity();
        ContactTypesEntity contactTypesEntity = new ContactTypesEntity();

        jobTitlesEntity.setId(4);
        contactTypesEntity.setId(1);

        contactsEntity.setEmail("test@gmail.com");
        contactsEntity.setFirstname("Max");
        contactsEntity.setLastname("Zab");
        contactsEntity.setJobTitlesByIdJobTitles(jobTitlesEntity);
        contactsEntity.setContactTypesByIdContactTypes(contactTypesEntity);
        contactsEntity.setPhoneNumber("0477777777");

        contactsBean.save(contactsEntity);

    }
}