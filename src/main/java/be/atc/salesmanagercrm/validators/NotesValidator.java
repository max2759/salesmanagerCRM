package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.NotesEntity;
import lombok.extern.slf4j.Slf4j;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@FacesValidator("notesValidator")
public class NotesValidator implements Validator {

    public static List<String> validate(NotesEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("L'utilisateur n'existe pas !");
            errors.add("Veuillez renseigner un message dans votre note");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L'utilisateur n'existe pas !");
        }
        if (entity.getMessage() == null || entity.getMessage().isEmpty()) {
            errors.add("Veuillez renseigner un message dans votre note");
        }

        CheckEntitiesValidator checkEntitiesValidator = new CheckEntitiesValidator();

        checkEntitiesValidator.checkUser(entity.getUsersByIdUsers());


        return errors;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

    }
}
