package es.uclm.esi.iso2.common.reports;

import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import java.time.LocalDateTime;

public record DiagnosticReportDto(String reportId, String orderId, String patientId, String physicianId,
        String findings, String impression, ReportStatus status, LocalDateTime signedAt) {
    public static DiagnosticReportDto from(DiagnosticReport report) {
        return new DiagnosticReportDto(report.getReportId(), report.getOrderId(), report.getPatientId(),
                report.getPhysicianId(), report.getFindings(), report.getImpression(), report.getStatus(),
                report.getFinalizedAt());
    }
}
