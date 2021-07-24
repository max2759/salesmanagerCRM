package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.beans.TransactionPhasesBean;
import be.atc.salesmanagercrm.entities.TransactionPhasesEntity;
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
@FacesConverter("transactionPhasesConverter")
public class TransactionPhasesConverter implements Converter {

    @Getter
    @Setter
    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    private final TransactionPhasesBean transactionPhasesBean = new TransactionPhasesBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        TransactionPhasesEntity transactionPhasesEntity;

        log.info("value:  " + value);

        if (value != null || !value.isEmpty()) {
            try {
                transactionPhasesEntity = transactionPhasesBean.findByLabel(value);
                return transactionPhasesEntity;
            } catch (EntityNotFoundException exception) {
                log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "transactionPhaseNotExist"), null));
            }
        } else {
            log.warn("Erreur Converter Transaction Type");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(locale, "transactionPhaseNotExist"), null));
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return null;
    }
}
