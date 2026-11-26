package es.uclm.esi.iso2.common.appointments;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Appointments Contracts Tests")
class AppointmentsContractsTest {

    @Test
    void testAppointmentStatusEnum() {
        assertEquals(6, AppointmentStatus.values().length);
        assertEquals(AppointmentStatus.CONFIRMED, AppointmentStatus.valueOf("CONFIRMED"));
    }

    @Test
    void testAppointmentDto() {
        LocalDateTime now = LocalDateTime.now();
        AppointmentDto dto = new AppointmentDto("APP-1", "CIP123", "ORD-1", now, AppointmentStatus.CONFIRMED, "Room-1", null);

        assertAll(
                () -> assertEquals("APP-1", dto.appointmentId()),
                () -> assertEquals("CIP123", dto.patientCip()),
                () -> assertEquals("ORD-1", dto.orderNumber()),
                () -> assertEquals(now, dto.scheduledTime()),
                () -> assertEquals(AppointmentStatus.CONFIRMED, dto.status()),
                () -> assertEquals("Room-1", dto.roomNumber()),
                () -> assertNull(dto.cancellationReason())
        );

        assertThrows(NullPointerException.class, () -> new AppointmentDto(null, "CIP", "ORD", now, AppointmentStatus.SCHEDULED, "R1", null));
        assertThrows(NullPointerException.class, () -> new AppointmentDto("APP", null, "ORD", now, AppointmentStatus.SCHEDULED, "R1", null));
        assertThrows(NullPointerException.class, () -> new AppointmentDto("APP", "CIP", "ORD", now, null, "R1", null));
    }

    @Test
    void testAppointmentRequestDto() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        AppointmentRequestDto req = new AppointmentRequestDto("CIP123", "MRI", start, end, "Patient allergic to iodine");

        assertAll(
                () -> assertEquals("CIP123", req.patientCip()),
                () -> assertEquals("MRI", req.modality()),
                () -> assertEquals(start, req.preferredDateRangeStart()),
                () -> assertEquals(end, req.preferredDateRangeEnd()),
                () -> assertEquals("Patient allergic to iodine", req.clinicalNotes())
        );

        assertThrows(NullPointerException.class, () -> new AppointmentRequestDto(null, "MRI", start, end, null));
        assertThrows(NullPointerException.class, () -> new AppointmentRequestDto("CIP", null, start, end, null));
    }

    @Test
    void testRescheduleRequestDto() {
        LocalDateTime reqTime = LocalDateTime.now().plusDays(3);
        RescheduleRequestDto req = new RescheduleRequestDto("APP-1", reqTime, "Work conflict");

        assertAll(
                () -> assertEquals("APP-1", req.appointmentId()),
                () -> assertEquals(reqTime, req.requestedTime()),
                () -> assertEquals("Work conflict", req.justification())
        );

        assertThrows(NullPointerException.class, () -> new RescheduleRequestDto(null, reqTime, "Reason"));
        assertThrows(NullPointerException.class, () -> new RescheduleRequestDto("APP-1", null, "Reason"));
    }
}
