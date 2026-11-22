package es.uclm.esi.iso2.client.controller;

import es.uclm.esi.iso2.client.presentation.OrderManagementView;
import es.uclm.esi.iso2.client.presentation.OrderPrescriptionView;
import es.uclm.esi.iso2.common.access.dto.AuthSessionDto;
import es.uclm.esi.iso2.common.orders.contracts.IExaminationOrderService;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderRequest;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.SwingUtilities;

/** Mediates Swing actions and the examination-order application contract. */
public class ClientOrderController {
    private final IExaminationOrderService orderService;
    private final ClientSessionContext sessionContext;
    private OrderPrescriptionView prescriptionView;
    private OrderManagementView managementView;

    public ClientOrderController(IExaminationOrderService orderService) {
        this(orderService, ClientSessionContext.getInstance());
    }

    public ClientOrderController(IExaminationOrderService orderService, ClientSessionContext sessionContext) {
        if (orderService == null) {
            throw new IllegalArgumentException("An examination-order service is required");
        }
        this.orderService = orderService;
        this.sessionContext = sessionContext == null ? ClientSessionContext.getInstance() : sessionContext;
    }

    public void bind(OrderPrescriptionView prescriptionView, OrderManagementView managementView) {
        this.prescriptionView = prescriptionView;
        this.managementView = managementView;
    }

    public IExaminationOrderService getOrderService() {
        return orderService;
    }

    public ClientSessionContext getSessionContext() {
        return sessionContext;
    }

    public ExaminationOrderDto submitOrder(
            String patientId, String modality, Priority priority, String clinicalJustification) {
        validatePatientId(patientId);
        validateText(modality, "Modality");
        if (priority == null) {
            throw new IllegalArgumentException("Priority is required");
        }
        validateText(clinicalJustification, "Clinical justification");
        if (!sessionContext.isPhysician()) {
            throw new IllegalArgumentException("An authenticated physician session is required");
        }

        AuthSessionDto session = sessionContext.getSession();
        ExaminationOrderRequest request = new ExaminationOrderRequest(
                patientId.trim(), session.username(), modality.trim(), clinicalJustification.trim(), priority);
        ExaminationOrderDto created = orderService.createOrder(request);
        if (prescriptionView != null) {
            prescriptionView.showFeedback("Order created: " + created.orderId());
        }
        refreshOrders(null);
        return created;
    }

    public ExaminationOrderDto createOrder(
            String patientId, String modality, Priority priority, String clinicalJustification) {
        return submitOrder(patientId, modality, priority, clinicalJustification);
    }

    public ExaminationOrderDto submitOrder(
            String patientId, String modality, String priority, String clinicalJustification) {
        return submitOrder(patientId, modality, parsePriority(priority), clinicalJustification);
    }

    public ExaminationOrderDto createOrder(
            String patientId, String modality, String priority, String clinicalJustification) {
        return submitOrder(patientId, modality, priority, clinicalJustification);
    }

    private Priority parsePriority(String priority) {
        if (priority == null || priority.isBlank()) {
            throw new IllegalArgumentException("Priority is required");
        }
        try {
            return Priority.valueOf(priority.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid priority: " + priority, ex);
        }
    }

    public List<ExaminationOrderDto> refreshOrders(OrderStatus status) {
        if (!sessionContext.isAuthenticated()) {
            throw new IllegalArgumentException("An authenticated session is required");
        }
        List<ExaminationOrderDto> orders = orderService.listOrders(status);
        List<ExaminationOrderDto> snapshot = orders == null
                ? Collections.emptyList()
                : List.copyOf(orders);
        updateManagementView(view -> view.setOrders(snapshot));
        if (managementView != null) {
            managementView.showFeedback(snapshot.size() == 1
                    ? "1 active order"
                    : snapshot.size() + " active orders");
        }
        return snapshot;
    }

    public List<ExaminationOrderDto> listOrders(OrderStatus status) {
        return refreshOrders(status);
    }

    public List<ExaminationOrderDto> filterOrders(OrderStatus status) {
        return refreshOrders(status);
    }

    public ExaminationOrderDto cancelOrder(String orderId) {
        validateText(orderId, "Order ID");
        if (!sessionContext.isPhysician()) {
            throw new IllegalArgumentException("An authenticated physician session is required");
        }
        ExaminationOrderDto cancelled = orderService.cancelOrder(orderId.trim());
        refreshOrders(null);
        if (managementView != null) {
            managementView.showFeedback("Order cancelled: " + orderId.trim());
        }
        return cancelled;
    }

    public void submitPrescription(String patientId, String modality, Priority priority,
                                   String clinicalJustification) {
        submitOrder(patientId, modality, priority, clinicalJustification);
    }

    public void handleCancellation(String orderId) {
        cancelOrder(orderId);
    }

    public void showError(Exception exception) {
        if (managementView != null) {
            managementView.showError(exception.getMessage());
        }
        if (prescriptionView != null) {
            prescriptionView.showError(exception.getMessage());
        }
    }

    private void validatePatientId(String patientId) {
        validateText(patientId, "Patient ID");
    }

    private void validateText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " is required");
        }
    }

    private void updateManagementView(java.util.function.Consumer<OrderManagementView> update) {
        if (managementView == null) {
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            update.accept(managementView);
        } else {
            SwingUtilities.invokeLater(() -> update.accept(managementView));
        }
    }
}
