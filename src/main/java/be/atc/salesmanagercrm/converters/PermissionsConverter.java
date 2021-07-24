package be.atc.salesmanagercrm.converters;


import be.atc.salesmanagercrm.beans.PermissionsBean;
import be.atc.salesmanagercrm.entities.PermissionsEntity;
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
@FacesConverter(value = "permissionsConverter")
public class PermissionsConverter implements Converter {
    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    private final PermissionsBean permissionsBean = new PermissionsBean();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        PermissionsEntity permissionsEntity;

        log.info("value:  " + value);
        if (value != null || !value.isEmpty()) {
            try {
                permissionsEntity = permissionsBean.findByLabel(value);
                log.info("in roleConverter " + permissionsEntity.getLabel() + " " + permissionsEntity.getId());
                return permissionsEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyTypes.error")));
            }
        } else {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyTypes.error")));
        }
        //return null;
      /*  PermissionsEntity permissionsEntity = new PermissionsEntity();
        permissionsEntity.setId(1);
        return permissionsEntity;
        */

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return null;
    }

}
