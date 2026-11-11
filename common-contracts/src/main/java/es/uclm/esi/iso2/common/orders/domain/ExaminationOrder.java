package es.uclm.esi.iso2.common.orders.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Diagnostic examination that a physician orders for a patient (CU10, CU12). Patient and physician are
 * referenced by their ids. What was ordered does not change after the order is created; only the priority and
 * the status do. An order is cancelled, never deleted (D-015).
 */
public class ExaminationOrder {
    private final String orderId;
    private final String patientId;
    private final String physicianId;
    private final String modality;
    private final String clinicalIndication;
    private Priority priority;
    private OrderStatus orderStatus;
    private LocalDateTime cancelledAt;

    public ExaminationOrder(String orderId, String patientId, String physicianId, String modality,
            String clinicalIndication, Priority priority) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.physicianId = physicianId;
        this.modality = modality;
        this.clinicalIndication = clinicalIndication;
        this.priority = priority;
        this.orderStatus = OrderStatus.ORDERED;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPhysicianId() {
        return physicianId;
    }

    /** Type of examination, for example "CT" or "MRI". */
    public String getModality() {
        return modality;
    }

    public String getClinicalIndication() {
        return clinicalIndication;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /** Moves the order to the next step. An order is cancelled with {@link #cancel}, not with this method. */
    public void setOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("The order " + orderId + " has to be cancelled with cancel");
        }
        if (isCancelled()) {
            throw new IllegalStateException("The order " + orderId + " is cancelled");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCancelled() {
        return orderStatus == OrderStatus.CANCELLED;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    /** The order is marked as cancelled and nothing is deleted (D-015). */
    public void cancel(LocalDateTime when) {
        if (isCancelled()) {
            throw new IllegalStateException("The order " + orderId + " is already cancelled");
        }
        orderStatus = OrderStatus.CANCELLED;
        cancelledAt = when;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExaminationOrder)) {
            return false;
        }
        return Objects.equals(orderId, ((ExaminationOrder) other).orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
