package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.*;
import be.atc.salesmanagercrm.dao.impl.*;
import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import java.io.Serializable;

@Slf4j
@SessionScoped
public class CheckEntities implements Serializable {

    private static final long serialVersionUID = -6795998607327751632L;

    ContactsDao contactsDao = new ContactsDaoImpl();
    UsersDao usersDao = new UsersDaoImpl();
    CompaniesDao companiesDao = new CompaniesDaoImpl();
    TaskTypesDao taskTypesDao = new TaskTypesDaoImpl();
    VoucherStatusDao voucherStatusDao = new VoucherStatusDaoImpl();

    /**
     * Check if Contact exist in DB
     *
     * @param entity : ContactsEntity
     */
    public void checkContact(ContactsEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            ContactsEntity contactsEntity = contactsDao.findById(em, entity.getId());
            if (contactsEntity == null) {
                log.warn("Contact with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucun contact avec l ID " + entity.getId() + " n a ete trouve dans la BDD", ErrorCodes.CONTACT_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check uf company exist in DB
     *
     * @param entity : CompaniesEntity
     */

    public void checkCompany(CompaniesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            CompaniesEntity companiesEntity = companiesDao.findById(em, entity.getId());
            if (companiesEntity == null) {
                log.warn("Company with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucune compagnie avec l ID " + entity.getId() + " n a ete trouvee dans la BDD", ErrorCodes.COMPAGNY_NOT_FOUND
                );
            }
        }
    }


    /**
     * Check if user exist in DB
     *
     * @param entity : UsersEntity
     */

    public void checkUser(UsersEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();

            UsersEntity usersEntity = usersDao.findById(em, entity.getId());
            if (usersEntity == null) {
                log.warn("User with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucun utilisateur avec l ID " + entity.getId() + " n a ete trouve dans la BDD", ErrorCodes.USER_NOT_FOUND
                );
            }

            if (!usersEntity.isActive()) {
                log.warn("User with ID {} is not active", entity.getId());
                throw new InvalidEntityException(
                        "L utilisateur avec l id " + usersEntity.getId() + " est desactive", ErrorCodes.USER_NOT_VALID
                );
            }
        }
    }


    /**
     * Check if TaskType exist in DB
     *
     * @param entity : TaskTypesEntity
     */
    public void checkTaskType(TaskTypesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();

            TaskTypesEntity taskTypesEntity = taskTypesDao.findById(em, entity.getId());
            if (taskTypesEntity == null) {
                log.warn("Task type with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucun type de tâche avec l ID " + entity.getId() + " n a ete trouvee dans la BDD", ErrorCodes.TASKTYPE_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if VoucherStatus exit in DB
     *
     * @param entity VoucherStatusEntity
     */
    public void checkVoucherStatus(VoucherStatusEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();

            VoucherStatusEntity voucherStatusEntity = voucherStatusDao.findById(em, entity.getId());
            if (voucherStatusEntity == null) {
                log.warn("Voucher Status with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucun statut de ticket avec l ID " + entity.getId() + " n a ete trouvee dans la BDD", ErrorCodes.VOUCHERSTATUS_NOT_FOUND
                );
            }
        }
    }
}
