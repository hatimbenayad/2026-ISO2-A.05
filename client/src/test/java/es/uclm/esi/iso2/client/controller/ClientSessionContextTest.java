package es.uclm.esi.iso2.client.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.uclm.esi.iso2.common.access.domain.Role;
import es.uclm.esi.iso2.common.access.dto.AuthSessionDto;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ClientSessionContext")
class ClientSessionContextTest {
    private ClientSessionContext context;

    @BeforeEach
    void setUp() {
        context = ClientSessionContext.getInstance();
        context.clearSession();
    }

    @Test
    void singletonIdentityAndSessionLifecycle() {
        assertSame(context, ClientSessionContext.getInstance());
        assertFalse(context.isAuthenticated());
        context.setSession(session(Role.PHYSICIAN));
        assertTrue(context.isAuthenticated());
        assertTrue(context.isPhysician());
        context.clear();
        assertFalse(context.isAuthenticated());
    }

    @Test
    void roleVerificationIncludesPatientForPhysician() {
        context.setSession(session(Role.PHYSICIAN));
        assertTrue(context.hasRole(Role.PHYSICIAN));
        assertTrue(context.hasRole(Role.PATIENT));
        assertFalse(context.hasRole(Role.ADMINISTRATIVE_STAFF));
    }

    @Test
    void unauthorizedAndExpiredSessionsAreNotAuthenticated() {
        context.setSession(session(Role.ADMINISTRATIVE_STAFF));
        assertFalse(context.isPhysician());
        context.setSession(new AuthSessionDto("token", "old", List.of(Role.PHYSICIAN),
                LocalDateTime.now().minusHours(1)));
        assertFalse(context.isAuthenticated());
    }

    @Test
    void nullRolesAndNullRoleAreHandledSafely() {
        assertFalse(context.hasRole(null));
        context.setSession(new AuthSessionDto("token", "nobody", null,
                LocalDateTime.now().plusHours(1)));
        assertFalse(context.hasRole(Role.PHYSICIAN));
        assertEquals(0, context.getRoles().size());
        assertEquals("nobody", context.getSession().username());
    }

    private AuthSessionDto session(Role role) {
        return new AuthSessionDto("token", "physician", List.of(role),
                LocalDateTime.now().plusHours(1));
    }
}
