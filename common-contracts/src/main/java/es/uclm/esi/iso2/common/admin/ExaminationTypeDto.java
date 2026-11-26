package es.uclm.esi.iso2.common.admin;

import java.util.Objects;

public record ExaminationTypeDto(
        String examCode,
        String name,
        String modalityCode,
        int estimatedMinutes,
        boolean requiresContrast
) {
    public ExaminationTypeDto {
        Objects.requireNonNull(examCode, "examCode must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(modalityCode, "modalityCode must not be null");
        if (estimatedMinutes <= 0) {
            throw new IllegalArgumentException("estimatedMinutes must be positive");
        }
    }
}
