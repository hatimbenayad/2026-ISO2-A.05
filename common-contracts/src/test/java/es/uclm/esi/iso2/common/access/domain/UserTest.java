package es.uclm.esi.iso2.common.access.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link User}: its copy of the roles, the inherited patient role and the deactivation (D-015). */
@DisplayName("User")
class UserTest {
    private User physician;

    @BeforeEach
    void setUp() {
        physician = new User("mlopez", "María López Test", "maria@test.test", List.of(Role.PHYSICIAN));
    }

    @Test
    @DisplayName("A new user is active and keeps its data")
    void newUserIsActiveAndKeepsItsData() {
        // Assert
        assertAll(
                () -> assertEquals("mlopez", physician.getUsername()),
                () -> assertEquals("María López Test", physician.getFullName()),
                () -> assertEquals(List.of(Role.PHYSICIAN), physician.getRoles()),
                () -> assertTrue(physician.isActive()),
                () -> assertNull(physician.getDeactivatedAt()));
    }

    @Test
    @DisplayName("Adding a role to the list used to create the user does not give the user that role")
    void userIsNotChangedThroughTheOriginalList() {
        // Arrange
        List<Role> source = new ArrayList<>(List.of(Role.PATIENT));
        User user = new User("ana", "Ana Test", "ana@test.test", source);

        // Act
        source.add(Role.SYSTEM_ADMINISTRATOR);

        // Assert
        assertEquals(List.of(Role.PATIENT), user.getRoles());
    }

    @Test
    @DisplayName("Nobody can add a role through getRoles")
    void getRolesDoesNotAllowAddingRoles() {
        // Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> physician.getRoles().add(Role.PATIENT));
    }

    @Test
    @DisplayName("A physician also has the patient role, but not the administrator ones")
    void physicianAlsoHasPatientRole() {
        // Assert
        assertAll(
                () -> assertTrue(physician.hasRole(Role.PHYSICIAN)),
                () -> assertTrue(physician.hasRole(Role.PATIENT)),
                () -> assertFalse(physician.hasRole(Role.ADMINISTRATIVE_STAFF)),
                () -> assertFalse(physician.hasRole(Role.SYSTEM_ADMINISTRATOR)));
    }

    @Test
    @DisplayName("Deactivating a user keeps all its data and saves the date")
    void deactivateKeepsDataAndSavesDate() {
        // Arrange
        LocalDateTime when = LocalDateTime.of(2026, 11, 10, 9, 30);

        // Act
        physician.deactivate(when);

        // Assert
        assertAll(
                () -> assertFalse(physician.isActive()),
                () -> assertEquals(when, physician.getDeactivatedAt()),
                () -> assertEquals("mlopez", physician.getUsername()),
                () -> assertEquals(List.of(Role.PHYSICIAN), physician.getRoles()));
    }

    @Test
    @DisplayName("An inactive user cannot be deactivated again")
    void inactiveUserCannotBeDeactivatedAgain() {
        // Arrange
        physician.deactivate(LocalDateTime.of(2026, 11, 10, 9, 30));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> physician.deactivate(LocalDateTime.of(2026, 11, 11, 9, 30)));
    }

    @Test
    @DisplayName("Two users are the same if they have the same username")
    void usersWithSameUsernameAreEqual() {
        // Arrange
        User sameUsername = new User("mlopez", "Other name", null, List.of(Role.PATIENT));
        User otherUsername = new User("jperez", "María López Test", "maria@test.test", List.of(Role.PHYSICIAN));

        // Assert
        assertAll(
                () -> assertEquals(physician, sameUsername),
                () -> assertEquals(physician.hashCode(), sameUsername.hashCode()),
                () -> assertNotEquals(physician, otherUsername));
    }
}
