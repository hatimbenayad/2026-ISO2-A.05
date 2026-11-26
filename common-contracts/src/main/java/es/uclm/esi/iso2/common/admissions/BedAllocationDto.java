package es.uclm.esi.iso2.common.admissions;

import java.util.Objects;

public record BedAllocationDto(
        String bedId,
        String wardNumber,
        boolean isOccupied,
        String assignedPatientCip
) {
    public BedAllocationDto {
        Objects.requireNonNull(bedId, "bedId must not be null");
        Objects.requireNonNull(wardNumber, "wardNumber must not be null");
    }
}
