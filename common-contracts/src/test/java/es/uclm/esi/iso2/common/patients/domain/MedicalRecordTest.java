package es.uclm.esi.iso2.common.patients.domain;

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

/** Tests of {@link MedicalRecord}: its own copy of the clinical lists and the deactivation (D-015). */
@DisplayName("MedicalRecord")
class MedicalRecordTest {
    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecord = new MedicalRecord("MR-0001", "PAT-0001", "A+", List.of("Penicillin"),
                List.of("Asthma", "Hypertension"));
    }

    @Test
    @DisplayName("A new medical record is active and keeps its clinical data")
    void newRecordIsActiveAndKeepsItsData() {
        // Assert
        assertAll(
                () -> assertEquals("MR-0001", medicalRecord.getRecordNumber()),
                () -> assertEquals("PAT-0001", medicalRecord.getPatientId()),
                () -> assertEquals("A+", medicalRecord.getBloodType()),
                () -> assertEquals(List.of("Penicillin"), medicalRecord.getAllergies()),
                () -> assertEquals(List.of("Asthma", "Hypertension"), medicalRecord.getChronicalConditions()),
                () -> assertTrue(medicalRecord.isActive()),
                () -> assertNull(medicalRecord.getDeactivatedAt()));
    }

    @Test
    @DisplayName("Without known allergies or conditions the lists are empty, not null")
    void missingListsBecomeEmpty() {
        // Act
        MedicalRecord empty = new MedicalRecord("MR-0002", "PAT-0002", null, null, null);

        // Assert
        assertAll(
                () -> assertTrue(empty.getAllergies().isEmpty()),
                () -> assertTrue(empty.getChronicalConditions().isEmpty()),
                () -> assertNull(empty.getBloodType()));
    }

    @Test
    @DisplayName("Later changes in the lists passed to the record do not reach it")
    void recordKeepsItsOwnCopyOfTheLists() {
        // Arrange
        List<String> allergies = new ArrayList<>(List.of("Latex"));
        List<String> conditions = new ArrayList<>(List.of("Diabetes"));
        MedicalRecord copied = new MedicalRecord("MR-0003", "PAT-0003", "0-", allergies, conditions);

        // Act
        allergies.add("Iodinated contrast");
        conditions.clear();

        // Assert
        assertAll(
                () -> assertEquals(List.of("Latex"), copied.getAllergies()),
                () -> assertEquals(List.of("Diabetes"), copied.getChronicalConditions()));
    }

    @Test
    @DisplayName("The allergies and conditions cannot be edited through the getters")
    void gettersReturnReadOnlyLists() {
        // Act and Assert
        assertAll(
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> medicalRecord.getAllergies().add("Latex")),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> medicalRecord.getChronicalConditions().remove(0)));
    }

    @Test
    @DisplayName("Updating the clinical data replaces it with a copy of the new values")
    void updatingTheClinicalData() {
        // Arrange
        List<String> newAllergies = new ArrayList<>(List.of("Penicillin", "Iodinated contrast"));

        // Act
        medicalRecord.setBloodType("A-");
        medicalRecord.setAllergies(newAllergies);
        medicalRecord.setChronicalConditions(null);
        newAllergies.add("Latex");

        // Assert
        assertAll(
                () -> assertEquals("A-", medicalRecord.getBloodType()),
                () -> assertEquals(List.of("Penicillin", "Iodinated contrast"), medicalRecord.getAllergies()),
                () -> assertTrue(medicalRecord.getChronicalConditions().isEmpty()));
    }

    @Test
    @DisplayName("Deactivating a medical record keeps the clinical data and saves the date")
    void deactivateKeepsTheClinicalData() {
        // Arrange
        LocalDateTime when = LocalDateTime.of(2026, 11, 12, 10, 15);

        // Act
        medicalRecord.deactivate(when);

        // Assert
        assertAll(
                () -> assertFalse(medicalRecord.isActive()),
                () -> assertEquals(when, medicalRecord.getDeactivatedAt()),
                () -> assertEquals("PAT-0001", medicalRecord.getPatientId()),
                () -> assertEquals(List.of("Penicillin"), medicalRecord.getAllergies()));
    }

    @Test
    @DisplayName("An inactive medical record cannot be deactivated again")
    void inactiveRecordCannotBeDeactivatedAgain() {
        // Arrange
        medicalRecord.deactivate(LocalDateTime.of(2026, 11, 12, 10, 15));

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> medicalRecord.deactivate(LocalDateTime.of(2026, 11, 13, 9, 0)));
    }

    @Test
    @DisplayName("Two medical records are the same if they have the same record number")
    void recordsWithSameNumberAreEqual() {
        // Arrange
        MedicalRecord sameNumber = new MedicalRecord("MR-0001", "PAT-0009", "B+", List.of(), List.of());
        MedicalRecord otherNumber = new MedicalRecord("MR-0002", "PAT-0001", "A+", List.of("Penicillin"),
                List.of("Asthma", "Hypertension"));

        // Assert
        assertAll(
                () -> assertEquals(medicalRecord, sameNumber),
                () -> assertEquals(medicalRecord.hashCode(), sameNumber.hashCode()),
                () -> assertNotEquals(medicalRecord, otherNumber));
    }
}
