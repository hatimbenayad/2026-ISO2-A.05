package es.uclm.esi.iso2.server.pacs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import es.uclm.esi.iso2.common.pacs.DicomSeriesDto;
import es.uclm.esi.iso2.common.pacs.StudyNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PacsIntegrationService (black box)")
class PacsIntegrationServiceTest {
    private PacsIntegrationService service;

    @BeforeEach
    void setUp() {
        service = new PacsIntegrationService();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.2.840.113619.2.55", "STUDY-1.2.840.10008"})
    @DisplayName("EP-PACS-01: valid study UID returns a non-empty deterministic series list")
    void testValidStudyUidReturnsDicomSeries(String studyUid) {
        List<DicomSeriesDto> result = service.getDicomSeriesByStudyUid(studyUid);

        assertFalse(result.isEmpty());
        assertEquals(studyUid, result.get(0).studyInstanceUid());
    }

    @Test
    @DisplayName("EP-PACS-02: unknown study UID throws StudyNotFoundException")
    void testNonExistentStudyUidThrowsStudyNotFoundException() {
        StudyNotFoundException exception = assertThrows(StudyNotFoundException.class,
                () -> service.getDicomSeriesByStudyUid("NON_EXISTENT_UID"));

        assertEquals("Study not found in PACS archive: NON_EXISTENT_UID", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("EP-PACS-03: null or empty study UID throws IllegalArgumentException")
    void testNullOrEmptyStudyUidThrowsIllegalArgumentException(String studyUid) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getDicomSeriesByStudyUid(studyUid));

        assertEquals("Study UID cannot be null or blank", exception.getMessage());
    }
}
