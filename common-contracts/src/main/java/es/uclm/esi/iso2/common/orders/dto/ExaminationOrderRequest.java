package es.uclm.esi.iso2.common.orders.dto;

import es.uclm.esi.iso2.common.orders.domain.Priority;

/** Request used by the desktop client to create an examination order. */
public record ExaminationOrderRequest(
        String patientId,
        String physicianId,
        String modality,
        String clinicalIndication,
        Priority priority) {
}
