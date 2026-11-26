package es.uclm.esi.iso2.common.admin;

import static org.junit.jupiter.api.Assertions.*;

import es.uclm.esi.iso2.common.access.domain.Role;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Admin Contracts Tests")
class AdminContractsTest {

    @Test
    void testUserAccountDto() {
        LocalDateTime now = LocalDateTime.now();
        UserAccountDto dto = new UserAccountDto("U-1", "jdoe", "jdoe@sescam.es", Role.PHYSICIAN, true, now);

        assertAll(
                () -> assertEquals("U-1", dto.userId()),
                () -> assertEquals("jdoe", dto.username()),
                () -> assertEquals("jdoe@sescam.es", dto.email()),
                () -> assertEquals(Role.PHYSICIAN, dto.role()),
                () -> assertTrue(dto.isActive()),
                () -> assertEquals(now, dto.createdAt())
        );

        assertThrows(NullPointerException.class, () -> new UserAccountDto(null, "u", "e", Role.PHYSICIAN, true, now));
    }

    @Test
    void testModalityConfigDto() {
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(20, 0);
        ModalityConfigDto dto = new ModalityConfigDto("MRI", "Magnetic Resonance Imaging", 30, start, end, true);

        assertAll(
                () -> assertEquals("MRI", dto.modalityCode()),
                () -> assertEquals("Magnetic Resonance Imaging", dto.name()),
                () -> assertEquals(30, dto.slotDurationMinutes()),
                () -> assertEquals(start, dto.operatingHoursStart()),
                () -> assertEquals(end, dto.operatingHoursEnd()),
                () -> assertTrue(dto.active())
        );

        assertThrows(IllegalArgumentException.class, () -> new ModalityConfigDto("CT", "CT", 0, start, end, true));
        assertThrows(NullPointerException.class, () -> new ModalityConfigDto(null, "CT", 15, start, end, true));
    }

    @Test
    void testExaminationTypeDto() {
        ExaminationTypeDto dto = new ExaminationTypeDto("EX-01", "Brain MRI with Contrast", "MRI", 45, true);

        assertAll(
                () -> assertEquals("EX-01", dto.examCode()),
                () -> assertEquals("Brain MRI with Contrast", dto.name()),
                () -> assertEquals("MRI", dto.modalityCode()),
                () -> assertEquals(45, dto.estimatedMinutes()),
                () -> assertTrue(dto.requiresContrast())
        );

        assertThrows(IllegalArgumentException.class, () -> new ExaminationTypeDto("E1", "N", "M", -5, false));
        assertThrows(NullPointerException.class, () -> new ExaminationTypeDto(null, "N", "M", 30, false));
    }

    @Test
    void testMaintenanceWindowDto() {
        LocalDateTime start = LocalDateTime.of(2026, 12, 1, 22, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 2, 6, 0);
        MaintenanceWindowDto dto = new MaintenanceWindowDto("MAINT-1", "Room-102", start, end, "Cryogen refill");

        assertAll(
                () -> assertEquals("MAINT-1", dto.maintenanceId()),
                () -> assertEquals("Room-102", dto.roomNumber()),
                () -> assertEquals(start, dto.startWindow()),
                () -> assertEquals(end, dto.endWindow()),
                () -> assertEquals("Cryogen refill", dto.reason())
        );

        assertThrows(IllegalArgumentException.class, () -> new MaintenanceWindowDto("M1", "R1", end, start, "Err"));
        assertThrows(NullPointerException.class, () -> new MaintenanceWindowDto(null, "R1", start, end, "Err"));
    }

    @Test
    void testAuditEntryDto() {
        LocalDateTime now = LocalDateTime.now();
        AuditEntryDto dto = new AuditEntryDto("AUD-1", "user-42", "LOGIN_ATTEMPT", "AuthSession", now, "10.0.0.1");

        assertAll(
                () -> assertEquals("AUD-1", dto.auditId()),
                () -> assertEquals("user-42", dto.actorId()),
                () -> assertEquals("LOGIN_ATTEMPT", dto.action()),
                () -> assertEquals("AuthSession", dto.entityAffected()),
                () -> assertEquals(now, dto.timestamp()),
                () -> assertEquals("10.0.0.1", dto.ipAddress())
        );

        assertThrows(NullPointerException.class, () -> new AuditEntryDto(null, "u", "a", "e", now, "ip"));
    }

    @Test
    void testAdapterEndpointConfigDto() {
        AdapterEndpointConfigDto dto = new AdapterEndpointConfigDto("PACS", "https://pacs.sescam.es/api", 15, 3, true);

        assertAll(
                () -> assertEquals("PACS", dto.adapterCode()),
                () -> assertEquals("https://pacs.sescam.es/api", dto.serviceUrl()),
                () -> assertEquals(15, dto.timeoutSeconds()),
                () -> assertEquals(3, dto.retryAttempts()),
                () -> assertTrue(dto.mockMode())
        );

        assertThrows(IllegalArgumentException.class, () -> new AdapterEndpointConfigDto("EHR", "url", 0, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new AdapterEndpointConfigDto("EHR", "url", 10, -1, false));
        assertThrows(NullPointerException.class, () -> new AdapterEndpointConfigDto(null, "url", 10, 1, false));
    }
}
