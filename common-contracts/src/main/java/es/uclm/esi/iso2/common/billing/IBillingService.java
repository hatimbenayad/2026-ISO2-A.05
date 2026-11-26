package es.uclm.esi.iso2.common.billing;

import java.math.BigDecimal;

public interface IBillingService {
    BigDecimal calculateTariff(String procedureCode, boolean isUrgent);
    InvoiceDto generateInvoice(String patientCip, String orderNumber, String policyNumber);
    InsuranceAuthorizationDto verifyInsurance(String policyNumber, String procedureCode);
    AdmissionIncidentDto recordAdmissionIncident(String admissionId, String description, String reportedBy);
}
