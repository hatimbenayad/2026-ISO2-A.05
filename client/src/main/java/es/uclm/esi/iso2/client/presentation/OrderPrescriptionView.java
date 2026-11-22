package es.uclm.esi.iso2.client.presentation;

import es.uclm.esi.iso2.client.controller.ClientOrderController;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/** CU10: physician order-prescription form. */
public class OrderPrescriptionView extends JPanel {
    private final JTextField patientIdField = new JTextField(20);
    private final JComboBox<String> modalitySelector =
            new JComboBox<>(new String[]{"CT", "MRI", "X-RAY", "ULTRASOUND", "MAMMOGRAPHY"});
    private final JComboBox<Priority> prioritySelector = new JComboBox<>(Priority.values());
    private final JTextArea justificationArea = new JTextArea(5, 30);
    private final JButton submitButton = new JButton("Submit order");
    private final JLabel feedbackLabel = new JLabel(" ");
    private final ClientOrderController controller;

    public OrderPrescriptionView(ClientOrderController controller) {
        this.controller = controller;
        buildUi();
        submitButton.addActionListener(event -> submitOrder());
    }

    public OrderPrescriptionView() {
        this(new ClientOrderController(new NoOpOrderService()));
    }

    private void buildUi() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Prescription"));
        justificationArea.setLineWrap(true);
        justificationArea.setWrapStyleWord(true);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Patient ID"), constraints);
        constraints.gridx = 1;
        add(patientIdField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel("Modality"), constraints);
        constraints.gridx = 1;
        add(modalitySelector, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel("Priority"), constraints);
        constraints.gridx = 1;
        add(prioritySelector, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(new JLabel("Clinical justification"), constraints);
        constraints.gridx = 1;
        add(new JScrollPane(justificationArea), constraints);
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(submitButton, constraints);
        constraints.gridy = 5;
        add(feedbackLabel, constraints);
    }

    public void submitOrder() {
        try {
            controller.submitOrder(
                    patientIdField.getText(),
                    (String) modalitySelector.getSelectedItem(),
                    (Priority) prioritySelector.getSelectedItem(),
                    justificationArea.getText());
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    public void showFeedback(String message) {
        feedbackLabel.setText(message == null ? " " : message);
    }

    public void showError(String message) {
        feedbackLabel.setText("Error: " + (message == null ? "Unknown error" : message));
    }

    public JTextField getPatientIdField() {
        return patientIdField;
    }

    public JComboBox<String> getModalitySelector() {
        return modalitySelector;
    }

    public JComboBox<Priority> getPrioritySelector() {
        return prioritySelector;
    }

    public JTextArea getJustificationArea() {
        return justificationArea;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JLabel getFeedbackLabel() {
        return feedbackLabel;
    }

    public ClientOrderController getController() {
        return controller;
    }
}
