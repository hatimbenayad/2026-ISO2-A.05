package es.uclm.esi.iso2.server.orders.service;

import java.time.LocalDateTime;

/**
 * Parameters recorded when a scheduled examination is performed (CU16). {@code contrastVolumeMl} is {@code null}
 * when no contrast medium was used.
 */
public record ProcedureExecutionLog(String orderId, double effectiveDoseMsv, Double contrastVolumeMl,
        String equipmentId, LocalDateTime performedAt) {

    public boolean usedContrast() {
        return contrastVolumeMl != null;
    }
}
