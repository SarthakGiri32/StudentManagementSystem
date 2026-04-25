import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
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
        String[] columnHeaders = {ID, NAME, ROLL_NUMBER, DEPARTMENT, EMAIL_ID, PHONE_NUMBER, MARKS};
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

        JScrollPane studentTableScrollPane = new JScrollPane(studentTable);

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
                                resultSet.getInt("marks")
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
