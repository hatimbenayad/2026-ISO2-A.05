package es.uclm.esi.iso2.common.billing;

import java.time.LocalDateTime;
import java.util.Objects;

public record AdmissionIncidentDto(
        String incidentId,
        String admissionId,
        String description,
        LocalDateTime recordedAt,
        String reportedBy
) {
    public AdmissionIncidentDto {
        Objects.requireNonNull(incidentId, "incidentId must not be null");
        Objects.requireNonNull(admissionId, "admissionId must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(recordedAt, "recordedAt must not be null");
    }
}
