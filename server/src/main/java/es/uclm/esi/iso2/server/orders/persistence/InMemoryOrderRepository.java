package es.uclm.esi.iso2.server.orders.persistence;

import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps the examination orders in memory until the team chooses the database (D-030): no SQL and no JPA, only a
 * thread-safe map. It starts empty: orders are created through CU10 and CU11.
 */
public class InMemoryOrderRepository implements IOrderRepository {
    private final ConcurrentHashMap<String, ExaminationOrder> orders = new ConcurrentHashMap<>();

    @Override
    public ExaminationOrder findById(String orderId) {
        return orderId == null ? null : orders.get(orderId);
    }

    @Override
    public List<ExaminationOrder> findByPatientId(String patientId) {
        if (patientId == null) {
            return List.of();
        }
        return orders.values().stream()
                .filter(order -> patientId.equals(order.getPatientId()))
                .sorted(Comparator.comparing(ExaminationOrder::getOrderId))
                .toList();
    }

    @Override
    public List<ExaminationOrder> findByStatus(OrderStatus status) {
        if (status == null) {
            return List.of();
        }
        return orders.values().stream()
                .filter(order -> order.getOrderStatus() == status)
                .sorted(Comparator.comparing(ExaminationOrder::getOrderId))
                .toList();
    }

    @Override
    public void save(ExaminationOrder order) {
        String orderId = Objects.requireNonNull(order.getOrderId(), "orderId");
        // putIfAbsent is atomic, so two threads saving the same id cannot both succeed
        if (orders.putIfAbsent(orderId, order) != null) {
            throw new IllegalArgumentException("The order " + orderId + " is already stored");
        }
    }

    @Override
    public void update(ExaminationOrder order) {
        String orderId = Objects.requireNonNull(order.getOrderId(), "orderId");
        if (orders.replace(orderId, order) == null) {
            throw new IllegalArgumentException("The order " + orderId + " is not stored");
        }
    }

    @Override
    public boolean existsById(String orderId) {
        return orderId != null && orders.containsKey(orderId);
    }
}
