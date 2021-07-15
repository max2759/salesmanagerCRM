package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.ContactsDao;
import be.atc.salesmanagercrm.dao.impl.ContactsDaoImpl;
import be.atc.salesmanagercrm.entities.ContactsEntity;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Slf4j
@Named(value = "contactsBean")
@ViewScoped
public class ContactsBean implements Serializable {

    private static final long serialVersionUID = 848519777793777451L;

    @Getter
    @Setter
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Getter
    @Setter
    private ContactsDao dao = new ContactsDaoImpl();

    /**
     * Find Contacts entities by id User
     *
     * @param idUser UsersEntity
     * @return List ContactsEntity
     */
    public List<ContactsEntity> findContactsEntityByIdUser(int idUser) {

        if (idUser == 0) {
            log.error("User ID is null");
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();

        List<ContactsEntity> contactsEntities = dao.findContactsEntityByIdUser(em, idUser);

        em.clear();
        em.close();

        return contactsEntities;
    }

    /**
     * Find Contact by ID
     *
     * @param id ContactsEntity
     * @return Contacts Entity
     */
    public ContactsEntity findByIdContactAndByIdUser(int id, int idUser) {

        FacesMessage msg;

        if (id == 0) {
            log.error("Contact ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "contactNotExist"), null);
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
            return dao.findByIdContactAndByIdUser(em, id, idUser);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun contact avec l ID " + id + " et l ID User " + idUser + " n a ete trouvee dans la BDD",
                    ErrorCodes.CONTACT_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }
}
