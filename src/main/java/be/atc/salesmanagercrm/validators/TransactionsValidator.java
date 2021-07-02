package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.TransactionsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Younes Arifi
 */
public class TransactionsValidator {

    public static List<String> validate(TransactionsEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("L utilisateur n existe pas !");
            errors.add("Veuillez renseigner un un titre dans votre transaction");
            errors.add("Veuillez renseigner un nombre positif dans votre transaction");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L utilisateur n existe pas !");
        }
        if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
            errors.add("Veuillez renseigner un titre dans votre transaction");
        }
        if (entity.getAmount() != null) {
            if (entity.getAmount() < 0) {
                errors.add("Veuillez renseigner un nombre positif dans votre transaction");
            }
        }
        return errors;
    }
}
