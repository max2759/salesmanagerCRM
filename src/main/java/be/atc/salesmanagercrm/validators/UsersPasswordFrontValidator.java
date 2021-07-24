package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.beans.CheckEntities;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidOperationException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@FacesValidator("userPasswordValidator")
public class UsersPasswordFrontValidator implements Validator {
    Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        CheckEntities checkEntities = new CheckEntities();

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setPassword((String) value);


        try {
            checkPasswordRegexe(usersEntity);
        } catch (InvalidOperationException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            throw new ValidatorException(new FacesMessage(getMessageErrorPaswword()));
        }
    }

    /**
     * Return message for username already in DB
     *
     * @return jsf utils message
     */
    private String getMessageErrorPaswword() {
        return JsfUtils.returnMessage(locale, "users.regexPassError");
    }

    /**
     * Check if user exist in DB
     *
     * @param entity : UsersEntity
     */

    public void checkPasswordRegexe(UsersEntity entity) {
        if (entity != null) {
            Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$");
            Matcher matcher = pattern.matcher(entity.getPassword());
            boolean bool = matcher.matches();
            if (bool == false) {
                log.warn("wrong regex password", entity.getPassword());
                throw new InvalidOperationException(
                        "Le mot de passe doit posséder au minimum 8 caractéres, 1 chiffre, 1 caractére spécial et une majuscule " + entity.getPassword(), ErrorCodes.USER_BAD_PASS_REGEX
                );
            }
        }
    }

}
