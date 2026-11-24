package es.uclm.esi.iso2.common.reports;

public record ClinicalIncidentDto(String incidentId, String patientId, String description,
        boolean severeAdverseReaction) {
}
