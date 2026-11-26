package es.uclm.esi.iso2.common.admin;

import java.time.LocalTime;
import java.util.Objects;

public record ModalityConfigDto(
        String modalityCode,
        String name,
        int slotDurationMinutes,
        LocalTime operatingHoursStart,
        LocalTime operatingHoursEnd,
        boolean active
) {
    public ModalityConfigDto {
        Objects.requireNonNull(modalityCode, "modalityCode must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if (slotDurationMinutes <= 0) {
            throw new IllegalArgumentException("slotDurationMinutes must be positive");
        }
    }
}
