package com.lulski.aries.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data model for `users` collection
 */
@Document("users")
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class User {

    private ObjectId id;

    @NotNull(message = "Username must not be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5-50 characters")
    private String username;

    @NotNull(message = "Firstname must not be empty")
    @Size(min = 3, max = 50, message = "Firstname must be between 3-50 characters")
    private String firstName;

    @NotNull(message = "Lastname must not be empty")
    @Size(min = 3, max = 50, message = "Lastname must be between 3-50 characters")
    private String lastName;

    @NotNull(message = "Email must not be empty")
    private String email;

    private boolean isArchived = false;

    /**
     * main constructor
     *
     * @param id
     * @param username
     * @param firstName
     * @param lastName
     * @param email
     * @param isArchived
     */
    public User(ObjectId id, String username, String firstName, String lastName, String email, Boolean isArchived) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isArchived = (isArchived == null) ? false : isArchived;
    }

    public User() {
    }

    public boolean isArchived() {
        return this.isArchived;
    }

    public void setArchived(Boolean archived) {
        this.isArchived = (archived == null) ? false : archived;
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP")
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
