package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.beans.TaskTypesBean;
import be.atc.salesmanagercrm.entities.TaskTypesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.Locale;

/**
 * @author Younes Arifi
 */
@Slf4j
@FacesConverter("taskTypeConverter")
public class TaskTypesConverter implements Converter {

    @Getter
    @Setter
    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    private final TaskTypesBean taskTypesBean = new TaskTypesBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        TaskTypesEntity taskTypesEntity;

        log.info("value:  " + value);

        if (value != null || !value.isEmpty()) {
            try {
                taskTypesEntity = taskTypesBean.findByLabel(value);
                return taskTypesEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "taskTypesNotFound"), null));
            }
        } else {
            log.warn("Erreur Converter Task Type");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "taskTypesNotFound"), null));
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return null;
    }
}
