package es.uclm.esi.iso2.common.patients.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Clinical data of a patient that the physician checks before ordering an examination: blood type, allergies
 * and chronic conditions. It comes from the EHR (E02) and points to its patient by {@code patientId}. A record
 * is deactivated, never deleted (D-015).
 */
public class MedicalRecord {
    private final String recordNumber;
    private final String patientId;
    private String bloodType;
    private List<String> allergies;
    private List<String> chronicalConditions;
    private boolean active;
    private LocalDateTime deactivatedAt;

    public MedicalRecord(String recordNumber, String patientId, String bloodType, List<String> allergies,
            List<String> chronicalConditions) {
        this.recordNumber = recordNumber;
        this.patientId = patientId;
        this.bloodType = bloodType;
        this.allergies = copyOrEmpty(allergies);
        this.chronicalConditions = copyOrEmpty(chronicalConditions);
        this.active = true;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    /** As the EHR writes it, for example "A+" or "0-". */
    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = copyOrEmpty(allergies);
    }

    public List<String> getChronicalConditions() {
        return chronicalConditions;
    }

    public void setChronicalConditions(List<String> chronicalConditions) {
        this.chronicalConditions = copyOrEmpty(chronicalConditions);
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getDeactivatedAt() {
        return deactivatedAt;
    }

    /** The record stays stored with all its clinical data; only the state changes (D-015). */
    public void deactivate(LocalDateTime when) {
        if (!active) {
            throw new IllegalStateException("The medical record " + recordNumber + " is already inactive");
        }
        active = false;
        deactivatedAt = when;
    }

    /** A missing list means that nothing is known yet, so it is kept as an empty one. */
    private static List<String> copyOrEmpty(List<String> values) {
        return values == null ? List.of() : List.copyOf(values);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MedicalRecord)) {
            return false;
        }
        return Objects.equals(recordNumber, ((MedicalRecord) other).recordNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordNumber);
    }
}
