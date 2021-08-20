package be.atc.salesmanagercrm.validators;


import be.atc.salesmanagercrm.entities.ConversationsEntity;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
public class ConversationsValidator {

    public static List<String> validate(ConversationsEntity entity) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        FacesMessage msg;

        List<String> errors = new ArrayList<>();

        log.info(String.valueOf(entity));
        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getMessage() == null || entity.getMessage().isEmpty()) {
            errors.add("Le message est vide");
            msg = new FacesMessage(JsfUtils.returnMessage(locale, "message.empty"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        return errors;
    }

    public static List<String> validateEntity(ConversationsEntity entity) {
        List<String> errors = new ArrayList<>();

        log.info(String.valueOf(entity));
        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }

        return errors;
    }

}
