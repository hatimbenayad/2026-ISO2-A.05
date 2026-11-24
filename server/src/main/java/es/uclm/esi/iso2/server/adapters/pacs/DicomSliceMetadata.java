package es.uclm.esi.iso2.server.adapters.pacs;

import java.time.LocalDateTime;
import java.util.Objects;

/** Metadata for one slice in a synthetic DICOM study. */
public record DicomSliceMetadata(
        String studyInstanceUid,
        String seriesInstanceUid,
        String sopInstanceUid,
        String modality,
        int sliceNumber,
        LocalDateTime acquiredAt) {

    /**
     * Creates DICOM slice metadata.
     *
     * @param studyInstanceUid the study instance UID
     * @param seriesInstanceUid the series instance UID
     * @param sopInstanceUid the SOP instance UID
     * @param modality the imaging modality
     * @param sliceNumber the one-based slice number
     * @param acquiredAt the acquisition date and time
     * @throws NullPointerException if any object field is null
     * @throws IllegalArgumentException if the slice number is less than one
     */
    public DicomSliceMetadata {
        Objects.requireNonNull(studyInstanceUid, "studyInstanceUid");
        Objects.requireNonNull(seriesInstanceUid, "seriesInstanceUid");
        Objects.requireNonNull(sopInstanceUid, "sopInstanceUid");
        Objects.requireNonNull(modality, "modality");
        Objects.requireNonNull(acquiredAt, "acquiredAt");
        if (sliceNumber < 1) {
            throw new IllegalArgumentException("The slice number starts at 1");
        }
    }
}
