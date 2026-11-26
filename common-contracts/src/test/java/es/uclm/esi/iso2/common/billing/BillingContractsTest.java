package es.uclm.esi.iso2.common.billing;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Billing Contracts Tests")
class BillingContractsTest {

    @Test
    void testInvoiceStatusEnum() {
        assertEquals(5, InvoiceStatus.values().length);
        assertEquals(InvoiceStatus.PAID, InvoiceStatus.valueOf("PAID"));
    }

    @Test
    void testTariffDto() {
        TariffDto dto = new TariffDto("PROC-01", "MRI", new BigDecimal("150.00"), new BigDecimal("1.50"));
        assertAll(
                () -> assertEquals("PROC-01", dto.procedureCode()),
                () -> assertEquals("MRI", dto.modality()),
                () -> assertEquals(new BigDecimal("150.00"), dto.baseRate()),
                () -> assertEquals(new BigDecimal("1.50"), dto.urgentMultiplier())
        );

        assertThrows(NullPointerException.class, () -> new TariffDto(null, "MRI", BigDecimal.TEN, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> new TariffDto("P1", "MRI", new BigDecimal("-10"), BigDecimal.ONE));
    }

    @Test
    void testInvoiceDto() {
        LocalDateTime now = LocalDateTime.now();
        InvoiceDto dto = new InvoiceDto("INV-1", "CIP123", "ORD-1", new BigDecimal("225.00"), InvoiceStatus.ISSUED, now, "POL-88");

        assertAll(
                () -> assertEquals("INV-1", dto.invoiceId()),
                () -> assertEquals("CIP123", dto.patientCip()),
                () -> assertEquals("ORD-1", dto.orderNumber()),
                () -> assertEquals(new BigDecimal("225.00"), dto.totalAmount()),
                () -> assertEquals(InvoiceStatus.ISSUED, dto.status()),
                () -> assertEquals(now, dto.issueDate()),
                () -> assertEquals("POL-88", dto.insurancePolicyNumber())
        );

        assertThrows(NullPointerException.class, () -> new InvoiceDto(null, "CIP", "ORD", BigDecimal.TEN, InvoiceStatus.DRAFT, now, null));
    }

    @Test
    void testInsuranceAuthorizationDto() {
        InsuranceAuthorizationDto dto = new InsuranceAuthorizationDto("AUTH-1", "POL-88", "Adeslas", true, new BigDecimal("80.00"), "APPR-99");
        assertAll(
                () -> assertEquals("AUTH-1", dto.authId()),
                () -> assertEquals("POL-88", dto.policyNumber()),
                () -> assertEquals("Adeslas", dto.insurerName()),
                () -> assertTrue(dto.isApproved()),
                () -> assertEquals(new BigDecimal("80.00"), dto.coveragePercentage()),
                () -> assertEquals("APPR-99", dto.approvalCode())
        );

        InsuranceAuthorizationDto defaultCov = new InsuranceAuthorizationDto("AUTH-2", "POL-99", "Sanitas", false, null, null);
        assertEquals(BigDecimal.ZERO, defaultCov.coveragePercentage());
        assertThrows(NullPointerException.class, () -> new InsuranceAuthorizationDto(null, "P", "I", false, null, null));
    }

    @Test
    void testAdmissionIncidentDto() {
        LocalDateTime now = LocalDateTime.now();
        AdmissionIncidentDto dto = new AdmissionIncidentDto("INC-1", "ADM-1", "Machine delay 30m", now, "technician-1");
        assertAll(
                () -> assertEquals("INC-1", dto.incidentId()),
                () -> assertEquals("ADM-1", dto.admissionId()),
                () -> assertEquals("Machine delay 30m", dto.description()),
                () -> assertEquals(now, dto.recordedAt()),
                () -> assertEquals("technician-1", dto.reportedBy())
        );

        assertThrows(NullPointerException.class, () -> new AdmissionIncidentDto(null, "A", "D", now, "T"));
    }
}
