package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidOperationException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Locale;

/**
 * @author Larche Marie-Élise
 */
@Slf4j
@FacesValidator("userLastnameFrontValidator")
public class UserLastnameFrontValidator implements Validator {

    @Getter
    @Setter
    private static Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setLastname((String) value);


        try {
            validateLength(usersEntity);
        } catch (InvalidOperationException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            throw new ValidatorException(new FacesMessage(getMessageLenghtError()));
        }

        try {
            validateName(usersEntity);
        } catch (InvalidOperationException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            throw new ValidatorException(new FacesMessage(getMessageLenghtError()));
        }

    }

    /**
     * Return message for username already in DB
     *
     * @return jsf utils message
     */
    private String getMessageLenghtError() {
        return JsfUtils.returnMessage(locale, "error.notEnoughChar");
    }

    /**
     * Return message for username already in DB
     *
     * @return jsf utils message
     */
    private String getMessageStringError() {
        return JsfUtils.returnMessage(locale, "error.notEnoughChar");
    }

    public void validateLength(UsersEntity entity) {

        if (entity.getFirstname() != null && entity.getFirstname().length() < 2) {
            log.warn("longueur trop courte", entity.getFirstname());
            throw new InvalidOperationException(
                    "longueur trop courte " + entity.getFirstname(), ErrorCodes.USER_NOT_VALID
            );
          /*  errorMessage = JsfUtils.returnMessage(getLocale(), "error.notEnoughChar");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
*/
            //  FacesContext.getCurrentInstance().addMessage("mail", new FacesMessage(FacesMessage.SEVERITY_INFO, "error.notEnoughChar", "2"));
        }
    }

    public void validateName(UsersEntity entity) {

        if (entity.getFirstname() != null && entity.getFirstname().charAt(0) == ' ') {
            log.warn("au moins 1 caractére", entity.getFirstname());
            throw new InvalidOperationException(
                    "au moiins 1 caractére " + entity.getFirstname(), ErrorCodes.ROLES_NOT_VALID
            );
        }
    }


}
