package es.uclm.esi.iso2.common.patients.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link Patient}: the state of a new patient and the discharge without deleting anything (D-015). */
@DisplayName("Patient")
class PatientTest {
    private static final LocalDateTime REGISTERED = LocalDateTime.of(2026, 11, 10, 8, 0);
    private static final LocalDate BIRTH = LocalDate.of(1980, 5, 17);

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient("PAT-0001", "12345678Z", "CLM0001", "Ana", "Ruiz Test", BIRTH, Gender.FEMALE,
                "600000000", "ana@test.test", "Calle Mayor 1, Ciudad Real", REGISTERED);
    }

    @Test
    @DisplayName("A new patient is active and keeps its data")
    void newPatientIsActiveAndKeepsItsData() {
        // Assert
        assertAll(
                () -> assertEquals("PAT-0001", patient.getId()),
                () -> assertEquals("12345678Z", patient.getNationalId()),
                () -> assertEquals("CLM0001", patient.getHealthCardNumber()),
                () -> assertEquals(BIRTH, patient.getBirthDate()),
                () -> assertEquals(PatientStatus.ACTIVE, patient.getStatus()),
                () -> assertTrue(patient.isActive()),
                () -> assertEquals(REGISTERED, patient.getRegisteredAt()),
                () -> assertNull(patient.getDischargedAt()));
    }

    @Test
    @DisplayName("A patient created with only the mandatory data has no contact data and an unspecified gender")
    void patientWithMandatoryDataOnly() {
        // Act
        Patient minimal = new Patient("PAT-0002", "87654321X", "CLM0002", "Luis", "Gómez Test", BIRTH, REGISTERED);

        // Assert
        assertAll(
                () -> assertEquals(Gender.UNSPECIFIED, minimal.getGender()),
                () -> assertNull(minimal.getPhone()),
                () -> assertNull(minimal.getEmail()),
                () -> assertNull(minimal.getAddress()),
                () -> assertTrue(minimal.isActive()));
    }

    @Test
    @DisplayName("Discharging a patient only changes the state and keeps all the data")
    void dischargeKeepsAllTheData() {
        // Arrange
        LocalDateTime when = REGISTERED.plusDays(3);

        // Act
        patient.discharge(when);

        // Assert
        assertAll(
                () -> assertEquals(PatientStatus.INACTIVE, patient.getStatus()),
                () -> assertFalse(patient.isActive()),
                () -> assertEquals(when, patient.getDischargedAt()),
                () -> assertEquals("Ana", patient.getFirstName()),
                () -> assertEquals("12345678Z", patient.getNationalId()),
                () -> assertEquals("600000000", patient.getPhone()));
    }

    @Test
    @DisplayName("A discharged patient cannot be discharged again")
    void dischargedPatientCannotBeDischargedAgain() {
        // Arrange
        patient.discharge(REGISTERED.plusDays(1));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> patient.discharge(REGISTERED.plusDays(2)));
    }

    @Test
    @DisplayName("Two patients are the same if they have the same id")
    void patientsWithSameIdAreEqual() {
        // Arrange
        Patient sameId = new Patient("PAT-0001", "87654321X", "CLM0002", "Luis", "Gómez Test", BIRTH, REGISTERED);
        Patient otherId = new Patient("PAT-0002", "12345678Z", "CLM0001", "Ana", "Ruiz Test", BIRTH, REGISTERED);

        // Assert
        assertAll(
                () -> assertEquals(patient, sameId),
                () -> assertEquals(patient.hashCode(), sameId.hashCode()),
                () -> assertNotEquals(patient, otherId));
    }
}
