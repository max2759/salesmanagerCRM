package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.UsersValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "usersBean")
@SessionScoped
public class UsersBean implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @Getter
    @Setter
    private UsersDao dao = new UsersDaoImpl();
    @Getter
    @Setter
    private UsersEntity usersEntity = new UsersEntity();
    @Getter
    @Setter
    private RolesEntity rolesEntity = new RolesEntity();
    @Getter
    @Setter
    @Inject
    private RolesBean rolesBean;
    @Getter
    @Setter
    private String password2;
    @Getter
    @Setter
    private List<UsersEntity> usersEntityList;
    @Getter
    @Setter
    private boolean showPopup;

    @Getter
    @Setter
    private UsersEntity userForDialog;

    private HttpServletRequest request;

    public void initialiseDialogUserId(Integer idUser) {
        if (idUser == null || idUser < 1) {
            return;
        }
        userForDialog = getDao().findById(EMF.getEM(), idUser);
        usersEntity = dao.findById(EMF.getEM(), idUser);
    }


    /**
     * @throws NoSuchAlgorithmException
     */
    @RequiresAuthentication
    // @RequiresPermissions("addUsers")
    public void register() {

        Subject currentUser = SecurityUtils.getSubject();
        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("addUsers")) {
            log.info("Tu as l'autorisation Test.");
        } else {
            log.info("Sorry, lightsaber1 rings are for schwartz masters only.");
        }

        //regex password + hash + pseudo unique
        //  log.info(String.valueOf(usersEntity.getEmail()));
        //log.info(String.valueOf(rolesEntity.getId()));
        //String idRoles = String.valueOf(rolesEntity.getId());
        //int idRole = Integer.parseInt(idRoles);

        //log.info(String.valueOf(usersEntity.getRolesByIdRoles())); //null
        //RolesEntity rolesEntity = new RolesEntity();
        //rolesEntity.setId(rolesEntity.getId());


        //mettre dans un try catch + fermer l'em
        //rolesEntity = rolesBean.findById(em, idRole);
        try {
            validateUsers(usersEntity, password2);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();
        try {
            checkEntities.checkUserByUsername(usersEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        try {
            checkEntities.checkRole(usersEntity.getRolesByIdRoles());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }


        String password = usersEntity.getPassword();
        String hashPass = encrypt(password);
        log.info(hashPass);

        //ici, remplacer par un converter pour le hashage du mdp
      /*  MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // convertir bytes en hexadécimal
        StringBuilder s = new StringBuilder();
        for (byte b : hash) {
            s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        String hashPass = s.toString();
        */


        usersEntity.setPassword(hashPass);
        usersEntity.setActive(true);
        usersEntity.setRegisterDate(LocalDateTime.now());

        //    usersEntity.setRolesByIdRoles(rolesBean.findById(em, idRole));
//log.info("avant em , le role est: "+usersEntity.getRolesByIdRoles().getId());
        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.register(em, usersEntity);
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

    public void connection() {
        //Example using most common scenario of username/password pair:
        //https://shiro.apache.org/authentication.html


        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);


        String password = usersEntity.getPassword();
        String hashPass = encrypt(password);
        //   log.info(hashPass);
        UsernamePasswordToken token = new UsernamePasswordToken(usersEntity.getUsername(), hashPass);
        // log.info(String.valueOf(new UsernamePasswordToken(usersEntity.getUsername(), hashPass)));
        log.info(String.valueOf(token));
        Subject currentUser = SecurityUtils.getSubject();
        log.info(String.valueOf(currentUser));
        Session session = currentUser.getSession();

        if (!currentUser.isAuthenticated()) {
            //  log.info('in if');

            token.setRememberMe(true);

            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username or wrong password of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        log.info("test role : " + currentUser);
        //test a role:
        if (currentUser.hasRole("Admin")) {
            log.info("Bravo tu es Admin");
        } else {
            log.info("Hello, mere mortal.");
        }

        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("addUsers")) {
            log.info("Tu as l'autorisation Test.");
        } else {
            log.info("Sorry, lightsaber1 rings are for schwartz masters only.");
        }
        if (currentUser.isPermitted("test22")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        if (currentUser.isPermitted("test2")) {
            log.info("Tu as l'autorisation Test2.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //all done - log out!
        //     currentUser.logout();

        //    System.exit(0);
//no problem
        //faire le truc des roles

    }

/*

    public void connection(UsersEntity usersEntity) {
        log.info("My First Apache Shiro Application");

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // get the currently executing user:
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username):
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission:
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        currentUser.logout();

        System.exit(0);
    }
*/


    public void findAllUsers() {
        log.info("bgin findallusers");
        usersEntityList = findAll();
        log.info(String.valueOf(usersEntityList));
    }

    protected List<UsersEntity> findAll() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<UsersEntity> usersEntities = dao.findAllUsers(em);

        em.clear();
        em.close();

        return usersEntities;
    }

    private UsersEntity findById(EntityManager em, int id) {
        return dao.findById(em, id);
    }


    public void updateUsersAdmin() {
        log.info("begin updateUsrAdmin" + usersEntity.getUsername());
        log.info(String.valueOf(usersEntity.getEmail()));

//pour les mdp, comparer en db AVANT hashage si il correspondent. Si ils corresepondent pas, on le hash et on le modifie
        // !!! verifier avant d'"hasher le mdp si la regex est ok et si ce n'est pas un mdp deja hashé

        try {
            validateUsersUpdateByAdmin(usersEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();
        int userId = Integer.valueOf(usersEntity.getId());
        log.info(String.valueOf(usersEntity.getId()));

        EntityManager em = EMF.getEM();
        UsersEntity usersEntityOld = dao.findById(em, userId);
        log.info(usersEntityOld.getUsername());
        log.info(usersEntity.getUsername());
        if (!usersEntity.getUsername().equals(usersEntityOld.getUsername())) {
            try {
                checkEntities.checkUserByUsername(usersEntity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }
            usersEntity.setUsername(usersEntity.getUsername());
        }

        try {
            checkEntities.checkRole(usersEntity.getRolesByIdRoles());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, usersEntity);
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


    private String encrypt(String password) {
        return new Sha256Hash(password).toString();
    }


    private void validateUsers(UsersEntity entity, String password2) {
        List<String> errors = UsersValidator.validate(entity, password2);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validateUsersUpdateByAdmin(UsersEntity entity) {
        List<String> errors = UsersValidator.validateUpdateByAdmin(entity);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    public void logOut() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }


    /**
     * Ouvrir le popup d'edition ou d'ajout
     */
    public void showPopupModal() {
        log.info("Show PopupModal");
        showPopup = true;
    }

    /**
     * Ouvrir le popup d'edition ou d'ajout
     */
    public void hidePopupModal() {
        log.info("Show PopupModal");
        showPopup = false;
    }


}
