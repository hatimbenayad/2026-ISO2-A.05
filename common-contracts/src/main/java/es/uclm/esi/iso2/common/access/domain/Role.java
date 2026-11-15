package es.uclm.esi.iso2.common.access.domain;

/** The four kinds of user of the application, one for each human actor of the client diagram (A01-A04). */
public enum Role {
    PATIENT("Patient"),
    PHYSICIAN("Physician"),
    ADMINISTRATIVE_STAFF("Administrative / admissions staff"),
    SYSTEM_ADMINISTRATOR("System administrator");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

     /**
     * Role hierarchy and privilege inclusion (D-007, D-018).
     * <p>
     * A user with the {@code PHYSICIAN} role also includes {@code PATIENT} capabilities,
     * reflecting the client use case diagram actor generalization (Physician ──▷ Patient).
     * <b>Domain constraint:</b> In clinical workflows, this allows physicians to access patient-level
     * self-service features for their own personal care as an individual patient, but does not permit
     * bypassing administrative patient-registration workflows.
     * </p>
     *
     * @param other the target role being checked
     * @return {@code true} if this role grants the privileges of {@code other}
     */
    public boolean includes(Role other) {
        return this == other || (this == PHYSICIAN && other == PATIENT);
    }
}
