package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.NotesEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Younes Arifi
 */
@Slf4j
public class NotesValidator {

    public static List<String> validate(NotesEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("L utilisateur n existe pas !");
            errors.add("Veuillez renseigner un message dans votre note");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L utilisateur n existe pas !");
        }
        if (entity.getMessage() == null || entity.getMessage().isEmpty()) {
            errors.add("Veuillez renseigner un message dans votre note");
        }

        return errors;
    }
}
