package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.*;
import be.atc.salesmanagercrm.dao.impl.*;
import be.atc.salesmanagercrm.entities.*;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Random;

@Slf4j
@SessionScoped
public class CheckEntities extends ExtendBean implements Serializable {

    private static final long serialVersionUID = -6795998607327751632L;

    @Getter
    @Setter
    private ContactsDao contactsDao = new ContactsDaoImpl();
    @Getter
    @Setter
    private UsersDao usersDao = new UsersDaoImpl();
    @Getter
    @Setter
    private CompaniesDao companiesDao = new CompaniesDaoImpl();
    @Getter
    @Setter
    private CompanyTypesDao companyTypesDao = new CompanyTypesDaoImpl();
    @Getter
    @Setter
    private TaskTypesDao taskTypesDao = new TaskTypesDaoImpl();
    @Getter
    @Setter
    private JobTitlesDao jobTitlesDao = new JobTitlesDaoImpl();
    @Getter
    @Setter
    private BranchActivitiesDao branchActivitiesDao = new BranchActivitiesDaoImpl();
    @Getter
    @Setter
    private VoucherStatusDao voucherStatusDao = new VoucherStatusDaoImpl();
    @Getter
    @Setter
    private TransactionTypesDao transactionTypesDao = new TransactionTypesDaoImpl();
    @Getter
    @Setter
    private TransactionPhasesDao transactionPhasesDao = new TransactionPhasesDaoImpl();
    @Getter
    @Setter
    private RolesDao rolesDao = new RolesDaoImpl();
    @Getter
    @Setter
    private PermissionsDao permissionsDao = new PermissionsDaoImpl();
    @Getter
    @Setter
    private RolesPermissionsDao rolesPermissionsDao = new RolesPermissionsDaoImpl();
    @Getter
    @Setter
    private ContactTypesDao contactTypesDao = new ContactTypesDaoImpl();


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
                        "Aucune compagnie avec l ID " + entity.getId() + " n a ete trouvee dans la BDD", ErrorCodes.COMPANY_NOT_FOUND
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
     * @param jobTitlesEntity JobTitlesEntity
     */
    public void checkJobTitlesLabel(JobTitlesEntity jobTitlesEntity) {
        if (jobTitlesEntity != null) {
            EntityManager em = EMF.getEM();

            if (!(jobTitlesDao.findByLabel(em, jobTitlesEntity.getLabel()).isEmpty())) {
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
     * @param branchActivitiesEntity BranchActivitiesEntity
     */
    public void checkBranchActivitiesLabel(BranchActivitiesEntity branchActivitiesEntity) {
        if (branchActivitiesEntity != null) {
            EntityManager em = EMF.getEM();

            if (!(branchActivitiesDao.findByLabel(em, branchActivitiesEntity.getLabel()).isEmpty())) {
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
                        "Aucun type de tâche avec l ID " + entity.getId() + " n a ete trouve dans la BDD", ErrorCodes.TASKTYPE_NOT_FOUND
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
     * @param companyTypesEntity CompanyTypesEntity
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
     * Check if contact type exist
     *
     * @param contactTypesEntity ContactTypesEntity
     */
    public void checkContactType(ContactTypesEntity contactTypesEntity) {

        if (contactTypesEntity != null) {
            EntityManager em = EMF.getEM();

            ContactTypesEntity contactTypesEntityToCheck = contactTypesDao.findById(em, contactTypesEntity.getId());

            if (contactTypesEntityToCheck == null) {
                log.warn("Contact type with ID {} was not found in the DB", contactTypesEntity.getId());
                throw new EntityNotFoundException(
                        "Aucun type de contact avec l ID " + contactTypesEntity.getId() + " n'a été trouvé dans la DB", ErrorCodes.CONTACTTYPE_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if jobtitle exist
     *
     * @param jobTitlesEntity jobTitlesEntity
     */
    public void checkJobTitles(JobTitlesEntity jobTitlesEntity) {

        if (jobTitlesEntity != null) {
            EntityManager em = EMF.getEM();

            JobTitlesEntity jobTitlesEntityToCheck = jobTitlesDao.findById(em, jobTitlesEntity.getId());

            if (jobTitlesEntityToCheck == null) {
                log.warn("Contact type with ID {} was not found in the DB", jobTitlesEntity.getId());
                throw new EntityNotFoundException(
                        "Aucun intitulé de poste avec l ID " + jobTitlesEntity.getId() + " n'a été trouvé dans la DB", ErrorCodes.JOBTITLES_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if branch activities exist
     *
     * @param branchActivitiesEntity BranchActivitiesEntity
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
     * use in user
     *
     * @param entity : UsersEntity
     */

    public void checkUserByUsername(UsersEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            UsersEntity usersEntity = usersDao.findByUsername(em, entity.getUsername());
            if (usersEntity != null) {
                log.warn("User exists yet" + entity.getUsername());
                throw new InvalidEntityException(
                        "Il y a déjà un utilisateur avec ce pseudo: " + entity.getUsername(), ErrorCodes.USER_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if user exist in DB
     * use in user
     *
     * @param entity : UsersEntity
     */

    public void checkUserActiveForConnection(UsersEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            UsersEntity usersEntity = usersDao.findActiveUserForConnection(em, entity.getUsername());
            if (usersEntity != null) {
                log.warn("User inactive" + entity.getUsername());
                throw new InvalidEntityException(
                        "L'utilisateur est inactif: " + entity.getUsername(), ErrorCodes.USER_NOT_FOUND
                );
            }
        }
    }


    public String checkUserByUsernameAuto(String username, int number) {
        if (!username.isEmpty()) {
            EntityManager em = EMF.getEM();
            log.info(username);
            UsersEntity usersEntity1 = usersDao.findByUsername(em, username + number);
            if (usersEntity1 != null) {
                while (usersEntity1 != null) {
                    Random random = new Random();
                    number = random.nextInt(99 - 1);
                    log.info(String.valueOf(number));
                    if (number < 10) {
                        number = 0 + number;
                        log.info(String.valueOf(number));
                    }
                    usersEntity1 = usersDao.findByUsername(em, username + number);
                }

            }

        }
        return username + number;
    }


    /**
     * Check if user exist in DB
     * use in user
     *
     * @param entity : UsersEntity
     */

    public void checkRole(RolesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            RolesEntity rolesEntity = rolesDao.findById(em, entity.getId());
            if (rolesEntity == null) {
                log.warn("Roles doesn't exists yet" + entity.getId());
                throw new EntityNotFoundException(
                        "Ce rôle n'existe pas: " + entity.getId(), ErrorCodes.ROLES_NOT_FOUND
                );
            }
            rolesEntity = rolesDao.findActiveById(em, entity);
            if (rolesEntity == null) {
                log.warn("Roles doesn't exists yet" + entity.getId());
                throw new EntityNotFoundException(
                        "Ce rôle est inactif: " + entity.getId(), ErrorCodes.ROLES_NOT_FOUND
                );
            }
        }
    }

    /**
     * check if role exist in DB
     * use in role
     *
     * @param entity RolesEntity
     */
    public void checkRoleByLabel(RolesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            RolesEntity rolesEntity = rolesDao.findByLabel(em, entity.getLabel());
            if (rolesEntity != null) {
                log.warn("Role label exists yet" + entity.getLabel());
                throw new InvalidEntityException(
                        "Il y a déjà un role avec ce nom: " + entity.getLabel(), ErrorCodes.ROLES_FOUND
                );
            }
        }
    }

    /**
     * Check if permission exist in DB
     *
     * @param entity :PermissionsEntity
     */

    public void checkPermissions(PermissionsEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            PermissionsEntity permissionsEntity = permissionsDao.findById(em, entity.getId());
            if (permissionsEntity == null) {
                log.warn("permission doesn't exists yet" + entity.getId());
                throw new EntityNotFoundException(
                        "Cette permission n'existe pas: " + entity.getId(), ErrorCodes.PERMISSION_NOT_FOUND
                );
            }
        }
    }

    /**
     * Check if combo exist in DB
     */

    public void checkComboRolePerm(int idRole, int idPermission) {
        if (idRole > 0 && idPermission >= 0) {
            EntityManager em = EMF.getEM();
            RolesPermissionsEntity rolesPermissionsEntity = rolesPermissionsDao.getComboRolePerm(em, idRole, idPermission);
            if (rolesPermissionsEntity != null) {
                log.warn("Combo exists yet");
                throw new InvalidEntityException(
                        "Il y a déjà un combo existant: " + ErrorCodes.ROLE_PERMISSION_DUPLICATE
                );
            }
        }
    }


    public void checkForSafeDeleteRole(RolesEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            RolesEntity rolesEntity = (RolesEntity) rolesDao.findForDeleteSafe(em, entity.getId());
            log.info(String.valueOf(rolesEntity));
            PermissionsEntity permissionsEntity = permissionsDao.findById(em, entity.getId());
            if (permissionsEntity == null) {
                log.warn("permission doesn't exists yet" + entity.getId());
                throw new EntityNotFoundException(
                        "Cette permission n'existe pas: " + entity.getId(), ErrorCodes.PERMISSION_NOT_FOUND
                );
            }
        }
    }


    public void checkRoleForConnection(UsersEntity entity) {
        if (entity != null) {
            EntityManager em = EMF.getEM();
            RolesEntity entityActive = rolesDao.findRoleForConnection(em, entity.getRolesByIdRoles().getLabel());
            if (entityActive == null) {
                log.warn("role is inactive" + entity.getId());
                throw new EntityNotFoundException(
                        "Le role est inactif " + entity.getId(), ErrorCodes.ROLES_NOT_VALID
                );
            }
        }
    }


    public void checkUserByUsernameAndPassword(UsersEntity entity, String password) {
        if (entity != null) {
            log.info(entity.getUsername() + " " + entity.getPassword());
            EntityManager em = EMF.getEM();
            UsersEntity usersEntity = usersDao.findNUserByUsernameAndPassword(em, entity.getUsername(), password);
            log.info(String.valueOf(usersEntity));
            if (usersEntity == null) {
                log.warn("Wrong username or password" + entity.getId());
                throw new EntityNotFoundException(
                        "Votre identifiant ou mot de passe sont inccorects: " + entity.getId(), ErrorCodes.USER_NOT_FOUND
                );
            }
        }
    }


}
