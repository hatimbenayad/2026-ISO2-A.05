package es.uclm.esi.iso2.common.admissions;

import java.time.LocalDateTime;
import java.util.Objects;

public record RoomScheduleDto(
        String scheduleId,
        String roomNumber,
        RoomType roomType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String assignedOrderNumber
) {
    public RoomScheduleDto {
        Objects.requireNonNull(scheduleId, "scheduleId must not be null");
        Objects.requireNonNull(roomNumber, "roomNumber must not be null");
        Objects.requireNonNull(roomType, "roomType must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");
        Objects.requireNonNull(endTime, "endTime must not be null");
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime must not be before startTime");
        }
    }
}
