package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.ContactTypesDao;
import be.atc.salesmanagercrm.dao.impl.ContactTypesDaoImpl;
import be.atc.salesmanagercrm.entities.ContactTypesEntity;
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
import java.util.stream.Collectors;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@Named(value = "contactTypesBean")
@ViewScoped
public class ContactTypesBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 6138026438225503423L;
    @Getter
    @Setter
    private ContactTypesDao contactTypesDao = new ContactTypesDaoImpl();

    /**
     * Find ContactType by ID
     *
     * @param id ContactTypes
     * @return ContactTypesEntity
     */
    public ContactTypesEntity findById(int id) {

        FacesMessage facesMessage;

        if (id == 0) {
            log.error("CompanyTypes ID is null");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "companyTypes.NotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return contactTypesDao.findById(em, id);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun type de contact avec l ID " + id + " n'a été trouve dans la DB",
                    ErrorCodes.CONTACTTYPE_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find all ContactTypes entities
     *
     * @return List of Contact Types
     */
    public List<ContactTypesEntity> findAll() {

        EntityManager em = EMF.getEM();

        List<ContactTypesEntity> contactTypesEntities = contactTypesDao.findAll();

        em.clear();
        em.close();

        return contactTypesEntities;
    }

    /**
     * Auto complete for ContactTypesEntity
     *
     * @param search String
     * @return list of ContactTypesEntity
     */
    public List<ContactTypesEntity> completeContactType(String search) {

        String searchLowerCase = search.toLowerCase();

        List<ContactTypesEntity> contactTypesDropdown = findAll();

        return contactTypesDropdown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());
    }

}
