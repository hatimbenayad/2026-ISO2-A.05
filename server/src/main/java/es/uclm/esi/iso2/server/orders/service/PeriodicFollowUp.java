package es.uclm.esi.iso2.server.orders.service;

import es.uclm.esi.iso2.common.orders.domain.Priority;
import java.time.LocalDateTime;

/**
 * Periodic follow-up of a patient (CU11). Only the follow-up and its recurrence interval are stored here; the future
 * orders are generated later by the server Timer (SCU05), not in CU11.
 */
public record PeriodicFollowUp(String followUpId, String patientId, String physicianId, String modality,
        String clinicalIndication, Priority priority, int intervalDays, LocalDateTime createdAt) {
}
