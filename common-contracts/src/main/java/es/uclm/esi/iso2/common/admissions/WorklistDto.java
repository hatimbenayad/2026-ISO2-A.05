package es.uclm.esi.iso2.common.admissions;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record WorklistDto(
        String worklistId,
        String technicianId,
        LocalDate shiftDate,
        List<String> assignedOrderNumbers
) {
    public WorklistDto {
        Objects.requireNonNull(worklistId, "worklistId must not be null");
        Objects.requireNonNull(technicianId, "technicianId must not be null");
        Objects.requireNonNull(shiftDate, "shiftDate must not be null");
        assignedOrderNumbers = assignedOrderNumbers != null ? List.copyOf(assignedOrderNumbers) : List.of();
    }
}
