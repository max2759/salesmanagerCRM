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
    CompanyTypesDao companyTypesDao = new CompanyTypesDaoImpl();
    TaskTypesDao taskTypesDao = new TaskTypesDaoImpl();
    JobTitlesDao jobTitlesDao = new JobTitlesDaoImpl();
    BranchActivitiesDao branchActivitiesDao = new BranchActivitiesDaoImpl();
    VoucherStatusDao voucherStatusDao = new VoucherStatusDaoImpl();
    TransactionTypesDao transactionTypesDao = new TransactionTypesDaoImpl();
    TransactionPhasesDao transactionPhasesDao = new TransactionPhasesDaoImpl();

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
     * Check if company exist in DB
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
     * Check if JobTitle already exist in DB
     *
     * @param jobTitlesEntity
     */
    public void checkJobTitlesLabel(JobTitlesEntity jobTitlesEntity) {
        if (jobTitlesEntity != null) {
            EntityManager em = EMF.getEM();

            if (jobTitlesDao.findByLabel(em, jobTitlesEntity.getLabel())) {
                log.warn("Job Title label already exist");
                throw new InvalidEntityException(
                        "L'intitulé du poste " + jobTitlesEntity.getLabel() + " existe déjà", ErrorCodes.JOBTITLES_NOT_VALID
                );
            }
        }
    }

    /**
     * Check if Branch_Activities label already exist in DB
     *
     * @param branchActivitiesEntity
     */
    public void checkBranchActivitiesLabel(BranchActivitiesEntity branchActivitiesEntity) {
        if (branchActivitiesEntity != null) {
            EntityManager em = EMF.getEM();

            if (branchActivitiesDao.findByLabel(em, branchActivitiesEntity.getLabel())) {
                log.warn("Branch activities label already exist");
                throw new InvalidEntityException(
                        "L'intitulé du poste " + branchActivitiesEntity.getLabel() + " existe déjà", ErrorCodes.BRANCHACTIVITIESLABEL_NOT_VALID
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

    /**
     * Check if CompanyTypes exist in DB
     *
     * @param companyTypesEntity
     */
    public void checkCompanyTypes(CompanyTypesEntity companyTypesEntity) {
        if (companyTypesEntity != null) {
            EntityManager em = EMF.getEM();

            CompanyTypesEntity companyTypesEntityToCheck = companyTypesDao.findById(em, companyTypesEntity.getId());
            if (companyTypesEntityToCheck == null) {
                log.warn("Company type with ID {} was not found in the DB", companyTypesEntity.getId());
                throw new EntityNotFoundException(
                        "Aucun type de société avec l ID " + companyTypesEntity.getId() + " n a ete trouvé dans la DB", ErrorCodes.COMPANYTYPES_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if branch activities exist
     *
     * @param branchActivitiesEntity
     */
    public void checkBranchActivities(BranchActivitiesEntity branchActivitiesEntity) {
        if (branchActivitiesEntity != null) {
            EntityManager em = EMF.getEM();

            BranchActivitiesEntity branchActivitiesEntityToCheck = branchActivitiesDao.findById(em, branchActivitiesEntity.getId());
            if (branchActivitiesEntityToCheck == null) {
                log.warn("Company type with ID {} was not found in the DB", branchActivitiesEntity.getId());
                throw new EntityNotFoundException(
                        "Aucun type de société avec l ID " + branchActivitiesEntity.getId() + " n a ete trouvé dans la DB", ErrorCodes.BRANCHACTIVITIES_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if TransactionType exit in DB
     *
     * @param entity TransactionTypesEntity
     */
    public void checkTransactionTypes(TransactionTypesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();

            TransactionTypesEntity transactionTypesEntity = transactionTypesDao.findById(em, entity.getId());
            if (transactionTypesEntity == null) {
                log.warn("Transation Type with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucun type de transaction avec l ID " + entity.getId() + " n a ete trouvee dans la BDD", ErrorCodes.TRANSACTIONTYPE_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if TransactionPhase exit in DB
     *
     * @param entity TransactionPhasesEntity
     */
    public void checkTransactionPhases(TransactionPhasesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();

            TransactionPhasesEntity transactionPhasesEntity = transactionPhasesDao.findById(em, entity.getId());
            if (transactionPhasesEntity == null) {
                log.warn("Transation phase with ID {} was not found in the DB", entity.getId());
                throw new EntityNotFoundException(
                        "Aucune phase de transaction avec l ID " + entity.getId() + " n a ete trouvee dans la BDD", ErrorCodes.TRANSACTIONPHASE_NOT_FOUND
                );
            }
        }
    }


    /**
     * Check if user exist in DB
     *
     * @param entity : UsersEntity
     */

    public void checkUserByUsername(UsersEntity entity) {
        if (entity == null) {
            UsersEntity usersEntity = usersDao.findByUsername(entity.getUsername());
            if (usersEntity != null) {
                log.warn("User exists yet", entity.getUsername());
                throw new EntityNotFoundException(
                        "Il y a déjà un utilisateur avec ce pseudo: " + entity.getUsername(), ErrorCodes.USER_NOT_FOUND
                );
            }
        }
    }

}
