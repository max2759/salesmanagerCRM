package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.AddressesDao;
import be.atc.salesmanagercrm.dao.impl.AddressesDaoImpl;
import be.atc.salesmanagercrm.entities.AddressesEntity;
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

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "addressesBean")
@ViewScoped
public class AddressesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 5671519499945929696L;
    @Getter
    @Setter
    private AddressesDao addressesDao = new AddressesDaoImpl();

    @Getter
    @Setter
    private AddressesEntity addressesEntity = new AddressesEntity();

    @Inject
    private CompaniesBean companiesBean;
    @Inject
    private ContactsBean contactsBean;


    /**
     * Return addressEntity by id companies if exist else return new AddressesEntity
     */
    public void getAddressByIdCompany() {

        checkIfIdCompanyExist();

        try {
            addressesEntity = findByIdCompanies(companiesBean.getCompaniesEntity().getId());
        } catch (EntityNotFoundException exception) {
            addressesEntity = new AddressesEntity();
        }

    }


    /**
     * Return addressEntity by id contacts if exist else return new AddressesEntity
     */
    public void getAddressByIdContacts() {

        try {
            addressesEntity = findByIdContacts(contactsBean.getContactsEntity().getId());
        } catch (EntityNotFoundException exception) {
            addressesEntity = new AddressesEntity();
        }
    }


    /**
     * Find address by ID Company
     *
     * @param id id
     * @return AddressesEntity AddressesEntity
     */
    protected AddressesEntity findByIdCompanies(int id) {

        log.info("Start of findByIdCompanies");
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


    /**
     * Find address by ID Contacts
     *
     * @param id id
     * @return AddressesEntity AddressesEntity
     */
    protected AddressesEntity findByIdContacts(int id) {

        log.info("Start of findByIdContacts");
        FacesMessage facesMessage;

        if (id == 0) {
            log.error("ID Contacts is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "addressesNotFound"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return addressesDao.findByIdContacts(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun contact avec l'ID " + id + " n'a été trouve dans la DB",
                    ErrorCodes.CONTACT_NOT_FOUND
            );

        } finally {
            em.clear();
            em.close();
        }
    }


    /**
     * Method that call updateAddress
     */
    public void updateEntity() {

        if (addressesEntity.getCitiesByIdCities() != null) {
            updateAddress(this.addressesEntity);
        } else {
            addressesEntity = new AddressesEntity();
        }

    }

    /**
     * Update Address entity
     *
     * @param addressesEntity AddressesEntity
     */
    protected void updateAddress(AddressesEntity addressesEntity) {
        FacesMessage facesMessage;

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            addressesDao.update(em, addressesEntity);
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

    protected void checkIfIdCompanyExist() {
        FacesMessage facesMessage;

        log.info("Start of checkIfIdCompanyExist");
        log.info("Param : " + getParam("companyID"));

        int idTest;

        try {
            idTest = Integer.parseInt(getParam("companyID"));
        } catch (NumberFormatException exception) {
            log.info(exception.getMessage());
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, JsfUtils.returnMessage(getLocale(), "company.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }


}
