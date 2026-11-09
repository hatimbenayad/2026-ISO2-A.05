package es.uclm.esi.iso2.common.patients.dto;

import es.uclm.esi.iso2.common.patients.domain.Gender;
import java.time.LocalDate;

/** Data the client sends to modify a patient. National id and health card number are not here: they do not change. */
public record PatientUpdateRequest(String firstName, String lastName, LocalDate birthDate, Gender gender, String phone,
        String email, String address) {
}
