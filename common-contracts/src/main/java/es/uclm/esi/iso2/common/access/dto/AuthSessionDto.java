package es.uclm.esi.iso2.common.access.dto;

import es.uclm.esi.iso2.common.access.domain.AuthSession;
import es.uclm.esi.iso2.common.access.domain.Role;
import java.time.LocalDateTime;
import java.util.List;

/** What the client receives after logging in. It sends the token back with each request. */
public record AuthSessionDto(String token, String username, List<Role> roles, LocalDateTime expiresAt) {

    public static AuthSessionDto from(AuthSession session) {
        return new AuthSessionDto(session.getToken(), session.getUsername(), session.getRoles(),
                session.getExpiresAt());
    }
}
