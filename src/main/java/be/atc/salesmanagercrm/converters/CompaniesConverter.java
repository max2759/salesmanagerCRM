package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.beans.CompaniesBean;
import be.atc.salesmanagercrm.beans.UsersBean;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;


@RequestScoped
@Slf4j
@Named
public class CompaniesConverter implements Converter {

    @Inject
    private CompaniesBean companiesBean;
    @Inject
    private UsersBean usersBean;

    @Getter
    @Setter
    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        CompaniesEntity companiesEntity;
        log.info("Value = " + value);

        int id;
        if (value == null) {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyNotExist")));
        }
        try {
            id = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyNotExist")));
        }


        if (id != 0) {
            try {
                companiesEntity = companiesBean.findByIdCompanyAndByIdUser(id, usersBean.getUsersEntity().getId());
                return companiesEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyNotExist")));
            }
        } else {
            log.warn("Erreur Converter Contact");
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyNotExist")));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((CompaniesEntity) value).getId());
        } else {
            return null;
        }
    }
}
