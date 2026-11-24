package es.uclm.esi.iso2.server.orders.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderRequest;
import es.uclm.esi.iso2.common.patients.domain.Patient;
import es.uclm.esi.iso2.server.orders.persistence.InMemoryOrderRepository;
import es.uclm.esi.iso2.server.patients.persistence.InMemoryPatientRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ExaminationOrderService (CU10, CU11, CU12, CU16)")
class ExaminationOrderServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 24, 10, 0);

    private InMemoryOrderRepository orderRepository;
    private InMemoryPatientRepository patientRepository;
    private ExaminationOrderService service;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        patientRepository = new InMemoryPatientRepository();
        patientRepository.save(patient("PAT-0001"));
        Patient discharged = patient("PAT-0002");
        discharged.discharge(NOW.minusDays(1));
        patientRepository.save(discharged);
        Clock clock = Clock.fixed(Instant.parse("2026-11-24T10:00:00Z"), ZoneOffset.UTC);
        service = new ExaminationOrderService(orderRepository, patientRepository, clock);
    }

    private static Patient patient(String id) {
        return new Patient(id, "1234567" + id.charAt(id.length() - 1) + "Z", "CLM-" + id, "Ana", "García",
                LocalDate.of(1980, 1, 1), NOW.minusYears(1));
    }

    private static ExaminationOrderRequest request(String patientId, Priority priority) {
        return new ExaminationOrderRequest(patientId, "PHY-0001", "CT", "Persistent cough", priority);
    }

    private ExaminationOrderDto scheduledOrder() {
        ExaminationOrderDto created = service.createOrder(request("PAT-0001", Priority.ROUTINE));
        ExaminationOrder order = orderRepository.findById(created.orderId());
        order.setOrderStatus(OrderStatus.SCHEDULED);
        orderRepository.update(order);
        return created;
    }

    @Test
    @DisplayName("constructor rejects null dependencies")
    void constructorRejectsNulls() {
        assertThrows(NullPointerException.class, () -> new ExaminationOrderService(null, patientRepository));
        assertThrows(NullPointerException.class, () -> new ExaminationOrderService(orderRepository, null));
        assertThrows(NullPointerException.class,
                () -> new ExaminationOrderService(orderRepository, patientRepository, null));
    }

    @Nested
    @DisplayName("CU10 create order")
    class CreateOrder {
        @Test
        @DisplayName("a valid request creates an ORDERED order with trimmed data")
        void createsOrderedOrder() {
            ExaminationOrderDto dto = service.createOrder(new ExaminationOrderRequest(" PAT-0001 ", " PHY-0001 ",
                    " MRI ", " Headache ", Priority.URGENT));

            assertAll(
                    () -> assertEquals("ORD-0001", dto.orderId()),
                    () -> assertEquals("PAT-0001", dto.patientId()),
                    () -> assertEquals("PHY-0001", dto.physicianId()),
                    () -> assertEquals("MRI", dto.modality()),
                    () -> assertEquals("Headache", dto.clinicalIndication()),
                    () -> assertEquals(Priority.URGENT, dto.priority()),
                    () -> assertEquals(OrderStatus.ORDERED, dto.orderStatus()),
                    () -> assertNull(dto.cancelledAt()),
                    () -> assertTrue(orderRepository.existsById("ORD-0001")));
        }

        @Test
        @DisplayName("ids are consecutive and skip ids already stored")
        void idsSkipStoredOnes() {
            orderRepository.save(new ExaminationOrder("ORD-0001", "PAT-0001", "PHY-0001", "CT", "x",
                    Priority.ROUTINE));
            assertEquals("ORD-0002", service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId());
            assertEquals("ORD-0003", service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId());
        }

        @Test
        @DisplayName("the default interface aliases delegate to createOrder")
        void aliasesDelegate() {
            assertEquals("ORD-0001", service.create(request("PAT-0001", Priority.ROUTINE)).orderId());
            assertEquals("ORD-0002",
                    service.createExaminationOrder(request("PAT-0001", Priority.ROUTINE)).orderId());
        }

        @Test
        @DisplayName("a null request is rejected")
        void nullRequest() {
            assertThrows(IllegalArgumentException.class, () -> service.createOrder(null));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = "   ")
        @DisplayName("missing text fields are rejected and nothing is stored")
        void missingFields(String blank) {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> service.createOrder(
                            new ExaminationOrderRequest(blank, "PHY-0001", "CT", "x", Priority.ROUTINE))),
                    () -> assertThrows(IllegalArgumentException.class, () -> service.createOrder(
                            new ExaminationOrderRequest("PAT-0001", blank, "CT", "x", Priority.ROUTINE))),
                    () -> assertThrows(IllegalArgumentException.class, () -> service.createOrder(
                            new ExaminationOrderRequest("PAT-0001", "PHY-0001", blank, "x", Priority.ROUTINE))),
                    () -> assertThrows(IllegalArgumentException.class, () -> service.createOrder(
                            new ExaminationOrderRequest("PAT-0001", "PHY-0001", "CT", blank, Priority.ROUTINE))));
            assertTrue(service.listOrders(null).isEmpty());
        }

        @Test
        @DisplayName("a missing priority is rejected")
        void missingPriority() {
            assertThrows(IllegalArgumentException.class, () -> service.createOrder(request("PAT-0001", null)));
        }

        @Test
        @DisplayName("an unknown patient stops the flow")
        void unknownPatient() {
            assertThrows(NoSuchElementException.class,
                    () -> service.createOrder(request("PAT-9999", Priority.ROUTINE)));
        }

        @Test
        @DisplayName("a discharged patient stops the flow")
        void dischargedPatient() {
            assertThrows(IllegalStateException.class,
                    () -> service.createOrder(request("PAT-0002", Priority.ROUTINE)));
            assertTrue(service.listOrders(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("CU11 periodic follow-up")
    class FollowUp {
        @Test
        @DisplayName("a follow-up is stored with its interval and no order is created")
        void registersFollowUp() {
            PeriodicFollowUp followUp = service.registerFollowUp(request(" PAT-0001 ", Priority.PREFERENTIAL), 30);

            assertAll(
                    () -> assertEquals("FUP-0001", followUp.followUpId()),
                    () -> assertEquals("PAT-0001", followUp.patientId()),
                    () -> assertEquals(30, followUp.intervalDays()),
                    () -> assertEquals(Priority.PREFERENTIAL, followUp.priority()),
                    () -> assertEquals(NOW, followUp.createdAt()),
                    () -> assertEquals(List.of(followUp), service.getFollowUps("PAT-0001")),
                    () -> assertTrue(service.listOrders(null).isEmpty()));
        }

        @Test
        @DisplayName("priority defaults to ROUTINE")
        void defaultPriority() {
            assertEquals(Priority.ROUTINE, service.registerFollowUp(request("PAT-0001", null), 7).priority());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        @DisplayName("the interval must be positive")
        void invalidInterval(int days) {
            assertThrows(IllegalArgumentException.class,
                    () -> service.registerFollowUp(request("PAT-0001", Priority.ROUTINE), days));
        }

        @Test
        @DisplayName("missing data, unknown and discharged patients are rejected")
        void invalidRequests() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> service.registerFollowUp(null, 7)),
                    () -> assertThrows(IllegalArgumentException.class, () -> service.registerFollowUp(
                            new ExaminationOrderRequest("PAT-0001", "PHY-0001", " ", "x", null), 7)),
                    () -> assertThrows(NoSuchElementException.class,
                            () -> service.registerFollowUp(request("PAT-9999", null), 7)),
                    () -> assertThrows(IllegalStateException.class,
                            () -> service.registerFollowUp(request("PAT-0002", null), 7)));
            assertTrue(service.getFollowUps("PAT-0001").isEmpty());
        }

        @Test
        @DisplayName("getFollowUps rejects a blank patient id")
        void blankPatientId() {
            assertThrows(IllegalArgumentException.class, () -> service.getFollowUps(" "));
        }
    }

    @Nested
    @DisplayName("CU12 list and cancel orders")
    class ListAndCancel {
        @Test
        @DisplayName("listOrders(null) returns every status, most urgent first")
        void listsAllByPriority() {
            String routine = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            String urgent = service.createOrder(request("PAT-0001", Priority.URGENT)).orderId();
            String preferential = service.createOrder(request("PAT-0001", Priority.PREFERENTIAL)).orderId();
            service.cancelOrder(routine);

            List<String> ids = service.listOrders(null).stream().map(ExaminationOrderDto::orderId).toList();
            assertEquals(List.of(urgent, preferential, routine), ids);
        }

        @Test
        @DisplayName("listOrders(status) filters by status")
        void filtersByStatus() {
            String cancelled = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            String ordered = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            service.cancelOrder(cancelled);

            assertEquals(List.of(ordered), service.getOrders(OrderStatus.ORDERED).stream()
                    .map(ExaminationOrderDto::orderId).toList());
            assertEquals(List.of(cancelled), service.listOrders(OrderStatus.CANCELLED).stream()
                    .map(ExaminationOrderDto::orderId).toList());
            assertTrue(service.listOrders(OrderStatus.REPORTED).isEmpty());
        }

        @Test
        @DisplayName("findOrdersByPhysician returns only that physician's orders")
        void byPhysician() {
            service.createOrder(request("PAT-0001", Priority.ROUTINE));
            service.createOrder(new ExaminationOrderRequest("PAT-0001", "PHY-0002", "CT", "x", Priority.URGENT));

            List<ExaminationOrderDto> orders = service.findOrdersByPhysician(" PHY-0002 ");
            assertEquals(1, orders.size());
            assertEquals("PHY-0002", orders.get(0).physicianId());
            assertTrue(service.findOrdersByPhysician("PHY-9999").isEmpty());
            assertThrows(IllegalArgumentException.class, () -> service.findOrdersByPhysician(null));
        }

        @Test
        @DisplayName("cancelling marks the order CANCELLED with the date and keeps it stored (D-015)")
        void cancels() {
            String id = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();

            ExaminationOrderDto cancelled = service.cancelOrder(" " + id + " ");

            assertAll(
                    () -> assertEquals(OrderStatus.CANCELLED, cancelled.orderStatus()),
                    () -> assertEquals(NOW, cancelled.cancelledAt()),
                    () -> assertTrue(orderRepository.existsById(id)),
                    () -> assertEquals(OrderStatus.CANCELLED, orderRepository.findById(id).getOrderStatus()));
        }

        @Test
        @DisplayName("an order already cancelled cannot be cancelled again")
        void cancelTwice() {
            String id = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            service.softCancelOrder(id);
            assertThrows(IllegalStateException.class, () -> service.cancel(id));
        }

        @Test
        @DisplayName("unknown and blank ids are rejected")
        void cancelInvalid() {
            assertThrows(NoSuchElementException.class, () -> service.cancelOrder("ORD-9999"));
            assertThrows(IllegalArgumentException.class, () -> service.cancelOrder(""));
            assertThrows(IllegalArgumentException.class, () -> service.cancelOrder(null));
        }

        @Test
        @DisplayName("getOrder returns the stored order or fails")
        void getOrder() {
            String id = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            assertEquals(id, service.getOrder(id).orderId());
            assertThrows(NoSuchElementException.class, () -> service.getOrder("ORD-9999"));
        }
    }

    @Nested
    @DisplayName("CU16 procedure execution")
    class Execution {
        @Test
        @DisplayName("a scheduled order moves to PERFORMED and its log is kept")
        void performsScheduledOrder() {
            String id = scheduledOrder().orderId();

            ExaminationOrderDto performed = service.registerExecution(id, 2.5, 80.0, " EQ-CT-01 ");

            ProcedureExecutionLog log = service.getExecutionLog(id);
            assertAll(
                    () -> assertEquals(OrderStatus.PERFORMED, performed.orderStatus()),
                    () -> assertEquals(OrderStatus.PERFORMED, orderRepository.findById(id).getOrderStatus()),
                    () -> assertEquals(2.5, log.effectiveDoseMsv()),
                    () -> assertEquals(Double.valueOf(80.0), log.contrastVolumeMl()),
                    () -> assertTrue(log.usedContrast()),
                    () -> assertEquals("EQ-CT-01", log.equipmentId()),
                    () -> assertEquals(NOW, log.performedAt()));
        }

        @Test
        @DisplayName("contrast is optional")
        void withoutContrast() {
            String id = scheduledOrder().orderId();
            service.registerExecution(id, 0.0, null, "EQ-1");
            assertFalse(service.getExecutionLog(id).usedContrast());
        }

        @Test
        @DisplayName("orders that are not SCHEDULED cannot be performed")
        void notScheduled() {
            String ordered = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            String cancelled = service.createOrder(request("PAT-0001", Priority.ROUTINE)).orderId();
            service.cancelOrder(cancelled);
            String performed = scheduledOrder().orderId();
            service.registerExecution(performed, 1.0, null, "EQ-1");

            assertAll(
                    () -> assertThrows(IllegalStateException.class,
                            () -> service.registerExecution(ordered, 1.0, null, "EQ-1")),
                    () -> assertThrows(IllegalStateException.class,
                            () -> service.registerExecution(cancelled, 1.0, null, "EQ-1")),
                    () -> assertThrows(IllegalStateException.class,
                            () -> service.registerExecution(performed, 1.0, null, "EQ-1")));
            assertNull(service.getExecutionLog(ordered));
        }

        @Test
        @DisplayName("dose, equipment and contrast are validated before anything changes")
        void invalidParameters() {
            String id = scheduledOrder().orderId();
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(id, null, null, "EQ-1")),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(id, -0.1, null, "EQ-1")),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(id, Double.NaN, null, "EQ-1")),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(id, 1.0, null, " ")),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(id, 1.0, 0.0, "EQ-1")),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(id, 1.0, Double.NaN, "EQ-1")),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> service.registerExecution(null, 1.0, null, "EQ-1")),
                    () -> assertThrows(NoSuchElementException.class,
                            () -> service.registerExecution("ORD-9999", 1.0, null, "EQ-1")));
            assertEquals(OrderStatus.SCHEDULED, orderRepository.findById(id).getOrderStatus());
        }
    }
}
