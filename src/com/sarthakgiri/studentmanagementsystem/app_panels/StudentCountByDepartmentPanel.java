import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

/**
 * This class contains all the code for displaying the department-wise student count in table format
 */
public class StudentCountByDepartmentPanel extends BasePanel implements StatisticsDisplayColumnNames {

    private String databaseUrl, username, password;
    private DefaultTableModel studentCountByDepartmentTableModel;

    @Override
    protected JPanel build() {
        
        JPanel studentCountByDepartmentTablePanel = new JPanel(new BorderLayout(5, 5));
        studentCountByDepartmentTablePanel.setBorder(BorderFactory.createTitledBorder("Student Count Grouped By Department Table"));

        // column names
        String[] columnHeaders = {STUDENT_DEPARTMENT, STUDENT_COUNT_BY_DEPARTMENT};
        studentCountByDepartmentTableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable studentCountByDepartmentTable = new JTable(studentCountByDepartmentTableModel);
        studentCountByDepartmentTable.setRowHeight(25);
        studentCountByDepartmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentCountByDepartmentTable.getTableHeader().setReorderingAllowed(false);

        studentCountByDepartmentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        studentCountByDepartmentTable.getColumnModel().getColumn(1).setPreferredWidth(150);

        JScrollPane studentCountByDepartmentTableScrollPane = new JScrollPane(studentCountByDepartmentTable);

        JPanel returnToStudentStatisticOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton returnToStudentStatisticOptionsButton = new JButton("Return to Student Statistic Options");
        returnToStudentStatisticOptionsButton.setPreferredSize(new Dimension(300, 32));
        returnToStudentStatisticOptionsButton.addActionListener(_ -> navigationController.navigateTo(STUDENT_STATS));
        returnToStudentStatisticOptionsPanel.add(returnToStudentStatisticOptionsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(300, 32));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        returnToStudentStatisticOptionsPanel.add(logoutButton);

        studentCountByDepartmentTablePanel.add(studentCountByDepartmentTableScrollPane, BorderLayout.CENTER);
        studentCountByDepartmentTablePanel.add(returnToStudentStatisticOptionsPanel, BorderLayout.SOUTH);

        return studentCountByDepartmentTablePanel;

    }

    @Override
    public void onNavigatedTo() {
        
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        displayStudentCountByDepartment();

    }

    /**
     * Retrieves and displays the department-wise student count in table format
     */
    private void displayStudentCountByDepartment() {
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {

                String studentCountByDepartmentSQL = "SELECT department, COUNT(*) AS total_students " +
                        "FROM student GROUP BY department ORDER BY total_students DESC";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                     PreparedStatement preparedStatement = connection.prepareStatement(studentCountByDepartmentSQL);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    studentCountByDepartmentTableModel.setRowCount(0);
                    while (resultSet.next()) {
                        studentCountByDepartmentTableModel.addRow(new Object[]{
                                resultSet.getString("department"),
                                resultSet.getInt("total_students")
                        });
                    }

                    if (studentCountByDepartmentTableModel.getRowCount() == 0) {

                        SwingUtilities.invokeLater(() ->

                                JOptionPane.showMessageDialog(StudentCountByDepartmentPanel.this,
                                        "Student count by department retrieval failed",
                                        "Department-wise Student Count Retrieval Failed",
                                        JOptionPane.WARNING_MESSAGE)

                        );

                    }

                } catch (SQLException e) {

                    SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentCountByDepartmentPanel.this,
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
