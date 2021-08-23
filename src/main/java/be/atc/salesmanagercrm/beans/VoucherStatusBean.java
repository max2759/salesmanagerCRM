package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.dao.VoucherStatusDao;
import be.atc.salesmanagercrm.dao.impl.VoucherStatusDaoImpl;
import be.atc.salesmanagercrm.entities.VoucherStatusEntity;
import be.atc.salesmanagercrm.exceptions.EntityNotFoundException;
import be.atc.salesmanagercrm.exceptions.ErrorCodes;
import be.atc.salesmanagercrm.utils.EMF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Younes Arifi
 */
@Slf4j
@Named(value = "voucherStatusBean")
@ViewScoped
public class VoucherStatusBean extends ExtendBean implements Serializable {

    private static final long serialVersionUID = 8734002474639637398L;

    @Getter
    @Setter
    private VoucherStatusDao dao = new VoucherStatusDaoImpl();

    @Getter
    @Setter
    private VoucherStatusEntity voucherStatusEntity;

    @Getter
    @Setter
    private List<VoucherStatusEntity> voucherStatusEntities;


    /**
     * Fill the list with Task Entities
     */
    public void findAllVoucherStatusEntities() {
        voucherStatusEntities = findAll();
    }

    /**
     * Find VoucherStatusEntity by ID
     *
     * @param id VoucherStatusEntity
     * @return VoucherStatusEntity
     */
    protected VoucherStatusEntity findById(int id) {
        if (id == 0) {
            log.error("VoucherStatus ID is null");
            throw new EntityNotFoundException(
                    "L ID du statut ticket est incorrect",
                    ErrorCodes.VOUCHERSTATUS_NOT_FOUND
            );
        }

        EntityManager em = EMF.getEM();
        Optional<VoucherStatusEntity> optionalVoucherStatusEntity = Optional.ofNullable(dao.findById(em, id));
        em.clear();
        em.close();
        return optionalVoucherStatusEntity.orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun status de ticket avec l'ID " + id + " n a ete trouve dans la BDD",
                        ErrorCodes.VOUCHERSTATUS_NOT_FOUND
                ));
    }

    /**
     * Find VoucherStatusEntity by LABEL
     *
     * @param label VoucherStatusEntity
     * @return VoucherStatusEntity
     */
    public VoucherStatusEntity findByLabel(String label) {
        if (label == null) {
            log.error("VoucherStatus label is null");
            throw new EntityNotFoundException(
                    "Le label du statut du ticket est incorrect",
                    ErrorCodes.VOUCHERSTATUS_NOT_FOUND
            );
        }

        EntityManager em = EMF.getEM();
        try {
            return dao.findByLabel(em, label);
        } catch (Exception ex) {
            log.info("Nothing");
            throw new EntityNotFoundException(
                    "Aucun Statut de ticket avec le label " + label + " n a ete trouve dans la BDD",
                    ErrorCodes.VOUCHERSTATUS_NOT_FOUND
            );
        } finally {
            em.clear();
            em.close();
        }
    }

    /**
     * Find All VoucherStatus Entities
     *
     * @return List VoucherStatusEntity
     */
    protected List<VoucherStatusEntity> findAll() {
        return dao.findAll();
    }

}
