import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * This class contains all the code for searching through the database based on student roll number from user input in a screen
 */
public class StudentRollNumberSearchPanel extends BasePanel implements TableDisplayColumnNames {

    private String databaseUrl, username, password;
    private DefaultTableModel searchResultTableModel;
    private JTextField rollNumberSearchInputField;

    @Override
    protected JPanel build() {
        
        JPanel studentRollNumberSearchPanel = new JPanel(new BorderLayout(10, 10));
        
        studentRollNumberSearchPanel.add(createSearchInputPanel(), BorderLayout.NORTH);
        studentRollNumberSearchPanel.add(createSearchResultDisplayPanel(), BorderLayout.CENTER);

        return studentRollNumberSearchPanel;

    }

    /**
     * This panel section displays the user input option
     * @return a JPanel object containing all the elements necessary for user input
     */
    private JPanel createSearchInputPanel() {
        
        JPanel searchInputFieldPanel = new JPanel(new GridBagLayout());
        searchInputFieldPanel.setBorder(BorderFactory.createTitledBorder("Search By Roll Number Input"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        searchInputFieldPanel.add(new JLabel("Existing roll number:"), gbc);
        gbc.gridx = 1;
        rollNumberSearchInputField = new JTextField(20);
        searchInputFieldPanel.add(rollNumberSearchInputField, gbc);

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 32));
        searchButton.addActionListener(_ -> searchStudentByRollNumber());
        searchButtonPanel.add(searchButton);

        JButton returnToSearchOptionsButton = new JButton("Return to Search Options");
        returnToSearchOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToSearchOptionsButton.addActionListener(_ -> navigationController.navigateTo(SEARCH_STUDENT));
        searchButtonPanel.add(returnToSearchOptionsButton);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        searchInputFieldPanel.add(searchButtonPanel, gbc);

        gbc.gridy = 2;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        searchInputFieldPanel.add(logoutButton, gbc);

        return searchInputFieldPanel;
        
    }

    /**
     * This panel section displays the search results table
     * @return a JPanel object containing all the elements necessary to display the search results table
     */
    private JPanel createSearchResultDisplayPanel() {
        
        JPanel searchResultTablePanel = new JPanel(new BorderLayout(5, 5));
        searchResultTablePanel.setBorder(BorderFactory.createTitledBorder("Search Result Table"));

        // column names initialization
        String[] columnHeaders = {ID, NAME, ROLL_NUMBER, DEPARTMENT, EMAIL_ID, PHONE_NUMBER, MARKS, GRADE};
        searchResultTableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable searchResultTable = new JTable(searchResultTableModel);
        searchResultTable.setRowHeight(25);
        searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultTable.getTableHeader().setReorderingAllowed(false);

        // Column widths
        searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Roll Number
        searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Department
        searchResultTable.getColumnModel().getColumn(4).setPreferredWidth(250); // Email ID
        searchResultTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Phone Number
        searchResultTable.getColumnModel().getColumn(6).setPreferredWidth(50); // Marks
        searchResultTable.getColumnModel().getColumn(7).setPreferredWidth(50); // Grade

        JScrollPane searchResultTableScrollPane = new JScrollPane(searchResultTable);

        searchResultTablePanel.add(searchResultTableScrollPane, BorderLayout.CENTER);

        return searchResultTablePanel;

    }

    @Override
    public void onNavigatedTo() {
        
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        clearInputFields();
        searchResultTableModel.setRowCount(0);

    }

    @Override
    protected void clearInputFields() {
        rollNumberSearchInputField.setText("");
        rollNumberSearchInputField.requestFocus();
    }

    /**
     * Validates the user search input and calls the function to display the search results
     */
    private void searchStudentByRollNumber() {
        
        String rollNumber = rollNumberSearchInputField.getText().trim();

        if (rollNumber.isEmpty()) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, 
                "Roll number data is required to search through the database!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }       
        else if (!rollNumber.matches("^\\d{3}$")) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this,
                validRollNumberGuidelines + "Please enter a valid roll number",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;             
        }

        displaySearchResultTable(rollNumber);

    }

    /**
     * Retrieves and displays the search results from the database
     * @param rollNumber the user search input value
     */
    private void displaySearchResultTable(String rollNumber) {
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {

                String searchByRollNumberSQL = "SELECT * FROM student WHERE roll_no = ?";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                     PreparedStatement preparedStatement = connection.prepareStatement(searchByRollNumberSQL)) {

                    preparedStatement.setString(1, rollNumber);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {

                        searchResultTableModel.setRowCount(0);
                        if (resultSet.next()) {
                            searchResultTableModel.addRow(new Object[]{
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("roll_no"),
                                    resultSet.getString("department"),
                                    resultSet.getString("email"),
                                    resultSet.getString("phone"),
                                    resultSet.getInt("marks"),
                                    resultSet.getString("grade")
                            });

                            SwingUtilities.invokeLater(() ->

                                    JOptionPane.showMessageDialog(StudentRollNumberSearchPanel.this,
                                            "Search by roll number was successful",
                                            "Search Successful",
                                            JOptionPane.INFORMATION_MESSAGE)

                            );

                            SwingUtilities.invokeLater(() ->
                                    clearInputFields()
                            );

                        } else {
                            SwingUtilities.invokeLater(() ->

                                    JOptionPane.showMessageDialog(StudentRollNumberSearchPanel.this,
                                            "Search by roll number failed",
                                            "Search Failed",
                                            JOptionPane.WARNING_MESSAGE)

                            );
                        }

                    }

                } catch (SQLException e) {

                    SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentRollNumberSearchPanel.this,
                                    "Error:\n" + e,
                                    "MySQL Error",
                                    JOptionPane.WARNING_MESSAGE)

                    );
                }

                return null;

            }
        };

        worker.execute();

    }

}
