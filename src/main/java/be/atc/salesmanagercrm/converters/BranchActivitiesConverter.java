package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.beans.BranchActivitiesBean;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
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

/**
 * @author Zabbara - Maximilien
 */

@Slf4j
@FacesConverter("branchActivitiesConverter")
public class BranchActivitiesConverter implements Converter {

    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    private final BranchActivitiesBean branchActivitiesBean = new BranchActivitiesBean();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        log.info("Value = " + value);

        BranchActivitiesEntity branchActivitiesEntity;

        int id;
        if (value == null) {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "branchActivities.error")));
        }
        try {
            id = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "branchActivities.error")));
        }

        if (id != 0) {
            try {
                branchActivitiesEntity = branchActivitiesBean.findById(id);
                return branchActivitiesEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "branchActivities.error")));
            }
        } else {
            log.warn("Erreur Converter BranchActivity");
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "branchActivities.error")));
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null) {
            return String.valueOf(((BranchActivitiesEntity) value).getId());
        } else {
            return null;
        }
    }
}
