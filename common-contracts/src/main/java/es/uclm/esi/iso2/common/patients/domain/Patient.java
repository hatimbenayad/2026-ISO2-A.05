package es.uclm.esi.iso2.common.patients.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Demographic data of a patient, registered by the administrative staff (CU19). It is the patient as a record,
 * not the Patient actor: a patient does not need a user account. National id and health card number identify
 * the patient, so they are not changed after the registration.
 */
public class Patient {
    private final String id;
    private final String nationalId;
    private final String healthCardNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Sex sex;
    private String phone;
    private String email;
    private String address;
    private PatientStatus status;
    private final LocalDateTime registeredAt;
    private LocalDateTime dischargedAt;

    /** Patient with only the mandatory data. */
    public Patient(String id, String nationalId, String healthCardNumber, String firstName, String lastName,
            LocalDate birthDate, LocalDateTime registeredAt) {
        this(id, nationalId, healthCardNumber, firstName, lastName, birthDate, Sex.UNSPECIFIED, null, null, null,
                registeredAt);
    }

    public Patient(String id, String nationalId, String healthCardNumber, String firstName, String lastName,
            LocalDate birthDate, Sex sex, String phone, String email, String address, LocalDateTime registeredAt) {
        this.id = id;
        this.nationalId = nationalId;
        this.healthCardNumber = healthCardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = PatientStatus.ACTIVE;
        this.registeredAt = registeredAt;
    }

    public String getId() {
        return id;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public boolean isActive() {
        return status == PatientStatus.ACTIVE;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getDischargedAt() {
        return dischargedAt;
    }

    /** RF19 "discharge": the patient is marked as inactive and nothing is deleted (D-015). */
    public void discharge(LocalDateTime when) {
        if (!isActive()) {
            throw new IllegalStateException("The patient " + id + " is already discharged");
        }
        status = PatientStatus.INACTIVE;
        dischargedAt = when;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        return Objects.equals(id, ((Patient) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
