package es.uclm.esi.iso2.common.patients.contracts;

public class PatientRegistryException extends Exception {
    private final PatientRegistryFailureReason reason;

    public PatientRegistryException(PatientRegistryFailureReason reason) {
        super(reason.name());
        this.reason = reason;
    }

    public PatientRegistryFailureReason getReason() {
        return reason;
    }
}
