package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.dao.impl.RolesDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


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
    @Getter
    @Setter
    private List<RolesEntity> rolesEntityList;


    public RolesEntity findById(EntityManager em, int id) {
        return dao.findById(em, id);
    }

    public void findAllRoles() {
        log.info("bgin findallrole");
        rolesEntityList = findAll();
        log.info(String.valueOf(rolesEntityList));
    }

    protected List<RolesEntity> findAll() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<RolesEntity> rolesEntities = dao.findAllRoles(em);

        em.clear();
        em.close();

        return rolesEntities;
    }


    public List<SelectItem> getRolesEntitiesSelectItems() {
        return rolesEntityList.stream().map(c -> new SelectItem(c.getId(), c.getLabel())).collect(Collectors.toList());
    }


}
