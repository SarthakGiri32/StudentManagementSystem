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
import java.util.regex.Pattern;

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
 * This class contains all the code for searching through the database based on student name from user input in a screen
 */
public class StudentNameSearchPanel extends BasePanel implements TableDisplayColumnNames {

    private String databaseUrl, username, password;
    private DefaultTableModel searchResultTableModel;
    private JTextField nameSearchInputField;

    @Override
    protected JPanel build() {
        
        JPanel studentNameSearchPanel = new JPanel(new BorderLayout(10, 10));
        
        studentNameSearchPanel.add(createSearchInputPanel(), BorderLayout.NORTH);
        studentNameSearchPanel.add(createSearchResultDisplayPanel(), BorderLayout.CENTER);

        return studentNameSearchPanel;

    }

    /**
     * This panel section displays the user input option
     * @return a JPanel object containing all the elements necessary for user input
     */
    private JPanel createSearchInputPanel() {
        
        JPanel searchInputFieldPanel = new JPanel(new GridBagLayout());
        searchInputFieldPanel.setBorder(BorderFactory.createTitledBorder("Search By Name Input"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        searchInputFieldPanel.add(new JLabel("Existing name:"), gbc);
        gbc.gridx = 1;
        nameSearchInputField = new JTextField(20);
        searchInputFieldPanel.add(nameSearchInputField, gbc);

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 32));
        searchButton.addActionListener(_ -> searchStudentByName());
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
     * Validates the user search input and calls the function to display the search results
     */
    private void searchStudentByName() {
        
        String name = nameSearchInputField.getText().trim();

        String regex = "(\\p{Upper}\\p{Lower}+\\s?){2,3}";
        Pattern pattern = Pattern.compile(regex);

        if (name.isEmpty()) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, 
                "Name data is required to search through the database!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!pattern.matcher(name).matches()) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, 
                validNameGuidelines + "Please enter a valid name", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        displaySearchResultTable(name);
        
    }

    /**
     * Retrieves and displays the search results from the database
     * @param name the user search input value
     */
    private void displaySearchResultTable(String name) {
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {

                String searchByNameSQL = "SELECT * FROM student WHERE name = ?";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                     PreparedStatement preparedStatement = connection.prepareStatement(searchByNameSQL)) {

                    preparedStatement.setString(1, name);

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

                                    JOptionPane.showMessageDialog(StudentNameSearchPanel.this,
                                            "Search by name was successful",
                                            "Search Successful",
                                            JOptionPane.INFORMATION_MESSAGE)

                            );

                            SwingUtilities.invokeLater(() ->
                                    clearInputFields()
                            );

                        } else {
                            SwingUtilities.invokeLater(() ->

                                    JOptionPane.showMessageDialog(StudentNameSearchPanel.this,
                                            "Search by name failed",
                                            "Search Failed",
                                            JOptionPane.WARNING_MESSAGE)

                            );
                        }

                    }

                } catch (SQLException e) {

                    SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentNameSearchPanel.this,
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
        nameSearchInputField.setText("");
        nameSearchInputField.requestFocus();
    }

}
