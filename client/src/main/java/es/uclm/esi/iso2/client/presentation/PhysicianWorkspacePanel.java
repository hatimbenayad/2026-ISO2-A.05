package es.uclm.esi.iso2.client.presentation;

import es.uclm.esi.iso2.client.controller.ClientOrderController;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

/** Tabbed CU10/CU12 workspace for a physician. */
public class PhysicianWorkspacePanel extends JPanel {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JLabel statusLabel = new JLabel(" ");
    private final OrderPrescriptionView prescriptionView;
    private final OrderManagementView managementView;
    private final ClientOrderController controller;

    public PhysicianWorkspacePanel(ClientOrderController controller) {
        this.controller = controller;
        this.prescriptionView = new OrderPrescriptionView(controller);
        this.managementView = new OrderManagementView(controller);
        controller.bind(prescriptionView, managementView);
        buildUi();
    }

    public PhysicianWorkspacePanel() {
        this(new ClientOrderController(new NoOpOrderService()));
    }

    private void buildUi() {
        setLayout(new BorderLayout());
        tabbedPane.addTab("Prescribe order", prescriptionView);
        tabbedPane.addTab("Manage orders", managementView);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public OrderPrescriptionView getPrescriptionView() {
        return prescriptionView;
    }

    public OrderManagementView getOrderManagementView() {
        return managementView;
    }

    public OrderManagementView getManagementView() {
        return managementView;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void showStatus(String message) {
        statusLabel.setText(message == null ? " " : message);
    }

    public void notifyStatus(String message) {
        showStatus(message);
    }

    public ClientOrderController getController() {
        return controller;
    }
}
