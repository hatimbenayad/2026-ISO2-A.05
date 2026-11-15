package es.uclm.esi.iso2.common.patients.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import es.uclm.esi.iso2.common.patients.domain.MedicalRecord;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link MedicalRecordDto}: the client gets the clinical data and the state of the record. */
@DisplayName("MedicalRecordDto")
class MedicalRecordDtoTest {

    @Test
    @DisplayName("from keeps the clinical data of an inactive record")
    void fromKeepsTheClinicalData() {
        // Arrange
        LocalDateTime deactivated = LocalDateTime.of(2026, 11, 12, 10, 15);
        MedicalRecord medicalRecord = new MedicalRecord("MR-0001", "PAT-0001", "A+", List.of("Penicillin"),
                List.of("Asthma"));
        medicalRecord.deactivate(deactivated);

        // Act
        MedicalRecordDto dto = MedicalRecordDto.from(medicalRecord);

        // Assert
        assertEquals(new MedicalRecordDto("MR-0001", "PAT-0001", "A+", List.of("Penicillin"), List.of("Asthma"),
                false, deactivated), dto);
    }
}
