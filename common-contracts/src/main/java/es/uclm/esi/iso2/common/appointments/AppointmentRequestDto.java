package es.uclm.esi.iso2.common.appointments;

import java.time.LocalDateTime;
import java.util.Objects;

public record AppointmentRequestDto(
        String patientCip,
        String modality,
        LocalDateTime preferredDateRangeStart,
        LocalDateTime preferredDateRangeEnd,
        String clinicalNotes
) {
    public AppointmentRequestDto {
        Objects.requireNonNull(patientCip, "patientCip must not be null");
        Objects.requireNonNull(modality, "modality must not be null");
    }
}
