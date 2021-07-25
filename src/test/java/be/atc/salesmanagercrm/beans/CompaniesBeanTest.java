package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.entities.CompanyTypesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CompaniesBeanTest {

    private CompaniesBean companiesBean;
    private DateTimeFormatter dateFormatter;

    @BeforeEach
    public void init() {
        log.info("Appel avant chaque test");
        companiesBean = new CompaniesBean();
    }

    @AfterEach
    public void after() {
        log.info("Appel après chaque test");
        companiesBean = null;
    }

    @Test
    void save() {

        UsersEntity usersEntity = new UsersEntity();
        CompaniesEntity companiesEntity = new CompaniesEntity();
        BranchActivitiesEntity branchActivitiesEntity = new BranchActivitiesEntity();
        CompanyTypesEntity companyTypesEntity = new CompanyTypesEntity();

        usersEntity.setId(1);
        companyTypesEntity.setId(1);
        branchActivitiesEntity.setId(1);

        companiesEntity.setBranchActivitiesByIdBranchActivities(branchActivitiesEntity);
        companiesEntity.setUsersByIdUsers(usersEntity);
        companiesEntity.setCompanyTypesByIdCompanyTypes(companyTypesEntity);
        companiesEntity.setLabel("Coca-cola");
        companiesEntity.setDomainName("www.coca.be");
        companiesEntity.setEmployeesNumber(2000);
        companiesEntity.setRevenue(1000000.0);
        companiesEntity.setLinkedInPage("https://www.linkedin.com/company/the-coca-cola-company/");

        companiesBean.save(companiesEntity);
    }

    @Test
    void findCompaniesEntityByIdUser() {
        // Mettre un id correct
        int idUser = 1;

        List<CompaniesEntity> companiesEntities = companiesBean.findCompaniesEntityByIdUser(idUser);

        boolean test = !companiesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);

    }

    @Test
    void findByIdCompanyAndByIdUser() {

        int id = 1;
        int idUser = 1;

        CompaniesEntity companiesEntity = companiesBean.findByIdCompanyAndByIdUser(id, idUser);

        boolean test = !(companiesEntity == null);

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);

    }

    @Test
    void checkVATValidity() {

        String response = "BE 0696668549";

        String newResponse = response.substring(3);

        double newResponseDouble = Double.parseDouble(newResponse);

        double modulo = 97.00;

        double verif = Math.floor(newResponseDouble / 100);

        log.info("Résultat vérif : " + verif);

        Double digit = newResponseDouble % 100;

        log.info("Résultat digit : " + digit);

        Double check = modulo - (verif % modulo);

        log.info("Résultat check : " + check);

        assertThat(check.equals(digit)).isEqualTo(true);

    }


    @Test
    void checkVATValidityShoudlReturnFalse() {

        String response = "BE 06966685";

        String newResponse = response.substring(3);

        double newResponseDouble = Double.parseDouble(newResponse);

        double modulo = 97.00;

        double verif = Math.floor(newResponseDouble / 100);

        log.info("Résultat vérif : " + verif);

        Double digit = newResponseDouble % 100;

        log.info("Résultat digit : " + digit);

        Double check = modulo - (verif % modulo);

        log.info("Résultat check : " + check);

        assertThat(check.equals(digit)).isEqualTo(false);

    }

}