package es.uclm.esi.iso2.common.patients.dto;

import es.uclm.esi.iso2.common.patients.domain.MedicalRecord;
import java.time.LocalDateTime;
import java.util.List;

/** What the client receives about the medical record of a patient. */
public record MedicalRecordDto(String recordNumber, String patientId, String bloodType, List<String> allergies,
        List<String> chronicalConditions, boolean active, LocalDateTime deactivatedAt) {

    public static MedicalRecordDto from(MedicalRecord medicalRecord) {
        return new MedicalRecordDto(medicalRecord.getRecordNumber(), medicalRecord.getPatientId(),
                medicalRecord.getBloodType(), medicalRecord.getAllergies(), medicalRecord.getChronicalConditions(),
                medicalRecord.isActive(), medicalRecord.getDeactivatedAt());
    }
}
