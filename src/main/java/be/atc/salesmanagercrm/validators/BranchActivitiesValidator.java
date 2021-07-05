package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.beans.CheckEntities;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Locale;

@Slf4j
@FacesValidator("BranchActivitiesValidator")
public class BranchActivitiesValidator implements Validator {
    Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

        if (value == null) {
            log.info("Branch activity label is empty");
            throw new ValidatorException(new FacesMessage(getMessageError()));
        }

        CheckEntities checkEntities = new CheckEntities();

        BranchActivitiesEntity branchActivitiesEntity = new BranchActivitiesEntity();

        branchActivitiesEntity.setLabel((String) value);

        try {
            checkEntities.checkBranchActivitiesLabel(branchActivitiesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            throw new ValidatorException(new FacesMessage(getMessageError()));
        }
    }

    /**
     * Return message for size error
     *
     * @return
     */
    private String getMessageError() {
        return JsfUtils.returnMessage(locale, "branchActivities.labelError");
    }

}
