package es.uclm.esi.iso2.common.reports;

import java.time.LocalDateTime;

public record DiagnosticReportDto(String reportId, String orderId, String patientId, String physicianId,
        String findings, String impression, ReportStatus status, LocalDateTime signedAt) {
}
