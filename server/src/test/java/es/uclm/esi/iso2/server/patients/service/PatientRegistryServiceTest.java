package es.uclm.esi.iso2.server.patients.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import es.uclm.esi.iso2.common.patients.contracts.PatientRegistryException;
import es.uclm.esi.iso2.common.patients.contracts.PatientRegistryFailureReason;
import es.uclm.esi.iso2.common.patients.domain.Patient;
import es.uclm.esi.iso2.common.patients.domain.PatientStatus;
import es.uclm.esi.iso2.common.patients.domain.Gender;
import es.uclm.esi.iso2.common.patients.dto.PatientDto;
import es.uclm.esi.iso2.common.patients.dto.PatientRegistrationRequest;
import es.uclm.esi.iso2.common.patients.dto.PatientUpdateRequest;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests of {@link PatientRegistryService} (CU19): the checks of the data, the failure reasons and the
 * discharge without deleting the patient. The repository is a hand-written test double and the clock is fixed.
 */
@DisplayName("PatientRegistryService (unit)")
class PatientRegistryServiceTest {
    private static final ZoneId ZONE = ZoneId.of("Europe/Madrid");
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 10, 10, 0);
    private static final LocalDate BIRTH = LocalDate.of(1980, 5, 17);

    private FakePatientRepository repository;
    private PatientRegistryService service;

    @BeforeEach
    void setUp() {
        repository = new FakePatientRepository();
        service = new PatientRegistryService(repository, Clock.fixed(NOW.atZone(ZONE).toInstant(), ZONE));
    }

    private static PatientRegistrationRequest request(String nationalId) {
        return new PatientRegistrationRequest(nationalId, "CLM0001", "Ana", "Ruiz Test", BIRTH, Gender.FEMALE,
                "600000000", "ana@test.test", "Calle Mayor 1");
    }

    private static PatientUpdateRequest update(String firstName, LocalDate birthDate) {
        return new PatientUpdateRequest(firstName, "Ruiz Test", birthDate, Gender.FEMALE, "", "ana.new@test.test",
                "Calle Toledo 2");
    }

    @Test
    @DisplayName("A valid request registers an active patient with the date of the clock")
    void registerValidRequestCreatesActivePatient() throws PatientRegistryException {
        // Act
        PatientDto result = service.register(request("12345678Z"));

        // Assert
        assertAll(
                () -> assertEquals("TEST-1", result.id()),
                () -> assertEquals(PatientStatus.ACTIVE, result.status()),
                () -> assertEquals(NOW, result.registeredAt()),
                () -> assertEquals(1, repository.timesSaved()),
                () -> assertEquals(result, service.findById("TEST-1")));
    }

    @Test
    @DisplayName("The national id is saved in capitals and without spaces, and empty optional data is saved as null")
    void registerCleansTheData() throws PatientRegistryException {
        // Arrange
        PatientRegistrationRequest messy = new PatientRegistrationRequest(" 12345678z ", " CLM0001 ", " Ana ",
                "Ruiz Test", BIRTH, null, " ", "", null);

        // Act
        PatientDto result = service.register(messy);

        // Assert
        assertAll(
                () -> assertEquals("12345678Z", result.nationalId()),
                () -> assertEquals("CLM0001", result.healthCardNumber()),
                () -> assertEquals("Ana", result.firstName()),
                () -> assertEquals(Gender.UNSPECIFIED, result.gender()),
                () -> assertNull(result.phone()),
                () -> assertNull(result.email()));
    }

    // An empty value without quotes is null; '' is an empty text.
    @ParameterizedTest(name = "national id \"{0}\", card \"{1}\", name \"{2}\", last name \"{3}\" -> MISSING_DATA")
    @CsvSource({
        ",          CLM0001, Ana, Ruiz Test",
        "'',        CLM0001, Ana, Ruiz Test",
        "12345678Z, ,        Ana, Ruiz Test",
        "12345678Z, CLM0001, ' ', Ruiz Test",
        "12345678Z, CLM0001, Ana, "
    })
    @DisplayName("A request without a mandatory field is rejected and nothing is saved")
    void registerWithoutMandatoryDataIsRejected(String nationalId, String card, String firstName, String lastName) {
        // Arrange
        PatientRegistrationRequest incomplete = new PatientRegistrationRequest(nationalId, card, firstName, lastName,
                BIRTH, Gender.FEMALE, null, null, null);

        // Act
        PatientRegistryException exception =
                assertThrows(PatientRegistryException.class, () -> service.register(incomplete));

        // Assert
        assertAll(
                () -> assertEquals(PatientRegistryFailureReason.MISSING_DATA, exception.getReason()),
                () -> assertEquals(0, repository.timesSaved()));
    }

    @Test
    @DisplayName("A request without birth date is rejected")
    void registerWithoutBirthDateIsRejected() {
        // Act
        PatientRegistryException exception =
                assertThrows(PatientRegistryException.class, () -> service.register(bornOn(null)));

        // Assert
        assertEquals(PatientRegistryFailureReason.MISSING_DATA, exception.getReason());
    }

    private static PatientRegistrationRequest bornOn(LocalDate birthDate) {
        return new PatientRegistrationRequest("12345678Z", "CLM0001", "Ana", "Ruiz Test", birthDate, Gender.FEMALE,
                null, null, null);
    }

    // Today is the limit: a patient born today is valid, one born tomorrow is not.
    @Test
    @DisplayName("A patient born today can be registered")
    void patientBornTodayIsAccepted() throws PatientRegistryException {
        // Act
        PatientDto result = service.register(bornOn(NOW.toLocalDate()));

        // Assert
        assertEquals(NOW.toLocalDate(), result.birthDate());
    }

    @Test
    @DisplayName("A birth date in the future is rejected")
    void birthDateInTheFutureIsRejected() {
        // Act
        PatientRegistryException exception = assertThrows(PatientRegistryException.class,
                () -> service.register(bornOn(NOW.toLocalDate().plusDays(1))));

        // Assert
        assertAll(
                () -> assertEquals(PatientRegistryFailureReason.BIRTH_DATE_IN_THE_FUTURE, exception.getReason()),
                () -> assertEquals(0, repository.timesSaved()));
    }

    @Test
    @DisplayName("Two active patients cannot have the same national id, written in capitals or not")
    void duplicatedNationalIdIsRejected() throws PatientRegistryException {
        // Arrange
        service.register(request("12345678Z"));

        // Act
        PatientRegistryException exception =
                assertThrows(PatientRegistryException.class, () -> service.register(request("12345678z")));

        // Assert
        assertAll(
                () -> assertEquals(PatientRegistryFailureReason.DUPLICATED_NATIONAL_ID, exception.getReason()),
                () -> assertEquals(1, repository.timesSaved()));
    }

    @Test
    @DisplayName("A discharged patient can be registered again, and the search finds the new active one")
    void dischargedPatientCanBeRegisteredAgain() throws PatientRegistryException {
        // Arrange
        PatientDto first = service.register(request("12345678Z"));
        service.discharge(first.id());

        // Act
        PatientDto second = service.register(request("12345678Z"));

        // Assert
        assertAll(
                () -> assertEquals(second.id(), service.findByNationalId("12345678z").id()),
                () -> assertEquals(PatientStatus.INACTIVE, service.findById(first.id()).status()));
    }

    @Test
    @DisplayName("Modifying a patient changes its personal and contact data but not its national id")
    void updateChangesPersonalAndContactData() throws PatientRegistryException {
        // Arrange
        PatientDto registered = service.register(request("12345678Z"));

        // Act
        PatientDto result = service.update(registered.id(), update("Ana María", BIRTH));

        // Assert
        assertAll(
                () -> assertEquals("Ana María", result.firstName()),
                () -> assertNull(result.phone()),
                () -> assertEquals("ana.new@test.test", result.email()),
                () -> assertEquals("Calle Toledo 2", result.address()),
                () -> assertEquals("12345678Z", result.nationalId()),
                () -> assertEquals(2, repository.timesSaved()));
    }

    @Test
    @DisplayName("A wrong modification is rejected and the patient stays as it was")
    void wrongUpdateLeavesPatientAsItWas() throws PatientRegistryException {
        // Arrange
        PatientDto registered = service.register(request("12345678Z"));

        // Act
        PatientRegistryException exception = assertThrows(PatientRegistryException.class,
                () -> service.update(registered.id(), update("", BIRTH)));

        // Assert
        assertAll(
                () -> assertEquals(PatientRegistryFailureReason.MISSING_DATA, exception.getReason()),
                () -> assertEquals(registered, service.findById(registered.id())));
    }

    @Test
    @DisplayName("Discharging a patient keeps it stored but it is no longer in the list of active patients")
    void dischargeKeepsPatientStored() throws PatientRegistryException {
        // Arrange
        PatientDto registered = service.register(request("12345678Z"));

        // Act
        PatientDto result = service.discharge(registered.id());

        // Assert
        assertAll(
                () -> assertEquals(PatientStatus.INACTIVE, result.status()),
                () -> assertEquals(NOW, result.dischargedAt()),
                () -> assertEquals(1, repository.findAll().size()),
                () -> assertTrue(service.getActivePatients().isEmpty()),
                () -> assertNull(service.findByNationalId("12345678Z")));
    }

    @Test
    @DisplayName("A discharged patient cannot be modified or discharged again")
    void dischargedPatientCannotBeChanged() throws PatientRegistryException {
        // Arrange
        String id = service.register(request("12345678Z")).id();
        service.discharge(id);

        // Act
        PatientRegistryException onUpdate =
                assertThrows(PatientRegistryException.class, () -> service.update(id, update("Ana", BIRTH)));
        PatientRegistryException onDischarge =
                assertThrows(PatientRegistryException.class, () -> service.discharge(id));

        // Assert
        assertAll(
                () -> assertEquals(PatientRegistryFailureReason.PATIENT_DISCHARGED, onUpdate.getReason()),
                () -> assertEquals(PatientRegistryFailureReason.PATIENT_DISCHARGED, onDischarge.getReason()));
    }

    @Test
    @DisplayName("An unknown patient gives UNKNOWN_PATIENT and the search gives null")
    void unknownPatient() {
        // Act
        PatientRegistryException exception =
                assertThrows(PatientRegistryException.class, () -> service.discharge("NOPE"));

        // Assert
        assertAll(
                () -> assertEquals(PatientRegistryFailureReason.UNKNOWN_PATIENT, exception.getReason()),
                () -> assertNull(service.findById("NOPE")),
                () -> assertNull(service.findByNationalId(" ")));
    }

    @Test
    @DisplayName("The active patients are ordered by last name and then by first name")
    void activePatientsAreOrderedByName() {
        // Arrange
        LocalDateTime registered = NOW.minusDays(1);
        repository = new FakePatientRepository(
                new Patient("P1", "1A", "C1", "Luis", "Sánchez", BIRTH, registered),
                new Patient("P2", "2B", "C2", "Marta", "García", BIRTH, registered),
                new Patient("P3", "3C", "C3", "Ana", "García", BIRTH, registered));
        service = new PatientRegistryService(repository, Clock.fixed(NOW.atZone(ZONE).toInstant(), ZONE));

        // Act
        List<String> ids = service.getActivePatients().stream().map(PatientDto::id).toList();

        // Assert
        assertEquals(List.of("P3", "P2", "P1"), ids);
    }
}
