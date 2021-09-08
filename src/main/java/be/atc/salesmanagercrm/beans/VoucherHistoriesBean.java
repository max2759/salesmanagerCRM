package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.VoucherHistoriesDao;
import be.atc.salesmanagercrm.dao.impl.VoucherHistoriesDaoImpl;
import be.atc.salesmanagercrm.entities.UsersEntity;
import be.atc.salesmanagercrm.entities.VoucherHistoriesEntity;
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
@Named(value = "voucherHistoriesBean")
@ViewScoped
public class VoucherHistoriesBean extends ExtendBean implements Serializable {
    private static final long serialVersionUID = -6742454252414830482L;

    @Getter
    @Setter
    private VoucherHistoriesDao dao = new VoucherHistoriesDaoImpl();

    @Getter
    @Setter
    private List<VoucherHistoriesEntity> voucherHistoriesEntities;
    @Getter
    @Setter
    private List<VoucherHistoriesEntity> voucherHistoriesEntitiesFiltered;


    public void findAllEntities(int idVoucher, UsersEntity usersEntity) {
        voucherHistoriesEntities = findAllByIdUserAndByIdVoucher(idVoucher, usersEntity);
    }

    /**
     * Find Vouchers entities by id company
     *
     * @param idVoucher id VouchersEntity
     * @return List VouchersEntities
     */
    protected List<VoucherHistoriesEntity> findAllByIdUserAndByIdVoucher(int idVoucher, UsersEntity usersEntity) {
        log.info("VoucherHistoriesBean => method : findAllByIdUserAndByIdVoucher(int idVoucher, UsersEntity usersEntity)");

        FacesMessage msg;
        if (idVoucher == 0) {
            log.error("Voucher ID is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "vouchers.notExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }
        if (usersEntity == null) {
            log.error("User is null");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtils.returnMessage(getLocale(), "userNotExist"), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return Collections.emptyList();
        }

        EntityManager em = EMF.getEM();
        List<VoucherHistoriesEntity> voucherHistoriesEntities;
        try {
            voucherHistoriesEntities = dao.findAllByIdUserAndByIdVoucher(em, idVoucher, usersEntity.getId());
        } finally {
            em.clear();
            em.close();
        }

        return voucherHistoriesEntities;
    }
}
