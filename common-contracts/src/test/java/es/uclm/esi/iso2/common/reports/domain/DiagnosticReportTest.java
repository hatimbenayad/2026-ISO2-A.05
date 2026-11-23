package es.uclm.esi.iso2.common.reports.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDateTime;
import es.uclm.esi.iso2.common.reports.ReportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link DiagnosticReport}: its lifecycle and immutable finalized state. */
@DisplayName("DiagnosticReport")
class DiagnosticReportTest {
    private static final LocalDateTime CREATED = LocalDateTime.of(2026, 11, 23, 10, 0);
    private static final LocalDateTime FINALIZED = LocalDateTime.of(2026, 11, 23, 11, 0);

    private DiagnosticReport report;

    @BeforeEach
    void setUp() {
        report = new DiagnosticReport("REP-0001", "ORD-0001", "PAT-0001", "DOC-0001",
                "Initial findings", "Initial impression", CREATED);
    }

    @Test
    @DisplayName("A new report starts as a draft")
    void newReportStartsAsDraft() {
        assertAll(
                () -> assertEquals(ReportStatus.DRAFT, report.getStatus()),
                () -> assertEquals(CREATED, report.getCreatedAt()),
                () -> assertNull(report.getFinalizedAt()));
    }

    @Test
    @DisplayName("Draft findings and impression can be updated")
    void draftCanBeUpdated() {
        report.update("Updated findings", "Updated impression");

        assertAll(
                () -> assertEquals("Updated findings", report.getFindings()),
                () -> assertEquals("Updated impression", report.getImpression()));
    }

    @Test
    @DisplayName("A finalized report cannot be updated")
    void finalizedReportCannotBeUpdated() {
        report.finalizeReport(FINALIZED);

        assertThrows(IllegalStateException.class, () -> report.update("Changed", "Changed"));
    }

    @Test
    @DisplayName("Finalizing a report sets its status and date")
    void finalizeSetsStatusAndDate() {
        report.finalizeReport(FINALIZED);

        assertAll(
                () -> assertEquals(ReportStatus.FINALIZED, report.getStatus()),
                () -> assertEquals(FINALIZED, report.getFinalizedAt()));
    }

    @Test
    @DisplayName("A report cannot be finalized twice")
    void reportCannotBeFinalizedTwice() {
        report.finalizeReport(FINALIZED);

        assertThrows(IllegalStateException.class, () -> report.finalizeReport(FINALIZED.plusHours(1)));
    }

    @Test
    @DisplayName("A draft report cannot be archived")
    void draftCannotBeArchived() {
        assertThrows(IllegalStateException.class, () -> report.archive());
    }

    @Test
    @DisplayName("A finalized report can be archived")
    void finalizedReportCanBeArchived() {
        report.finalizeReport(FINALIZED);
        report.archive();

        assertEquals(ReportStatus.ARCHIVED, report.getStatus());
    }

    @Test
    @DisplayName("An archived report cannot be archived twice")
    void archivedReportCannotBeArchivedTwice() {
        report.finalizeReport(FINALIZED);
        report.archive();

        assertThrows(IllegalStateException.class, () -> report.archive());
    }
}
