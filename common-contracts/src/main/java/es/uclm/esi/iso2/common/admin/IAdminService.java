package es.uclm.esi.iso2.common.admin;

import es.uclm.esi.iso2.common.access.domain.Role;
import java.time.LocalDateTime;
import java.util.List;

public interface IAdminService {
    UserAccountDto createUser(String username, String email, Role role);
    void deactivateUser(String userId);
    void registerMaintenanceWindow(MaintenanceWindowDto window);
    List<AuditEntryDto> queryAuditLog(LocalDateTime from, LocalDateTime to, String actorId);
    void configureAdapterEndpoint(AdapterEndpointConfigDto config);
}
