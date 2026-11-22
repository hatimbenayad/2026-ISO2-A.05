package es.uclm.esi.iso2.server.orders.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link InMemoryOrderRepository}: save, update, the lookups and the use from several threads. */
@DisplayName("InMemoryOrderRepository")
class InMemoryOrderRepositoryTest {
    private static final LocalDateTime CANCELLED_AT = LocalDateTime.of(2026, 11, 25, 9, 30);

    private InMemoryOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    private static ExaminationOrder order(String id) {
        return order(id, "PAT-0001");
    }

    private static ExaminationOrder order(String id, String patientId) {
        return new ExaminationOrder(id, patientId, "PHY-0001", "CT", "Persistent cough", Priority.ROUTINE);
    }

    private static List<String> ids(List<ExaminationOrder> orders) {
        return orders.stream().map(ExaminationOrder::getOrderId).toList();
    }

    @FunctionalInterface
    private interface Task {
        void run(int thread) throws Exception;
    }

    /** Starts all the threads at the same moment and fails if any of them throws an unexpected exception. */
    private static void runInParallel(int threads, Task task) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        try {
            List<Future<?>> results = new ArrayList<>();
            for (int t = 0; t < threads; t++) {
                int thread = t;
                results.add(pool.submit(() -> {
                    start.await();
                    task.run(thread);
                    return null;
                }));
            }
            start.countDown();
            for (Future<?> result : results) {
                result.get(10, TimeUnit.SECONDS);
            }
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    @DisplayName("It starts empty")
    void startsEmpty() {
        // Assert
        assertAll(
                () -> assertTrue(repository.findByStatus(OrderStatus.ORDERED).isEmpty()),
                () -> assertTrue(repository.findByPatientId("PAT-0001").isEmpty()),
                () -> assertFalse(repository.existsById("ORD-0001")));
    }

    @Test
    @DisplayName("A saved order can be found by its id")
    void savedOrderIsFoundById() {
        // Arrange
        ExaminationOrder saved = order("ORD-0001");

        // Act
        repository.save(saved);

        // Assert
        assertAll(
                () -> assertSame(saved, repository.findById("ORD-0001")),
                () -> assertTrue(repository.existsById("ORD-0001")));
    }

    @Test
    @DisplayName("An order with an id that is already stored is rejected and the stored one is kept")
    void saveRejectsDuplicatedId() {
        // Arrange
        ExaminationOrder first = order("ORD-0001");
        repository.save(first);

        // Act and Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> repository.save(order("ORD-0001"))),
                () -> assertSame(first, repository.findById("ORD-0001")));
    }

    @Test
    @DisplayName("An order without id cannot be saved or updated")
    void orderWithoutIdIsRejected() {
        // Act and Assert
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> repository.save(order(null))),
                () -> assertThrows(NullPointerException.class, () -> repository.update(order(null))));
    }

    @Test
    @DisplayName("Update replaces the stored order that has the same id")
    void updateReplacesStoredOrder() {
        // Arrange
        repository.save(order("ORD-0001"));
        ExaminationOrder changed = order("ORD-0001");
        changed.setPriority(Priority.URGENT);

        // Act
        repository.update(changed);

        // Assert
        assertAll(
                () -> assertSame(changed, repository.findById("ORD-0001")),
                () -> assertEquals(Priority.URGENT, repository.findById("ORD-0001").getPriority()),
                () -> assertEquals(1, repository.findByPatientId("PAT-0001").size()));
    }

    @Test
    @DisplayName("Updating an order that was never saved is rejected and stores nothing")
    void updateRejectsUnknownOrder() {
        // Act and Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> repository.update(order("ORD-0001"))),
                () -> assertFalse(repository.existsById("ORD-0001")));
    }

    @Test
    @DisplayName("An id that was never saved gives null and does not exist")
    void unknownIdGivesNull() {
        // Arrange
        repository.save(order("ORD-0001"));

        // Act and Assert
        assertAll(
                () -> assertNull(repository.findById("ORD-9999")),
                () -> assertNull(repository.findById("ord-0001")),
                () -> assertNull(repository.findById("")),
                () -> assertNull(repository.findById(null)),
                () -> assertFalse(repository.existsById("ORD-9999")),
                () -> assertFalse(repository.existsById(null)));
    }

    @Test
    @DisplayName("findByPatientId gives only the orders of that patient, sorted by order id")
    void findByPatientIdFiltersAndSorts() {
        // Arrange
        repository.save(order("ORD-0003", "PAT-0001"));
        repository.save(order("ORD-0001", "PAT-0001"));
        repository.save(order("ORD-0002", "PAT-0002"));

        // Act and Assert
        assertAll(
                () -> assertEquals(List.of("ORD-0001", "ORD-0003"), ids(repository.findByPatientId("PAT-0001"))),
                () -> assertEquals(List.of("ORD-0002"), ids(repository.findByPatientId("PAT-0002"))));
    }

    @Test
    @DisplayName("findByPatientId gives an empty list for a patient without orders or for null")
    void findByPatientIdWithoutOrders() {
        // Arrange
        repository.save(order("ORD-0001"));

        // Act and Assert
        assertAll(
                () -> assertTrue(repository.findByPatientId("PAT-9999").isEmpty()),
                () -> assertTrue(repository.findByPatientId(null).isEmpty()));
    }

    @Test
    @DisplayName("Cancelled orders stay in the results of the patient (D-015)")
    void cancelledOrdersAreNotRemoved() {
        // Arrange
        ExaminationOrder cancelled = order("ORD-0001");
        repository.save(cancelled);
        cancelled.cancel(CANCELLED_AT);

        // Act
        repository.update(cancelled);

        // Assert
        assertEquals(List.of("ORD-0001"), ids(repository.findByPatientId("PAT-0001")));
    }

    @Test
    @DisplayName("findByStatus gives the orders in that status, sorted by order id")
    void findByStatusFiltersAndSorts() {
        // Arrange
        ExaminationOrder scheduled = order("ORD-0002");
        ExaminationOrder cancelled = order("ORD-0003");
        repository.save(order("ORD-0004"));
        repository.save(scheduled);
        repository.save(cancelled);
        repository.save(order("ORD-0001"));
        scheduled.setOrderStatus(OrderStatus.SCHEDULED);
        cancelled.cancel(CANCELLED_AT);
        repository.update(scheduled);
        repository.update(cancelled);

        // Act and Assert
        assertAll(
                () -> assertEquals(List.of("ORD-0001", "ORD-0004"), ids(repository.findByStatus(OrderStatus.ORDERED))),
                () -> assertEquals(List.of("ORD-0002"), ids(repository.findByStatus(OrderStatus.SCHEDULED))),
                () -> assertEquals(List.of("ORD-0003"), ids(repository.findByStatus(OrderStatus.CANCELLED))),
                () -> assertTrue(repository.findByStatus(OrderStatus.PERFORMED).isEmpty()),
                () -> assertTrue(repository.findByStatus(null).isEmpty()));
    }

    @Test
    @DisplayName("Changing the result of a lookup does not remove orders")
    void lookupsGiveReadOnlyLists() {
        // Arrange
        repository.save(order("ORD-0001"));

        // Act and Assert
        assertAll(
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> repository.findByPatientId("PAT-0001").clear()),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> repository.findByStatus(OrderStatus.ORDERED).clear()),
                () -> assertTrue(repository.existsById("ORD-0001")));
    }

    @Test
    @DisplayName("Orders saved at the same time from several threads are all kept")
    void concurrentSavesKeepEveryOrder() throws Exception {
        // Arrange
        int threads = 8;
        int ordersPerThread = 100;

        // Act
        runInParallel(threads, thread -> {
            for (int i = 0; i < ordersPerThread; i++) {
                repository.save(order(String.format("ORD-%d-%03d", thread, i), "PAT-" + thread));
            }
        });

        // Assert
        assertAll(
                () -> assertEquals(threads * ordersPerThread, repository.findByStatus(OrderStatus.ORDERED).size()),
                () -> assertEquals(ordersPerThread, repository.findByPatientId("PAT-3").size()),
                () -> assertTrue(repository.existsById("ORD-7-099")));
    }

    @Test
    @DisplayName("When several threads save the same id at the same time, only one of them succeeds")
    void concurrentSavesOfTheSameIdOnlyOneWins() throws Exception {
        // Arrange
        int threads = 20;
        AtomicInteger saved = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        // Act
        runInParallel(threads, thread -> {
            try {
                repository.save(order("ORD-0001"));
                saved.incrementAndGet();
            } catch (IllegalArgumentException alreadyStored) {
                rejected.incrementAndGet();
            }
        });

        // Assert
        assertAll(
                () -> assertEquals(1, saved.get()),
                () -> assertEquals(threads - 1, rejected.get()),
                () -> assertEquals(1, repository.findByStatus(OrderStatus.ORDERED).size()));
    }
}
