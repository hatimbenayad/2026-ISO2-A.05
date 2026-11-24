package es.uclm.esi.iso2.common.patients.contracts;

public enum PatientRegistryFailureReason {
    /** National id, health card number, first name, last name or birth date is missing. */
    MISSING_DATA,
    BIRTH_DATE_IN_THE_FUTURE,
    /** There is already an active patient with the same national id. */
    DUPLICATED_NATIONAL_ID,
    UNKNOWN_PATIENT,
    /** A discharged patient cannot be modified or discharged again. */
    PATIENT_DISCHARGED
}
