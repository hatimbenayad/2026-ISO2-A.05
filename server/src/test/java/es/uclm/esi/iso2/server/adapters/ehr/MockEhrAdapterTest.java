package es.uclm.esi.iso2.server.adapters.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests of {@link MockEhrAdapter}: synthetic histories and validation. */
@DisplayName("MockEhrAdapter")
class MockEhrAdapterTest {
    private MockEhrAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MockEhrAdapter();
    }

    @Test
    @DisplayName("PAT-0001 gets three episodes sorted by date")
    void pat0001GetsThreeSortedEpisodes() {
        // Arrange
        String patientId = "PAT-0001";

        // Act
        List<ClinicalEpisode> result = adapter.fetchEpisodes(patientId);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(episode -> episode.patientId().equals(patientId)));
        assertEquals(
                result.stream().map(ClinicalEpisode::date).sorted().toList(),
                result.stream().map(ClinicalEpisode::date).toList());
    }

    @Test
    @DisplayName("PAT-0002 gets one episode and PAT-0003 gets two")
    void remainingPatientsGetTheirEpisodes() {
        // Act
        List<ClinicalEpisode> pat0002 = adapter.fetchEpisodes("PAT-0002");
        List<ClinicalEpisode> pat0003 = adapter.fetchEpisodes("PAT-0003");

        // Assert
        assertEquals(1, pat0002.size());
        assertEquals("PAT-0002", pat0002.get(0).patientId());
        assertEquals(2, pat0003.size());
        assertTrue(pat0003.stream().allMatch(episode -> episode.patientId().equals("PAT-0003")));
    }

    @Test
    @DisplayName("The same patient always gets the same episodes")
    void episodesAreDeterministicAcrossCallsAndAdapters() {
        // Arrange
        MockEhrAdapter anotherAdapter = new MockEhrAdapter();

        // Act
        List<ClinicalEpisode> firstCall = adapter.fetchEpisodes("PAT-0001");
        List<ClinicalEpisode> secondCall = adapter.fetchEpisodes("PAT-0001");
        List<ClinicalEpisode> otherAdapter = anotherAdapter.fetchEpisodes("PAT-0001");

        // Assert
        assertEquals(firstCall, secondCall);
        assertEquals(firstCall, otherAdapter);
    }

    @Test
    @DisplayName("An unknown patient gets an empty list")
    void unknownPatientGetsEmptyList() {
        // Act
        List<ClinicalEpisode> result = adapter.fetchEpisodes("PAT-9999");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("The returned episode list cannot be changed")
    void returnedListIsUnmodifiable() {
        // Arrange
        List<ClinicalEpisode> result = adapter.fetchEpisodes("PAT-0001");

        // Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> result.clear());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("A null or blank patient id is rejected")
    void invalidPatientIdsAreRejected(String patientId) {
        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> adapter.fetchEpisodes(patientId),
                "The patient id is empty");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    @DisplayName("A clinical episode rejects each null field")
    void clinicalEpisodeRejectsNullFields(int nullField) {
        // Arrange
        String episodeId = nullField == 0 ? null : "EP-0001";
        String patientId = nullField == 1 ? null : "PAT-0001";
        LocalDate date = nullField == 2 ? null : LocalDate.of(2025, 1, 15);
        String diagnosis = nullField == 3 ? null : "Suspicion of pulmonary nodule";
        String treatment = nullField == 4 ? null : "Chest CT";

        // Act and Assert
        assertThrows(NullPointerException.class,
                () -> new ClinicalEpisode(episodeId, patientId, date, diagnosis, treatment));
    }
}
