package es.uclm.esi.iso2.server.patients.service;

import es.uclm.esi.iso2.common.patients.contracts.IPatientRegistryService;
import es.uclm.esi.iso2.common.patients.contracts.PatientRegistryException;
import es.uclm.esi.iso2.common.patients.contracts.PatientRegistryFailureReason;
import es.uclm.esi.iso2.common.patients.domain.Patient;
import es.uclm.esi.iso2.common.patients.domain.Sex;
import es.uclm.esi.iso2.common.patients.dto.PatientDto;
import es.uclm.esi.iso2.common.patients.dto.PatientRegistrationRequest;
import es.uclm.esi.iso2.common.patients.dto.PatientUpdateRequest;
import es.uclm.esi.iso2.server.patients.persistence.IPatientRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class PatientRegistryService implements IPatientRegistryService {
    private final IPatientRepository patientRepository;
    private final Clock clock;

    public PatientRegistryService(IPatientRepository patientRepository) {
        this(patientRepository, Clock.systemDefaultZone());
    }

    /** The clock is a parameter so that the tests can fix the date of the registrations and discharges. */
    public PatientRegistryService(IPatientRepository patientRepository, Clock clock) {
        this.patientRepository = patientRepository;
        this.clock = clock;
    }

    @Override
    public PatientDto register(PatientRegistrationRequest request) throws PatientRegistryException {
        if (isBlank(request.nationalId()) || isBlank(request.healthCardNumber())) {
            throw new PatientRegistryException(PatientRegistryFailureReason.MISSING_DATA);
        }
        checkPersonalData(request.firstName(), request.lastName(), request.birthDate());
        String nationalId = normalizeNationalId(request.nationalId());
        if (findActive(nationalId) != null) {
            throw new PatientRegistryException(PatientRegistryFailureReason.DUPLICATED_NATIONAL_ID);
        }

        Patient patient = new Patient(patientRepository.nextId(), nationalId, request.healthCardNumber().trim(),
                request.firstName().trim(), request.lastName().trim(), request.birthDate(), sexOrDefault(request.sex()),
                emptyToNull(request.phone()), emptyToNull(request.email()), emptyToNull(request.address()),
                LocalDateTime.now(clock));
        patientRepository.save(patient);
        return PatientDto.from(patient);
    }

    @Override
    public PatientDto update(String patientId, PatientUpdateRequest request) throws PatientRegistryException {
        Patient patient = existingActivePatient(patientId);
        // everything is checked before changing anything, so a wrong request leaves the patient as it was
        checkPersonalData(request.firstName(), request.lastName(), request.birthDate());

        patient.setFirstName(request.firstName().trim());
        patient.setLastName(request.lastName().trim());
        patient.setBirthDate(request.birthDate());
        patient.setSex(sexOrDefault(request.sex()));
        patient.setPhone(emptyToNull(request.phone()));
        patient.setEmail(emptyToNull(request.email()));
        patient.setAddress(emptyToNull(request.address()));
        patientRepository.save(patient);
        return PatientDto.from(patient);
    }

    @Override
    public PatientDto discharge(String patientId) throws PatientRegistryException {
        Patient patient = existingActivePatient(patientId);
        patient.discharge(LocalDateTime.now(clock));
        patientRepository.save(patient);
        return PatientDto.from(patient);
    }

    @Override
    public PatientDto findById(String patientId) {
        Patient patient = patientRepository.findById(patientId);
        return patient == null ? null : PatientDto.from(patient);
    }

    @Override
    public PatientDto findByNationalId(String nationalId) {
        if (isBlank(nationalId)) {
            return null;
        }
        Patient patient = findActive(normalizeNationalId(nationalId));
        return patient == null ? null : PatientDto.from(patient);
    }

    @Override
    public List<PatientDto> getActivePatients() {
        return patientRepository.findAll().stream()
                .filter(Patient::isActive)
                .sorted(Comparator.comparing(Patient::getLastName).thenComparing(Patient::getFirstName))
                .map(PatientDto::from)
                .toList();
    }

    private Patient existingActivePatient(String patientId) throws PatientRegistryException {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new PatientRegistryException(PatientRegistryFailureReason.UNKNOWN_PATIENT);
        }
        if (!patient.isActive()) {
            throw new PatientRegistryException(PatientRegistryFailureReason.PATIENT_DISCHARGED);
        }
        return patient;
    }

    /** A discharged patient can be registered again, so only the active ones count as duplicates. */
    private Patient findActive(String nationalId) {
        for (Patient patient : patientRepository.findAll()) {
            if (patient.isActive() && patient.getNationalId().equals(nationalId)) {
                return patient;
            }
        }
        return null;
    }

    private void checkPersonalData(String firstName, String lastName, LocalDate birthDate)
            throws PatientRegistryException {
        if (isBlank(firstName) || isBlank(lastName) || birthDate == null) {
            throw new PatientRegistryException(PatientRegistryFailureReason.MISSING_DATA);
        }
        if (birthDate.isAfter(LocalDate.now(clock))) {
            throw new PatientRegistryException(PatientRegistryFailureReason.BIRTH_DATE_IN_THE_FUTURE);
        }
    }

    /** "12345678z" and " 12345678Z " are the same DNI. */
    private static String normalizeNationalId(String nationalId) {
        return nationalId.trim().toUpperCase();
    }

    private static Sex sexOrDefault(Sex sex) {
        return sex == null ? Sex.UNSPECIFIED : sex;
    }

    private static String emptyToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
