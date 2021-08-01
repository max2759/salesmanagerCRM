package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.exceptions.InvalidOperationException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * @author Maximilien Zabbara
 */
@Slf4j
@FacesValidator("dateValidator")
public class DateValidator implements Validator {


    Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

        if (value != null) {
            CompaniesEntity companiesEntity = new CompaniesEntity();
            companiesEntity.setCreationDate(companiesEntity.getCreationDate());

            try {
                isValid(companiesEntity);
            } catch (InvalidOperationException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessageDateError(), null));
            }
        }

    }

    public boolean isValid(CompaniesEntity companiesEntity) {

        boolean valid = false;

        String creationDate = String.valueOf(companiesEntity.getCreationDate());

        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(creationDate,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

            valid = true;

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }

    /**
     * Return message for date format error
     *
     * @return jsf utils message
     */
    private String getMessageDateError() {
        return JsfUtils.returnMessage(locale, "company.creationDateNotValid");
    }

}
