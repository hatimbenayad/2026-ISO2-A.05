package es.uclm.esi.iso2.common.access.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/** A person who logs in to the application through the corporate SSO (E01). Users are never deleted (D-015). */
public class User {
    private final String username;
    private String fullName;
    private String email;
    private final List<Role> roles;
    private boolean active;
    private LocalDateTime deactivatedAt;

    public User(String username, String fullName, String email, List<Role> roles) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roles = List.copyOf(roles);
        this.active = true;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getDeactivatedAt() {
        return deactivatedAt;
    }

    /** True if one of the user's roles includes {@code role}, so a physician also has the patient role. */
    public boolean hasRole(Role role) {
        for (Role own : roles) {
            if (own.includes(role)) {
                return true;
            }
        }
        return false;
    }

    /** The user stays stored with all their data; only the state changes (D-015). */
    public void deactivate(LocalDateTime when) {
        if (!active) {
            throw new IllegalStateException("The user " + username + " is already inactive");
        }
        active = false;
        deactivatedAt = when;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        return Objects.equals(username, ((User) other).username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
