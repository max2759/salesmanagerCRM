package be.atc.salesmanagercrm.beans;


import be.atc.salesmanagercrm.dao.ConversationsDao;
import be.atc.salesmanagercrm.dao.UsersDao;
import be.atc.salesmanagercrm.dao.impl.ConversationsDaoImpl;
import be.atc.salesmanagercrm.dao.impl.UsersDaoImpl;
import be.atc.salesmanagercrm.entities.ConversationsEntity;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.exceptions.InvalidEntityException;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.validators.ConversationsValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "conversationsBean")
@SessionScoped
public class ConversationsBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 829261844584069844L;
    @Getter
    @Setter
    private ConversationsEntity conversationsEntity = new ConversationsEntity();

    @Getter
    @Setter
    private ConversationsDao dao = new ConversationsDaoImpl();
    @Getter
    @Setter
    private UsersDao userDao = new UsersDaoImpl();
    @Getter
    @Setter
    private List<ConversationsEntity> conversationsEntityList;
    @Getter
    @Setter
    private ConversationsEntity conversationsForDialog;

    @Inject
    private UsersBean usersBean;

    public void initialiseDialogConversId(Integer idConvers) {
        if (idConvers == null || idConvers < 1) {
            return;
        }
        conversationsForDialog = getDao().findById(EMF.getEM(), idConvers);
        conversationsEntity = dao.findById(EMF.getEM(), idConvers);
    }


    public void add() {
        log.info(String.valueOf(usersBean.getUsersEntity().getId()));
        log.info("user entity: " + usersBean.getUsersEntity().getUsername());
        try {
            validateConversations(conversationsEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        log.info(conversationsEntity.getMessage());
        String message = conversationsEntity.getMessage();
        message = message.replaceAll("\\<[^>]*>", "");
        log.info(message);

        CheckEntities checkEntities = new CheckEntities();
//finir les verifs ici
        //   usersBean.findUser();

        conversationsEntity.setMessage(message);
        conversationsEntity.setCreationDate(LocalDateTime.now());
        conversationsEntity.setActive(true);
        log.info(String.valueOf(usersBean.getUsersEntity()));
        conversationsEntity.setUsersByIdUsers(usersBean.getUsersEntity());

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.add(em, conversationsEntity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }


    public void updateEntity() {
        updateConvers(this.conversationsEntity);
        conversationsEntityList = findAll();
    }

    protected void updateConvers(ConversationsEntity entity) {

    /*    try {
            validateRoles(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }

        CheckEntities checkEntities = new CheckEntities();
        try {
            checkEntities.checkRoleByLabel(rolesEntity);
        } catch (InvalidEntityException exception) {
            log.warn("Code ERREUR " + exception.getErrorCodes().getCode() + " - " + exception.getMessage() + " : " + exception.getErrors().toString());
            return;
        }
*/

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }
    }


    public void delete(ConversationsEntity entity) {
        //verifs

        entity.setActive(false);

        EntityManager em = EMF.getEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            dao.update(em, entity);
            tx.commit();
            log.info("Persist ok");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            log.info("Persist echec");
        } finally {
            em.clear();
            em.clear();
        }

    }


    public void get10convers() {
        conversationsEntityList = find10convers();
    }

    private List<ConversationsEntity> find10convers() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<ConversationsEntity> conversationsEntities = dao.find10Last(em);

        em.clear();
        em.close();

        return conversationsEntities;
    }

    /*public void showAfterAdd() {
        updateUsersAdmin(this.usersEntity);
        usersEntityList = findAll();
    }
*/

    //  public void findUser() {
    //    usersBean.findUser();
    // }


    public void findAllConversations() {
        log.info("bgin findallrole");
        conversationsEntityList = findAll();
        log.info(String.valueOf(conversationsEntityList));
    }

    protected List<ConversationsEntity> findAll() {
        log.info("begin findall");
        EntityManager em = EMF.getEM();
        List<ConversationsEntity> conversationsEntities = dao.findAll(em);

        em.clear();
        em.close();

        return conversationsEntities;
    }

    private void validateConversations(ConversationsEntity entity) {
        List<String> errors = ConversationsValidator.validate(entity);
        if (!errors.isEmpty()) {
            log.error("Conversation is not valide {}", entity);
            throw new InvalidEntityException("L'ajout du message n'est pas valide", ErrorCodes.CONVERSATION_NOT_VALID, errors);
        }
    }


}
