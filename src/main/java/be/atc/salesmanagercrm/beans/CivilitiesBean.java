package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CivilitiesDao;
import be.atc.salesmanagercrm.dao.impl.CivilitiesDaoImpl;
import be.atc.salesmanagercrm.entities.CivilitiesEntity;
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
@Named(value = "civilitiesBean")
@ViewScoped
public class CivilitiesBean extends ExtendBean implements Serializable {

    @Getter
    @Setter
    private CivilitiesDao civilitiesDao = new CivilitiesDaoImpl();

    @Getter
    @Setter
    private CivilitiesEntity civilitiesEntity = new CivilitiesEntity();

    @Getter
    @Setter
    private List<CivilitiesEntity> civilitiesEntities;


    public void completeCiviltyEntitiesList() {
        civilitiesEntities = findCivilityEntitiesList();
    }

    /**
     * Find all Civilities entities
     *
     * @return List CivilitiesEntity
     */
    protected List<CivilitiesEntity> findCivilityEntitiesList() {

        EntityManager em = EMF.getEM();

        List<CivilitiesEntity> civilitiesEntities = civilitiesDao.findAll();

        em.clear();
        em.close();

        return civilitiesEntities;
    }

    /**
     * public method that return civility entity by its ID
     *
     * @param id id
     * @return CivilitiesEntity
     */
    public CivilitiesEntity returnFindById(int id) {
        return findById(id);
    }

    /**
     * Find Civility by Id
     *
     * @param id id
     * @return CivilitiesEntity
     */
    protected CivilitiesEntity findById(int id) {
        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Cities ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "civilitiesNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return civilitiesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun titre de civilité avec l'ID " + id + " n a été trouve dans la DB",
                    ErrorCodes.CIVILITY_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }
}
