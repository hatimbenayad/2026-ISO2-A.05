package es.uclm.esi.iso2.server.reports.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import es.uclm.esi.iso2.common.reports.ClinicalIncidentDto;
import es.uclm.esi.iso2.common.reports.DiagnosticReportDto;
import es.uclm.esi.iso2.common.reports.ReportStatus;
import es.uclm.esi.iso2.common.reports.UnauthorizedSignatureException;
import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import es.uclm.esi.iso2.server.orders.persistence.InMemoryOrderRepository;
import es.uclm.esi.iso2.server.reports.persistence.InMemoryReportRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("DiagnosticReportService (black box)")
class DiagnosticReportServiceTest {
    private InMemoryReportRepository reportRepository;
    private InMemoryOrderRepository orderRepository;
    private ExaminationOrder order;
    private Clock clock;
    private DiagnosticReportService service;

    @BeforeEach
    void setUp() {
        reportRepository = new InMemoryReportRepository();
        orderRepository = new InMemoryOrderRepository();
        order = new ExaminationOrder("ORD-1", "PAT-1", "DOC-1", "CT", "Chest pain", Priority.ROUTINE);
        order.setOrderStatus(OrderStatus.PERFORMED);
        orderRepository.save(order);
        clock = Clock.fixed(Instant.parse("2026-11-23T10:00:00Z"), ZoneOffset.UTC);
        service = new DiagnosticReportService(reportRepository, orderRepository, clock);
    }

    @Test
    @DisplayName("EP-REP-01: valid draft report is created with DRAFT status")
    void testCreateValidDraftReport() {
        DiagnosticReportDto report = service.createReport("ORD-1",
                "Normal chest XR", "No acute disease");

        assertAll(
                () -> assertEquals("REPORT-1", report.reportId()),
                () -> assertEquals("ORD-1", report.orderId()),
                () -> assertEquals("PAT-1", report.patientId()),
                () -> assertEquals("DOC-1", report.physicianId()),
                () -> assertEquals("Normal chest XR", report.findings()),
                () -> assertEquals("No acute disease", report.impression()),
                () -> assertEquals(ReportStatus.DRAFT, report.status()),
                () -> assertEquals(LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC),
                        reportRepository.findById(report.reportId()).getCreatedAt()),
                () -> assertNull(report.signedAt()));
    }

    @Test
    @DisplayName("createReport rejects an unknown order")
    void createReportRejectsUnknownOrder() {
        assertThrows(NoSuchElementException.class,
                () -> service.createReport("ORD-UNKNOWN", "Finding", "Impression"));
    }

    @Test
    @DisplayName("createReport rejects an order that is not PERFORMED")
    void createReportRejectsNonPerformedOrder() {
        ExaminationOrder ordered = new ExaminationOrder("ORD-2", "PAT-2", "DOC-2", "MRI",
                "Back pain", Priority.ROUTINE);
        orderRepository.save(ordered);

        assertThrows(IllegalStateException.class,
                () -> service.createReport("ORD-2", "Finding", "Impression"));
    }

    @Test
    @DisplayName("createReport rejects a second report for the same order")
    void createReportRejectsSecondReportForOrder() {
        service.createReport("ORD-1", "Finding", "Impression");

        assertThrows(IllegalStateException.class,
                () -> service.createReport("ORD-1", "Second finding", "Second impression"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("createReport rejects null, empty and blank order ids")
    void createReportRejectsInvalidOrderId(String orderId) {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReport(orderId, "Finding", "Impression"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("createReport rejects null, empty and blank findings")
    void createReportRejectsInvalidFindings(String findings) {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReport("ORD-1", findings, "Impression"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("createReport rejects null, empty and blank impressions")
    void createReportRejectsInvalidImpression(String impression) {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReport("ORD-1", "Finding", impression));
    }

    @Test
    @DisplayName("BVA-REP-02: findings and impression can be updated while draft")
    void testUpdateFindingsOnDraftSucceeds() {
        DiagnosticReportDto draft = service.createReport("ORD-1",
                "Initial finding", "Initial impression");

        DiagnosticReportDto updated = service.updateReport(draft.reportId(),
                "Updated finding", "Updated impression");

        assertAll(
                () -> assertEquals("Updated finding", updated.findings()),
                () -> assertEquals("Updated impression", updated.impression()),
                () -> assertEquals(ReportStatus.DRAFT, updated.status()));
    }

    @Test
    @DisplayName("BVA-REP-02: findings cannot be modified after reporting")
    void testUpdateFindingsOnReportedThrowsIllegalStateException() {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");
        service.finalizeReport(report.reportId(), "SIGN-TOKEN");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.updateReport(report.reportId(), "Changed", "Changed"));

        assertEquals("Cannot modify a finalized report", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("updateReport rejects null, empty and blank report ids")
    void updateReportRejectsInvalidReportId(String reportId) {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateReport(reportId, "Finding", "Impression"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("updateReport rejects null, empty and blank findings")
    void updateReportRejectsInvalidFindings(String findings) {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateReport(report.reportId(), findings, "Impression"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("updateReport rejects null, empty and blank impressions")
    void updateReportRejectsInvalidImpression(String impression) {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateReport(report.reportId(), "Finding", impression));
    }

    @Test
    @DisplayName("BVA-REP-01: valid signature transitions report to FINALIZED")
    void testSignReportTransitionsToReported() {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");

        DiagnosticReportDto signed = service.finalizeReport(report.reportId(), "SIGN-TOKEN");

        assertAll(
                () -> assertEquals(ReportStatus.FINALIZED, signed.status()),
                () -> assertEquals(LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC), signed.signedAt()),
                () -> assertEquals(OrderStatus.REPORTED, order.getOrderStatus()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("finalizeReport rejects null, empty and blank signature tokens")
    void testSignReportWithoutValidTokenThrowsUnauthorizedSignatureException(String token) {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");

        assertThrows(UnauthorizedSignatureException.class,
                () -> service.finalizeReport(report.reportId(), token));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("finalizeReport rejects null, empty and blank report ids")
    void finalizeReportRejectsInvalidReportId(String reportId) {
        assertThrows(IllegalArgumentException.class,
                () -> service.finalizeReport(reportId, "SIGN-TOKEN"));
    }

    @Test
    @DisplayName("Duplicate signature attempt rejects an already reported report")
    void testCannotSignAlreadyReportedOrArchivedReport() {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");
        service.finalizeReport(report.reportId(), "SIGN-TOKEN");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.finalizeReport(report.reportId(), "SIGN-TOKEN-2"));

        assertEquals("Cannot sign an already finalized report", exception.getMessage());
    }

    @Test
    @DisplayName("unknown report operations are rejected")
    void unknownReportOperationsAreRejected() {
        assertAll(
                () -> assertThrows(NoSuchElementException.class,
                        () -> service.updateReport("UNKNOWN", "Finding", "Impression")),
                () -> assertThrows(NoSuchElementException.class,
                        () -> service.finalizeReport("UNKNOWN", "SIGN-TOKEN")),
                () -> assertThrows(NoSuchElementException.class,
                        () -> service.getReport("UNKNOWN")),
                () -> assertThrows(NoSuchElementException.class,
                        () -> service.archiveReport("UNKNOWN")));
    }

    @Test
    @DisplayName("finalizeReport rejects a cancelled order without changing the draft")
    void finalizeReportRejectsCancelledOrder() {
        DiagnosticReportDto report = service.createReport("ORD-1", "Finding", "Impression");
        order.cancel(LocalDateTime.of(2026, 11, 23, 11, 0));

        assertThrows(IllegalStateException.class,
                () -> service.finalizeReport(report.reportId(), "SIGN-TOKEN"));
        assertEquals(ReportStatus.DRAFT, service.getReport(report.reportId()).status());
    }

    @Test
    @DisplayName("finalizeReport rejects a missing order without changing the draft")
    void finalizeReportRejectsMissingOrder() {
        DiagnosticReport report = new DiagnosticReport("REPORT-MISSING", "ORD-MISSING", "PAT-1", "DOC-1",
                "Finding", "Impression", LocalDateTime.now(clock));
        reportRepository.save(report);

        assertAll(
                () -> assertThrows(NoSuchElementException.class,
                        () -> service.finalizeReport("REPORT-MISSING", "SIGN-TOKEN")),
                () -> assertEquals(ReportStatus.DRAFT, service.getReport("REPORT-MISSING").status()));
    }

    @Test
    @DisplayName("getReport returns the stored report")
    void getReportReturnsStoredReport() {
        DiagnosticReportDto created = service.createReport("ORD-1", "Finding", "Impression");

        assertEquals(created, service.getReport(created.reportId()));
    }

    @Test
    @DisplayName("archiveReport rejects a draft and archives a finalized report once")
    void archiveReportLifecycle() {
        DiagnosticReportDto draft = service.createReport("ORD-1", "Finding", "Impression");

        assertThrows(IllegalStateException.class, () -> service.archiveReport(draft.reportId()));

        DiagnosticReportDto finalized = service.finalizeReport(draft.reportId(), "SIGN-TOKEN");
        DiagnosticReportDto archived = service.archiveReport(finalized.reportId());

        assertAll(
                () -> assertEquals(ReportStatus.ARCHIVED, archived.status()),
                () -> assertThrows(IllegalStateException.class,
                        () -> service.archiveReport(finalized.reportId())));
    }

    @Test
    @DisplayName("The constructor rejects each null dependency")
    void constructorRejectsNullDependencies() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> new DiagnosticReportService(null, orderRepository, clock)),
                () -> assertThrows(NullPointerException.class,
                        () -> new DiagnosticReportService(reportRepository, null, clock)),
                () -> assertThrows(NullPointerException.class,
                        () -> new DiagnosticReportService(reportRepository, orderRepository, null)));
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
    @DisplayName("Unknown report and invalid incident input are rejected")
    void testUnknownReportAndIncidentValidation() {
        assertAll(
                () -> assertThrows(NoSuchElementException.class,
                        () -> service.updateReport("UNKNOWN", "Finding", "Impression")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> service.logClinicalIncident("PAT-1", null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> service.logClinicalIncident("PAT-1",
                                new ClinicalIncidentDto("INC-1", "PAT-2", "Mismatch", false))));
    }
}
