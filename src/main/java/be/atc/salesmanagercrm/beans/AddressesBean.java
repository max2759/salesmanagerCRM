package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.AddressesDao;
import be.atc.salesmanagercrm.dao.impl.AddressesDaoImpl;
import be.atc.salesmanagercrm.entities.AddressesEntity;
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
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "addressesBean")
@ViewScoped
public class AddressesBean extends ExtendBean implements Serializable {

    @Getter
    @Setter
    private AddressesDao addressesDao = new AddressesDaoImpl();

    @Getter
    @Setter
    private AddressesEntity addressesEntity = new AddressesEntity();

    @Inject
    private CompaniesBean companiesBean;

    @Inject
    private CitiesBean citiesBean;


    /**
     * Create Adresse entity
     */
    public void createAddresseByCompanies() {

        log.info("Start of createAddresse");

        addressesEntity.setCompaniesByIdCompanies(companiesBean.getCompaniesEntity());

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            addressesDao.add(em, addressesEntity);
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
     * Auto Complete form creation
     *
     * @param query String
     * @return List Contacts Entities
     */
    public List<CitiesEntity> completeCitiesEntityContains(String query) {

        String queryLowerCase = query.toLowerCase();

        // TODO : à modifier
        List<CitiesEntity> citiesEntityListForm = citiesBean.findCitiesEntityList();

        return citiesEntityListForm.stream().filter(t -> t.getLabel().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }


    public AddressesEntity getAddressByIdCompany() {

        addressesEntity = findByIdCompanies(companiesBean.getCompaniesEntity().getId());

        if (addressesEntity != null) {
            return addressesEntity;
        }

        return null;
    }


    /**
     * Find address by ID Company
     *
     * @param id id
     * @return AddressesEntity AddressesEntity
     */
    protected AddressesEntity findByIdCompanies(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("ID Companies is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "addressesNotFound"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return addressesDao.findByIdCompanies(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucune adresse avec l'ID " + id + " n'a été trouve dans la DB",
                    ErrorCodes.ADDRESSES_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    public void deleteAddressByIdCompany() {
        deleteByIdCompanies(companiesBean.getCompaniesEntity().getId());
    }

    /**
     * Delete address by ID company
     *
     * @param id id
     */
    protected void deleteByIdCompanies(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Task ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "addressesNotFound"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        AddressesEntity addressesEntityToDelete;

        try {
            addressesEntityToDelete = findByIdCompanies(id);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "addressesNotFound"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }


        EntityManager em = EMF.getEM();
        em.getTransaction();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            addressesDao.deleteByIdCompanies(em, addressesEntityToDelete);
            tx.commit();
            log.info("Delete ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.error("Delete Error");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.close();
        }
    }

    protected void updateAddress() {
        FacesMessage facesMessage;

        AddressesEntity addressesEntityToUpdate;

        try {
            addressesEntityToUpdate = findByIdCompanies(companiesBean.getCompaniesEntity().getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "tasks.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            addressesDao.update(em, addressesEntityToUpdate);
            tx.commit();
            log.info("Update ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Update echec");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            em.clear();
            em.clear();
        }
    }


    protected void updateOrCreateAddress() {

        AddressesEntity addressesEntityChoice = findByIdCompanies(companiesBean.getCompaniesEntity().getId());

        if (addressesEntityChoice == null) {
            createAddresseByCompanies();
        } else {
            updateAddress();
        }
    }


}
