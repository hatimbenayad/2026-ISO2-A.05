package es.uclm.esi.iso2.common.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record InvoiceDto(
        String invoiceId,
        String patientCip,
        String orderNumber,
        BigDecimal totalAmount,
        InvoiceStatus status,
        LocalDateTime issueDate,
        String insurancePolicyNumber
) {
    public InvoiceDto {
        Objects.requireNonNull(invoiceId, "invoiceId must not be null");
        Objects.requireNonNull(patientCip, "patientCip must not be null");
        Objects.requireNonNull(orderNumber, "orderNumber must not be null");
        Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(issueDate, "issueDate must not be null");
    }
}
