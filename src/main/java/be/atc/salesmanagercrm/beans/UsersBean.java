package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.validators.UsersValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "usersBean")
@SessionScoped
public class UsersBean extends ExtendBean implements Serializable {

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
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @Getter
    @Setter
    private UsersEntity userForDialog;

    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesFiltered;
    @Getter
    @Setter
    private List<UsersEntity> usersEntities;
    @Getter
    @Setter
    private UsersEntity selectedUserEntity;

    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesRole = new ArrayList<>();


    public void initialiseDialogUserId(Integer idUser) {
        if (idUser == null || idUser < 1) {
            return;
        }
        userForDialog = getDao().findById(EMF.getEM(), idUser);
        usersEntity = dao.findById(EMF.getEM(), idUser);
    }


    public void register() {

        FacesMessage msg;
        FacesMessage msg1;

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
            validateUsers(usersEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }
        String firstName3Cara;
        String lastName3Cara;

        if (usersEntity.getFirstname().length() >= 3) {
            firstName3Cara = usersEntity.getFirstname().substring(0, 3);
            log.info(firstName3Cara);
        } else {
            char randomLetter = (char) (Math.random() * ('z' - 'a' + 1));
            firstName3Cara = usersEntity.getFirstname().substring(0, 3) + randomLetter;
            log.info(firstName3Cara);
        }

        if (usersEntity.getLastname().length() >= 3) {
            lastName3Cara = usersEntity.getLastname().substring(0, 3);
            log.info(lastName3Cara);
        } else {
            char randomLetter = (char) (Math.random() * ('z' - 'a' + 1));
            lastName3Cara = usersEntity.getLastname().substring(0, 3) + randomLetter;
            log.info(lastName3Cara);
        }


        String usernameWithoutNumber = firstName3Cara + lastName3Cara;

        Random random = new Random();
        int number = random.nextInt(99 - 1);

        CheckEntities checkEntities = new CheckEntities();

        String username = checkEntities.checkUserByUsernameAuto(usernameWithoutNumber, number);
        usersEntity.setUsername(username);

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


        char[] passwordUncrypted = aleaPassword();


        String passwordUncrypt = new String(passwordUncrypted);

        //      String password = usersEntity.getPassword();
        String hashPass = encrypt(passwordUncrypt);
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.registerUsername"), username);
            msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.registerPassword"), passwordUncrypt);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorRegister"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }

    }

    public void connection() throws IOException {
        //Example using most common scenario of username/password pair:
        //https://shiro.apache.org/authentication.html

        try {
            validateConnection(usersEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        String password = usersEntity.getPassword();
        String hashPass = encrypt(password);

        //CheckEntities checkEntities = new CheckEntities();
 /*       try {
            checkEntities.checkUserByUsernameAndPassword(usersEntity, hashPass);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }
*/

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        UsernamePasswordToken token = new UsernamePasswordToken(usersEntity.getUsername(), hashPass);
        // log.info(String.valueOf(new UsernamePasswordToken(usersEntity.getUsername(), hashPass)));
        log.info(String.valueOf(token));
        Subject currentUser = SecurityUtils.getSubject();
        log.info(String.valueOf(currentUser));
        Session session = currentUser.getSession();

        if (!currentUser.isAuthenticated()) {

            token.setRememberMe(true);

            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username or wrong password of " + token.getPrincipal());
                return;
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                return;
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                return;
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
        session.setAttribute("idUser", usersEntity.getId());

        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().redirect("userUpdateByUser.xhtml");
    }


    public void delete(int id) {
        FacesMessage msg;
        log.info(String.valueOf(id));

        EntityManager em = EMF.getEM();
        UsersEntity usersEntity1 = dao.findById(em, id);

        usersEntity1.setActive(false);

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, usersEntity1);
            tx.commit();
            log.info("Delete ok");
            findAllUsers();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.deleted"), null);
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

    public void activate(int id) {
        FacesMessage msg;

        EntityManager em = EMF.getEM();
        UsersEntity usersEntity1 = dao.findById(em, id);

        usersEntity1.setActive(true);

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, usersEntity1);
            tx.commit();
            log.info("Delete ok");
            findAllUsers();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.deleted"), null);
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


    public void updateByAdmin() {
        updateUsersAdmin(this.usersEntity);
        usersEntityList = findAll();
    }

    private void updateUsersAdmin(UsersEntity usersEntity) {
        log.info("begin updateUsrAdmin" + usersEntity.getUsername());
        log.info(String.valueOf(usersEntity.getEmail()));

//pour les mdp, comparer en db AVANT hashage si il correspondent. Si ils corresepondent pas, on le hash et on le modifie
        // !!! verifier avant d'"hasher le mdp si la regex est ok et si ce n'est pas un mdp deja hashé
        EntityManager em = EMF.getEM();
        CheckEntities checkEntities = new CheckEntities();

        UsersEntity usersEntityOld = dao.findById(em, usersEntity.getId());
        String usernameOld = usersEntityOld.getUsername();
        if (usernameOld.equals(usersEntity.getUsername())) {
            try {
                validateUsersUpdateByAdminNoChange(usersEntity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }
        } else {
            try {
                validateUsersUpdateByAdmin(usersEntity);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }

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

    public void updateByUser() {
        userUpdateByUser(this.usersEntity);
        EntityManager em = EMF.getEM();
        findById(em, usersEntity.getId());
    }

    /**
     * @param usersEntity
     */
    private void userUpdateByUser(UsersEntity usersEntity) {

        log.info("begin updateUsrByUser" + usersEntity.getUsername());

//pour les mdp, comparer en db AVANT hashage si il correspondent. Si ils corresepondent pas, on le hash et on le modifie
        // !!! verifier avant d'"hasher le mdp si la regex est ok et si ce n'est pas un mdp deja hashé
        log.info(password2);
        try {
            validateUsersUpdateByUser(usersEntity, password2);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        String password = encrypt(usersEntity.getPassword());

        usersEntity.setPassword(password);

        EntityManager em = EMF.getEM();

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


    private char[] aleaPassword() {
        int length = 8;
        String symbol = "-/.^&*_!@%=+>)";
        String cap_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String small_letter = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String finalString = cap_letter + small_letter +
                numbers + symbol;
        Random random = new Random();
        char[] password = new char[length];
        for (int i = 0; i < length; i++) {
            password[i] =
                    finalString.charAt(random.nextInt(finalString.length()));
        }
        System.out.println(password);

        return password;
    }

    public void findUser() {

        log.info(String.valueOf(usersEntity.getUsername()));
        String username = usersEntity.getUsername();
        //rechercher par pseudo
        EntityManager em = EMF.getEM();
        usersEntity = dao.findByUsername(em, username);
        log.info(String.valueOf(usersEntity));
        usersEntity = findById(em, usersEntity.getId());
        log.info(String.valueOf(usersEntity));
    }

    private void findByUsernameAndPass(UsersEntity entity) {
        List<String> errors = UsersValidator.connection(entity);
        if (!errors.isEmpty()) {
            log.error("Connexion is not valide {}", entity);
            throw new InvalidEntityException("Le mot de passe ou le pseudo ne sont pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validateUsers(UsersEntity entity) {
        List<String> errors = UsersValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    /**
     * for the case when username is different
     *
     * @param entity
     */
    private void validateUsersUpdateByAdmin(UsersEntity entity) {
        List<String> errors = UsersValidator.validateUpdateByAdmin(entity);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    /**
     * for the case when username haven't change
     *
     * @param entity
     */
    private void validateUsersUpdateByAdminNoChange(UsersEntity entity) {
        List<String> errors = UsersValidator.validateUpdateByAdminNoChange(entity);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validateUsersUpdateByUser(UsersEntity entity, String password2) {
        List<String> errors = UsersValidator.validateUpdateByUser(entity, password2);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validateConnection(UsersEntity entity) {
        List<String> errors = UsersValidator.connection(entity);
        if (!errors.isEmpty()) {
            log.error("Connexion is not valide {}", entity);
            throw new InvalidEntityException("La connexion n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    public void logOut() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }


}
