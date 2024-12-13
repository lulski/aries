package com.lulski.aries.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
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

    public User(ObjectId id, String username, String firstName, String lastName, String email, Boolean isArchived) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isArchived = (isArchived == null) ? false: isArchived;
    }

    public boolean isArchived() {
        return this.isArchived;
    }

    public void setArchived(Boolean archived) {
        this.isArchived = (archived == null) ? false : archived;
    }

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
