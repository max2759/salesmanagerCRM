package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.PermissionsDao;
import be.atc.salesmanagercrm.dao.impl.PermissionsDaoImpl;
import be.atc.salesmanagercrm.entities.PermissionsEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "permissionsBean")
@SessionScoped
public class PermissionsBean implements Serializable {
    private static final long serialVersionUID = -2338626292552177485L;

    @Getter
    @Setter
    private PermissionsDao dao = new PermissionsDaoImpl();
    @Getter
    @Setter
    private PermissionsEntity permissionsEntity = new PermissionsEntity();
    @Getter
    @Setter
    private List<PermissionsEntity> permissionsEntityList;
    @Getter
    @Setter
    private List<PermissionsEntity> permissionsEntities = new ArrayList<>();

    public void findAllPermissions() {
        log.info("bgin findallpermissions");
        permissionsEntityList = findAll();
        log.info(String.valueOf(permissionsEntityList));
    }

    protected List<PermissionsEntity> findAll() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<PermissionsEntity> permissionsEntities = dao.findAllPermissions(em);

        em.clear();
        em.close();

        return permissionsEntities;
    }


    public PermissionsEntity findByLabel(String label) {
        if (label == null) {
            log.info("label null in permbean");
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return dao.findByLabel(em, label);
        } catch (Exception exception) {
            log.info("nothing in roles bean");
            throw new EntityNotFoundException("aucune permission avec le label " + label + " n'a été trouvé en db", ErrorCodes.ROLES_NOT_FOUND);
        } finally {
            em.clear();
            em.close();
        }

    }


}
