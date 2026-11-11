package es.uclm.esi.iso2.common.access.dto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import es.uclm.esi.iso2.common.access.domain.AuthSession;
import es.uclm.esi.iso2.common.access.domain.Role;
import es.uclm.esi.iso2.common.access.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link UserDto} and {@link AuthSessionDto}: they carry the same data as the domain objects. */
@DisplayName("Access DTOs")
class AccessDtoTest {

    @Test
    @DisplayName("UserDto copies the data of the user, state included")
    void userDtoCopiesUserData() {
        // Arrange
        User user = new User("lgarcia", "Luis García Test", "luis@test.test", List.of(Role.ADMINISTRATIVE_STAFF));
        user.deactivate(LocalDateTime.of(2026, 11, 10, 9, 0));

        // Act
        UserDto dto = UserDto.from(user);

        // Assert
        assertEquals(new UserDto("lgarcia", "Luis García Test", "luis@test.test",
                List.of(Role.ADMINISTRATIVE_STAFF), false), dto);
    }

    @Test
    @DisplayName("AuthSessionDto copies the token, the user, the roles and the end of the session")
    void authSessionDtoCopiesSessionData() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2026, 11, 10, 9, 0);
        AuthSession session = new AuthSession("token-1", "lgarcia", List.of(Role.ADMINISTRATIVE_STAFF), start,
                start.plusHours(1));

        // Act
        AuthSessionDto dto = AuthSessionDto.from(session);

        // Assert
        assertAll(
                () -> assertEquals("token-1", dto.token()),
                () -> assertEquals("lgarcia", dto.username()),
                () -> assertEquals(List.of(Role.ADMINISTRATIVE_STAFF), dto.roles()),
                () -> assertEquals(start.plusHours(1), dto.expiresAt()));
    }
}
