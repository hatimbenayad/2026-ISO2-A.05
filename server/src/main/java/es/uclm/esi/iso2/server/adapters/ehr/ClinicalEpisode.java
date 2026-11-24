package es.uclm.esi.iso2.server.adapters.ehr;

import java.time.LocalDate;
import java.util.Objects;

/** A synthetic clinical episode in a patient's electronic health record. */
public record ClinicalEpisode(
        String episodeId,
        String patientId,
        LocalDate date,
        String diagnosis,
        String treatment) {

    /**
     * Creates a clinical episode.
     *
     * @param episodeId the episode identifier
     * @param patientId the patient identifier
     * @param date the episode date
     * @param diagnosis the diagnosis
     * @param treatment the prescribed treatment
     * @throws NullPointerException if any field is null
     */
    public ClinicalEpisode {
        Objects.requireNonNull(episodeId, "episodeId");
        Objects.requireNonNull(patientId, "patientId");
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(diagnosis, "diagnosis");
        Objects.requireNonNull(treatment, "treatment");
    }
}
