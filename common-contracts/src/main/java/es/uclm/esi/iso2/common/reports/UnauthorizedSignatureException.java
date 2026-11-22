package es.uclm.esi.iso2.common.reports;

public class UnauthorizedSignatureException extends RuntimeException {
    public UnauthorizedSignatureException(String message) {
        super(message);
    }
}
