package be.atc.salesmanagercrm.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

@Slf4j
public abstract class ExtendBean {

    @Getter
    @Setter
    private Locale locale;
    @Getter
    @Setter
    private Subject currentUser;

    @PostConstruct
    public void init() {
        setCurrentUser(SecurityUtils.getSubject());
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    /**
     * Méthode pour retourner les paramètres récupéré
     *
     * @param name Param form
     * @return param(name)
     */
    public String getParam(String name) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        return params.get(name);
    }
}
