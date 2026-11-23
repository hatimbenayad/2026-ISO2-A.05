package es.uclm.esi.iso2.common.reports.domain;

import es.uclm.esi.iso2.common.reports.ReportStatus;
import java.time.LocalDateTime;

/** Diagnostic report linked to an examination order. */
public class DiagnosticReport {
    private final String reportId;
    private final String orderId;
    private final String patientId;
    private final String physicianId;
    private final LocalDateTime createdAt;
    private String findings;
    private String impression;
    private ReportStatus status;
    private LocalDateTime finalizedAt;

    public DiagnosticReport(String reportId, String orderId, String patientId, String physicianId,
            String findings, String impression, LocalDateTime createdAt) {
        this.reportId = reportId;
        this.orderId = orderId;
        this.patientId = patientId;
        this.physicianId = physicianId;
        this.findings = findings;
        this.impression = impression;
        this.createdAt = createdAt;
        this.status = ReportStatus.DRAFT;
    }

    public String getReportId() {
        return reportId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPhysicianId() {
        return physicianId;
    }

    public String getFindings() {
        return findings;
    }

    public String getImpression() {
        return impression;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinalizedAt() {
        return finalizedAt;
    }

    public void update(String findings, String impression) {
        if (status != ReportStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify a finalized report");
        }
        this.findings = findings;
        this.impression = impression;
    }

    public void finalizeReport(LocalDateTime when) {
        if (status != ReportStatus.DRAFT) {
            throw new IllegalStateException("Cannot sign an already finalized report");
        }
        status = ReportStatus.FINALIZED;
        finalizedAt = when;
    }

    public void archive() {
        if (status == ReportStatus.DRAFT) {
            throw new IllegalStateException("Cannot archive a draft report");
        }
        if (status == ReportStatus.ARCHIVED) {
            throw new IllegalStateException("Report is already archived");
        }
        status = ReportStatus.ARCHIVED;
    }
}
