import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainApp extends JFrame implements PanelNames{

    public MainApp() {
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Initializing the navigation controller singleton object
        NavigationController navigationController = NavigationController.getInstance();
        navigationController.init(cardLayout, mainPanel);

        // adding all panels
        navigationController.addPanelToMainPanel(LOGIN, new LoginPanel());
        navigationController.addPanelToMainPanel(USER_OPTIONS, new UserOptionsPanel());
        navigationController.addPanelToMainPanel(READ_STUDENT, new ReadStudentPanel());

        // start on login page
        navigationController.navigateTo(LOGIN);

        add(mainPanel);
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
