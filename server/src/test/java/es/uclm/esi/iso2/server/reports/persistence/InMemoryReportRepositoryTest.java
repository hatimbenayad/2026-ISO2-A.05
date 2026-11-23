package es.uclm.esi.iso2.server.reports.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import es.uclm.esi.iso2.common.reports.domain.DiagnosticReport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link InMemoryReportRepository}: save, update and the lookups. */
@DisplayName("InMemoryReportRepository")
class InMemoryReportRepositoryTest {
    private static final LocalDateTime CREATED = LocalDateTime.of(2026, 11, 23, 10, 0);

    private InMemoryReportRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
    }

    private static DiagnosticReport report(String id) {
        return report(id, "ORD-0001", "PAT-0001");
    }

    private static DiagnosticReport report(String id, String orderId, String patientId) {
        return new DiagnosticReport(id, orderId, patientId, "PHY-0001", "Findings", "Impression", CREATED);
    }

    private static List<String> ids(List<DiagnosticReport> reports) {
        return reports.stream().map(DiagnosticReport::getReportId).toList();
    }

    @Test
    @DisplayName("It starts empty")
    void startsEmpty() {
        assertAll(
                () -> assertNull(repository.findById("REP-0001")),
                () -> assertNull(repository.findByOrderId("ORD-0001")),
                () -> assertTrue(repository.findByPatientId("PAT-0001").isEmpty()));
    }

    @Test
    @DisplayName("A saved report can be found by its id")
    void savedReportIsFoundById() {
        DiagnosticReport saved = report("REP-0001");

        repository.save(saved);

        assertSame(saved, repository.findById("REP-0001"));
    }

    @Test
    @DisplayName("A saved report can be found by its order id")
    void savedReportIsFoundByOrderId() {
        DiagnosticReport saved = report("REP-0001", "ORD-0001", "PAT-0001");

        repository.save(saved);

        assertSame(saved, repository.findByOrderId("ORD-0001"));
    }

    @Test
    @DisplayName("Unknown and null ids give null")
    void unknownAndNullIdsGiveNull() {
        repository.save(report("REP-0001"));

        assertAll(
                () -> assertNull(repository.findById("REP-9999")),
                () -> assertNull(repository.findById("rep-0001")),
                () -> assertNull(repository.findById(null)),
                () -> assertNull(repository.findByOrderId("ORD-9999")),
                () -> assertNull(repository.findByOrderId(null)));
    }

    @Test
    @DisplayName("findByPatientId gives an empty list for a patient without reports or for null")
    void findByPatientIdWithoutReports() {
        repository.save(report("REP-0001"));

        assertAll(
                () -> assertTrue(repository.findByPatientId("PAT-9999").isEmpty()),
                () -> assertTrue(repository.findByPatientId(null).isEmpty()));
    }

    @Test
    @DisplayName("findByPatientId filters and sorts reports by report id")
    void findByPatientIdFiltersAndSorts() {
        repository.save(report("REP-0003", "ORD-0003", "PAT-0001"));
        repository.save(report("REP-0001", "ORD-0001", "PAT-0001"));
        repository.save(report("REP-0002", "ORD-0002", "PAT-0002"));

        assertAll(
                () -> assertEquals(List.of("REP-0001", "REP-0003"),
                        ids(repository.findByPatientId("PAT-0001"))),
                () -> assertEquals(List.of("REP-0002"), ids(repository.findByPatientId("PAT-0002"))));
    }

    @Test
    @DisplayName("An id that is already stored rejects duplicate save and keeps the first report")
    void saveRejectsDuplicatedId() {
        DiagnosticReport first = report("REP-0001");
        repository.save(first);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> repository.save(report("REP-0001"))),
                () -> assertSame(first, repository.findById("REP-0001")));
    }

    @Test
    @DisplayName("A report without id cannot be saved or updated")
    void reportWithoutIdIsRejected() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> repository.save(report(null))),
                () -> assertThrows(NullPointerException.class, () -> repository.update(report(null))));
    }

    @Test
    @DisplayName("Update replaces the stored report that has the same id")
    void updateReplacesStoredReport() {
        repository.save(report("REP-0001"));
        DiagnosticReport changed = report("REP-0001");
        changed.update("Updated findings", "Updated impression");

        repository.update(changed);

        assertAll(
                () -> assertSame(changed, repository.findById("REP-0001")),
                () -> assertEquals("Updated findings", repository.findById("REP-0001").getFindings()),
                () -> assertEquals(1, repository.findByPatientId("PAT-0001").size()));
    }

    @Test
    @DisplayName("Updating a report that was never saved is rejected")
    void updateRejectsUnknownReport() {
        assertThrows(IllegalArgumentException.class, () -> repository.update(report("REP-0001")));
    }
}
