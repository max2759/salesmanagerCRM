package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.dao.ConversationsDao;
import be.atc.salesmanagercrm.dao.impl.ConversationsDaoImpl;
import be.atc.salesmanagercrm.entities.ConversationsEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Larché Marie-Élise
 */
@Slf4j
public class ConversationsValidator {

    public static List<String> validate(ConversationsEntity entity) {
        List<String> errors = new ArrayList<>();
        ConversationsDao dao = new ConversationsDaoImpl();

        log.info(String.valueOf(entity));
        EntityManager em = EMF.getEM();
        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getMessage() == null || entity.getMessage().isEmpty()) {
            errors.add("Le nom de rôle est vide");
        }

        return errors;
    }

}
