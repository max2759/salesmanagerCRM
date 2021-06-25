package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.TasksEntity;

import java.util.ArrayList;
import java.util.List;

public class TasksValidator {

    public static List<String> validate(TasksEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("L'utilisateur n'existe pas !");
            errors.add("Veuillez renseigner un un titre dans votre tâche");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L'utilisateur n'existe pas !");
        }
        if (entity.getTitle().isEmpty() || entity.getTitle() == null) {
            errors.add("Veuillez renseigner un titre dans votre tâche");
        }
        return errors;
    }
}
