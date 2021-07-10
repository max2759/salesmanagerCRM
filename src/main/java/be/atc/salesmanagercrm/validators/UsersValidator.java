package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.UsersEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
        if (entity.getPassword() == null || entity.getPassword().isEmpty() || pass2 == null || pass2.isEmpty()) {
            errors.add("Votre mot de passe ne peut pas être vide");
        } else if (!validatePassword(entity.getPassword()) || !validatePassword(pass2)) {
            errors.add("Votre mot de passe doit faire 6 caractéres, posséder une majuscule et un chiffre ou un caractére spécial");
        } else if (!vaidateMatchPassword(entity.getPassword(), pass2)) {
            errors.add("Vos mots de passe doivent être identique");
        }
        if (entity.getUsername() == null || entity.getUsername().isEmpty()) {
            errors.add("Votre pseudo ne peux pas être vide");
        }


        return errors;
    }

    public static boolean validatePassword(String password) {
        String regex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }


    public static boolean validateEmail(UsersEntity entity) {
        String regex = "#^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]{2,}\\.[a-z]{2,4}$#";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(entity.getEmail());

        return matcher.find();
    }

    public static boolean vaidateMatchPassword(String pass, String pass2) {
        return pass.equals(pass2);
    }

}
