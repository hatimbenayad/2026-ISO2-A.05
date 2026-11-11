package es.uclm.esi.iso2.server.patients.persistence;

import es.uclm.esi.iso2.common.patients.domain.Patient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Keeps the patients in memory until the team chooses the database (A08). It starts empty: patients are created
 * through CU19.
 */
public class InMemoryPatientRepository implements IPatientRepository {
    private final List<Patient> patients = new ArrayList<>();
    private int lastNumber;

    @Override
    public Patient findById(String id) {
        for (Patient patient : patients) {
            if (patient.getId().equals(id)) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public List<Patient> findAll() {
        return List.copyOf(patients);
    }

    @Override
    public void save(Patient patient) {
        Objects.requireNonNull(patient.getId(), "id");
        patients.removeIf(existing -> existing.getId().equals(patient.getId()));
        patients.add(patient);
    }

    @Override
    public String nextId() {
        lastNumber++;
        return String.format("PAT-%04d", lastNumber);
    }
}
