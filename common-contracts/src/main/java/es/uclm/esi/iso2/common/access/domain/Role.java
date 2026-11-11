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
     * A physician can also do everything a patient can do (D-007). This is how the generalization of the
     * diagram is kept in the code: with the roles, not with a {@code Physician extends Patient} class.
     */
    public boolean includes(Role other) {
        return this == other || (this == PHYSICIAN && other == PATIENT);
    }
}
