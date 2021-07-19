package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.RolesEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
public class RolesValidator {

    public static List<String> validate(RolesEntity entity) {
        List<String> errors = new ArrayList<>();

        log.info(String.valueOf(entity));

        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getLabel() == null || entity.getLabel().isEmpty()) {
            errors.add("Le nom de rôle est vide");
        }

        return errors;
    }


}
