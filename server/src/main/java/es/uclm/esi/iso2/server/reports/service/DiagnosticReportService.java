package es.uclm.esi.iso2.server.reports.service;

import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.reports.ClinicalIncidentDto;
import es.uclm.esi.iso2.common.reports.DiagnosticReportDto;
import es.uclm.esi.iso2.common.reports.ReportStatus;
import es.uclm.esi.iso2.common.reports.UnauthorizedSignatureException;
import es.uclm.esi.iso2.common.reports.contracts.IDiagnosticReportService;
import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import es.uclm.esi.iso2.server.orders.persistence.IOrderRepository;
import es.uclm.esi.iso2.server.reports.persistence.IReportRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DiagnosticReportService implements IDiagnosticReportService {
    private final IReportRepository reportRepository;
    private final IOrderRepository orderRepository;
    private final Clock clock;
    private final ConcurrentMap<String, List<ClinicalIncidentDto>> incidents = new ConcurrentHashMap<>();
    private final Set<String> emergencyPatients = ConcurrentHashMap.newKeySet();
    private final AtomicInteger reportSequence = new AtomicInteger();

    public DiagnosticReportService(IReportRepository reportRepository, IOrderRepository orderRepository, Clock clock) {
        this.reportRepository = Objects.requireNonNull(reportRepository, "reportRepository");
        this.orderRepository = Objects.requireNonNull(orderRepository, "orderRepository");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public DiagnosticReportDto createReport(String orderId, String findings, String impression) {
        validateRequired(orderId, "Order ID");
        validateRequired(findings, "Findings");
        validateRequired(impression, "Impression");

        String normalizedOrderId = orderId.trim();
        ExaminationOrder order = orderRepository.findById(normalizedOrderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found: " + orderId);
        }
        if (order.getOrderStatus() != OrderStatus.PERFORMED) {
            throw new IllegalStateException("Only performed orders can be reported");
        }
        if (reportRepository.findByOrderId(normalizedOrderId) != null) {
            throw new IllegalStateException("The order already has a report");
        }

        String reportId = "REPORT-" + reportSequence.incrementAndGet();
        DiagnosticReport report = new DiagnosticReport(reportId, normalizedOrderId, order.getPatientId(),
                order.getPhysicianId(), findings.trim(), impression.trim(), LocalDateTime.now(clock));
        reportRepository.save(report);
        return DiagnosticReportDto.from(report);
    }

    @Override
    public DiagnosticReportDto updateReport(String reportId, String findings, String impression) {
        validateRequired(reportId, "Report ID");
        validateRequired(findings, "Findings");
        validateRequired(impression, "Impression");

        DiagnosticReport report = requireReport(reportId);
        report.update(findings.trim(), impression.trim());
        reportRepository.update(report);
        return DiagnosticReportDto.from(report);
    }

    @Override
    public DiagnosticReportDto finalizeReport(String reportId, String physicianSignatureToken) {
        validateRequired(reportId, "Report ID");
        DiagnosticReport report = requireReport(reportId);
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new IllegalStateException("Cannot sign an already finalized report");
        }
        if (isBlank(physicianSignatureToken)) {
            throw new UnauthorizedSignatureException("Invalid or unauthorized signature credentials");
        }

        ExaminationOrder order = orderRepository.findById(report.getOrderId());
        if (order == null) {
            throw new NoSuchElementException("Order not found: " + report.getOrderId());
        }
        order.setOrderStatus(OrderStatus.REPORTED);

        report.finalizeReport(LocalDateTime.now(clock));
        reportRepository.update(report);
        orderRepository.update(order);
        return DiagnosticReportDto.from(report);
    }

    public DiagnosticReportDto archiveReport(String reportId) {
        validateRequired(reportId, "Report ID");
        DiagnosticReport report = requireReport(reportId);
        report.archive();
        reportRepository.update(report);
        return DiagnosticReportDto.from(report);
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
        return DiagnosticReportDto.from(requireReport(reportId));
    }

    private DiagnosticReport requireReport(String reportId) {
        DiagnosticReport report = reportRepository.findById(reportId.trim());
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
