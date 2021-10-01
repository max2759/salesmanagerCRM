package be.atc.salesmanagercrm.converters;


import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Locale;

/**
 * @author Zabbara - Maximilien
 */
@FacesConverter(value = "usersConverter")
public class UsersConverter implements Converter {

    private final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    UsersDao usersDao = new UsersDaoImpl();

    //TODO
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        return null;
    }

}
