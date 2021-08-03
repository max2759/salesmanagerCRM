package be.atc.salesmanagercrm.beans;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Slf4j
@Named(value = "accessControlBean")
@ViewScoped
public class AccessControlBean extends ExtendBean implements Serializable {
    private static final long serialVersionUID = -3081158727046882548L;

    private final FacesContext fc = FacesContext.getCurrentInstance();
    private final ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();

    /**
     * Check if user isn't Logged
     */
    public void isNotLogged() {
        log.info("AccessControlBean isNotLogged()");
        if (!getCurrentUser().isAuthenticated()) {
            nav.performNavigation("/connection.xhtml");
        }
    }

    /**
     * Check permission user.
     *
     * @param event ComponentSystemEvent
     */
    public void checkPermission(ComponentSystemEvent event) {
        setCurrentUser(SecurityUtils.getSubject());
        if (getCurrentUser().isAuthenticated()) {
            String permission = (String) event.getComponent().getAttributes().get("permRequired");

            if (!getCurrentUser().isPermitted(permission)) {
                log.info("Vous n'avez la permission : " + permission + ". Accès refusé");
                hasNotPermission();
            }

        } else {
            isNotLogged();
        }
    }

    /**
     * Redirect user to AccessDenied !
     */
    public void hasNotPermission() {
        nav.performNavigation("/errorPages/accessDenied.xhtml");
    }

    /**
     * Check permission with String Value
     * @param permission String
     * @return boolean
     */
    public boolean checkPermission(String permission) {
        setCurrentUser(SecurityUtils.getSubject());
        return getCurrentUser().isPermitted(permission);
    }

}
