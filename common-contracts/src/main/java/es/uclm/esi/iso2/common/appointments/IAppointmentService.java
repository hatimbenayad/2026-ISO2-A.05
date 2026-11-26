package es.uclm.esi.iso2.common.appointments;

import java.util.List;

public interface IAppointmentService {
    AppointmentDto requestAppointment(AppointmentRequestDto request);
    AppointmentDto confirmAppointment(String appointmentId, String patientCip);
    AppointmentDto requestReschedule(RescheduleRequestDto request);
    AppointmentDto cancelAppointment(String appointmentId, String patientCip, String justification);
    List<AppointmentDto> getPatientAppointments(String patientCip);
}
