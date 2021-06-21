package be.atc.salesmanagercrm.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "branch_activities", schema = "salesmanagercrm")
public class BranchActivitiesEntity {
    private int id;
    private String label;
    private Collection<CompaniesEntity> companiesById;
    private Collection<ContactsEntity> contactsById;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchActivitiesEntity that = (BranchActivitiesEntity) o;
        return id == that.id && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }

    @OneToMany(mappedBy = "branchActivitiesByIdBranchActivities")
    public Collection<CompaniesEntity> getCompaniesById() {
        return companiesById;
    }

    public void setCompaniesById(Collection<CompaniesEntity> companiesById) {
        this.companiesById = companiesById;
    }

    @OneToMany(mappedBy = "branchActivitiesByIdBranchActivities")
    public Collection<ContactsEntity> getContactsById() {
        return contactsById;
    }

    public void setContactsById(Collection<ContactsEntity> contactsById) {
        this.contactsById = contactsById;
    }
}
