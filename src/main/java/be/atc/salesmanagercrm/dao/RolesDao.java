package be.atc.salesmanagercrm.dao;

import be.atc.salesmanagercrm.entities.RolesEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface RolesDao {

    RolesEntity findById(EntityManager em, int id);

    List<RolesEntity> findAllRoles(EntityManager em);

    List<RolesEntity> findAllRolesActive(EntityManager em);

    Optional<RolesEntity> findByLabel(EntityManager em, String label);

    void register(EntityManager em, RolesEntity rolesEntity);

    void update(EntityManager em, RolesEntity rolesEntity);

    List<RolesEntity> findForDeleteSafe(EntityManager em, int id);

    RolesEntity findRoleForConnection(EntityManager em, String label);

    RolesEntity findActiveById(EntityManager em, RolesEntity rolesEntity);


}
