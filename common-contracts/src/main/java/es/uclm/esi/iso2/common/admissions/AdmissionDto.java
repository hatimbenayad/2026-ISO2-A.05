package es.uclm.esi.iso2.common.admissions;

import java.time.LocalDateTime;
import java.util.Objects;

public record AdmissionDto(
        String admissionId,
        String patientCip,
        AdmissionStatus status,
        LocalDateTime admissionTime,
        LocalDateTime dischargeTime,
        String bedNumber
) {
    public AdmissionDto {
        Objects.requireNonNull(admissionId, "admissionId must not be null");
        Objects.requireNonNull(patientCip, "patientCip must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }
}
