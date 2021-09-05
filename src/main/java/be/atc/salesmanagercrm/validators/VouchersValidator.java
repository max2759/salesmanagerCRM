package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.entities.VouchersEntity;
import be.atc.salesmanagercrm.enums.EnumPriority;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Younes Arifi
 */
public class VouchersValidator {
    @Getter
    private static final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public static List<String> validate(VouchersEntity entity) {
        List<String> errors = new ArrayList<>();
        String errorMessage = null;

        if (entity == null) {
            errors.add("L utilisateur n existe pas !");
            errors.add("Veuillez renseigner un titre dans votre ticket");

            errorMessage = JsfUtils.returnMessage(getLocale(), "userNotExist") + "\n" + JsfUtils.returnMessage(getLocale(), "vouchers.validator.title") + "\n" + JsfUtils.returnMessage(getLocale(), "vouchers.validator.status");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return errors;
        }
        if (entity.getUsersByIdUsers() == null) {
            errors.add("L utilisateur n existe pas !");

            errorMessage = JsfUtils.returnMessage(getLocale(), "userNotExist") + "\n";
        }
        if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
            errors.add("Veuillez renseigner un titre dans votre ticket");

            errorMessage = errorMessage == null ? JsfUtils.returnMessage(getLocale(), "vouchers.validator.title") + "\n" : errorMessage + JsfUtils.returnMessage(getLocale(), "voucher.validator.title") + "\n";
        }
        if (entity.getTitle().length() > 255) {
            errors.add("Le nombre de caractere du titre doit etre inferieur ou egale a 255");

            errorMessage = errorMessage == null ? JsfUtils.returnMessage(getLocale(), "validator.titleLength") + "\n" : errorMessage + JsfUtils.returnMessage(getLocale(), "validator.titleLength") + "\n";
        }
        if (entity.getVoucherStatusByIdVoucherStatus() == null) {
            errors.add("Le statut du ticket n existe pas !");

            errorMessage = errorMessage == null ? JsfUtils.returnMessage(getLocale(), "vouchers.validator.status") + "\n" : errorMessage + JsfUtils.returnMessage(getLocale(), "vouchers.validator.status") + "\n";
        }
        if (entity.getPriority() != null) {
            if (
                    !Arrays.stream(EnumPriority.values())
                            .map(EnumPriority::getLabel)
                            .collect(Collectors.toSet())
                            .contains(entity.getPriority().getLabel())
            ) {
                errors.add("Veuillez renseigner une priorité valide");

                errorMessage = errorMessage == null ? JsfUtils.returnMessage(getLocale(), "vouchers.validator.priority") + "\n" : errorMessage + JsfUtils.returnMessage(getLocale(), "vouchers.validator.priority") + "\n";
            }
        }
        if (entity.getDescription().length() > 255) {
            errors.add("Le nombre de caractere de la description doit etre inferieur ou egale a 255");

            errorMessage = errorMessage == null ? JsfUtils.returnMessage(getLocale(), "validator.descriptionLength") + "\n" : errorMessage + JsfUtils.returnMessage(getLocale(), "validator.descriptionLength") + "\n";
        }
        if (errorMessage != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return errors;
    }
}