package es.uclm.esi.iso2.common.notifications;

import java.time.LocalDateTime;
import java.util.Objects;

public record ClinicalHistoryRecordDto(
        String recordId,
        String patientCip,
        String orderNumber,
        String procedureName,
        LocalDateTime procedureDate,
        String diagnosticSummary,
        String radiologistName
) {
    public ClinicalHistoryRecordDto {
        Objects.requireNonNull(recordId, "recordId must not be null");
        Objects.requireNonNull(patientCip, "patientCip must not be null");
        Objects.requireNonNull(orderNumber, "orderNumber must not be null");
    }
}
