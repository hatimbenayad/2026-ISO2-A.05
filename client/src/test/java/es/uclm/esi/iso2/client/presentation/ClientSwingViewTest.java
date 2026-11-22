package es.uclm.esi.iso2.client.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import es.uclm.esi.iso2.client.controller.ClientOrderController;
import es.uclm.esi.iso2.common.orders.contracts.IExaminationOrderService;
import es.uclm.esi.iso2.common.access.domain.Role;
import es.uclm.esi.iso2.common.access.dto.AuthSessionDto;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderRequest;
import java.util.List;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Headless Swing views")
class ClientSwingViewTest {
    private StubOrderService service;
    private ClientOrderController controller;

    @BeforeEach
    void setUp() {
        service = new StubOrderService();
        controller = new ClientOrderController(service);
        controller.getSessionContext().clearSession();
    }

    @Test
    void prescriptionViewExposesWiredWidgets() {
        OrderPrescriptionView view = new OrderPrescriptionView(controller);
        assertNotNull(view.getSubmitButton());
        assertNotNull(view.getPatientIdField());
        assertNotNull(view.getModalitySelector());
        assertNotNull(view.getPrioritySelector());
        view.getPatientIdField().setText(" ");
        view.submitOrder();
        assertEquals("Error: Patient ID is required", view.getFeedbackLabel().getText());
    }

    @Test
    void managementViewUpdatesTableModelAndFeedback() {
        OrderManagementView view = new OrderManagementView(controller);
        ExaminationOrderDto order = new ExaminationOrderDto(
                "ORD-1", "PAT-1", "doctor", "MRI", "pain",
                Priority.ROUTINE, OrderStatus.ORDERED, null);
        view.setOrders(List.of(order));
        assertEquals(1, view.getTableModel().getRowCount());
        assertEquals("ORD-1", view.getTableModel().getValueAt(0, 0));
        view.showFeedback("Filtered");
        assertEquals("Filtered", view.getFeedbackLabel().getText());
        view.showError(null);
        assertNotNull(view.getOrdersTable());
        assertNotNull(view.getStatusFilter());
        assertNotNull(view.getFilterButton());
        assertNotNull(view.getCancelButton());
        view.cancelSelected();
        assertEquals("Error: Select an order first", view.getFeedbackLabel().getText());
        view.setOrders(null);
        assertEquals(0, view.getTableModel().getRowCount());
    }

    @Test
    void workspaceHostsBothViews() {
        PhysicianWorkspacePanel panel = new PhysicianWorkspacePanel(controller);
        assertEquals(2, panel.getTabbedPane().getTabCount());
        assertNotNull(panel.getPrescriptionView());
        assertNotNull(panel.getManagementView());
        panel.showStatus("Ready");
        assertEquals("Ready", panel.getStatusLabel().getText());
        panel.notifyStatus(null);
        assertEquals(" ", panel.getStatusLabel().getText());
        assertEquals(controller, panel.getController());
    }

    @Test
    void statusFilterAndRefreshPathsAreHeadlessSafe() {
        controller.getSessionContext().setSession(new AuthSessionDto("token", "doctor",
                List.of(Role.PHYSICIAN), LocalDateTime.now().plusHours(1)));
        OrderManagementView view = new OrderManagementView(controller);
        controller.bind(null, view);
        view.getStatusFilter().setSelectedItem("ALL");
        view.refresh();
        view.getStatusFilter().setSelectedItem(OrderStatus.REPORTED);
        view.refresh();
        view.getStatusFilter().setSelectedIndex(1);
        view.getOrdersTable().clearSelection();
        view.cancelSelected();
    }

    @Test
    void prescriptionFeedbackAndMainWindowFactoryAreHeadlessSafe() {
        OrderPrescriptionView view = new OrderPrescriptionView(controller);
        view.showFeedback(null);
        assertEquals(" ", view.getFeedbackLabel().getText());
        view.showError(null);
        assertNotNull(view.getModalitySelector());
        assertNull(MainWindowFrame.createIfSupported(controller));
    }

    private static final class StubOrderService implements IExaminationOrderService {
        @Override
        public ExaminationOrderDto createOrder(ExaminationOrderRequest request) {
            return new ExaminationOrderDto("ORD-1", request.patientId(), request.physicianId(),
                    request.modality(), request.clinicalIndication(), request.priority(),
                    OrderStatus.ORDERED, null);
        }

        @Override
        public List<ExaminationOrderDto> listOrders(OrderStatus status) {
            return List.of();
        }

        @Override
        public ExaminationOrderDto cancelOrder(String orderId) {
            return new ExaminationOrderDto(orderId, "PAT-1", "doctor", "MRI", "pain",
                    Priority.ROUTINE, OrderStatus.CANCELLED, null);
        }
    }
}
