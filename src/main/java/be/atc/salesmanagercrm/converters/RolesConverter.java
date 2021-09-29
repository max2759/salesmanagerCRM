package be.atc.salesmanagercrm.converters;


import be.atc.salesmanagercrm.beans.RolesBean;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.Locale;

@Slf4j
@FacesConverter(value = "rolesConverter")
public class RolesConverter implements Converter {
    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    private final RolesBean rolesBean = new RolesBean();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        log.info("value:  " + value);

        if (value == null) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "roleNoExist"), null));
        }

        if (rolesBean.findByLabel(value) != null) {
            try {
                return rolesBean.findByLabel(value);
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "roleNoExist"), null));
            }
        } else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "roleNoExist"), null));
        }

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return null;
    }

}
