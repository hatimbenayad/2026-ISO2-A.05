package es.uclm.esi.iso2.common.notifications;

import java.util.List;
import java.util.Objects;

public record PreparationGuidelineDto(
        String procedureCode,
        String title,
        List<String> instructions,
        boolean requiresFasting,
        int fastingHours
) {
    public PreparationGuidelineDto {
        Objects.requireNonNull(procedureCode, "procedureCode must not be null");
        Objects.requireNonNull(title, "title must not be null");
        instructions = instructions != null ? List.copyOf(instructions) : List.of();
    }
}
