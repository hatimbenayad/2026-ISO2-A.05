package es.uclm.esi.iso2.server.orders.persistence;

import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import java.util.List;

/** Storage of the examination orders (CU10, CU11, CU12, CU16). There is no delete operation (D-015). */
public interface IOrderRepository {
    /** {@code null} when no order was stored with that id. */
    ExaminationOrder findById(String orderId);

    /**
     * The orders of the patient, cancelled ones included (D-015), sorted by order id so the result is always in the
     * same order. Empty when the patient has none.
     */
    List<ExaminationOrder> findByPatientId(String patientId);

    /** The orders that are in that status, sorted by order id. Empty when there are none. */
    List<ExaminationOrder> findByStatus(OrderStatus status);

    /**
     * Stores a new order.
     *
     * @throws IllegalArgumentException if an order with the same id is already stored
     */
    void save(ExaminationOrder order);

    /**
     * Replaces the stored order that has the same id.
     *
     * @throws IllegalArgumentException if no order with that id is stored
     */
    void update(ExaminationOrder order);

    boolean existsById(String orderId);
}
