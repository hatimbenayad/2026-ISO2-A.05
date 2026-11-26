package es.uclm.esi.iso2.common.admissions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IAdmissionsService {
    AdmissionDto admitPatient(String patientCip, String bedNumber);
    AdmissionDto dischargePatient(String admissionId);
    RoomScheduleDto scheduleRoom(String roomNumber, RoomType roomType, LocalDateTime start, LocalDateTime end, String orderNumber);
    List<RoomScheduleDto> getRoomSchedules(String roomNumber, LocalDate date);
    WorklistDto generateWorklist(String technicianId, LocalDate date);
}
