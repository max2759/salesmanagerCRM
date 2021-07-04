package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.dao.impl.RolesDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "rolesBean")
@SessionScoped
public class RolesBean implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @Getter
    @Setter
    private RolesDao dao = new RolesDaoImpl();
    @Getter
    @Setter
    private RolesEntity rolesEntity = new RolesEntity();


    public RolesEntity findById(int id) {
        return dao.findById(id);
    }


}
