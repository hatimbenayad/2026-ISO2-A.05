package es.uclm.esi.iso2.common.patients.dto;

import es.uclm.esi.iso2.common.patients.domain.Patient;
import es.uclm.esi.iso2.common.patients.domain.PatientStatus;
import es.uclm.esi.iso2.common.patients.domain.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** What the client receives about a patient. */
public record PatientDto(String id, String nationalId, String healthCardNumber, String firstName, String lastName,
        LocalDate birthDate, Gender gender, String phone, String email, String address, PatientStatus status,
        LocalDateTime registeredAt, LocalDateTime dischargedAt) {

    public static PatientDto from(Patient patient) {
        return new PatientDto(patient.getId(), patient.getNationalId(), patient.getHealthCardNumber(),
                patient.getFirstName(), patient.getLastName(), patient.getBirthDate(), patient.getGender(),
                patient.getPhone(), patient.getEmail(), patient.getAddress(), patient.getStatus(),
                patient.getRegisteredAt(), patient.getDischargedAt());
    }
}
