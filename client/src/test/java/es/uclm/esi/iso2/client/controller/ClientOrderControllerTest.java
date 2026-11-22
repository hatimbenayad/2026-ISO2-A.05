package es.uclm.esi.iso2.client.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.uclm.esi.iso2.common.access.domain.Role;
import es.uclm.esi.iso2.common.access.dto.AuthSessionDto;
import es.uclm.esi.iso2.common.orders.contracts.IExaminationOrderService;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ClientOrderController")
class ClientOrderControllerTest {
    private StubOrderService service;
    private ClientOrderController controller;
    private ClientSessionContext context;

    @BeforeEach
    void setUp() {
        context = ClientSessionContext.getInstance();
        context.clearSession();
        service = new StubOrderService();
        controller = new ClientOrderController(service, context);
    }

    @Test
    void rejectsBlankPatientIdAndJustification() {
        authenticate();
        assertThrows(IllegalArgumentException.class,
                () -> controller.submitOrder(" ", "MRI", Priority.ROUTINE, "pain"));
        assertThrows(IllegalArgumentException.class,
                () -> controller.submitOrder("PAT-1", "MRI", Priority.ROUTINE, " "));
    }

    @Test
    void rejectsMissingSessionAndPriority() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.submitOrder("PAT-1", "MRI", Priority.ROUTINE, "pain"));
        authenticate();
        assertThrows(IllegalArgumentException.class,
                () -> controller.submitOrder("PAT-1", "MRI", (Priority) null, "pain"));
    }

    @Test
    void createsOrderWithAssembledRequest() {
        authenticate();
        ExaminationOrderDto created =
                controller.submitOrder(" PAT-1 ", "MRI", Priority.URGENT, "  severe pain  ");

        assertEquals("ORD-1", created.orderId());
        assertEquals("PAT-1", service.lastRequest.patientId());
        assertEquals("doctor", service.lastRequest.physicianId());
        assertEquals("MRI", service.lastRequest.modality());
        assertEquals("severe pain", service.lastRequest.clinicalIndication());
        assertEquals(Priority.URGENT, service.lastRequest.priority());
    }

    @Test
    void filtersOrdersAndCancelsSelectedOrder() {
        authenticate();
        List<ExaminationOrderDto> ordered =
                controller.refreshOrders(OrderStatus.ORDERED);
        assertEquals(1, ordered.size());
        assertEquals(OrderStatus.ORDERED, service.lastStatus);

        ExaminationOrderDto cancelled = controller.cancelOrder("ORD-1");
        assertEquals(OrderStatus.CANCELLED, cancelled.orderStatus());
        assertEquals("ORD-1", service.lastCancelledId);
    }

    @Test
    void rejectsUnauthorisedCancellation() {
        context.setSession(new AuthSessionDto("token", "staff", List.of(Role.ADMINISTRATIVE_STAFF),
                LocalDateTime.now().plusHours(1)));
        assertThrows(IllegalArgumentException.class, () -> controller.cancelOrder("ORD-1"));
    }

    @Test
    void aliasesAndErrorDisplayAreAvailable() {
        authenticate();
        assertEquals("ORD-1", controller.createOrder("PAT-1", "CT", "routine", "headache").orderId());
        assertEquals("ORD-1", controller.submitOrder("PAT-1", "CT", "urgent", "headache").orderId());
        assertEquals(1, controller.listOrders(null).size());
        assertEquals(1, controller.filterOrders(OrderStatus.REPORTED).size());
        controller.showError(new IllegalArgumentException("test error"));
        assertThrows(IllegalArgumentException.class,
                () -> controller.submitOrder("PAT-1", "CT", "invalid", "headache"));
        assertThrows(IllegalArgumentException.class,
                () -> controller.submitOrder("PAT-1", " ", Priority.ROUTINE, "headache"));
        assertEquals(service, controller.getOrderService());
        assertEquals(context, controller.getSessionContext());
    }

    @Test
    void refreshRejectsUnauthenticatedSessionAndNullStatusIsHandled() {
        assertThrows(IllegalArgumentException.class, () -> controller.refreshOrders(null));
        authenticate();
        assertEquals(1, controller.refreshOrders(null).size());
    }

    private void authenticate() {
        context.setSession(new AuthSessionDto("token", "doctor", List.of(Role.PHYSICIAN),
                LocalDateTime.now().plusHours(1)));
    }

    private static final class StubOrderService implements IExaminationOrderService {
        ExaminationOrderRequest lastRequest;
        OrderStatus lastStatus;
        String lastCancelledId;

        @Override
        public ExaminationOrderDto createOrder(ExaminationOrderRequest request) {
            lastRequest = request;
            return order("ORD-1", request.patientId(), request.modality(), request.priority(),
                    OrderStatus.ORDERED);
        }

        @Override
        public List<ExaminationOrderDto> listOrders(OrderStatus status) {
            lastStatus = status;
            return new ArrayList<>(List.of(order("ORD-1", "PAT-1", "MRI", Priority.ROUTINE,
                    status == null ? OrderStatus.ORDERED : status)));
        }

        @Override
        public ExaminationOrderDto cancelOrder(String orderId) {
            lastCancelledId = orderId;
            return order(orderId, "PAT-1", "MRI", Priority.ROUTINE, OrderStatus.CANCELLED);
        }

        private ExaminationOrderDto order(String id, String patientId, String modality,
                                          Priority priority, OrderStatus status) {
            return new ExaminationOrderDto(id, patientId, "doctor", modality, "indication",
                    priority, status, null);
        }
    }
}
