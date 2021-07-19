package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.PermissionsDao;
import be.atc.salesmanagercrm.dao.RolesPermissionsDao;
import be.atc.salesmanagercrm.dao.impl.PermissionsDaoImpl;
import be.atc.salesmanagercrm.dao.impl.RolesPermissionsDaoImpl;
import be.atc.salesmanagercrm.entities.PermissionsEntity;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.entities.RolesPermissionsEntity;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.PermissionsValidator;
import be.atc.salesmanagercrm.validators.RolesPermissionsValidator;
import be.atc.salesmanagercrm.validators.RolesValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "rolesPermissionsBean")
@SessionScoped
public class RolePermissionsBean implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @Getter
    @Setter
    private RolesPermissionsDao dao = new RolesPermissionsDaoImpl();
    @Getter
    @Setter
    private RolesPermissionsEntity rolesPermissionsEntity = new RolesPermissionsEntity();
    @Getter
    @Setter
    private List<RolesPermissionsEntity> rolesPermissionsEntityList;
    @Getter
    @Setter
    private RolesPermissionsEntity rolePermissionsForDialog;
    @Getter
    @Setter
    private RolesBean rolesBean;
    @Getter
    @Setter
    private PermissionsBean permissionsBean;
    @Getter
    @Setter
    private RolesEntity rolesEntity;
    @Getter
    @Setter
    private PermissionsEntity permissionsEntity;
    @Getter
    @Setter
    private List<Integer> permissionsEntities = new ArrayList<>();
    @Getter
    @Setter
    private PermissionsDao pdao = new PermissionsDaoImpl();


    public void initialiseDialogRoleId(Integer idRole) {
        if (idRole == null || idRole < 1) {
            return;
        }
        rolePermissionsForDialog = getDao().findById(EMF.getEM(), idRole);
        rolesPermissionsEntity = dao.findById(EMF.getEM(), idRole);
    }


    public RolesPermissionsEntity findById(EntityManager em, int id) {
        return dao.findById(em, id);
    }

    public void findAllRolesPermissions() {
        log.info("bgin findallrole");
        rolesPermissionsEntityList = findAll();
        log.info(String.valueOf(rolesPermissionsEntityList));
    }

    protected List<RolesPermissionsEntity> findAll() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<RolesPermissionsEntity> rolesPermissionsEntities = dao.findAllRolesPermissions(em);

        em.clear();
        em.close();

        return rolesPermissionsEntities;
    }

    /**
     * va definir si on doit ajouter ou modifier. Si un combo existe, on update. Si un combo avec le role n'esxsiste pas déjà, on va ajouter
     */
    public void manageRolesPermissions() {
        //verifier si un combo existe


        //ne pas séparer les methodes, mais faire un if else car je sais pas comment ca va se passer avec la boucle
        //possiblement, en cas d'update, vider tous les combos et reinserer dedans a nouveau . Ou alors, verifier en db les combos et verifier par rapport a ce qu'on a recu si ils existent ou non

        log.info(String.valueOf(rolesPermissionsEntity.getRolesByIdRoles())); // je recoit mon role la c'est bon
        log.info(String.valueOf(rolesPermissionsEntity.getPermissionsByIdPermissions()));

        try {
            validateRolesPermissions(rolesPermissionsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            validateRoles(rolesPermissionsEntity.getRolesByIdRoles());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            validatePermissions(permissionsEntities);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }


        for (Integer permissionsEntity : permissionsEntities) {
            EntityManager em = EMF.getEM();
            log.info("deda, " + permissionsEntity);
            RolesPermissionsEntity rolesPermissionsEntity = new RolesPermissionsEntity();

            PermissionsEntity permissionsEntityId = pdao.findById(em, permissionsEntity);

            rolesPermissionsEntity.setPermissionsByIdPermissions(permissionsEntityId);
            rolesPermissionsEntity.setRolesByIdRoles(rolesEntity);
            //faire un add du combo
            log.info(String.valueOf(rolesPermissionsEntity));
        }
    }

    /**
     * adrole est la creation d'un combo. il faut verifier que le roel n'est pas inacvltif
     */
    public void addRolesPermissions() {

        log.info(String.valueOf(rolesPermissionsEntity.getRolesByIdRoles())); // je recoit mon role la c'est bon
        log.info(String.valueOf(rolesPermissionsEntity.getPermissionsByIdPermissions()));

        try {
            validateRolesPermissions(rolesPermissionsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            validateRoles(rolesPermissionsEntity.getRolesByIdRoles());
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }
/*
        CheckEntities checkEntities = new CheckEntities();
        try {
            checkEntities.checkRoleByLabel(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

         */


        //    log.info(String.valueOf(rolesEntity.getRolesPermissionsById()));
        //   log.info(String.valueOf(permissionsEntity.getLabel()));


/*
        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.addRolePermissions(em, rolesPermissionsEntity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
*/
    }

    /**
     * c'est la modification d'un combo existant. IL faut verifier que le role n'est pas inactif
     */
    public void updateRole() {

        try {
            validateRolesPermissions(rolesPermissionsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();
       /* try {
            checkEntities.checkRoleByLabel(rolesPermissionsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }9*/

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, rolesPermissionsEntity);
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


    private void validateRolesPermissions(RolesPermissionsEntity entity) {
        List<String> errors = RolesPermissionsValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Register role is not valide {}", entity);
            throw new InvalidEntityException("L'ajou de role n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validateRoles(RolesEntity entity) {
        List<String> errors = RolesValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Register role is not valide {}", entity);
            throw new InvalidEntityException("L'ajou de role n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validatePermissions(List<Integer> entities) {
        List<String> errors = PermissionsValidator.validate(entities);
        if (!errors.isEmpty()) {
            log.error("Register role is not valide {}", entities);
            throw new InvalidEntityException("L'ajou de role n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }


   /* public void manageRolesPermissions() {
        //verifier si un combo existe


        //ne pas séparer les methodes, mais faire un if else car je sais pas comment ca va se passer avec la boucle
        //possiblement, en cas d'update, vider tous les combos et reinserer dedans a nouveau . Ou alors, verifier en db les combos et verifier par rapport a ce qu'on a recu si ils existent ou non

        log.info(String.valueOf(rolesPermissionsEntity.getRolesByIdRoles())); // je recoit mon role la c'est bon
        log.info(String.valueOf(rolesPermissionsEntity.getPermissionsByIdPermissions()));

        for (Integer permissionsEntity : permissionsEntities) {
            EntityManager em = EMF.getEM();
            log.info("deda, " + permissionsEntity);
            RolesPermissionsEntity rolesPermissionsEntity = new RolesPermissionsEntity();

            PermissionsEntity permissionsEntityId = pdao.findById(em, permissionsEntity );

            rolesPermissionsEntity.setPermissionsByIdPermissions(permissionsEntityId);
            rolesPermissionsEntity.setRolesByIdRoles(rolesEntity);
            //faire un add du combo
            log.info(String.valueOf(rolesPermissionsEntity));
        }
    }*/

}
