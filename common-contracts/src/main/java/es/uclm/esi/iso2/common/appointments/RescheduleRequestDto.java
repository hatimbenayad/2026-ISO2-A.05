package es.uclm.esi.iso2.common.appointments;

import java.time.LocalDateTime;
import java.util.Objects;

public record RescheduleRequestDto(
        String appointmentId,
        LocalDateTime requestedTime,
        String justification
) {
    public RescheduleRequestDto {
        Objects.requireNonNull(appointmentId, "appointmentId must not be null");
        Objects.requireNonNull(requestedTime, "requestedTime must not be null");
    }
}
