package es.uclm.esi.iso2.server.adapters.ehr;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/** Supplies fixed synthetic radiology-related episodes for patients. */
public class MockEhrAdapter implements IEhrAdapter {
    private final Map<String, List<ClinicalEpisode>> episodes;

    /** Creates an adapter initialized with fixed synthetic patient histories. */
    public MockEhrAdapter() {
        Map<String, List<ClinicalEpisode>> data = new LinkedHashMap<>();
        data.put("PAT-0001", List.of(
                new ClinicalEpisode("EP-0001", "PAT-0001", LocalDate.of(2025, 1, 15),
                        "Suspicion of pulmonary nodule", "Chest CT and respiratory follow-up"),
                new ClinicalEpisode("EP-0002", "PAT-0001", LocalDate.of(2025, 3, 4),
                        "Lumbar degenerative disc disease", "Lumbar MRI and physiotherapy"),
                new ClinicalEpisode("EP-0003", "PAT-0001", LocalDate.of(2025, 6, 20),
                        "Post-traumatic wrist fracture", "Wrist radiography and immobilization")));
        data.put("PAT-0002", List.of(
                new ClinicalEpisode("EP-0004", "PAT-0002", LocalDate.of(2025, 2, 10),
                        "Right knee meniscal tear", "Knee MRI and orthopedic assessment")));
        data.put("PAT-0003", List.of(
                new ClinicalEpisode("EP-0005", "PAT-0003", LocalDate.of(2025, 4, 8),
                        "Suspicious breast microcalcifications", "Mammography and biopsy follow-up"),
                new ClinicalEpisode("EP-0006", "PAT-0003", LocalDate.of(2025, 8, 12),
                        "Chronic sinusitis", "Sinus CT and ENT follow-up")));
        episodes = Map.copyOf(data);
    }

    /**
     * Gets the fixed synthetic episodes for a patient, sorted by date.
     *
     * @param patientId the patient identifier
     * @return an unmodifiable list of episodes, or an empty list for an unknown patient
     * @throws IllegalArgumentException if the patient id is null or blank
     */
    @Override
    public List<ClinicalEpisode> fetchEpisodes(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("The patient id is empty");
        }
        List<ClinicalEpisode> patientEpisodes = episodes.get(patientId);
        if (patientEpisodes == null) {
            return List.of();
        }
        return List.copyOf(patientEpisodes.stream()
                .sorted(Comparator.comparing(ClinicalEpisode::date))
                .toList());
    }
}
