package be.atc.salesmanagercrm.validators;

import be.atc.salesmanagercrm.dao.RolesDao;
import be.atc.salesmanagercrm.dao.impl.RolesDaoImpl;
import be.atc.salesmanagercrm.entities.RolesEntity;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
public class RolesValidator {

    public static List<String> validate(RolesEntity entity) {
        List<String> errors = new ArrayList<>();
        RolesDao dao = new RolesDaoImpl();

        log.info(String.valueOf(entity));
        EntityManager em = EMF.getEM();
        if (entity == null) {
            errors.add("La reception des données à échouée");
            errors.add("Veuillez recommencer votre inscription");
            return errors;
        }
        if (entity.getLabel() == null || entity.getLabel().isEmpty()) {
            errors.add("Le nom de rôle est vide");
        } else if (dao.findByLabel(em, entity.getLabel()) != null) {
            errors.add("Votre nom de rôle existe déjà");
        }

        return errors;
    }


}
