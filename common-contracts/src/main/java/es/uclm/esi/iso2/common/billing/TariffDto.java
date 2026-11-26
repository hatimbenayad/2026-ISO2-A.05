package es.uclm.esi.iso2.common.billing;

import java.math.BigDecimal;
import java.util.Objects;

public record TariffDto(
        String procedureCode,
        String modality,
        BigDecimal baseRate,
        BigDecimal urgentMultiplier
) {
    public TariffDto {
        Objects.requireNonNull(procedureCode, "procedureCode must not be null");
        Objects.requireNonNull(modality, "modality must not be null");
        Objects.requireNonNull(baseRate, "baseRate must not be null");
        Objects.requireNonNull(urgentMultiplier, "urgentMultiplier must not be null");
        if (baseRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("baseRate cannot be negative");
        }
    }
}
