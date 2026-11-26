package es.uclm.esi.iso2.common.admin;

import es.uclm.esi.iso2.common.access.domain.Role;
import java.time.LocalDateTime;
import java.util.Objects;

public record UserAccountDto(
        String userId,
        String username,
        String email,
        Role role,
        boolean isActive,
        LocalDateTime createdAt
) {
    public UserAccountDto {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(role, "role must not be null");
    }
}
