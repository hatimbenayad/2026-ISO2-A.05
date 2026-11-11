package es.uclm.esi.iso2.server.patients.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import es.uclm.esi.iso2.common.patients.domain.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests of {@link InMemoryPatientRepository}: the ids, {@code save} and the lookups. */
@DisplayName("InMemoryPatientRepository")
class InMemoryPatientRepositoryTest {
    private static final LocalDateTime REGISTERED = LocalDateTime.of(2026, 11, 10, 8, 0);

    private InMemoryPatientRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPatientRepository();
    }

    private static Patient patient(String id, String firstName) {
        return new Patient(id, "12345678Z", "CLM0001", firstName, "Ruiz Test", LocalDate.of(1980, 5, 17), REGISTERED);
    }

    @Test
    @DisplayName("It starts empty")
    void startsEmpty() {
        // Assert
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("The ids follow the order PAT-0001, PAT-0002...")
    void idsAreConsecutive() {
        // Act
        String first = repository.nextId();
        String second = repository.nextId();

        // Assert
        assertAll(
                () -> assertEquals("PAT-0001", first),
                () -> assertEquals("PAT-0002", second));
    }

    @Test
    @DisplayName("A saved patient can be found by its id")
    void savedPatientIsFoundById() {
        // Arrange
        Patient ana = patient("PAT-0001", "Ana");

        // Act
        repository.save(ana);

        // Assert
        assertSame(ana, repository.findById("PAT-0001"));
    }

    @Test
    @DisplayName("Saving a patient with an existing id replaces it instead of adding another one")
    void saveReplacesPatientWithSameId() {
        // Arrange
        repository.save(patient("PAT-0001", "Ana"));

        // Act
        repository.save(patient("PAT-0001", "Ana María"));

        // Assert
        assertAll(
                () -> assertEquals(1, repository.findAll().size()),
                () -> assertEquals("Ana María", repository.findById("PAT-0001").getFirstName()));
    }

    @Test
    @DisplayName("A patient without id cannot be saved")
    void patientWithoutIdCannotBeSaved() {
        // Act and Assert
        assertThrows(NullPointerException.class, () -> repository.save(patient(null, "Ana")));
    }

    @Test
    @DisplayName("Clearing the result of findAll does not remove patients")
    void findAllGivesAReadOnlyList() {
        // Arrange
        repository.save(patient("PAT-0001", "Ana"));

        // Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> repository.findAll().clear());
    }

    // Ids are generated in capitals, so "pat-0001" is a different id.
    @ParameterizedTest(name = "\"{0}\" is not found")
    @NullAndEmptySource
    @ValueSource(strings = { "PAT-9999", "pat-0001" })
    @DisplayName("An id that was never saved gives null")
    void unknownIdGivesNull(String id) {
        // Arrange
        repository.save(patient("PAT-0001", "Ana"));

        // Act and Assert
        assertNull(repository.findById(id));
    }
}
