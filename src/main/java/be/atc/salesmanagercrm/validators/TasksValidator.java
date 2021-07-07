package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.TasksEntity;
import be.atc.salesmanagercrm.enums.EnumPriority;
import lombok.Getter;
import lombok.Setter;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Younes Arifi
 */
public class TasksValidator {
    @Getter
    @Setter
    private static Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public static List<String> validate(TasksEntity entity) {

        List<String> errors = new ArrayList<>();
        String message = null;

        if (entity == null) {
            errors.add("L utilisateur n existe pas !");
            errors.add("Veuillez renseigner un titre dans votre tache");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L utilisateur n existe pas !");
        }
        if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
            errors.add("Veuillez renseigner un titre dans votre tache");
        }
        if (entity.getPriority() != null) {
            if (
                    !Arrays.stream(EnumPriority.values())
                            .map(EnumPriority::getLabel)
                            .collect(Collectors.toSet())
                            .contains(entity.getPriority().getLabel())
            ) {
                errors.add("Veuillez renseigner une priorité valide");
            }
        }
        return errors;
    }
}
