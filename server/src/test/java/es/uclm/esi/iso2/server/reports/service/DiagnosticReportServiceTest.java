package es.uclm.esi.iso2.server.reports.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.uclm.esi.iso2.common.reports.ClinicalIncidentDto;
import es.uclm.esi.iso2.common.reports.DiagnosticReportDto;
import es.uclm.esi.iso2.common.reports.ReportStatus;
import es.uclm.esi.iso2.common.reports.UnauthorizedSignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("DiagnosticReportService (black box)")
class DiagnosticReportServiceTest {
    private DiagnosticReportService service;

    @BeforeEach
    void setUp() {
        service = new DiagnosticReportService();
    }

    @Test
    @DisplayName("EP-REP-01: valid draft report is created with DRAFT status")
    void testCreateValidDraftReport() {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Normal chest XR", "No acute disease");

        assertEquals("REPORT-1", report.reportId());
        assertEquals("Normal chest XR", report.findings());
        assertEquals("No acute disease", report.impression());
        assertEquals(ReportStatus.DRAFT, report.status());
        assertEquals(null, report.signedAt());
    }

    @Test
    @DisplayName("BVA-REP-02: findings and impression can be updated while draft")
    void testUpdateFindingsOnDraftSucceeds() {
        DiagnosticReportDto draft = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Initial finding", "Initial impression");

        DiagnosticReportDto updated = service.updateFindings(draft.reportId(),
                "Updated finding", "Updated impression");

        assertEquals("Updated finding", updated.findings());
        assertEquals("Updated impression", updated.impression());
        assertEquals(ReportStatus.DRAFT, updated.status());
    }

    @Test
    @DisplayName("BVA-REP-02: findings cannot be modified after reporting")
    void testUpdateFindingsOnReportedThrowsIllegalStateException() {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Finding", "Impression");
        service.signReport(report.reportId(), "DOC-1", "SIGN-TOKEN");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.updateFindings(report.reportId(), "Changed", "Changed"));

        assertEquals("Cannot modify a finalized report", exception.getMessage());
    }

    @Test
    @DisplayName("BVA-REP-01: valid signature transitions report to REPORTED")
    void testSignReportTransitionsToReported() {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Finding", "Impression");

        DiagnosticReportDto signed = service.signReport(report.reportId(), "DOC-1", "SIGN-TOKEN");

        assertEquals(ReportStatus.REPORTED, signed.status());
        assertNotNull(signed.signedAt());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("Signature rejects null, empty, and blank signature tokens")
    void testSignReportWithoutValidTokenThrowsUnauthorizedSignatureException(String token) {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Finding", "Impression");

        assertThrows(UnauthorizedSignatureException.class,
                () -> service.signReport(report.reportId(), "DOC-1", token));
    }

    @Test
    @DisplayName("Signature rejects a physician ID that does not match the report")
    void testSignReportWithMismatchedPhysicianThrowsUnauthorizedSignatureException() {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Finding", "Impression");

        assertThrows(UnauthorizedSignatureException.class,
                () -> service.signReport(report.reportId(), "OTHER-DOC", "SIGN-TOKEN"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("Signature rejects null, empty, and blank physician IDs")
    void testSignReportWithoutPhysicianThrowsUnauthorizedSignatureException(String physicianId) {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Finding", "Impression");

        assertThrows(UnauthorizedSignatureException.class,
                () -> service.signReport(report.reportId(), physicianId, "SIGN-TOKEN"));
    }

    @Test
    @DisplayName("Duplicate signature attempt rejects an already reported report")
    void testCannotSignAlreadyReportedOrArchivedReport() {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1",
                "Finding", "Impression");
        service.signReport(report.reportId(), "DOC-1", "SIGN-TOKEN");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.signReport(report.reportId(), "DOC-1", "SIGN-TOKEN-2"));

        assertEquals("Cannot sign an already finalized report", exception.getMessage());
    }

    @Test
    @DisplayName("EP-INC-01: severe contrast extravasation flags an emergency alert")
    void testLogSevereAdverseReactionFlagsEmergencyAlert() {
        ClinicalIncidentDto incident = new ClinicalIncidentDto(
                "INC-1", "PAT-1", "Severe contrast extravasation", true);

        service.logClinicalIncident("PAT-1", incident);

        assertTrue(service.hasEmergencyAlert("PAT-1"));
        assertEquals(incident, service.getPatientIncidents("PAT-1").get(0));
    }

    @Test
    @DisplayName("EP-INC-02: mild nausea is logged without an emergency alert")
    void testLogNonSevereIncidentDoesNotFlagEmergency() {
        ClinicalIncidentDto incident = new ClinicalIncidentDto(
                "INC-2", "PAT-2", "Mild nausea", false);

        service.logClinicalIncident("PAT-2", incident);

        assertFalse(service.hasEmergencyAlert("PAT-2"));
        assertEquals(1, service.getPatientIncidents("PAT-2").size());
        assertEquals(incident, service.getPatientIncidents("PAT-2").get(0));
    }

    @Test
    @DisplayName("Invalid draft input is rejected")
    void testCreateDraftInputValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createDraft(null, "PAT-1", "DOC-1", "Finding", "Impression"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createDraft("ORD-1", " ", "DOC-1", "Finding", "Impression"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createDraft("ORD-1", "PAT-1", null, "Finding", "Impression"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createDraft("ORD-1", "PAT-1", "DOC-1", "", "Impression"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createDraft("ORD-1", "PAT-1", "DOC-1", "Finding", " "));
    }

    @Test
    @DisplayName("Unknown report and invalid incident input are rejected")
    void testUnknownReportAndIncidentValidation() {
        assertThrows(java.util.NoSuchElementException.class,
                () -> service.updateFindings("UNKNOWN", "Finding", "Impression"));
        assertThrows(IllegalArgumentException.class,
                () -> service.logClinicalIncident("PAT-1", null));
        assertThrows(IllegalArgumentException.class,
                () -> service.logClinicalIncident("PAT-1",
                        new ClinicalIncidentDto("INC-1", "PAT-2", "Mismatch", false)));
    }

    @Test
    @DisplayName("The four-argument draft convenience overload creates a draft")
    void testCreateDraftConvenienceOverload() {
        DiagnosticReportDto report = service.createDraft("ORD-1", "PAT-1", "DOC-1", "Finding");

        assertEquals(ReportStatus.DRAFT, report.status());
        assertEquals("", report.impression());
    }
}
