package es.uclm.esi.iso2.common.access.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The session a user gets after logging in through the SSO (SCU01). It cannot be changed and it expires, so a
 * forgotten session does not stay open (ISO 25010, accountability).
 */
public class AuthSession {
    public static final Duration DEFAULT_DURATION = Duration.ofMinutes(30);

    private final String token;
    private final String username;
    private final List<Role> roles;
    private final LocalDateTime startedAt;
    private final LocalDateTime expiresAt;

    /** Session for {@code user} that lasts {@link #DEFAULT_DURATION}. */
    public AuthSession(String token, User user, LocalDateTime startedAt) {
        this(token, user.getUsername(), user.getRoles(), startedAt, startedAt.plus(DEFAULT_DURATION));
    }

    public AuthSession(String token, String username, List<Role> roles, LocalDateTime startedAt,
            LocalDateTime expiresAt) {
        if (!expiresAt.isAfter(startedAt)) {
            throw new IllegalArgumentException("A session has to end after it starts");
        }
        this.token = token;
        this.username = username;
        this.roles = List.copyOf(roles);
        this.startedAt = startedAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /** The session is no longer valid from {@code expiresAt} on, that minute included. */
    public boolean isExpired(LocalDateTime now) {
        return !now.isBefore(expiresAt);
    }

    /** True if the session has not expired and one of its roles includes {@code role}. */
    public boolean allows(Role role, LocalDateTime now) {
        if (isExpired(now)) {
            return false;
        }
        for (Role own : roles) {
            if (own.includes(role)) {
                return true;
            }
        }
        return false;
    }
}
