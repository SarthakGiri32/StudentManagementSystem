import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * This class contains all the code for displaying the entire student database table in a screen
 */
public class ReadStudentPanel extends BasePanel implements TableDisplayColumnNames {

    private DefaultTableModel studentTableModel;
    private JButton downloadButton;
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
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Download student list as csv button
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        downloadButton = new JButton("Download Student Table as CSV");
        downloadButton.setFont(new Font("Arial", Font.BOLD, 14));
        downloadButton.setBackground(new Color(34, 139, 34));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setFocusPainted(false);
        downloadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        downloadButton.setPreferredSize(new Dimension(320, 50));
        downloadButton.addActionListener(_ -> downloadStudentListAsCSV());
        buttonsPanel.add(downloadButton, gbc);

        JPanel returnButtonsFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // 'Go back to user options' button
        JButton returnToUserOptionsButton = new JButton("Return to User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 20));
        returnToUserOptionsButton.addActionListener(_ -> navigationController.navigateTo(USER_OPTIONS));
        returnButtonsFlowPanel.add(returnToUserOptionsButton);   

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 20));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        returnButtonsFlowPanel.add(logoutButton);

        gbc.gridy = 1;
        buttonsPanel.add(returnButtonsFlowPanel, gbc);

        studentTableDisplayPanel.add(buttonsPanel, BorderLayout.SOUTH);
        studentTableDisplayPanel.add(studentTableScrollPane, BorderLayout.CENTER);

        return studentTableDisplayPanel;
    }

    /**
     * Displays a 'Save file' dialog box for the user to choose the destination for saving the exported CSV file
     */
    private void downloadStudentListAsCSV() {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Student List as CSV File");
        fileChooser.setSelectedFile(new File("student_list.csv"));

        int userFileSaveDialogBoxResult = fileChooser.showSaveDialog(this);

        if (userFileSaveDialogBoxResult == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();

            // Ensure .csv extension
            if (!fileToSave.getName().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            exportStudentListToCSVInBackground(fileToSave);

        }

    }

    /**
     * Executes the database and file writing code in the 'exportStudentListToCSV' function in the background without compromising the UI
     * @param fileToSave 'File' class datatype object that points to the exported CSV file
     */
    private void exportStudentListToCSVInBackground(File fileToSave) {
        
        SwingWorker<Integer, Void> worker = new SwingWorker<>() {

            @Override
            protected Integer doInBackground() {
                return exportStudentListToCSV(fileToSave);
            }

            @Override
            protected void done() {
                
                try {
                    int rowCount = get();

                    if (rowCount >= 0) {

                        JOptionPane.showMessageDialog(ReadStudentPanel.this,
                            "CSV file exported successfully!\n" +
                            "File: " + fileToSave.getAbsolutePath() + "\n" +
                            "Total Records: " + rowCount,
                            "Export Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(ReadStudentPanel.this,
                        "Export failed: " + ex,
                        "Export Error",
                        JOptionPane.WARNING_MESSAGE);

                } finally {

                    downloadButton.setEnabled(true);
                    downloadButton.setText("Download Student Table as CSV");

                }

            }
            
        };

        downloadButton.setEnabled(false);
        downloadButton.setText("Exporting...");

        worker.execute();

    }

    /**
     * Executes the database reading and file writing code for exporting the student table records to a CSV file
     * @param fileToSave 'File' class datatype object that points to the exported CSV file
     * @return the number of rows written to the exported CSV file
     */
    private int exportStudentListToCSV(File fileToSave) {
        
        String exportStudentListToCSVSQL = "SELECT * FROM student";

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(exportStudentListToCSVSQL);
             ResultSet resultSet = preparedStatement.executeQuery();
             PrintWriter writer = new PrintWriter(new FileWriter(fileToSave)))
        {
            writer.println(ID + "," + 
                        NAME + "," + 
                        ROLL_NUMBER + "," + 
                        DEPARTMENT + "," + 
                        EMAIL_ID + "," + 
                        PHONE_NUMBER + "," + 
                        MARKS + "," + 
                        GRADE);

            int rowCount = 0;
            while (resultSet.next()) {

                rowCount++;
                writer.printf("%d,%s,%s,%s,%s,%s,%d,%s%n",
                    resultSet.getInt("id"),
                    escapeSpecialCharactersInCSV(resultSet.getString("name")),
                    escapeSpecialCharactersInCSV(resultSet.getString("roll_no")),
                    escapeSpecialCharactersInCSV(resultSet.getString("department")),
                    escapeSpecialCharactersInCSV(resultSet.getString("email")),
                    escapeSpecialCharactersInCSV(resultSet.getString("phone")),
                    resultSet.getInt("marks"),
                    escapeSpecialCharactersInCSV(resultSet.getString("grade"))
                );

            }

            return rowCount;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                "Database error: " + e,
                "MySQL Error",
                JOptionPane.WARNING_MESSAGE);
            return -1;

        } catch (IOException e) {
            
            JOptionPane.showMessageDialog(this,
                "File error: " + e,
                "File Error",
                JOptionPane.WARNING_MESSAGE);
            return -1;

        } 

    }

    /**
     * Surrounds values containing certain special characters with double quotes to prevent CSV parsing errors
     * @param value the data containing special characters
     * @return the value enclosed in double quotes
     */
    private String escapeSpecialCharactersInCSV(String value) {
        if (value == null) return "";

        // Check for any character that requires quoting
        if (value.contains(".")  ||
            value.contains("_")  ||
            value.contains("%")  ||
            value.contains("+")  ||
            value.contains("-")) {

            return "\"" + value + "\"";           // wrap in double quotes

        }

        return value;
    }

    @Override
    public void onNavigatedTo() {
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        displayAllStudentRecords();
    }

    /**
     * Retrieves and displays all student records in a Swing GUI table
     */
    private void displayAllStudentRecords() {
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {

                try (Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {

                    String readStudentDetailsSQLInOrder = "SELECT * FROM student ORDER BY id ASC";

                    try (Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery(readStudentDetailsSQLInOrder)) {

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
                                        "Error: Student data reading failed:\n" + e,
                                        "Database reading error",
                                        JOptionPane.WARNING_MESSAGE)

                        );
                    }

                } catch (SQLException e) {

                    SwingUtilities.invokeLater(() ->

                            JOptionPane.showMessageDialog(ReadStudentPanel.this,
                                    "Error: MySQL server connection failed:\n" + e,
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
