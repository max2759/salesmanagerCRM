package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.beans.CompaniesBean;
import be.atc.salesmanagercrm.entities.CompaniesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.List;
import java.util.Locale;


@Slf4j
@FacesConverter(value = "companiesListConverter")
public class CompaniesListConverter implements Converter {

    private final CompaniesBean companiesBean = new CompaniesBean();

    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        List<CompaniesEntity> companiesEntityList;
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
                // TODO : A modifier et à rajouter l'id de User
                companiesEntityList = companiesBean.callFindByIdCompaniAndByIdUser(id);
                return companiesEntityList;
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyNotExist")));
            }
        } else {
            log.warn("Erreur Converter ContactList");
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
