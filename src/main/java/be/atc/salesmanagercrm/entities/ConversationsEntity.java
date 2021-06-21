package be.atc.salesmanagercrm.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "conversations", schema = "salesmanagercrm")
public class ConversationsEntity {
    private int id;
    private LocalDateTime creationDate;
    private String message;
    private boolean isActive;
    private UsersEntity usersByIdUsers;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Creation_Date")
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "Message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "IsActive")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationsEntity that = (ConversationsEntity) o;
        return id == that.id && isActive == that.isActive && Objects.equals(creationDate, that.creationDate) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, message, isActive);
    }

    @ManyToOne
    @JoinColumn(name = "ID_Users", referencedColumnName = "ID", nullable = false)
    public UsersEntity getUsersByIdUsers() {
        return usersByIdUsers;
    }

    public void setUsersByIdUsers(UsersEntity usersByIdUsers) {
        this.usersByIdUsers = usersByIdUsers;
    }
}
