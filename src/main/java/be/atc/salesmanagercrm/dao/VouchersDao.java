package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.VouchersEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Younes Arifi
 */
public interface VouchersDao {

    void save(EntityManager em, VouchersEntity entity);

    VouchersEntity findById(EntityManager em, int id, int idUser);

    List<VouchersEntity> findVouchersEntityByContactsByIdContacts(EntityManager em, int id, int idUser);

    List<VouchersEntity> findVouchersEntityByCompaniesByIdCompanies(EntityManager em, int id, int idUser);

    List<VouchersEntity> findAll(EntityManager em, int idUser);

    void update(EntityManager em, VouchersEntity entity);
}
