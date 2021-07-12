package be.atc.salesmanagercrm.converters;


import be.atc.salesmanagercrm.beans.RolesBean;
import be.atc.salesmanagercrm.entities.RolesEntity;
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
        RolesEntity rolesEntity;

        log.info("value:  " + value);
        if (value != null || !value.isEmpty()) {
            try {
                rolesEntity = rolesBean.findByLabel(value);
                log.info("in roleConverter " + rolesEntity.getLabel() + " " + rolesEntity.getId());
                return rolesEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyTypes.error")));
            }
        } else {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyTypes.error")));
        }
        //return null;
      /*  RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setId(1);
        return rolesEntity;
        */

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return null;
    }

}
