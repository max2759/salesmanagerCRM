package be.atc.salesmanagercrm.validators;

import lombok.extern.slf4j.Slf4j;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@Slf4j
@FacesValidator("userUsernameValidator")
public class UsersPasswordFrontValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

    }
}
