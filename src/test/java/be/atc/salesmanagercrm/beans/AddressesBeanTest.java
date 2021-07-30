package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.AddressesEntity;
import be.atc.salesmanagercrm.entities.CitiesEntity;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AddressesBeanTest {

    private AddressesBean addressesBean;

    @BeforeEach
    public void init() {
        log.info("Appel avant chaque test");
        addressesBean = new AddressesBean();
    }

    @AfterEach
    public void after() {
        log.info("Appel après chaque test");
        addressesBean = null;
    }

    @Test
    void createAddresseByCompanies() {

        CompaniesEntity companiesEntity = new CompaniesEntity();
        CitiesEntity citiesEntity = new CitiesEntity();

        companiesEntity.setId(3);
        citiesEntity.setId(3);

        AddressesEntity addressesEntity = new AddressesEntity();

        addressesEntity.setNumber("10");
        addressesEntity.setStreet("Rue test");
        addressesEntity.setCitiesByIdCities(citiesEntity);
        addressesEntity.setCompaniesByIdCompanies(companiesEntity);

        addressesBean.createAddresseByCompanies();
    }

    @Test
    void findByIdCompanies() {
        int id = 10;

        AddressesEntity addressesEntity = addressesBean.findByIdCompanies(id);

        log.info("Address entity : " + addressesEntity);

        boolean test = !addressesEntity.equals(null);

        log.info("Valeur du test = " + test);

        assertThat(test).isEqualTo(true);
    }
}