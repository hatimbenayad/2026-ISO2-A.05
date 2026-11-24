package es.uclm.esi.iso2.client.presentation;

import es.uclm.esi.iso2.client.controller.ClientOrderController;
import es.uclm.esi.iso2.client.controller.ClientSessionContext;
import es.uclm.esi.iso2.common.access.dto.AuthSessionDto;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/** Main desktop container for the physician workspace. */
public class MainWindowFrame extends JFrame {
    private final PhysicianWorkspacePanel workspace;
    private final JLabel statusBar = new JLabel(" ");
    private final JMenuItem signOutItem = new JMenuItem("Clear session");

    public MainWindowFrame(ClientOrderController controller) {
        super("SESCAM Radiodiagnostics");
        this.workspace = new PhysicianWorkspacePanel(controller);
        buildUi();
    }

    public MainWindowFrame() {
        this(new ClientOrderController(new NoOpOrderService()));
    }

    public static MainWindowFrame createIfSupported(ClientOrderController controller) {
        if (GraphicsEnvironment.isHeadless()) {
            return null;
        }
        return new MainWindowFrame(controller);
    }

    private void buildUi() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        add(workspace, BorderLayout.CENTER);
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusBar, BorderLayout.SOUTH);
        setSize(960, 640);
        setLocationRelativeTo(null);
        signOutItem.addActionListener(event -> {
            ClientSessionContext.getInstance().clearSession();
            showStatus("Session cleared");
        });
        updateSessionStatus();
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> dispose());
        fileMenu.add(exitItem);

        JMenu sessionMenu = new JMenu("Session");
        sessionMenu.add(signOutItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(event ->
                JOptionPane.showMessageDialog(this,
                        "SESCAM Radiodiagnostics - Physician Workspace",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(sessionMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public PhysicianWorkspacePanel getWorkspace() {
        return workspace;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public void showStatus(String message) {
        statusBar.setText(message == null ? " " : message);
        workspace.showStatus(message);
    }

    public void updateSessionStatus() {
        AuthSessionDto session = ClientSessionContext.getInstance().getSession();
        if (session == null) {
            showStatus("No active session");
        } else {
            showStatus("User: " + session.username() + " | Roles: " + session.roles());
        }
    }
}
