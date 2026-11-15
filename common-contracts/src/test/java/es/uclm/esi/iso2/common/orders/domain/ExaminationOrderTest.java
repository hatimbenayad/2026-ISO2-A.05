package es.uclm.esi.iso2.common.orders.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Tests of {@link ExaminationOrder}: a new order, its steps and the cancellation without deleting it (D-015). */
@DisplayName("ExaminationOrder")
class ExaminationOrderTest {
    private static final LocalDateTime CANCELLED = LocalDateTime.of(2026, 11, 11, 12, 30);

    private ExaminationOrder order;

    @BeforeEach
    void setUp() {
        order = new ExaminationOrder("ORD-0001", "PAT-0001", "mlopez", "CT", "Persistent headache after a fall",
                Priority.PREFERENTIAL);
    }

    @Test
    @DisplayName("A new order starts as ordered and keeps what the physician asked for")
    void newOrderStartsAsOrdered() {
        // Assert
        assertAll(
                () -> assertEquals("ORD-0001", order.getOrderId()),
                () -> assertEquals("PAT-0001", order.getPatientId()),
                () -> assertEquals("mlopez", order.getPhysicianId()),
                () -> assertEquals("CT", order.getModality()),
                () -> assertEquals("Persistent headache after a fall", order.getClinicalIndication()),
                () -> assertEquals(Priority.PREFERENTIAL, order.getPriority()),
                () -> assertEquals(OrderStatus.ORDERED, order.getOrderStatus()),
                () -> assertFalse(order.isCancelled()),
                () -> assertNull(order.getCancelledAt()));
    }

    @ParameterizedTest(name = "{0}")
    @EnumSource(value = OrderStatus.class, names = {"SCHEDULED", "PERFORMED", "REPORTED"})
    @DisplayName("Moving the order to the next step only changes its status")
    void movingTheOrderOnlyChangesTheStatus(OrderStatus next) {
        // Act
        order.setOrderStatus(next);

        // Assert
        assertAll(
                () -> assertEquals(next, order.getOrderStatus()),
                () -> assertEquals("PAT-0001", order.getPatientId()),
                () -> assertEquals("CT", order.getModality()),
                () -> assertFalse(order.isCancelled()));
    }

    @Test
    @DisplayName("The physician can raise the priority of an order")
    void priorityCanBeRaised() {
        // Act
        order.setPriority(Priority.URGENT);

        // Assert
        assertEquals(Priority.URGENT, order.getPriority());
    }

    @Test
    @DisplayName("Priorities go from the most to the least urgent")
    void prioritiesAreSortedByUrgency() {
        // Assert
        assertEquals(List.of(Priority.URGENT, Priority.PREFERENTIAL, Priority.ROUTINE), List.of(Priority.values()));
    }

    @Test
    @DisplayName("Cancelling an order keeps all its data and saves the date")
    void cancelKeepsAllTheData() {
        // Act
        order.cancel(CANCELLED);

        // Assert
        assertAll(
                () -> assertEquals(OrderStatus.CANCELLED, order.getOrderStatus()),
                () -> assertTrue(order.isCancelled()),
                () -> assertEquals(CANCELLED, order.getCancelledAt()),
                () -> assertEquals("PAT-0001", order.getPatientId()),
                () -> assertEquals("mlopez", order.getPhysicianId()),
                () -> assertEquals("Persistent headache after a fall", order.getClinicalIndication()));
    }

    @Test
    @DisplayName("A cancelled order cannot be cancelled again")
    void cancelledOrderCannotBeCancelledAgain() {
        // Arrange
        order.cancel(CANCELLED);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> order.cancel(CANCELLED.plusHours(1)));
    }

    @Test
    @DisplayName("A cancelled order does not go on to the next steps")
    void cancelledOrderDoesNotMoveOn() {
        // Arrange
        order.cancel(CANCELLED);

        // Act and Assert
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> order.setOrderStatus(OrderStatus.SCHEDULED)),
                () -> assertEquals(OrderStatus.CANCELLED, order.getOrderStatus()));
    }

    @Test
    @DisplayName("Setting the status to cancelled is refused, so the cancellation date is never lost")
    void cancelledStatusOnlyThroughCancel() {
        // Act and Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> order.setOrderStatus(OrderStatus.CANCELLED)),
                () -> assertEquals(OrderStatus.ORDERED, order.getOrderStatus()),
                () -> assertNull(order.getCancelledAt()));
    }

    @Test
    @DisplayName("Two orders are the same if they have the same id")
    void ordersWithSameIdAreEqual() {
        // Arrange
        ExaminationOrder sameId = new ExaminationOrder("ORD-0001", "PAT-0002", "jperez", "MRI", "Back pain",
                Priority.ROUTINE);
        ExaminationOrder otherId = new ExaminationOrder("ORD-0002", "PAT-0001", "mlopez", "CT",
                "Persistent headache after a fall", Priority.PREFERENTIAL);

        // Assert
        assertAll(
                () -> assertEquals(order, sameId),
                () -> assertEquals(order.hashCode(), sameId.hashCode()),
                () -> assertNotEquals(order, otherId));
    }
}
