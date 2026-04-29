import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This is the main class that initializes all app panels and starts the app from the login page
 */
public class MainApp extends JFrame implements PanelNames{

    public MainApp() {
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Initializing the navigation controller singleton object
        // used as the class to help navigate between the various panels (screens) of the app
        NavigationController navigationController = NavigationController.getInstance();
        navigationController.init(cardLayout, mainPanel);

        // adding all panels
        navigationController.addPanelToMainPanel(LOGIN, new LoginPanel());
        navigationController.addPanelToMainPanel(USER_OPTIONS, new UserOptionsPanel());
        navigationController.addPanelToMainPanel(CREATE_STUDENT, new CreateStudentPanel());
        navigationController.addPanelToMainPanel(READ_STUDENT, new ReadStudentPanel());       
        navigationController.addPanelToMainPanel(UPDATE_STUDENT, new UpdateStudentPanel());
        navigationController.addPanelToMainPanel(DELETE_STUDENT, new DeleteStudentPanel());
        navigationController.addPanelToMainPanel(SEARCH_STUDENT, new SearchStudentPanel());
        navigationController.addPanelToMainPanel(NAME_SEARCH, new StudentNameSearchPanel());
        navigationController.addPanelToMainPanel(ROLL_NUMBER_SEARCH, new StudentRollNumberSearchPanel());
        navigationController.addPanelToMainPanel(DEPARTMENT_SEARCH, new StudentDepartmentSearchPanel());
        navigationController.addPanelToMainPanel(MARKS_RANGE_SEARCH, new StudentMarksSearchPanel());
        navigationController.addPanelToMainPanel(STUDENT_STATS, new StudentStatisticOptionsPanel());
        navigationController.addPanelToMainPanel(STUDENT_COUNT_AND_MARKS, new StudentCountAndHighestAndLowestMarksPanel());
        navigationController.addPanelToMainPanel(STUDENT_DEPARTMENT_COUNT, new StudentCountByDepartmentPanel());

        // start on login page
        navigationController.navigateTo(LOGIN);

        add(mainPanel);
        setTitle("Student Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
