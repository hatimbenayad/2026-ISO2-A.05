package es.uclm.esi.iso2.common.patients.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import es.uclm.esi.iso2.common.patients.domain.Patient;
import es.uclm.esi.iso2.common.patients.domain.PatientStatus;
import es.uclm.esi.iso2.common.patients.domain.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link PatientDto}: it carries every field of the patient, the discharge included. */
@DisplayName("PatientDto")
class PatientDtoTest {

    @Test
    @DisplayName("from copies every field of a discharged patient")
    void fromCopiesEveryField() {
        // Arrange
        LocalDateTime registered = LocalDateTime.of(2026, 11, 10, 8, 0);
        Patient patient = new Patient("PAT-0001", "12345678Z", "CLM0001", "Ana", "Ruiz Test",
                LocalDate.of(1980, 5, 17), Gender.FEMALE, "600000000", "ana@test.test", "Calle Mayor 1", registered);
        patient.discharge(registered.plusDays(1));

        // Act
        PatientDto dto = PatientDto.from(patient);

        // Assert
        assertEquals(new PatientDto("PAT-0001", "12345678Z", "CLM0001", "Ana", "Ruiz Test", LocalDate.of(1980, 5, 17),
                Gender.FEMALE, "600000000", "ana@test.test", "Calle Mayor 1", PatientStatus.INACTIVE, registered,
                registered.plusDays(1)), dto);
    }
}
