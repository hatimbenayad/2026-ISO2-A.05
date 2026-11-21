package es.uclm.esi.iso2.server.pacs.service;

import es.uclm.esi.iso2.common.pacs.DicomSeriesDto;
import es.uclm.esi.iso2.common.pacs.StudyNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PacsIntegrationService {
    private static final String PRIMARY_STUDY = "STUDY-1.2.840.10008";
    private static final String SECONDARY_STUDY = "1.2.840.113619.2.55";

    private final Map<String, List<DicomSeriesDto>> archive = new LinkedHashMap<>();

    public PacsIntegrationService() {
        archive.put(PRIMARY_STUDY, List.of(
                new DicomSeriesDto("SERIES-CT-001", "CT", 120, PRIMARY_STUDY),
                new DicomSeriesDto("SERIES-CT-002", "CT", 80, PRIMARY_STUDY)));
        archive.put(SECONDARY_STUDY, List.of(
                new DicomSeriesDto("SERIES-MR-001", "MR", 96, SECONDARY_STUDY),
                new DicomSeriesDto("SERIES-MR-002", "MR", 64, SECONDARY_STUDY)));
    }

    public List<DicomSeriesDto> getDicomSeriesByStudyUid(String studyInstanceUid) {
        if (studyInstanceUid == null || studyInstanceUid.isBlank()) {
            throw new IllegalArgumentException("Study UID cannot be null or blank");
        }
        List<DicomSeriesDto> series = archive.get(studyInstanceUid);
        if (series == null) {
            throw new StudyNotFoundException("Study not found in PACS archive: " + studyInstanceUid);
        }
        return series;
    }
}
