package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.CompaniesContactsDao;
import be.atc.salesmanagercrm.dao.impl.CompaniesContactsDaoImpl;
import be.atc.salesmanagercrm.entities.CompaniesContactsEntity;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
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
    private ContactsBean contactsBean;

    /**
     * Save companies and contacts in CompaniesContacts
     */
    protected void createCompaniesContacts() {

        FacesMessage facesMessage;
        log.info("Start of createCompaniesContacts");

        companiesContactsEntity.setContactsByIdContacts(contactsBean.getContactsEntity());

//        try{
//            companiesContactsEntity = findByIdContacts(contactsBean.getContactsEntity());
//        }catch (EntityNotFoundException exception) {
//            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
//            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
//            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
//            return;
//        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            companiesContactsDao.update(em, companiesContactsEntity);
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
     * Find companiesContacts by id contact
     *
     * @param id id
     * @return CompaniesContactsEntity
     */
    protected CompaniesContactsEntity findByIdContacts(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("Contact ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contact.error"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();
        try {
            return companiesContactsDao.findByIdContacts(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun contact avec l ID " + id + " n'a été trouvé dans la DB",
                    ErrorCodes.CONTACT_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }
}
