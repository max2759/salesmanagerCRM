package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.TaskTypesEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Younes Arifi
 */
public class TaskTypesValidator {

    public static List<String> validate(TaskTypesEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("Veuillez renseigner un label dans votre tache");
            return errors;
        }
        if (entity.getLabel() == null) {
            errors.add("Veuillez renseigner un label dans votre tache !");
        }

        return errors;
    }
}
