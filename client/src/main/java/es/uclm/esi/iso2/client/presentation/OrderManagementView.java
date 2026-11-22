package es.uclm.esi.iso2.client.presentation;

import es.uclm.esi.iso2.client.controller.ClientOrderController;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/** CU12: active-order list, filtering and soft cancellation. */
public class OrderManagementView extends JPanel {
    private static final String[] COLUMNS =
            {"Order ID", "Patient ID", "Physician", "Modality", "Justification", "Priority", "Status"};
    private final DefaultTableModel tableModel =
            new DefaultTableModel(COLUMNS, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
    private final JTable ordersTable = new JTable(tableModel);
    private final JComboBox<Object> statusFilter =
            new JComboBox<>(new Object[]{"ALL", OrderStatus.ORDERED, OrderStatus.SCHEDULED,
                    OrderStatus.PERFORMED, OrderStatus.REPORTED, OrderStatus.CANCELLED});
    private final JButton filterButton = new JButton("Refresh");
    private final JButton cancelButton = new JButton("Cancel selected");
    private final JLabel feedbackLabel = new JLabel(" ");
    private final ClientOrderController controller;

    public OrderManagementView(ClientOrderController controller) {
        this.controller = controller;
        buildUi();
        filterButton.addActionListener(event -> refresh());
        cancelButton.addActionListener(event -> cancelSelected());
    }

    public OrderManagementView() {
        this(new ClientOrderController(new NoOpOrderService()));
    }

    private void buildUi() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Order Management"));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(new JLabel("Status"));
        toolbar.add(statusFilter);
        toolbar.add(filterButton);
        toolbar.add(cancelButton);
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        add(feedbackLabel, BorderLayout.SOUTH);
    }

    public void refresh() {
        Object selection = statusFilter.getSelectedItem();
        if (selection == null || "ALL".equals(selection)) {
            controller.filterOrders(null);
        } else {
            controller.filterOrders((OrderStatus) selection);
        }
    }

    public void cancelSelected() {
        int row = ordersTable.getSelectedRow();
        if (row < 0) {
            showError("Select an order first");
            return;
        }
        String orderId = String.valueOf(tableModel.getValueAt(row, 0));
        try {
            controller.cancelOrder(orderId);
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    public void setOrders(List<ExaminationOrderDto> orders) {
        tableModel.setRowCount(0);
        if (orders == null) {
            return;
        }
        for (ExaminationOrderDto order : orders) {
            tableModel.addRow(new Object[]{
                    order.orderId(),
                    order.patientId(),
                    order.physicianId(),
                    order.modality(),
                    order.clinicalIndication(),
                    order.priority(),
                    order.orderStatus()
            });
        }
    }

    public void showFeedback(String message) {
        feedbackLabel.setText(message == null ? " " : message);
    }

    public void showError(String message) {
        feedbackLabel.setText("Error: " + (message == null ? "Unknown error" : message));
    }

    public JTable getOrdersTable() {
        return ordersTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JComboBox<Object> getStatusFilter() {
        return statusFilter;
    }

    public JButton getFilterButton() {
        return filterButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JLabel getFeedbackLabel() {
        return feedbackLabel;
    }

    public ClientOrderController getController() {
        return controller;
    }
}
