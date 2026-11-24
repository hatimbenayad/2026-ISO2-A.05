package es.uclm.esi.iso2.server.adapters.ehr;

import java.util.List;

/** Provides access to the clinical history stored in the electronic health record (E02). */
public interface IEhrAdapter {

    /**
     * Gets the clinical history of the patient, sorted by date.
     *
     * @param patientId the patient identifier
     * @return the patient's episodes, or an empty list when there is no history
     * @throws IllegalArgumentException if the patient id is null or blank
     */
    List<ClinicalEpisode> fetchEpisodes(String patientId);
}
