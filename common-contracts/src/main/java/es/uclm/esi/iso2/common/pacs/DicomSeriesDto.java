package es.uclm.esi.iso2.common.pacs;

public record DicomSeriesDto(String seriesInstanceUid, String modality, int numberOfInstances,
        String studyInstanceUid) {
}
