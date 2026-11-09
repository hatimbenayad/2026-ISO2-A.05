package es.uclm.esi.iso2.common.patients.dto;

import es.uclm.esi.iso2.common.patients.domain.Sex;
import java.time.LocalDate;

/** Data the client sends to modify a patient. National id and health card number are not here: they do not change. */
public record PatientUpdateRequest(String firstName, String lastName, LocalDate birthDate, Sex sex, String phone,
        String email, String address) {
}
