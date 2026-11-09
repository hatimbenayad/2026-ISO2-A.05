package es.uclm.esi.iso2.server.patients.service;

import es.uclm.esi.iso2.common.patients.domain.Patient;
import es.uclm.esi.iso2.server.patients.persistence.IPatientRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Repository for the service tests: keeps the patients in a map and remembers how many times one was saved. */
class FakePatientRepository implements IPatientRepository {
    private final Map<String, Patient> patients = new LinkedHashMap<>();
    private int timesSaved;
    private int lastNumber;

    FakePatientRepository(Patient... initialPatients) {
        for (Patient patient : initialPatients) {
            patients.put(patient.getId(), patient);
        }
    }

    @Override
    public Patient findById(String id) {
        return patients.get(id);
    }

    @Override
    public List<Patient> findAll() {
        return List.copyOf(patients.values());
    }

    @Override
    public void save(Patient patient) {
        timesSaved++;
        patients.put(patient.getId(), patient);
    }

    @Override
    public String nextId() {
        lastNumber++;
        return "TEST-" + lastNumber;
    }

    int timesSaved() {
        return timesSaved;
    }
}
