package es.uclm.esi.iso2.common.admissions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Admissions Contracts Tests")
class AdmissionsContractsTest {

    @Test
    void testAdmissionStatusAndRoomTypeEnums() {
        assertEquals(4, AdmissionStatus.values().length);
        assertEquals(AdmissionStatus.ADMITTED, AdmissionStatus.valueOf("ADMITTED"));

        assertEquals(4, RoomType.values().length);
        assertEquals(RoomType.MRI, RoomType.valueOf("MRI"));
    }

    @Test
    void testAdmissionDtoValidAndInvariants() {
        LocalDateTime now = LocalDateTime.now();
        AdmissionDto dto = new AdmissionDto("adm-1", "CIP123", AdmissionStatus.ADMITTED, now, null, "Bed-01");

        assertAll(
                () -> assertEquals("adm-1", dto.admissionId()),
                () -> assertEquals("CIP123", dto.patientCip()),
                () -> assertEquals(AdmissionStatus.ADMITTED, dto.status()),
                () -> assertEquals(now, dto.admissionTime()),
                () -> assertNull(dto.dischargeTime()),
                () -> assertEquals("Bed-01", dto.bedNumber())
        );

        assertThrows(NullPointerException.class, () -> new AdmissionDto(null, "CIP123", AdmissionStatus.ADMITTED, now, null, "Bed-01"));
        assertThrows(NullPointerException.class, () -> new AdmissionDto("adm-1", null, AdmissionStatus.ADMITTED, now, null, "Bed-01"));
        assertThrows(NullPointerException.class, () -> new AdmissionDto("adm-1", "CIP123", null, now, null, "Bed-01"));
    }

    @Test
    void testBedAllocationDto() {
        BedAllocationDto dto = new BedAllocationDto("bed-1", "Ward-A", true, "CIP123");
        assertAll(
                () -> assertEquals("bed-1", dto.bedId()),
                () -> assertEquals("Ward-A", dto.wardNumber()),
                () -> assertTrue(dto.isOccupied()),
                () -> assertEquals("CIP123", dto.assignedPatientCip())
        );

        assertThrows(NullPointerException.class, () -> new BedAllocationDto(null, "Ward-A", false, null));
        assertThrows(NullPointerException.class, () -> new BedAllocationDto("bed-1", null, false, null));
    }

    @Test
    void testRoomScheduleDto() {
        LocalDateTime start = LocalDateTime.of(2026, 12, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 1, 11, 0);
        RoomScheduleDto dto = new RoomScheduleDto("sch-1", "Room-101", RoomType.MRI, start, end, "ORD-99");

        assertAll(
                () -> assertEquals("sch-1", dto.scheduleId()),
                () -> assertEquals("Room-101", dto.roomNumber()),
                () -> assertEquals(RoomType.MRI, dto.roomType()),
                () -> assertEquals(start, dto.startTime()),
                () -> assertEquals(end, dto.endTime()),
                () -> assertEquals("ORD-99", dto.assignedOrderNumber())
        );

        assertThrows(NullPointerException.class, () -> new RoomScheduleDto(null, "R1", RoomType.CT, start, end, "O1"));
        assertThrows(IllegalArgumentException.class, () -> new RoomScheduleDto("sch-1", "R1", RoomType.CT, end, start, "O1"));
    }

    @Test
    void testWorklistDtoImmutability() {
        List<String> mutableList = new ArrayList<>();
        mutableList.add("ORD-1");
        WorklistDto dto = new WorklistDto("wk-1", "tech-1", LocalDate.of(2026, 12, 1), mutableList);

        mutableList.add("ORD-2");
        assertEquals(1, dto.assignedOrderNumbers().size());
        assertEquals("ORD-1", dto.assignedOrderNumbers().get(0));

        WorklistDto nullListDto = new WorklistDto("wk-2", "tech-2", LocalDate.of(2026, 12, 1), null);
        assertNotNull(nullListDto.assignedOrderNumbers());
        assertTrue(nullListDto.assignedOrderNumbers().isEmpty());

        assertThrows(NullPointerException.class, () -> new WorklistDto(null, "tech-1", LocalDate.now(), List.of()));
        assertThrows(NullPointerException.class, () -> new WorklistDto("wk-1", null, LocalDate.now(), List.of()));
        assertThrows(NullPointerException.class, () -> new WorklistDto("wk-1", "tech-1", null, List.of()));
    }
}
