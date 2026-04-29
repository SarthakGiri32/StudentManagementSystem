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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class StudentCountAndHighestAndLowestMarksPanel extends BasePanel implements StatisticsDisplayColumnNames {

    private String databaseUrl, username, password;
    private DefaultTableModel studentCountTableModel, highestAndLowestMarksTableModel;

    @Override
    protected JPanel build() {
        
        JPanel studentCountAndMarksPanel = new JPanel(new GridBagLayout());
        studentCountAndMarksPanel.setBorder(BorderFactory.createTitledBorder("Student Count and Marks Statistics"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 0.3;
        studentCountAndMarksPanel.add(createStudentCountPanel(), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 1.0; gbc.weighty = 0.5;
        studentCountAndMarksPanel.add(createHighestAndLowestMarksPanel(), gbc);

        JPanel returnToStudentStatisticOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton returnToStudentStatisticOptionsButton = new JButton("Return to Student Statistic Options");
        returnToStudentStatisticOptionsButton.setPreferredSize(new Dimension(300, 32));
        returnToStudentStatisticOptionsButton.addActionListener(e -> navigationController.navigateTo(STUDENT_STATS));
        returnToStudentStatisticOptionsPanel.add(returnToStudentStatisticOptionsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(300, 32));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));
        returnToStudentStatisticOptionsPanel.add(logoutButton);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 1.0; gbc.weighty = 0.2;
        studentCountAndMarksPanel.add(returnToStudentStatisticOptionsPanel, gbc);

        return studentCountAndMarksPanel;

    }

    @Override
    public void onNavigatedTo() {
        
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        displayStudentTotalCount();
        displayStudentHighestAndLowestMarks();

    }

    private JPanel createStudentCountPanel() {
        
        JPanel studentCountTablePanel = new JPanel(new BorderLayout(5, 5));
        studentCountTablePanel.setBorder(BorderFactory.createTitledBorder("Total Student Count Table"));

        // column names
        String[] columnHeaders = {TOTAL_STUDENT_COUNT};
        studentCountTableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        JTable studentCountTable = new JTable(studentCountTableModel);
        studentCountTable.setRowHeight(25);
        studentCountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentCountTable.getTableHeader().setReorderingAllowed(false);

        studentCountTable.getColumnModel().getColumn(0).setPreferredWidth(300);

        JScrollPane studentCountTableScrollPane = new JScrollPane(studentCountTable);

        studentCountTablePanel.add(studentCountTableScrollPane, BorderLayout.CENTER);

        return studentCountTablePanel;

    }

    private JPanel createHighestAndLowestMarksPanel() {
        
        JPanel highestAndLowestMarksTablePanel = new JPanel(new BorderLayout(5, 5));
        highestAndLowestMarksTablePanel.setBorder(BorderFactory.createTitledBorder("Student Highest And Lowest Marks Table"));

        // column names
        String[] columnHeaders = {STUDENT_HIGHEST_MARKS, STUDENT_LOWEST_MARKS};
        highestAndLowestMarksTableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        JTable highestAndLowestMarksTable = new JTable(highestAndLowestMarksTableModel);
        highestAndLowestMarksTable.setRowHeight(25);
        highestAndLowestMarksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        highestAndLowestMarksTable.getTableHeader().setReorderingAllowed(false);

        highestAndLowestMarksTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        highestAndLowestMarksTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        JScrollPane highestAndLowestMarksTableScrollPane = new JScrollPane(highestAndLowestMarksTable);

        highestAndLowestMarksTablePanel.add(highestAndLowestMarksTableScrollPane, BorderLayout.CENTER);

        return highestAndLowestMarksTablePanel;

    }

    private void displayStudentTotalCount() {

        SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {

            @Override
            protected Void doInBackground() {

                String studentTotalCountSQL = "SELECT COUNT(*) AS total_students FROM student";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                    PreparedStatement preparedStatement = connection.prepareStatement(studentTotalCountSQL);
                    ResultSet resultSet = preparedStatement.executeQuery())
                {
                    
                    studentCountTableModel.setRowCount(0);
                    if (resultSet.next()) {
                        studentCountTableModel.addRow(new Object[] {
                            resultSet.getInt("total_students")
                        });

                        SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentCountAndHighestAndLowestMarksPanel.this,
                                "Total student count was retrieved successfully",
                                "Total Student Count Retrieval Successful",
                                JOptionPane.INFORMATION_MESSAGE)

                        );

                    } else {

                        SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentCountAndHighestAndLowestMarksPanel.this,
                                "Total student count retrieval failed",
                                "Total Student Count Retrieval Failed",
                                JOptionPane.WARNING_MESSAGE)

                        );

                    }

                } catch (SQLException e) {
                            
                    SwingUtilities.invokeLater(() ->

                        JOptionPane.showMessageDialog(StudentCountAndHighestAndLowestMarksPanel.this,
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
    
    private void displayStudentHighestAndLowestMarks() {

        SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {

            @Override
            protected Void doInBackground() {

                String highestAndLowestMarksSQL = "SELECT MAX(marks) AS highest_marks, MIN(marks) AS lowest_marks FROM student";

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
                    PreparedStatement preparedStatement = connection.prepareStatement(highestAndLowestMarksSQL);
                    ResultSet resultSet = preparedStatement.executeQuery())
                {
                
                    highestAndLowestMarksTableModel.setRowCount(0);
                    if (resultSet.next()) {
                        highestAndLowestMarksTableModel.addRow(new Object[] {
                            resultSet.getInt("highest_marks"),
                            resultSet.getInt("lowest_marks")
                        });

                        SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentCountAndHighestAndLowestMarksPanel.this,
                                "Highest and lowest marks have been retrieved successfully",
                                "Highest and lowest Marks Retrieval Successful",
                                JOptionPane.INFORMATION_MESSAGE)

                        );

                    } else {

                        SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(StudentCountAndHighestAndLowestMarksPanel.this,
                                "Highest and lowest marks retrieval failed",
                                "Highest and lowest Marks Retrieval Failed",
                                JOptionPane.WARNING_MESSAGE)

                        );

                    }
                    
                } catch (SQLException e) {
                            
                    SwingUtilities.invokeLater(() ->

                        JOptionPane.showMessageDialog(StudentCountAndHighestAndLowestMarksPanel.this,
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
