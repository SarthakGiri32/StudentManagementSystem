import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class ReadStudentPanel extends BasePanel implements TableDisplayColumnNames {

    private DefaultTableModel studentTableModel;
    private String username, password, databaseUrl;

    @Override
    protected JPanel build() {
        
        JPanel studentTableDisplayPanel = new JPanel(new BorderLayout(5, 5));
        studentTableDisplayPanel.setBorder(BorderFactory.createTitledBorder("Student Table Display"));

        // column names initialization
        String[] columnHeaders = {ID, NAME, ROLL_NUMBER, DEPARTMENT, EMAIL_ID, PHONE_NUMBER, MARKS, GRADE};
        studentTableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);

        // Column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Roll Number
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Department
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(250); // Email ID
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Phone Number
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(50); // Marks
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(50); // Grade

        JScrollPane studentTableScrollPane = new JScrollPane(studentTable);

        // button panel
        JPanel returnButtonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 'Go back to user options' button
        gbc.gridx = 0; gbc.gridy = 0;
        JButton returnToUserOptionsButton = new JButton("Return to User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 20));
        returnToUserOptionsButton.addActionListener(e -> navigationController.navigateTo(USER_OPTIONS));
        returnButtonsPanel.add(returnToUserOptionsButton, gbc);

        gbc.gridx = 1;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 20));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));
        returnButtonsPanel.add(logoutButton, gbc);

        studentTableDisplayPanel.add(returnButtonsPanel, BorderLayout.SOUTH);
        studentTableDisplayPanel.add(studentTableScrollPane, BorderLayout.CENTER);

        return studentTableDisplayPanel;
    }

    @Override
    public void onNavigatedTo() {
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        displayAllStudentRecords();
    }

    private void displayAllStudentRecords() {
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() {
                
                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {

                    String readStudentDetailsSQLInOrder = "SELECT * FROM student ORDER BY id ASC";

                    try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(readStudentDetailsSQLInOrder)) 
                    {
                        
                        studentTableModel.setRowCount(0);
                        while (resultSet.next()) {
                            studentTableModel.addRow(new Object[]{
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
                    } catch (SQLException e) {
                            
                        SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(ReadStudentPanel.this,
                                "Error: Student data reading failed:\n" + e.toString(),
                                "Database reading error",
                                JOptionPane.WARNING_MESSAGE)

                        );
                    }

                } catch (SQLException e) {
                        
                    SwingUtilities.invokeLater(() ->

                        JOptionPane.showMessageDialog(ReadStudentPanel.this,
                            "Error: MySQL server connection failed:\n" + e.toString(),
                            "Server connection failed",
                            JOptionPane.WARNING_MESSAGE)

                    );

                }
                return null;
            }
        };

        worker.execute();
    }

}
