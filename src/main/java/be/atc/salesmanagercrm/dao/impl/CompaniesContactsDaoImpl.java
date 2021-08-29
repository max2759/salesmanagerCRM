package be.atc.salesmanagercrm.dao.impl;

import be.atc.salesmanagercrm.dao.CompaniesContactsDao;
import be.atc.salesmanagercrm.entities.CompaniesContactsEntity;
import be.atc.salesmanagercrm.utils.EntityFinderImpl;

import javax.persistence.EntityManager;
import java.util.List;

public class CompaniesContactsDaoImpl extends EntityFinderImpl<CompaniesContactsEntity> implements CompaniesContactsDao {

    @Override
    public void update(EntityManager em, CompaniesContactsEntity companiesContactsEntity) {
        em.merge(companiesContactsEntity);
    }

    @Override
    public List<CompaniesContactsEntity> findByIdContacts(EntityManager em, int idContacts) {
        return em.createNamedQuery("CompaniesContacts.findByIdContacts",
                        CompaniesContactsEntity.class)
                .setParameter("idContacts", idContacts)
                .getResultList();
    }

    @Override
    public CompaniesContactsEntity findById(EntityManager em, int id) {
        return em.createNamedQuery("CompaniesContacts.findById",
                        CompaniesContactsEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void delete(EntityManager em, CompaniesContactsEntity companiesContactsEntity) {
        em.remove(em.merge(companiesContactsEntity));
    }
}
