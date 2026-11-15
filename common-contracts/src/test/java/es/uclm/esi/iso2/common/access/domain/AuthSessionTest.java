package es.uclm.esi.iso2.common.access.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests of {@link AuthSession}: when it expires and which roles it allows. */
@DisplayName("AuthSession")
class AuthSessionTest {
    private static final LocalDateTime START = LocalDateTime.of(2026, 11, 10, 9, 0);

    private final User physician = new User("mlopez", "María López Test", "maria@test.test", List.of(Role.PHYSICIAN));

    @Test
    @DisplayName("A session created for a user copies its username and roles and lasts 30 minutes")
    void sessionForUserCopiesUsernameAndRoles() {
        // Act
        AuthSession session = new AuthSession("token-1", physician, START);

        // Assert
        assertAll(
                () -> assertEquals("token-1", session.getToken()),
                () -> assertEquals("mlopez", session.getUsername()),
                () -> assertEquals(List.of(Role.PHYSICIAN), session.getRoles()),
                () -> assertEquals(START, session.getStartedAt()),
                () -> assertEquals(START.plusMinutes(30), session.getExpiresAt()));
    }

    // The limit is the 30th minute: at that moment the session is already expired.
    @ParameterizedTest(name = "{0} seconds after the start, expired = {1}")
    @CsvSource({
        "1799, false",
        "1800, true",
        "1801, true"
    })
    @DisplayName("The session expires exactly 30 minutes after it starts")
    void sessionExpiresAfterThirtyMinutes(long secondsAfterStart, boolean expected) {
        // Arrange
        AuthSession session = new AuthSession("token-1", physician, START);

        // Act
        boolean expired = session.isExpired(START.plusSeconds(secondsAfterStart));

        // Assert
        assertEquals(expected, expired);
    }

    @ParameterizedTest(name = "ends {0} minutes after the start")
    @CsvSource({ "0", "-1" })
    @DisplayName("A session cannot end before or when it starts")
    void sessionCannotEndBeforeItStarts(long minutes) {
        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuthSession("token-1", "mlopez", List.of(Role.PATIENT), START, START.plusMinutes(minutes)));
    }

    @Test
    @DisplayName("A valid session allows the roles of the user and the inherited patient role")
    void validSessionAllowsUserRoles() {
        // Arrange
        AuthSession session = new AuthSession("token-1", physician, START);
        LocalDateTime now = START.plusMinutes(5);

        // Assert
        assertAll(
                () -> assertTrue(session.allows(Role.PHYSICIAN, now)),
                () -> assertTrue(session.allows(Role.PATIENT, now)),
                () -> assertFalse(session.allows(Role.SYSTEM_ADMINISTRATOR, now)));
    }

    @Test
    @DisplayName("An expired session allows nothing")
    void expiredSessionAllowsNothing() {
        // Arrange
        AuthSession session = new AuthSession("token-1", physician, START);

        // Act and Assert
        assertFalse(session.allows(Role.PHYSICIAN, session.getExpiresAt()));
    }
}
