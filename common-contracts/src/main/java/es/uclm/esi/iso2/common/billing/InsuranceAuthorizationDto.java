package es.uclm.esi.iso2.common.billing;

import java.math.BigDecimal;
import java.util.Objects;

public record InsuranceAuthorizationDto(
        String authId,
        String policyNumber,
        String insurerName,
        boolean isApproved,
        BigDecimal coveragePercentage,
        String approvalCode
) {
    public InsuranceAuthorizationDto {
        Objects.requireNonNull(authId, "authId must not be null");
        Objects.requireNonNull(policyNumber, "policyNumber must not be null");
        Objects.requireNonNull(insurerName, "insurerName must not be null");
        coveragePercentage = coveragePercentage != null ? coveragePercentage : BigDecimal.ZERO;
    }
}
