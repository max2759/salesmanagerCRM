package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.VouchersEntity;
import be.atc.salesmanagercrm.enums.EnumPriority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Younes Arifi
 */
public class VouchersValidator {

    public static List<String> validate(VouchersEntity entity) {
        List<String> errors = new ArrayList<>();

        if (entity == null) {
            errors.add("L utilisateur n existe pas !");
            errors.add("Veuillez renseigner un titre dans votre ticket");
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L utilisateur n existe pas !");
        }
        if (entity.getVoucherStatusByIdVoucherStatus() == null) {
            errors.add("Le statut du ticket n existe pas !");
        }
        if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
            errors.add("Veuillez renseigner un titre dans votre ticket");
        }
        if (entity.getPriority() != null) {
            if (
                    !Arrays.stream(EnumPriority.values())
                            .map(EnumPriority::getLabel)
                            .collect(Collectors.toSet())
                            .contains(entity.getPriority().getLabel())
            ) {
                errors.add("Veuillez renseigner une priorité valide");
            }
        }
        return errors;
    }
}