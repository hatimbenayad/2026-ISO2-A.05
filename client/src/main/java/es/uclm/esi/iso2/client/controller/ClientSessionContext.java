package es.uclm.esi.iso2.client.controller;

import es.uclm.esi.iso2.common.access.domain.Role;
import es.uclm.esi.iso2.common.access.dto.AuthSessionDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/** Process-wide, thread-safe context for the active desktop session. */
public final class ClientSessionContext {
    private static final ClientSessionContext INSTANCE = new ClientSessionContext();
    private final AtomicReference<AuthSessionDto> session = new AtomicReference<>();

    private ClientSessionContext() {
    }

    public static ClientSessionContext getInstance() {
        return INSTANCE;
    }

    public void setSession(AuthSessionDto activeSession) {
        session.set(activeSession);
    }

    public AuthSessionDto getSession() {
        return session.get();
    }

    public boolean isAuthenticated() {
        AuthSessionDto current = session.get();
        if (current == null) {
            return false;
        }
        if (current.expiresAt() != null && !current.expiresAt().isAfter(LocalDateTime.now())) {
            return false;
        }
        return current.token() != null && !current.token().isBlank();
    }

    public boolean hasRole(Role role) {
        if (role == null) {
            return false;
        }
        AuthSessionDto current = session.get();
        if (current == null || current.roles() == null) {
            return false;
        }
        return current.roles().stream().anyMatch(granted -> granted != null && granted.includes(role));
    }

    public boolean isPhysician() {
        return isAuthenticated() && hasRole(Role.PHYSICIAN);
    }

    public void clearSession() {
        session.set(null);
    }

    public void clear() {
        clearSession();
    }

    public List<Role> getRoles() {
        AuthSessionDto current = session.get();
        return current == null || current.roles() == null ? List.of() : List.copyOf(current.roles());
    }
}
