package es.uclm.esi.iso2.common.patients.contracts;

import es.uclm.esi.iso2.common.patients.dto.PatientDto;
import es.uclm.esi.iso2.common.patients.dto.PatientRegistrationRequest;
import es.uclm.esi.iso2.common.patients.dto.PatientUpdateRequest;
import java.util.List;

/**
 * Patient demographics (RF19, CU19, SCU02): the administrative staff registers, modifies and discharges
 * patients. A discharge never deletes the patient (D-015).
 */
public interface IPatientRegistryService {
    PatientDto register(PatientRegistrationRequest request) throws PatientRegistryException;

    PatientDto update(String patientId, PatientUpdateRequest request) throws PatientRegistryException;

    PatientDto discharge(String patientId) throws PatientRegistryException;

    /** Also finds discharged patients. {@code null} when the id is unknown. */
    PatientDto findById(String patientId);

    /** Only looks among the active patients. {@code null} when none has that national id. */
    PatientDto findByNationalId(String nationalId);

    /** Discharged patients are left out. Sorted by last name, then first name. */
    List<PatientDto> getActivePatients();
}
