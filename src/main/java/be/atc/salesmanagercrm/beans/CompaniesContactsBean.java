package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompaniesContactsDao;
import be.atc.salesmanagercrm.dao.impl.CompaniesContactsDaoImpl;
import be.atc.salesmanagercrm.entities.CompaniesContactsEntity;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "companiesContactsBean")
@ViewScoped
public class CompaniesContactsBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = -1631508572484407774L;
    @Getter
    @Setter
    private CompaniesContactsEntity companiesContactsEntity = new CompaniesContactsEntity();

    @Getter
    @Setter
    private CompaniesContactsDao companiesContactsDao = new CompaniesContactsDaoImpl();

    @Getter
    @Setter
    private CompaniesEntity companiesEntity = new CompaniesEntity();

    @Inject
    private CompaniesBean companiesBean;

    /**
     * Save companies and contacts in CompaniesContacts
     */
    public void createCompaniesContacts() {

        log.info("Start of createCompaniesContacts");

        companiesContactsEntity.setCompaniesByIdCompanies(companiesBean.getCompaniesEntity());


        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            companiesContactsDao.add(em, companiesContactsEntity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }

    }
}
