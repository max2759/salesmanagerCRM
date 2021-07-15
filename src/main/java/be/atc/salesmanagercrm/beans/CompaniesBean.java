package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompaniesDao;
import be.atc.salesmanagercrm.dao.impl.CompaniesDaoImpl;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
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
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "companiesBean")
@ViewScoped
public class CompaniesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 5861943957551000529L;

    @Getter
    @Setter
    private CompaniesDao companiesDao = new CompaniesDaoImpl();

    @Getter
    @Setter
    private CompaniesEntity companiesEntity;

    @Getter
    @Setter
    private List<CompaniesEntity> companiesEntityList;

    /**
     * public method that call save
     */
    public void saveCompany() {
        save(companiesEntity);
    }

    /**
     * Save Companies Entity
     *
     * @param companiesEntity CompaniesEntity
     */
    protected void save(CompaniesEntity companiesEntity) {

        companiesEntity.setRegisterDate(LocalDateTime.now());
        companiesEntity.setActive(true);

        CheckEntities checkEntities = new CheckEntities();

        try {
            checkEntities.checkUser(companiesEntity.getUsersByIdUsers());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        if (companiesEntity.getBranchActivitiesByIdBranchActivities() != null) {
            try {
                checkEntities.checkBranchActivities(companiesEntity.getBranchActivitiesByIdBranchActivities());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        if (companiesEntity.getCompanyTypesByIdCompanyTypes() != null) {
            try {
                checkEntities.checkCompanyTypes(companiesEntity.getCompanyTypesByIdCompanyTypes());
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            companiesDao.add(em, companiesEntity);
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


    /**
     * Find all Companies entities by userID
     *
     * @param idUser idUser
     * @return List CompaniesEntities
     */
    protected List<CompaniesEntity> findAll(int idUser) {
        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<CompaniesEntity> companiesEntities = companiesDao.findAll(em, idUser);

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find Companies entities by id User
     *
     * @param idUser UsersEntity
     * @return List CompaniesEntity
     */
    public List<CompaniesEntity> findCompaniesEntityByIdUser(int idUser) {

        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<CompaniesEntity> companiesEntities = companiesDao.findCompaniesEntityByIdUser(em, idUser);

        em.clear();
        em.close();

        return companiesEntities;
    }

    /**
     * Find Company by ID
     *
     * @param id CompaniesEntity
     * @return Companies Entity
     */
    public CompaniesEntity findByIdCompanyAndByIdUser(int id, int idUser) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Company ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return companiesDao.findByIdCompanyAndByIdUser(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune compagny avec l ID " + id + " et l ID User " + idUser + " n a ete trouvee dans la BDD",
                    ErrorCodes.CONTACT_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }
}
