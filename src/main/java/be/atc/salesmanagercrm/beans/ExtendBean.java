package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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

    /**
     * Create and return Users Entity
     *
     * @param id int
     * @return UsersEntity
     */
    public UsersEntity returnUsersEntity(int id) {
        if (id == 0) {
            throw new EntityNotFoundException(
                    "Aucun utilisateur n a ete trouve", ErrorCodes.USER_NOT_FOUND
            );
        }
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(id);
        return usersEntity;
    }
}
