package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CitiesDao;
import be.atc.salesmanagercrm.dao.impl.CitiesDaoImpl;
import be.atc.salesmanagercrm.entities.CitiesEntity;
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
@Named(value = "citiesBean")
@ViewScoped
public class CitiesBean extends ExtendBean implements Serializable {

    @Getter
    @Setter
    private CitiesDao citiesDao = new CitiesDaoImpl();

    @Getter
    @Setter
    private CitiesEntity citiesEntity = new CitiesEntity();

    /**
     * Find all Cities entities
     *
     * @return List BranchActivitiesEntity
     */
    public List<CitiesEntity> findCitiesEntityList() {

        EntityManager em = EMF.getEM();

        List<CitiesEntity> citiesEntities = citiesDao.findAll();

        em.clear();
        em.close();

        return citiesEntities;
    }

    /**
     * Find City by ID
     *
     * @param id CitiesEntity
     * @return CitiesEntity CitiesEntity
     */
    public CitiesEntity findById(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Cities ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }


        EntityManager em = EMF.getEM();
        try {
            return citiesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune ville avec l'ID " + id + " n a été trouve dans la DB",
                    ErrorCodes.CITIES_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Sort Cities by group in form
     *
     * @param entityGroup CitiesEntity
     * @return label of entitygroup
     */

    public char getCitiesEntityGroup(CitiesEntity entityGroup) {
        return entityGroup.getLabel().charAt(0);
    }


}
