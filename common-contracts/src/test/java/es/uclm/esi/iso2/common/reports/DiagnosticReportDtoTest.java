package es.uclm.esi.iso2.common.reports;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link DiagnosticReportDto#from(DiagnosticReport)}. */
@DisplayName("DiagnosticReportDto")
class DiagnosticReportDtoTest {
    @Test
    @DisplayName("from copies every report field")
    void fromCopiesEveryField() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 11, 23, 10, 0);
        LocalDateTime finalizedAt = LocalDateTime.of(2026, 11, 23, 11, 0);
        DiagnosticReport report = new DiagnosticReport("REP-0001", "ORD-0001", "PAT-0001", "DOC-0001",
                "Findings", "Impression", createdAt);
        report.finalizeReport(finalizedAt);

        DiagnosticReportDto dto = DiagnosticReportDto.from(report);

        assertAll(
                () -> assertEquals("REP-0001", dto.reportId()),
                () -> assertEquals("ORD-0001", dto.orderId()),
                () -> assertEquals("PAT-0001", dto.patientId()),
                () -> assertEquals("DOC-0001", dto.physicianId()),
                () -> assertEquals("Findings", dto.findings()),
                () -> assertEquals("Impression", dto.impression()),
                () -> assertEquals(ReportStatus.FINALIZED, dto.status()),
                () -> assertEquals(finalizedAt, dto.signedAt()));
    }
}
