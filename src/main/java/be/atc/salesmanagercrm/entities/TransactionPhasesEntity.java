package be.atc.salesmanagercrm.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "transaction_phases", schema = "salesmanagercrm")
public class TransactionPhasesEntity {
    private int id;
    private String label;
    private Collection<TransactionHistoriesEntity> transactionHistoriesById;
    private Collection<TransactionsEntity> transactionsById;

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
        TransactionPhasesEntity that = (TransactionPhasesEntity) o;
        return id == that.id && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }

    @OneToMany(mappedBy = "transactionPhasesByIdTransactionPhases")
    public Collection<TransactionHistoriesEntity> getTransactionHistoriesById() {
        return transactionHistoriesById;
    }

    public void setTransactionHistoriesById(Collection<TransactionHistoriesEntity> transactionHistoriesById) {
        this.transactionHistoriesById = transactionHistoriesById;
    }

    @OneToMany(mappedBy = "transactionPhasesByIdTransactionPhases")
    public Collection<TransactionsEntity> getTransactionsById() {
        return transactionsById;
    }

    public void setTransactionsById(Collection<TransactionsEntity> transactionsById) {
        this.transactionsById = transactionsById;
    }
}
