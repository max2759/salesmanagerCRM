package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Larché Marie-Élise
 */
@Slf4j
public class UsersValidator {

    @Getter
    @Setter
    private static UsersDao dao = new UsersDaoImpl();

    public static List<String> validate(UsersEntity entity, String pass2) {
        List<String> errors = new ArrayList<>();

        log.info(String.valueOf(entity));

        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getRolesByIdRoles() == null) {
            errors.add("Le rôle est vide");
        }
        if (entity.getLastname() == null || entity.getLastname().isEmpty()) {
            errors.add("Le nom de famille est vide");
        }
        if (entity.getFirstname() == null || entity.getFirstname().isEmpty()) {
            errors.add("Le prénom est vide");
        }
        if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
            errors.add("L'email ne peut pas être vide'");
        } else if (!validateEmail(entity)) {
            errors.add("Votre email n'est pas valide");
        }

       /* if (entity.getUsername() == null || entity.getUsername().isEmpty()) {
            errors.add("Votre pseudo ne peux pas être vide");
        }
*/

        return errors;
    }

    public static List<String> validateUpdateByAdmin(UsersEntity entity) {
        List<String> errors = new ArrayList<>();

        log.info(String.valueOf(entity));
        EntityManager em = EMF.getEM();

        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getRolesByIdRoles() == null) {
            errors.add("Le rôle est vide");
        }
        if (entity.getLastname() == null || entity.getLastname().isEmpty()) {
            errors.add("Le nom de famille est vide");
        }
        if (entity.getFirstname() == null || entity.getFirstname().isEmpty()) {
            errors.add("Le prénom est vide");
        }
        if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
            errors.add("L'email ne peut pas être vide'");
        } else if (!validateEmail(entity)) {
            errors.add("Votre email n'est pas valide");
        }
        if (entity.getUsername() == null || entity.getUsername().isEmpty()) {
            errors.add("Votre pseudo ne peux pas être vide");
        } else if (dao.findByUsername(em, entity.getUsername()) != null) {
            errors.add("Votre pseudo est déjà pris");
        }


        return errors;
    }

    public static List<String> validateUpdateByUser(UsersEntity entity) {
        List<String> errors = new ArrayList<>();

        log.info(String.valueOf(entity));
        EntityManager em = EMF.getEM();

        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getLastname() == null || entity.getLastname().isEmpty()) {
            errors.add("Le nom de famille est vide");
        }
        if (entity.getFirstname() == null || entity.getFirstname().isEmpty()) {
            errors.add("Le prénom est vide");
        }
        if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
            errors.add("L'email ne peut pas être vide'");
        } else if (!validateEmail(entity)) {
            errors.add("Votre email n'est pas valide");
        }
        if (entity.getUsername() == null || entity.getUsername().isEmpty()) {
            errors.add("Votre pseudo ne peux pas être vide");
        } else if (dao.findByUsername(em, entity.getUsername()) != null) {
            errors.add("Votre pseudo est déjà pris");
        }


        return errors;
    }

    public static boolean validatePassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }


    public static boolean validateEmail(UsersEntity entity) {
        String regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]{2,}\\.[a-z]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(entity.getEmail());

        return matcher.find();
    }

    public static boolean vaidateMatchPassword(String pass, String pass2) {
        return pass.equals(pass2);
    }

}
