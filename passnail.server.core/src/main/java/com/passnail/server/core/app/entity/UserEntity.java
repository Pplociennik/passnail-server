package com.passnail.server.core.app.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * An {@link Entity} being a representation of user in the database.
 */
@Entity
@Table(name = "COM_PSSNL_USR")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {


    /**
     * Unique entity's identifier of the {@link UUID} type. It is being auto generated while persisting an object to the database.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "USR_ID", insertable = false, updatable = false, nullable = false, unique = true)
    private UUID userID;


    /**
     * A {@link String} being the user's login to the application.
     */
    @Column(name = "USR_LOGIN", nullable = false)
    private String login;


    /**
     * A encrypted {@link String} being the user's password to the application.
     */
    @Column(name = "USR_PASS", nullable = false)
    private String password;


    /**
     * A {@link String} being the user's email address;
     */
    @Column(name = "USR_EMAIL", unique = true, nullable = false)
    private String emailAddress;


    /**
     * A {@link Date} being a date when an account was created.
     */
    @Column(name = "USR_CRT", nullable = false)
    private Date creationDate;


    @Column(name = "USR_MOD")
    private Date lastModificationDate;


    /**
     * A flag to distinguish if user is local or not.
     */
    @Column(name = "USR_LOCAL")
    private Boolean local;


    /**
     * An identifier for online data synchronization.
     */
    @Column(name = "USR_ONLINE_ID", unique = true)
    private String onlineID;


    /**
     * A {@link Set} of {@link CredentialsEntity} typed objects being a list of credentials created by user.
     */
    @OneToMany(mappedBy = "credentialsOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CredentialsEntity> savedCredentials;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(creationDate, that.creationDate) &&
                lastModificationDate.equals(that.lastModificationDate) &&
                Objects.equals(local, that.local) &&
                onlineID.equals(that.onlineID) &&
                savedCredentials.equals(that.savedCredentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, emailAddress, creationDate, lastModificationDate, local, onlineID, savedCredentials);
    }
}
