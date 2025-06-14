package com.lulski.aries.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data model for `users` collection.
 */
@Document("users")
public class User implements UserDetails {

    private ObjectId id;

    @NotBlank(message = "Username must not be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5-50 characters")
    private String username;

    @NotBlank(message = "password cannot be empty")
    private String password;

    @NotBlank(message = "authorities must be set")
    private Set<SimpleGrantedAuthority> authorities;

    @NotBlank(message = "Firstname must not be empty")
    @Size(min = 3, max = 50, message = "Firstname must be between 3-50 characters")
    private String firstName;

    @NotBlank(message = "Lastname must not be empty")
    @Size(min = 3, max = 50, message = "Lastname must be between 3-50 characters")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    private String email;

    /**
     * main constructor
     *
     * @param id
     * @param username
     * @param firstName
     * @param lastName
     * @param email
     */
    public User(
        ObjectId id,
        String username,
        String password,
        String firstName,
        String lastName,
        String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    @JsonIgnore
    public @NotBlank(message = "authorities must be set") Set<SimpleGrantedAuthority>
    getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities =
            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public Set<String> getAuthoritiesNames() {
        if (this.authorities==null) {
            return Collections.emptySet();
        }

        return this.authorities.stream()
            .map(SimpleGrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
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

    public static final class UserBuilder {
        private String username;
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private Set<String> authorities;

        public UserBuilder() {
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder authorities(Set<String> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(this.username);
            user.setFirstName(this.firstname);
            user.setLastName(this.lastname);
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setAuthorities(this.authorities);
            return user;
        }
    }
}
