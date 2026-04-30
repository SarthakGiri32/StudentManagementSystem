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
 * This class contains all the code for searching through the database based on a student marks range from user input in a screen
 */
public class StudentMarksSearchPanel extends BasePanel implements TableDisplayColumnNames {

    private String databaseUrl, username, password;
    private DefaultTableModel searchResultTableModel;
    private JTextField marksRangeUpperLimitField, marksRangeLowerLimitField;

    @Override
    protected JPanel build() {

        JPanel studentMarksSearchPanel = new JPanel(new BorderLayout(10, 10));

        studentMarksSearchPanel.add(createSearchInputPanel(), BorderLayout.NORTH);
        studentMarksSearchPanel.add(createSearchResultDisplayPanel(), BorderLayout.CENTER);

        return studentMarksSearchPanel;

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
        marksRangeUpperLimitField.setText("");
        marksRangeLowerLimitField.setText("");
        marksRangeUpperLimitField.requestFocus();
    }

    /**
     * This panel section displays the user input option
     * @return a JPanel object containing all the elements necessary for user input
     */
    private JPanel createSearchInputPanel() {

        JPanel searchInputFieldPanel = new JPanel(new GridBagLayout());
        searchInputFieldPanel.setBorder(BorderFactory.createTitledBorder("Search By Marks Range Input"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        searchInputFieldPanel.add(new JLabel("Enter marks range upper limit:"), gbc);
        gbc.gridx = 1;
        marksRangeUpperLimitField = new JTextField(10);
        searchInputFieldPanel.add(marksRangeUpperLimitField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        searchInputFieldPanel.add(new JLabel("Enter marks range lower limit:"), gbc);
        gbc.gridx = 1;
        marksRangeLowerLimitField = new JTextField(10);
        searchInputFieldPanel.add(marksRangeLowerLimitField, gbc);

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 32));
        searchButton.addActionListener(_ -> searchStudentByMarksRange());
        searchButtonPanel.add(searchButton);

        JButton returnToSearchOptionsButton = new JButton("Return to Search Options");
        returnToSearchOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToSearchOptionsButton.addActionListener(_ -> navigationController.navigateTo(SEARCH_STUDENT));
        searchButtonPanel.add(returnToSearchOptionsButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        searchInputFieldPanel.add(searchButtonPanel, gbc);

        gbc.gridy = 3;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        searchInputFieldPanel.add(logoutButton, gbc);

        return searchInputFieldPanel;

    }

    /**
     * Validates the user search input and calls the function to display the search results
     */
    private void searchStudentByMarksRange() {

        String lowerMarksLimit = marksRangeLowerLimitField.getText().trim();
        String upperMarksLimit = marksRangeUpperLimitField.getText().trim();

        if (lowerMarksLimit.isEmpty() || upperMarksLimit.isEmpty()) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, 
                "Marks range data is required to search through the database!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!lowerMarksLimit.matches("^\\d{1,3}$") ||
            !upperMarksLimit.matches("^\\d{1,3}$")) 
        {
            searchResultTableModel.setRowCount(0);

            if (!lowerMarksLimit.matches("^\\d{1,3}$")) {
                JOptionPane.showMessageDialog(this,
                    validMarksGuidelines + "Please enter valid lower marks limit",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            }
            if (!upperMarksLimit.matches("^\\d{1,3}$")) {
                JOptionPane.showMessageDialog(this,
                    validMarksGuidelines + "Please enter valid upper marks limit",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
        else {
            boolean upperMarksRangeValidation = !(Integer.parseInt(upperMarksLimit) >= 0 && Integer.parseInt(upperMarksLimit) <= 100);
            boolean lowerMarksRangeValidation = !(Integer.parseInt(lowerMarksLimit) >= 0 && Integer.parseInt(lowerMarksLimit) <= 100);
            if (upperMarksRangeValidation || lowerMarksRangeValidation)
            {
                searchResultTableModel.setRowCount(0);

                if (lowerMarksRangeValidation) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter lower marks limit between 0 and 100",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                }
                if (upperMarksRangeValidation) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter upper marks limit between 0 and 100",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                }
                return;
            }
            else if (Integer.parseInt(upperMarksLimit) < Integer.parseInt(lowerMarksLimit)) {
                searchResultTableModel.setRowCount(0);
                JOptionPane.showMessageDialog(this,
                    "The upper marks limit cannot be less than the lower marks limit",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        displaySearchResultTable(Integer.parseInt(lowerMarksLimit), Integer.parseInt(upperMarksLimit));

    }

    /**
     * Retrieves and displays the search results from the database
     * @param lowerMarksLimit the lower marks limit value from user input
     * @param upperMarksLimit the upper marks limit value from user input
     */
    private void displaySearchResultTable(int lowerMarksLimit, int upperMarksLimit) {
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {

                String searchByMarksRangeSQL = "SELECT * FROM student WHERE marks BETWEEN ? AND ? ORDER BY marks DESC";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                     PreparedStatement preparedStatement = connection.prepareStatement(searchByMarksRangeSQL)) {

                    preparedStatement.setInt(1, lowerMarksLimit);
                    preparedStatement.setInt(2, upperMarksLimit);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {

                        searchResultTableModel.setRowCount(0);
                        while (resultSet.next()) {
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
                        }

                        if (searchResultTableModel.getRowCount() > 0) {

                            SwingUtilities.invokeLater(() ->

                                    JOptionPane.showMessageDialog(StudentMarksSearchPanel.this,
                                            "Search by marks range was successful",
                                            "Search Successful",
                                            JOptionPane.INFORMATION_MESSAGE)

                            );

                            SwingUtilities.invokeLater(() ->
                                    clearInputFields()
                            );

                        } else {

                            SwingUtilities.invokeLater(() ->

                                    JOptionPane.showMessageDialog(StudentMarksSearchPanel.this,
                                            "Search by marks range failed",
                                            "Search Failed",
                                            JOptionPane.WARNING_MESSAGE)

                            );

                        }

                    }

                } catch (SQLException e) {

                    SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentMarksSearchPanel.this,
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

}
