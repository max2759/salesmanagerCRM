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

    public static List<String> validate(UsersEntity entity) {
        List<String> errors = new ArrayList<>();

        return errors;
    }

    public static List<String> validateRegister(UsersEntity entity) {
        List<String> errors = new ArrayList<>();

        String usernname = entity.getUsername();
        boolean validPass = validatePassword(entity);


        UsersEntity result = dao.findByUsername(usernname);

        if (result != null) {
            errors.add("Votre nom d'utilisateur est déjà pris");
        }
        if (validPass == false) {
            errors.add("Votre mot de passe doit faire 8 caractéres, posséder une majuscule et un chiffre ou un caractére spécial");
        }
        return errors;
    }

    public static boolean validatePassword(UsersEntity entity) {
        String regex = "(?=^.{8,}$)((?=.*d)|(?=.*W+))(?![.])(?=.*[A-Z])(?=.*[a-z]).*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(entity.getPassword());
        boolean matchFound = matcher.find();

        return matchFound;
    }

}
