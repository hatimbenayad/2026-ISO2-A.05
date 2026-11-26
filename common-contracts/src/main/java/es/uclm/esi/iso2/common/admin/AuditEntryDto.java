package es.uclm.esi.iso2.common.admin;

import java.time.LocalDateTime;
import java.util.Objects;

public record AuditEntryDto(
        String auditId,
        String actorId,
        String action,
        String entityAffected,
        LocalDateTime timestamp,
        String ipAddress
) {
    public AuditEntryDto {
        Objects.requireNonNull(auditId, "auditId must not be null");
        Objects.requireNonNull(actorId, "actorId must not be null");
        Objects.requireNonNull(action, "action must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
    }
}
