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

public class StudentDepartmentSearchPanel extends BasePanel implements TableDisplayColumnNames {

    private String databaseUrl, username, password;
    private DefaultTableModel searchResultTableModel;
    private JTextField departmentSearchInputField;

    @Override
    protected JPanel build() {
        JPanel studentDepartmentSearchPanel = new JPanel(new BorderLayout(10, 10));
        
        studentDepartmentSearchPanel.add(createSearchInputPanel(), BorderLayout.NORTH);
        studentDepartmentSearchPanel.add(createSearchResultDisplayPanel(), BorderLayout.CENTER);

        return studentDepartmentSearchPanel;
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
        departmentSearchInputField.setText("");
        departmentSearchInputField.requestFocus();
    }

    private JPanel createSearchInputPanel() {
        
        JPanel searchInputFieldPanel = new JPanel(new GridBagLayout());
        searchInputFieldPanel.setBorder(BorderFactory.createTitledBorder("Search By Department Input"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        searchInputFieldPanel.add(new JLabel("Existing department name:"), gbc);
        gbc.gridx = 1;
        departmentSearchInputField = new JTextField(20);
        searchInputFieldPanel.add(departmentSearchInputField, gbc);

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 32));
        searchButton.addActionListener(e -> searchStudentByDepartment());
        searchButtonPanel.add(searchButton);

        JButton returnToSearchOptionsButton = new JButton("Return to Search Options");
        returnToSearchOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToSearchOptionsButton.addActionListener(e -> navigationController.navigateTo(SEARCH_STUDENT));
        searchButtonPanel.add(returnToSearchOptionsButton);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        searchInputFieldPanel.add(searchButtonPanel, gbc);

        gbc.gridy = 2;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));
        searchInputFieldPanel.add(logoutButton, gbc);

        return searchInputFieldPanel;
        
    }

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

    private void searchStudentByDepartment() {
        
        String department = departmentSearchInputField.getText().trim();

        if (department.isEmpty()) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, 
                "Department name data is required to search through the database!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!department.matches("^[A-Za-z\\s]{2,100}$")) {
            searchResultTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this,
                validDepartmentNameGuidelines + "Please enter a valid department name",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        displaySearchResultTable(department);

    }

    private void displaySearchResultTable(String department) {
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() {

                String searchByDepartmentNameSQL = "SELECT * FROM student WHERE department = ?";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                    PreparedStatement preparedStatement = connection.prepareStatement(searchByDepartmentNameSQL))
                {

                    preparedStatement.setString(1, department);

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

                                JOptionPane.showMessageDialog(StudentDepartmentSearchPanel.this,
                                    "Search by department name was successful",
                                    "Search Successful",
                                    JOptionPane.INFORMATION_MESSAGE)

                            );

                            SwingUtilities.invokeLater(() -> 
                                clearInputFields()
                            );

                        } else {

                            SwingUtilities.invokeLater(() ->

                                JOptionPane.showMessageDialog(StudentDepartmentSearchPanel.this,
                                    "Search by department name failed",
                                    "Search Failed",
                                    JOptionPane.WARNING_MESSAGE)

                            );

                        }
       
                    }

                } catch (SQLException e) {
                            
                    SwingUtilities.invokeLater(() ->

                        JOptionPane.showMessageDialog(StudentDepartmentSearchPanel.this,
                            "Error:\n" + e.toString(),
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
