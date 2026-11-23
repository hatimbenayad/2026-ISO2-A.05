package es.uclm.esi.iso2.server.reports.persistence;

import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps the diagnostic reports in memory until the team chooses the database (D-030): no SQL and no JPA, only a
 * thread-safe map.
 */
public class InMemoryReportRepository implements IReportRepository {
    private final ConcurrentHashMap<String, DiagnosticReport> reports = new ConcurrentHashMap<>();

    @Override
    public DiagnosticReport findById(String reportId) {
        return reportId == null ? null : reports.get(reportId);
    }

    @Override
    public DiagnosticReport findByOrderId(String orderId) {
        if (orderId == null) {
            return null;
        }
        return reports.values().stream()
                .filter(report -> orderId.equals(report.getOrderId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<DiagnosticReport> findByPatientId(String patientId) {
        if (patientId == null) {
            return List.of();
        }
        return reports.values().stream()
                .filter(report -> patientId.equals(report.getPatientId()))
                .sorted(Comparator.comparing(DiagnosticReport::getReportId))
                .toList();
    }

    @Override
    public void save(DiagnosticReport report) {
        String reportId = Objects.requireNonNull(report.getReportId(), "reportId");
        // putIfAbsent is atomic, so two threads saving the same id cannot both succeed
        if (reports.putIfAbsent(reportId, report) != null) {
            throw new IllegalArgumentException("The report " + reportId + " is already stored");
        }
    }

    @Override
    public void update(DiagnosticReport report) {
        String reportId = Objects.requireNonNull(report.getReportId(), "reportId");
        if (reports.replace(reportId, report) == null) {
            throw new IllegalArgumentException("The report " + reportId + " is not stored");
        }
    }
}
