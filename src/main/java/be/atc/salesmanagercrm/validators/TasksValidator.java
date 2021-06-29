package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.TasksEntity;
import be.atc.salesmanagercrm.enums.EnumPriority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TasksValidator {

    public static List<String> validate(TasksEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("L'utilisateur n'existe pas !");
            errors.add("Veuillez renseigner un titre dans votre tâche");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L'utilisateur n'existe pas !");
        }
        if (entity.getTitle().isEmpty() || entity.getTitle() == null) {
            errors.add("Veuillez renseigner un titre dans votre tâche");
        }
        if (entity.getPriority() != null) {
            if (
                    !Arrays.stream(EnumPriority.values())
                            .map(EnumPriority::getLabel)
                            .collect(Collectors.toSet())
                            .contains(entity.getPriority())
            ) {
                errors.add("Veuillez renseigner une priorité valide");
            }
        }
        return errors;
    }
}
