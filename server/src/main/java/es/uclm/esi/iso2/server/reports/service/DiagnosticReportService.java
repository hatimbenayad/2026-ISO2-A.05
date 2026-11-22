package es.uclm.esi.iso2.server.reports.service;

import es.uclm.esi.iso2.common.reports.ClinicalIncidentDto;
import es.uclm.esi.iso2.common.reports.DiagnosticReportDto;
import es.uclm.esi.iso2.common.reports.ReportStatus;
import es.uclm.esi.iso2.common.reports.UnauthorizedSignatureException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DiagnosticReportService {
    private final ConcurrentMap<String, DiagnosticReportDto> reports = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<ClinicalIncidentDto>> incidents = new ConcurrentHashMap<>();
    private final Set<String> emergencyPatients = ConcurrentHashMap.newKeySet();
    private final AtomicInteger reportSequence = new AtomicInteger();

    public DiagnosticReportDto createDraft(String orderId, String patientId, String physicianId,
            String findings, String impression) {
        validateRequired(orderId, "Order ID");
        validateRequired(patientId, "Patient ID");
        validateRequired(physicianId, "Physician ID");
        validateRequired(findings, "Findings");
        validateRequired(impression, "Impression");

        String reportId = "REPORT-" + reportSequence.incrementAndGet();
        DiagnosticReportDto report = new DiagnosticReportDto(reportId, orderId.trim(), patientId.trim(),
                physicianId.trim(), findings.trim(), impression.trim(), ReportStatus.DRAFT, null);
        reports.put(reportId, report);
        return report;
    }

    public DiagnosticReportDto createDraft(String orderId, String patientId, String physicianId, String findings) {
        validateRequired(orderId, "Order ID");
        validateRequired(patientId, "Patient ID");
        validateRequired(physicianId, "Physician ID");
        validateRequired(findings, "Findings");
        String reportId = "REPORT-" + reportSequence.incrementAndGet();
        DiagnosticReportDto report = new DiagnosticReportDto(reportId, orderId.trim(), patientId.trim(),
                physicianId.trim(), findings.trim(), "", ReportStatus.DRAFT, null);
        reports.put(reportId, report);
        return report;
    }

    public DiagnosticReportDto updateFindings(String reportId, String newFindings, String newImpression) {
        validateRequired(reportId, "Report ID");
        validateRequired(newFindings, "Findings");
        validateRequired(newImpression, "Impression");
        DiagnosticReportDto report = requireReport(reportId);
        if (report.status() == ReportStatus.REPORTED || report.status() == ReportStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot modify a finalized report");
        }
        DiagnosticReportDto updated = new DiagnosticReportDto(report.reportId(), report.orderId(),
                report.patientId(), report.physicianId(), newFindings.trim(), newImpression.trim(),
                report.status(), report.signedAt());
        reports.put(reportId, updated);
        return updated;
    }

    public DiagnosticReportDto signReport(String reportId, String physicianId, String signatureToken) {
        validateRequired(reportId, "Report ID");
        DiagnosticReportDto report = requireReport(reportId);
        if (report.status() == ReportStatus.REPORTED || report.status() == ReportStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot sign an already finalized report");
        }
        if (isBlank(physicianId) || isBlank(signatureToken)
                || !report.physicianId().equals(physicianId.trim())) {
            throw new UnauthorizedSignatureException("Invalid or unauthorized signature credentials");
        }
        DiagnosticReportDto signed = new DiagnosticReportDto(report.reportId(), report.orderId(),
                report.patientId(), report.physicianId(), report.findings(), report.impression(),
                ReportStatus.REPORTED, LocalDateTime.now());
        reports.put(reportId, signed);
        return signed;
    }

    public DiagnosticReportDto archiveReport(String reportId) {
        validateRequired(reportId, "Report ID");
        DiagnosticReportDto report = requireReport(reportId);
        if (report.status() == ReportStatus.ARCHIVED) {
            throw new IllegalStateException("Report is already archived");
        }
        if (report.status() == ReportStatus.DRAFT) {
            throw new IllegalStateException("Cannot archive a draft report");
        }
        DiagnosticReportDto archived = new DiagnosticReportDto(report.reportId(), report.orderId(),
                report.patientId(), report.physicianId(), report.findings(), report.impression(),
                ReportStatus.ARCHIVED, report.signedAt());
        reports.put(reportId, archived);
        return archived;
    }

    public void logClinicalIncident(String patientId, ClinicalIncidentDto incident) {
        validateRequired(patientId, "Patient ID");
        if (incident == null) {
            throw new IllegalArgumentException("Incident cannot be null");
        }
        validateRequired(incident.patientId(), "Incident patient ID");
        validateRequired(incident.description(), "Incident description");
        validateRequired(incident.incidentId(), "Incident ID");
        if (!patientId.trim().equals(incident.patientId().trim())) {
            throw new IllegalArgumentException("Incident patient ID does not match patient ID");
        }

        String normalizedPatientId = patientId.trim();
        incidents.computeIfAbsent(normalizedPatientId, key -> new CopyOnWriteArrayList<>()).add(incident);
        if (incident.severeAdverseReaction()) {
            emergencyPatients.add(normalizedPatientId);
        }
    }

    public boolean hasEmergencyAlert(String patientId) {
        validateRequired(patientId, "Patient ID");
        return emergencyPatients.contains(patientId.trim());
    }

    public List<ClinicalIncidentDto> getPatientIncidents(String patientId) {
        validateRequired(patientId, "Patient ID");
        return List.copyOf(incidents.getOrDefault(patientId.trim(), List.of()));
    }

    public DiagnosticReportDto getReport(String reportId) {
        validateRequired(reportId, "Report ID");
        return requireReport(reportId);
    }

    private DiagnosticReportDto requireReport(String reportId) {
        DiagnosticReportDto report = reports.get(reportId.trim());
        if (report == null) {
            throw new NoSuchElementException("Report not found: " + reportId);
        }
        return report;
    }

    private static void validateRequired(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
