package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompanyTypesDao;
import be.atc.salesmanagercrm.dao.impl.CompanyTypesDaoImpl;
import be.atc.salesmanagercrm.entities.CompanyTypesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class CompanyTypesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 6697758657863337372L;
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

    /**
     * Find CompanyTypes entities
     *
     * @return List CompanyTypesEntity
     */
    public List<CompanyTypesEntity> findCompanyTypesList() {


        EntityManager em = EMF.getEM();

        List<CompanyTypesEntity> companyTypesEntities = companyTypesDao.findAll();

        em.clear();
        em.close();

        return companyTypesEntities;
    }

    public CompanyTypesEntity findById(int id) {
        FacesMessage facesMessage;

        if (id == 0) {
            log.error("CompanyTypes ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyTypes.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return companyTypesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun type d'entreprise avec l ID " + id + " n'a été trouve dans la DB",
                    ErrorCodes.COMPANYTYPES_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

}
