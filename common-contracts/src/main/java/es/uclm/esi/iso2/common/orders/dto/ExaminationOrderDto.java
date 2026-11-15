package es.uclm.esi.iso2.common.orders.dto;

import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import java.time.LocalDateTime;

/** What the client receives about an examination order. */
public record ExaminationOrderDto(String orderId, String patientId, String physicianId, String modality,
        String clinicalIndication, Priority priority, OrderStatus orderStatus, LocalDateTime cancelledAt) {

    public static ExaminationOrderDto from(ExaminationOrder order) {
        return new ExaminationOrderDto(order.getOrderId(), order.getPatientId(), order.getPhysicianId(),
                order.getModality(), order.getClinicalIndication(), order.getPriority(), order.getOrderStatus(),
                order.getCancelledAt());
    }
}
