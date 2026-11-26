package es.uclm.esi.iso2.common.admin;

import java.time.LocalDateTime;
import java.util.Objects;

public record MaintenanceWindowDto(
        String maintenanceId,
        String roomNumber,
        LocalDateTime startWindow,
        LocalDateTime endWindow,
        String reason
) {
    public MaintenanceWindowDto {
        Objects.requireNonNull(maintenanceId, "maintenanceId must not be null");
        Objects.requireNonNull(roomNumber, "roomNumber must not be null");
        Objects.requireNonNull(startWindow, "startWindow must not be null");
        Objects.requireNonNull(endWindow, "endWindow must not be null");
        if (endWindow.isBefore(startWindow)) {
            throw new IllegalArgumentException("endWindow must not be before startWindow");
        }
    }
}
