package be.atc.salesmanagercrm.converters;

import be.atc.salesmanagercrm.dao.BranchActivitiesDao;
import be.atc.salesmanagercrm.dao.impl.BranchActivitiesDaoImpl;
import be.atc.salesmanagercrm.entities.BranchActivitiesEntity;
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

@FacesConverter("branchActivitiesConverter")
public class BranchActivitiesConverter implements Converter {

    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    BranchActivitiesDao branchActivitiesDao = new BranchActivitiesDaoImpl();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        BranchActivitiesEntity branchActivitiesEntity;
        EntityManager em = EMF.getEM();

        if (value != null) {
            branchActivitiesEntity = branchActivitiesDao.findByLabelEntity(em, value);
            return branchActivitiesEntity;
        } else {
            throw new ConverterException(new FacesMessage(JsfUtils.returnMessage(locale, "branchActivities.error")));
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        return null;
    }
}
