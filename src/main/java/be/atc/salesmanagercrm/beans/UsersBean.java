package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.RolesDaoImpl;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import be.atc.salesmanagercrm.utils.PDFUtil;
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
import java.util.stream.Collectors;


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
    private RolesDao rolesDao = new RolesDaoImpl();
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
    private String passwordCo;
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
    private List<RolesEntity> rolesEntities;
    @Getter
    @Setter
    private UsersEntity selectedUserEntity;
    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesRole = new ArrayList<>();
    @Getter
    @Setter
    private UsersEntity userEntityNew = new UsersEntity();


    @Getter
    @Setter
    private UsersEntity usersEntityOther = new UsersEntity();
    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesActive;
    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesActiveFiltered;
    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesDisable;
    @Getter
    @Setter
    private List<UsersEntity> usersEntitiesDisableFiltered;

    @Getter
    @Setter
    private Subject currentUser;

    @Getter
    @Setter
    private Session session;

    @Inject
    AccessControlBean accessControlBean;

    public void validateLength() {
        if (userEntityNew.getEmail() != null && userEntityNew.getEmail().length() < 2) {
            FacesContext.getCurrentInstance().addMessage("mail", new FacesMessage(FacesMessage.SEVERITY_ERROR, "error.notEnoughChar", "2"));
        }
    }

    public void initialiseDialogUserId(Integer idUser) {
        //  userForDialog = new UsersEntity();
        log.info(String.valueOf(idUser));
        if (idUser == null || idUser < 1) {
            return;
        }
        userForDialog = getDao().findById(EMF.getEM(), idUser);
        usersEntityOther = dao.findById(EMF.getEM(), idUser);
    }

    public void createNewUserEntity() {
        userEntityNew = new UsersEntity();
    }


    public static String getRandomStr(int n) {
        //choisissez un caractére au hasard à partir de cette chaîne
        String str = "abcdefghijklmnopqrstuvxyz";

        StringBuilder s = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (str.length() * Math.random());
            s.append(str.charAt(index));
        }
        return s.toString();
    }

    public void registerEntity() {
        register(userEntityNew);
        createNewUserEntity();
    }

    protected void register(UsersEntity entityToAdd) {

        String username;
        FacesMessage msg;
        log.info("in register");
        if (this.currentUser.isPermitted("addUsers")) {
            log.info("Tu as l'autorisation Test.");

            log.info(String.valueOf(entityToAdd));
            log.info(String.valueOf(entityToAdd));

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
                validateUsers(entityToAdd);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }
            String firstName3Cara;
            String lastName3Cara;

            if (entityToAdd.getFirstname().length() >= 3) {
                firstName3Cara = entityToAdd.getFirstname().substring(0, 3);
                log.info(firstName3Cara);
            } else {
                String randomLetter = getRandomStr(1);
                firstName3Cara = entityToAdd.getFirstname().substring(0, 2) + randomLetter;
                log.info(firstName3Cara);
            }

            if (entityToAdd.getLastname().length() >= 3) {
                lastName3Cara = entityToAdd.getLastname().substring(0, 3);
                log.info(lastName3Cara);
            } else {
                String randomLetter = getRandomStr(1);
                lastName3Cara = entityToAdd.getLastname().substring(0, 2) + randomLetter;
                log.info(lastName3Cara);
            }


            String usernameWithoutNumber = firstName3Cara + lastName3Cara;

            Random random = new Random();
            int number = random.nextInt(99 - 1);

            CheckEntities checkEntities = new CheckEntities();


            username = checkEntities.checkUserByUsernameAuto(usernameWithoutNumber, number);


            entityToAdd.setUsername(username);

            try {
                checkEntities.checkUserByUsername(entityToAdd);
            } catch (InvalidEntityException exception) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorRegister"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }

            try {
                checkEntities.checkRole(entityToAdd.getRolesByIdRoles());
            } catch (EntityNotFoundException exception) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorRegister"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
                return;
            }

            log.info("username final " + username);
            char[] passwordUncrypted = aleaPassword();


            String passwordUncrypt = new String(passwordUncrypted);

            //      String password = usersEntity.getPassword();
            String hashPass = encrypt(passwordUncrypt);
            log.info(hashPass);

            entityToAdd.setPassword(hashPass);

            entityToAdd.setActive(true);
            entityToAdd.setRegisterDate(LocalDateTime.now());

            //    usersEntity.setRolesByIdRoles(rolesBean.findById(em, idRole));
//log.info("avant em , le role est: "+usersEntity.getRolesByIdRoles().getId());
            EntityManager em = EMF.getEM();
            EntityTransaction tx = null;
            try {
                tx = em.getTransaction();
                tx.begin();
                dao.register(em, entityToAdd);
                tx.commit();
                generatePDF(passwordUncrypt);
                //    JavaMailUtil.sendMail(entityToAdd);
                log.info("Persist ok");
                loadListEntities();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.register"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Exception ex) {
                if (tx != null && tx.isActive()) tx.rollback();
                log.info("Persist echec");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorRegister"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } finally {
                em.clear();
                em.clear();
            }
        } else {
            log.info("Sorry, lightsaber1 rings are for schwartz masters only.");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorRegister"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void connection() throws IOException {
        //Example using most common scenario of username/password pair:
        //https://shiro.apache.org/authentication.html
        FacesMessage msg;
        log.info(passwordCo);

        try {
            validateConnection(usersEntity, passwordCo);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        //String password = usersEntity.getPassword();
        String hashPass = encrypt(passwordCo);

        CheckEntities checkEntities = new CheckEntities();
 /*       try {
            checkEntities.checkUserByUsernameAndPassword(usersEntity, hashPass);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }
*/

        try {
            checkEntities.checkUserActiveForConnection(usersEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "user"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        UsernamePasswordToken token = new UsernamePasswordToken(usersEntity.getUsername(), hashPass);
        // log.info(String.valueOf(new UsernamePasswordToken(usersEntity.getUsername(), hashPass)));
        log.info(String.valueOf(token));
        this.currentUser = SecurityUtils.getSubject();
        log.info(String.valueOf(currentUser));
        this.session = currentUser.getSession();

        if (!this.currentUser.isAuthenticated()) {

//            token.setRememberMe(true);

            try {
                this.currentUser.login(token);
            } catch (UnknownAccountException uae) {
                //message ici a chque fois
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userOrPasswordFalse"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                log.info("There is no user with username or wrong password of " + token.getPrincipal());
                return;
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userOrPasswordFalse"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }


        log.info("User [" + this.currentUser.getPrincipal() + "] logged in successfully.");

        log.info("test role : " + this.currentUser);
        //test a role:
       /* if (this.currentUser.hasRole("Admin")) {
            log.info("Bravo tu es Admin");
        } else {
            log.info("Hello, mere mortal.");
        }

        //test a typed permission (not instance-level)
        if (this.currentUser.isPermitted("addUsers")) {
            log.info("Tu as l'autorisation Test.");
        } else {
            log.info("Sorry, lightsaber1 rings are for schwartz masters only.");
        }
        if (this.currentUser.isPermitted("test22")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        if (this.currentUser.isPermitted("test2")) {
            log.info("Tu as l'autorisation Test2.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }
*/
        //all done - log out!
        //     this.currentUser.logout();

        //    System.exit(0);
//no problem
        //faire le truc des roles
        session.setAttribute("idUser", usersEntity.getId());
        session.setAttribute("username", usersEntity.getUsername());
        log.info("usersEntity : " + session.getAttribute("idUser"));

        try {
            usersEntity = findByUsername(usersEntity.getUsername());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().redirect("app/dashboard.xhtml?faces-redirect=true");
    }


    public void delete(int id) {
        FacesMessage msg;
        log.info(String.valueOf(id));


        // UsersEntity usersEntity1 = dao.findById(em, id);
        UsersEntity usersEntity1 = findById(id);

        usersEntity1.setActive(false);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, usersEntity1);
            tx.commit();
            log.info("Delete ok");
            loadListEntities();
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

        CheckEntities checkEntities = new CheckEntities();
        //UsersEntity usersEntity1 = dao.findById(em, id);
        UsersEntity usersEntity1 = findById(id);

        try {
            checkEntities.checkRoleForConnection(usersEntity1);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "roleInactive"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        usersEntity1.setActive(true);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, usersEntity1);
            tx.commit();
            log.info("Delete ok");
            loadListEntities();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.reactivate"), null);
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
        loadListEntities();

    }

    protected List<UsersEntity> findAll() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<UsersEntity> usersEntities = dao.findAllUsers(em);

        em.clear();
        em.close();

        return usersEntities;
    }

    protected UsersEntity findById(int id) {
        log.info("UsersBean => method : findById(int id)");

        FacesMessage msg;

        if (id == 0) {
            log.error("ID User is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new EntityNotFoundException(
                    "L ID de l utilisateur est incorrect", ErrorCodes.USER_NOT_FOUND
            );
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findById(em, id);
        } catch (Exception ex) {
            log.warn("User with ID {} was not found in the DB", id);
            throw new EntityNotFoundException(
                    "Aucun utilisateur avec l ID " + id + " n a ete trouve dans la BDD",
                    ErrorCodes.USER_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }


    protected UsersEntity findByUsername(String label) {
        log.info("UsersBean => method : findByUsername(String label)");

        FacesMessage msg;

        if (label == null || label.isEmpty()) {
            log.error("Username is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new EntityNotFoundException(
                    "Le nom d utilisateur est incorrect", ErrorCodes.USER_NOT_FOUND
            );
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findByUsername(em, label);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun utilisateur avec le label " + label + " n a ete trouve dans la BDD",
                    ErrorCodes.USER_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }


    public void updateByAdmin() {
        updateUsersAdmin(this.usersEntityOther);
        //usersEntityList = findAll();
        findAllUsers();
    }

    private void updateUsersAdmin(UsersEntity usersEntityOther) {
        FacesMessage msg;
        log.info("begin updateUsrAdmin" + usersEntityOther.getUsername());
        log.info(String.valueOf(usersEntity.getEmail()));

//pour les mdp, comparer en db AVANT hashage si il correspondent. Si ils corresepondent pas, on le hash et on le modifie
        // !!! verifier avant d'"hasher le mdp si la regex est ok et si ce n'est pas un mdp deja hashé

        CheckEntities checkEntities = new CheckEntities();
        UsersEntity usersEntityOld;
        try {
            usersEntityOld = findById(usersEntityOther.getId());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }


        String usernameOld = usersEntityOld.getUsername();
        if (usernameOld.equals(usersEntityOther.getUsername())) {
            try {
                validateUsersUpdateByAdminNoChange(usersEntityOther);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }
        } else {
            try {
                validateUsersUpdateByAdmin(usersEntityOther);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }

         /*   try {
                checkEntities.checkUserByUsername(usersEntityOther);
            } catch (InvalidEntityException exception) {
                log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
                return;
            }
            usersEntityOther.setUsername(usersEntityOther.getUsername());*/
        }


        try {
            checkEntities.checkRole(usersEntityOther.getRolesByIdRoles());
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, usersEntityOther);
            tx.commit();
            loadListEntities();
            log.info("Persist ok");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.updated"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }

    }

    /**
     * @param usersEntity UsersEntity
     */
    private void userUpdateByUser(UsersEntity usersEntity) {
        FacesMessage msg;
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, JsfUtils.returnMessage(getLocale(), "user.updated"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "errorOccured"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            em.clear();
            em.clear();
        }
    }


    /**
     * method to know which entity to reload
     */
    public void loadListEntities() {
        usersEntitiesActive = findAllUsersActive();
        usersEntitiesDisable = findAllDisable();
    }

    /**
     * Find usersTypes entities
     *
     * @return List UsersEntity
     */
    public List<UsersEntity> findAllUsersActive() {

        EntityManager em = EMF.getEM();

        List<UsersEntity> usersEntitiesActive = dao.findActiveUsers(em);

        em.clear();
        em.close();

        return usersEntitiesActive;
    }

    /**
     * Find usersTypes entities
     *
     * @return List UsersEntity
     */
    public List<UsersEntity> findAllDisable() {

        EntityManager em = EMF.getEM();

        List<UsersEntity> usersEntities = dao.findDisableUsers(em);

        em.clear();
        em.close();

        return usersEntities;
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


    /**
     * Auto complete for RolesType
     *
     * @param search String
     * @return list of rolesEntity
     */
    public List<RolesEntity> completeRolesAjax(String search) {

        String searchLowerCase = search.toLowerCase();

        List<RolesEntity> rolesEntitiesDropDown = findRoleAjaxList();
        log.info(String.valueOf(rolesEntitiesDropDown));
        log.info(String.valueOf(rolesEntitiesDropDown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList())));

        return rolesEntitiesDropDown.stream().filter(t -> t.getLabel().toLowerCase().contains(searchLowerCase)).collect(Collectors.toList());

    }

    /**
     * Find rolesTypes entities
     *
     * @return List RolesEntity
     */
    public List<RolesEntity> findRoleAjaxList() {

        EntityManager em = EMF.getEM();

        List<RolesEntity> rolesEntities = rolesDao.findAllRolesActive(em);

        em.clear();
        em.close();

        return rolesEntities;
    }

    /* private void findByUsernameAndPass(UsersEntity entity) {
         List<String> errors = UsersValidator.connection(entity);
         if (!errors.isEmpty()) {
             log.error("Connexion is not valide {}", entity);
             throw new InvalidEntityException("Le mot de passe ou le pseudo ne sont pas valide", ErrorCodes.USER_NOT_VALID, errors);
         }
     }
 */
    private void validateUsers(UsersEntity entity) {
        List<String> errors = UsersValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    private void validateRole(UsersEntity entity) {
        FacesMessage msg;
        CheckEntities checkEntities = new CheckEntities();
        try {
            checkEntities.checkRoleForConnection(entity);
        } catch (EntityNotFoundException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "user"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
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

    private void validateConnection(UsersEntity entity, String passwordCo) {
        List<String> errors = UsersValidator.connection(entity, passwordCo);
        if (!errors.isEmpty()) {
            log.error("Connexion is not valide {}", entity);
            throw new InvalidEntityException("La connexion n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    public void logOut() {
        this.currentUser = SecurityUtils.getSubject();
        this.currentUser.logout();
        accessControlBean.isNotLogged();
    }

    protected void generatePDF(String password) {
        PDFUtil.generatePDF(userEntityNew, password);
    }
}
