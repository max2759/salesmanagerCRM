package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.beans.ContactsBean;
import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.Locale;


@Slf4j
@FacesConverter(value = "contactConverter")
public class ContactsConverter implements Converter {

    private final ContactsBean contactsBean = new ContactsBean();

    @Getter
    @Setter
    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        ContactsEntity contactsEntity;
        log.info("Value = " + value);

        int id;
        if (value == null) {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "contactNotExist")));
        }
        try {
            id = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "contactNotExist")));
        }


        if (id != 0) {
            try {
                // TODO : A modifier et à rajouter l'id de User
                contactsEntity = contactsBean.findByIdContactAndByIdUser(id, 1);
                return contactsEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "contactNotExist")));
            }
        } else {
            log.warn("Erreur Converter Contact");
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "contactNotExist")));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((ContactsEntity) value).getId());
        } else {
            return null;
        }
    }
}
