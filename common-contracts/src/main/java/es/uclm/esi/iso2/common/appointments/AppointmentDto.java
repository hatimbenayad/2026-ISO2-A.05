package es.uclm.esi.iso2.common.appointments;

import java.time.LocalDateTime;
import java.util.Objects;

public record AppointmentDto(
        String appointmentId,
        String patientCip,
        String orderNumber,
        LocalDateTime scheduledTime,
        AppointmentStatus status,
        String roomNumber,
        String cancellationReason
) {
    public AppointmentDto {
        Objects.requireNonNull(appointmentId, "appointmentId must not be null");
        Objects.requireNonNull(patientCip, "patientCip must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }
}
