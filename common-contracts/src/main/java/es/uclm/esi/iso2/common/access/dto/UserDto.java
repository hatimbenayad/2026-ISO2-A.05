package es.uclm.esi.iso2.common.access.dto;

import es.uclm.esi.iso2.common.access.domain.Role;
import es.uclm.esi.iso2.common.access.domain.User;
import java.util.List;

/** What the client receives about a user. */
public record UserDto(String username, String fullName, String email, List<Role> roles, boolean active) {

    public static UserDto from(User user) {
        return new UserDto(user.getUsername(), user.getFullName(), user.getEmail(), user.getRoles(),
                user.isActive());
    }
}
