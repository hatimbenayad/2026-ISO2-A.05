package es.uclm.esi.iso2.common.notifications;

import java.time.LocalDateTime;
import java.util.Objects;

public record NotificationDto(
        String notificationId,
        String recipientCip,
        NotificationType type,
        String message,
        LocalDateTime sentAt,
        boolean isRead
) {
    public NotificationDto {
        Objects.requireNonNull(notificationId, "notificationId must not be null");
        Objects.requireNonNull(recipientCip, "recipientCip must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(sentAt, "sentAt must not be null");
    }
}
