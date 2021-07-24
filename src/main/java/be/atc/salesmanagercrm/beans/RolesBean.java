package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.dao.impl.RolesDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.RolesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;


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
    @Getter
    @Setter
    private RolesEntity roleForDialog;
    @Getter
    @Setter
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();


    public void initialiseDialogRoleId(Integer idRole) {
        if (idRole == null || idRole < 1) {
            return;
        }
        roleForDialog = getDao().findById(EMF.getEM(), idRole);
        rolesEntity = dao.findById(EMF.getEM(), idRole);
    }


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

    public RolesEntity findByLabel(String label) {
        if (label == null) {
            log.info("label null in rolebean");
            return null;
        }

        EntityManager em = EMF.getEM();

        try {
            return dao.findByLabel(em, label);
        } catch (Exception exception) {
            log.info("nothing in roles bean");
            throw new EntityNotFoundException("aucun role avec le label " + label + " n'a été trouvé en db", ErrorCodes.ROLES_NOT_FOUND);
        } finally {
            em.clear();
            em.close();
        }

    }


    public void addRoles() {
        //rolesEntity = rolesBean.findById(em, idRole);
        try {
            validateRoles(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();
        try {
            checkEntities.checkRoleByLabel(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.register(em, rolesEntity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }

    }

    public void updateEntity() {
        updateRole(this.rolesEntity);
        rolesEntityList = findAll();
    }

    protected void updateRole(RolesEntity rolesEntity) {

        try {
            validateRoles(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();
        try {
            checkEntities.checkRoleByLabel(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, rolesEntity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }

    public void delete(int id) {
        FacesMessage msg;
        log.info(String.valueOf(id));

        EntityManager em = EMF.getEM();
        RolesEntity rolesEntity1 = dao.findById(em, id);

        List<RolesEntity> rolesEntityList2 = dao.findForDeleteSafe(em, rolesEntity1.getId());
        log.info(String.valueOf(rolesEntityList2));

        if (dao.findForDeleteSafe(em, rolesEntity1.getId()) == null || dao.findForDeleteSafe(em, rolesEntity1.getId()).size() == 0) {
            rolesEntity1.setActive(false);

            EntityTransaction tx = null;
            try {
                tx = em.getTransaction();
                tx.begin();
                dao.update(em, rolesEntity1);
                tx.commit();
                log.info("Delete ok");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "role.deleted"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Exception ex) {
                if (tx != null && tx.isActive()) tx.rollback();
                log.error("Delete Error");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } finally {
                em.clear();
                em.close();
            }
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "role.activeUserHaveTheRole"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }


    }

    public void activate(int id) {
        FacesMessage msg;
        log.info(String.valueOf(id));

        EntityManager em = EMF.getEM();
        RolesEntity rolesEntity1 = dao.findById(em, id);

        rolesEntity1.setActive(true);

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, rolesEntity1);
            tx.commit();
            log.info("Delete ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "role.activate"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.error("Delete Error");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.close();
        }
    }


    private void validateRoles(RolesEntity entity) {
        List<String> errors = RolesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Register role is not valide {}", entity);
            throw new InvalidEntityException("L'ajou de role n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }


}
