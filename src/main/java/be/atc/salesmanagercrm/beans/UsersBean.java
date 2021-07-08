package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.UsersValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
    @Inject
    private RolesBean rolesBean;
    @Getter
    @Setter
    private String password2;
    @Getter
    @Setter
    private List<UsersEntity> usersEntityList;

    private HttpServletRequest request;


    /**
     * @throws NoSuchAlgorithmException
     */
    public void register() throws NoSuchAlgorithmException {
        //regex password + hash + pseudo unique

        LocalDateTime currentDate = LocalDateTime.now();

        //    log.info(password2);

        //valid if no errors
        try {
            List<String> errors = UsersValidator.validateRegister(usersEntity, password2);
            if (!errors.isEmpty()) {
                return;
            }
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            return;
        }

        //if it's ok, hash the password
        //verifier dans le cahier des charge si il est generé automatiquement
        String password = usersEntity.getPassword();
        //ici, remplacer par un converter pour le hashage du mdp
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // convertir bytes en hexadécimal
        StringBuilder s = new StringBuilder();
        for (byte b : hash) {
            s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        //  System.out.println(s.toString());
        String hashPass = s.toString();

        usersEntity.setPassword(hashPass);
        usersEntity.setActive(true);
        usersEntity.setRolesByIdRoles(rolesBean.findById(1));
        usersEntity.setRegisterDate(LocalDateTime.now());

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


    public void securityUtils() {
        Subject usr = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("mike", "abcdef");
        try {
            usr.login(token);
        } catch (AuthenticationException ae) {
            log.error(ae.toString());
            return;
        }
        log.info("User [" + usr.getPrincipal() + "] logged in successfully.");
    }

    private void validateUsers(UsersEntity entity) {
        List<String> errors = UsersValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Register is not valide {}", entity);
            throw new InvalidEntityException("L'inscription n est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

}
