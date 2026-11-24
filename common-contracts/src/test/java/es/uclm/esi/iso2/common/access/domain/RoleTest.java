package es.uclm.esi.iso2.common.access.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

/** Tests of {@link Role}: which role includes which, following the generalization of the client diagram (D-007). */
@DisplayName("Role")
class RoleTest {

    @ParameterizedTest(name = "{0} includes itself")
    @EnumSource(Role.class)
    @DisplayName("Every role includes itself")
    void everyRoleIncludesItself(Role role) {
        // Act and Assert
        assertTrue(role.includes(role));
    }

    @ParameterizedTest(name = "{0} includes {1}: {2}")
    @CsvSource({
        "PHYSICIAN,            PATIENT,              true",
        "PATIENT,              PHYSICIAN,            false",
        "ADMINISTRATIVE_STAFF, PATIENT,              false",
        "SYSTEM_ADMINISTRATOR, PATIENT,              false",
        "SYSTEM_ADMINISTRATOR, ADMINISTRATIVE_STAFF, false",
        "PHYSICIAN,            SYSTEM_ADMINISTRATOR, false"
    })
    @DisplayName("Only the physician includes another role, the patient")
    void onlyPhysicianIncludesPatient(Role role, Role other, boolean expected) {
        // Act
        boolean result = role.includes(other);

        // Assert
        assertEquals(expected, result);
    }
}
