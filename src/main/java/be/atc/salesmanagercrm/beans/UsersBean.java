package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.UsersEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

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


    public void register(UsersEntity usersEntity) {

    }

    public void connection(UsersEntity usersEntity) {

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


}
