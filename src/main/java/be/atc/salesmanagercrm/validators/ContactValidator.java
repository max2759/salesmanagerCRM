package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.ContactsEntity;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
public class ContactValidator {

    public static List<String> validate(ContactsEntity contactsEntity) {

        List<String> errors = new ArrayList<>();

        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        FacesMessage facesMessage;

        if (contactsEntity == null) {
            errors.add("Le prénom est obligatoire");
            errors.add("Le nom est obligatoire");

            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "contacts.firstnameNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return errors;
        }

        if (!(contactsEntity.getEmail().equals(""))) {
            if (!(validateEmail(contactsEntity))) {
                errors.add("L'adresse email doit être valide");
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "contacts.emailNotValid"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        }

        if (contactsEntity.getFirstname() == null || contactsEntity.getFirstname().isEmpty()) {
            errors.add("Le prénom est obligatoire");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "contacts.firstnameNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

        if (contactsEntity.getLastname() == null || contactsEntity.getLastname().isEmpty()) {
            errors.add("Le nom est obligatoire");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "contacts.lastnameNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

        if (contactsEntity.getContactTypesByIdContactTypes() == null) {
            errors.add("Veuillez choisir un type de contact");
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "contacts.ContactTypeNotValid"), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

        if (!(contactsEntity.getPhoneNumber().equals(""))) {
            if (!(validatePhoneNumber(contactsEntity))) {
                errors.add("Le prénom est obligatoire");
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "contacts.phoneNumberNotValid"), null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        }

        return errors;
    }

    /**
     * Check if email is correct
     *
     * @param contactsEntity contactsEntity
     * @return boolean
     */
    public static boolean validateEmail(ContactsEntity contactsEntity) {
        String regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]{2,}\\.[a-z]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(contactsEntity.getEmail());

        return matcher.find();
    }

    /**
     * Check if phoneNumber is correct
     *
     * @param contactsEntity contactsEntity
     * @return boolean
     */
    public static boolean validatePhoneNumber(ContactsEntity contactsEntity) {

        String regex = "^[\\+]?[(]?[0-9 ]{3}[)]?[0-9]{7,9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contactsEntity.getPhoneNumber());

        return matcher.find();
    }

}
