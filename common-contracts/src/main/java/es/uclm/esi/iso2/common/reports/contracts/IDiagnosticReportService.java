package es.uclm.esi.iso2.common.reports.contracts;

import es.uclm.esi.iso2.common.reports.DiagnosticReportDto;
import es.uclm.esi.iso2.common.reports.UnauthorizedSignatureException;
import java.util.NoSuchElementException;

/** Diagnostic report operations exposed to clients. */
public interface IDiagnosticReportService {
    /**
     * Creates a DRAFT report linked to an existing PERFORMED examination order.
     * There can be at most one report per order.
     *
     * @throws IllegalArgumentException if an input is null or blank
     * @throws NoSuchElementException if the order is unknown
     * @throws IllegalStateException if the order is not PERFORMED or already has a report
     */
    DiagnosticReportDto createReport(String orderId, String findings, String impression);

    /**
     * Updates a DRAFT report.
     *
     * @throws IllegalArgumentException if an input is null or blank
     * @throws NoSuchElementException if the report is unknown
     * @throws IllegalStateException if the report is not DRAFT
     */
    DiagnosticReportDto updateReport(String reportId, String findings, String impression);

    /**
     * Validates the signature token, finalizes the report and marks its order REPORTED.
     *
     * @throws IllegalArgumentException if the report id is null or blank
     * @throws NoSuchElementException if the report is unknown
     * @throws IllegalStateException if the report or order has the wrong status, or the order is already reported
     * @throws UnauthorizedSignatureException if the signature token is missing
     */
    DiagnosticReportDto finalizeReport(String reportId, String physicianSignatureToken);
}
