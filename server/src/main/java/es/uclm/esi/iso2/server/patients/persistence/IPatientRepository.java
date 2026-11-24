package es.uclm.esi.iso2.server.patients.persistence;

import es.uclm.esi.iso2.common.patients.domain.Patient;
import java.util.List;

public interface IPatientRepository {
    /** {@code null} when no patient was saved with that id. */
    Patient findById(String id);

    /** Every stored patient, discharged ones included: nothing is deleted (D-015). */
    List<Patient> findAll();

    /** Adds the patient, or replaces the stored one with the same id. There is no delete operation. */
    void save(Patient patient);

    /** A new id that no stored patient has. */
    String nextId();
}
