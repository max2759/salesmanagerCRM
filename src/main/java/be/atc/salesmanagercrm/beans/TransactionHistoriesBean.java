package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.TransactionHistoriesDao;
import be.atc.salesmanagercrm.dao.impl.TransactionHistoriesDaoImpl;
import be.atc.salesmanagercrm.entities.TransactionHistoriesEntity;
import be.atc.salesmanagercrm.utils.EMF;
import be.atc.salesmanagercrm.utils.JsfUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Younes Arifi
 */
@Slf4j
@Named(value = "transactionHistoriesBean")
@ViewScoped
public class TransactionHistoriesBean extends ExtendBean implements Serializable {
    private static final long serialVersionUID = -6742454252414830482L;

    @Getter
    @Setter
    private TransactionHistoriesDao dao = new TransactionHistoriesDaoImpl();

    @Getter
    @Setter
    private List<TransactionHistoriesEntity> transactionHistoriesEntities;
    @Getter
    @Setter
    private List<TransactionHistoriesEntity> transactionHistoriesEntitiesFiltered;


    public void findAllEntities(int idTransaction, int idUser) {
        transactionHistoriesEntities = findAllByIdUserAndByIdTransaction(idTransaction, idUser);
    }

    /**
     * Find Transactions entities by id company
     *
     * @param idTransaction id TransactionsEntity
     * @return List TransactionsEntities
     */
    protected List<TransactionHistoriesEntity> findAllByIdUserAndByIdTransaction(int idTransaction, int idUser) {
        FacesMessage msg;
        if (idTransaction == 0) {
            log.error("Transaction ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "transactions.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (idUser == 0) {
            log.error("User ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<TransactionHistoriesEntity> TransactionHistoriesEntity = dao.findAllByIdUserAndByIdTransaction(em, idTransaction, idUser);

        em.clear();
        em.close();

        return TransactionHistoriesEntity;
    }
}
