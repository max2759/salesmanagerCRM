package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.beans.CheckEntities;
import be.atc.salesmanagercrm.dao.PermissionsDao;
import be.atc.salesmanagercrm.dao.impl.PermissionsDaoImpl;
import be.atc.salesmanagercrm.entities.PermissionsEntity;
import be.atc.salesmanagercrm.entities.RolesPermissionsEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.extern.slf4j.Slf4j;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import java.util.Locale;

/**
 * @author Larche Marie-Élise
 */
@Slf4j
@FacesValidator("rolePermissionsComboValidator")
public class RolesPermissionsComboFrontValidator implements Validator {

    PermissionsDao pdao = new PermissionsDaoImpl();

    Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    // recup les roles pour comparer
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        CheckEntities checkEntities = new CheckEntities();
        EntityManager em = EMF.getEM();

        RolesPermissionsEntity rolesPermissionsEntity = new RolesPermissionsEntity();
        log.info((String) value);
        PermissionsEntity permissionsEntity = pdao.findByLabel(em, (String) value);



/*
        try {
            checkEntities.checkRoleByLabel(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code erreur : " + exception.getErrorCodes().getCode() + " - " + exception.getMessage());
            throw new ValidatorException(new FacesMessage(getMessageErrorLabelAlreadyExist()));
        }
    }*/
    }
}
