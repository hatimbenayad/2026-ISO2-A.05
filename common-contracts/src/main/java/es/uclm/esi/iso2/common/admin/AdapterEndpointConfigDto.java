package es.uclm.esi.iso2.common.admin;

import java.util.Objects;

public record AdapterEndpointConfigDto(
        String adapterCode,
        String serviceUrl,
        int timeoutSeconds,
        int retryAttempts,
        boolean mockMode
) {
    public AdapterEndpointConfigDto {
        Objects.requireNonNull(adapterCode, "adapterCode must not be null");
        Objects.requireNonNull(serviceUrl, "serviceUrl must not be null");
        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("timeoutSeconds must be positive");
        }
        if (retryAttempts < 0) {
            throw new IllegalArgumentException("retryAttempts cannot be negative");
        }
    }
}
