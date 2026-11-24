package es.uclm.esi.iso2.common.patients.dto;

import es.uclm.esi.iso2.common.patients.domain.Gender;
import java.time.LocalDate;

/** Data the client sends to register a patient. Phone, email and address can be null. */
public record PatientRegistrationRequest(String nationalId, String healthCardNumber, String firstName,
        String lastName, LocalDate birthDate, Gender gender, String phone, String email, String address) {
}
