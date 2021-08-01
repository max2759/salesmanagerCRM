package be.atc.salesmanagercrm.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "companies_contacts", schema = "salesmanagercrm")
public class CompaniesContactsEntity {
    private int id;
    private CompaniesEntity companiesByIdCompanies;
    private ContactsEntity contactsByIdContacts;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompaniesContactsEntity that = (CompaniesContactsEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @ManyToOne
    @JoinColumn(name = "ID_Companies", referencedColumnName = "ID", nullable = false)
    public CompaniesEntity getCompaniesByIdCompanies() {
        return companiesByIdCompanies;
    }

    public void setCompaniesByIdCompanies(CompaniesEntity companiesByIdCompanies) {
        this.companiesByIdCompanies = companiesByIdCompanies;
    }

    @ManyToOne
    @JoinColumn(name = "ID_Contacts", referencedColumnName = "ID", nullable = false)
    public ContactsEntity getContactsByIdContacts() {
        return contactsByIdContacts;
    }

    public void setContactsByIdContacts(ContactsEntity contactsByIdContacts) {
        this.contactsByIdContacts = contactsByIdContacts;
    }
}
