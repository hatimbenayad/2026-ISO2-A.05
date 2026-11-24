package es.uclm.esi.iso2.server.reports.persistence;

import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import java.util.List;

/** Storage of diagnostic reports (CU14). There is no delete operation (D-015). */
public interface IReportRepository {
    /** {@code null} when no report was stored with that id. */
    DiagnosticReport findById(String reportId);

    /** {@code null} when no report was stored for that order id. */
    DiagnosticReport findByOrderId(String orderId);

    /**
     * The reports of the patient, sorted by report id so the result is always in the same order.
     * Empty when the patient has none.
     */
    List<DiagnosticReport> findByPatientId(String patientId);

    /**
     * Stores a new report.
     *
     * @throws IllegalArgumentException if a report with the same id is already stored
     */
    void save(DiagnosticReport report);

    /**
     * Replaces the stored report that has the same id.
     *
     * @throws IllegalArgumentException if no report with that id is stored
     */
    void update(DiagnosticReport report);
}
