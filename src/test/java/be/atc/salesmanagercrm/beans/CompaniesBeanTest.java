package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.entities.CompanyTypesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class CompaniesBeanTest {

    private CompaniesBean companiesBean;

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
}