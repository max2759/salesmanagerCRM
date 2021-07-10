package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.dao.CompanyTypesDao;
import be.atc.salesmanagercrm.dao.impl.CompanyTypesDaoImpl;
import be.atc.salesmanagercrm.entities.CompanyTypesEntity;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import java.util.Locale;

/**
 * @author Zabbara - Maximilien
 */
@FacesConverter("companyTypesConverter")
public class CompanyTypesConverter implements Converter {

    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    CompanyTypesDao companyTypesDao = new CompanyTypesDaoImpl();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        CompanyTypesEntity companyTypesEntity;
        EntityManager em = EMF.getEM();

        if (value != null) {
            companyTypesEntity = companyTypesDao.findByLabel(em, value);
            return companyTypesEntity;
        } else {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "companyTypes.error")));
        }

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        return null;
    }
}
