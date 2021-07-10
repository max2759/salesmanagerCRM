package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompanyTypesDao;
import be.atc.salesmanagercrm.dao.impl.CompanyTypesDaoImpl;
import be.atc.salesmanagercrm.entities.CompanyTypesEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "companyTypesBean")
@ViewScoped
public class CompanyTypesBean implements Serializable {

    @Getter
    @Setter
    private CompanyTypesDao companyTypesDao = new CompanyTypesDaoImpl();

    @Getter
    @Setter
    private CompanyTypesEntity companyTypesEntity = new CompanyTypesEntity();

    @Getter
    @Setter
    private List<CompanyTypesEntity> companyTypesEntities;

    /**
     * public method that call findAll
     */
    public void findAllCompanyTypes() {
        companyTypesEntities = findAll();
    }

    /**
     * Find all Company Types
     *
     * @return List of Company Types Entity
     */
    protected List<CompanyTypesEntity> findAll() {
        EntityManager em = EMF.getEM();
        List<CompanyTypesEntity> companyTypesEntities = companyTypesDao.findAll();

        em.clear();
        em.close();

        return companyTypesEntities;
    }

}
