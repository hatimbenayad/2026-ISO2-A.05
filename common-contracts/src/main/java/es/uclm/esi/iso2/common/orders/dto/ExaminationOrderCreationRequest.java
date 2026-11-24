package es.uclm.esi.iso2.common.orders.dto;

import es.uclm.esi.iso2.common.orders.domain.Priority;

/** Backward-compatible descriptive name for an examination-order creation request. */
public record ExaminationOrderCreationRequest(
        String patientId,
        String physicianId,
        String modality,
        String clinicalIndication,
        Priority priority) {

    public ExaminationOrderRequest toOrderRequest() {
        return new ExaminationOrderRequest(patientId, physicianId, modality, clinicalIndication, priority);
    }
}
