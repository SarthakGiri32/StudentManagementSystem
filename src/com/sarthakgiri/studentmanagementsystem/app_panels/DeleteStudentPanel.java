import java.awt.Dimension;
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
import javax.swing.JTextField;

public class DeleteStudentPanel extends BasePanel {

    private String databaseUrl, username, password;
    private JTextField rollNumberField;

    @Override
    protected JPanel build() {
         
        JPanel deleteStudentRecordsPanel = new JPanel(new GridBagLayout());
        deleteStudentRecordsPanel.setBorder(BorderFactory.createTitledBorder("Delete Existing Student Record"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        deleteStudentRecordsPanel.add(new JLabel("Existing roll number:"), gbc);
        gbc.gridx = 1;
        rollNumberField = new JTextField(20);
        deleteStudentRecordsPanel.add(rollNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JButton deleteStudentRecordButton = new JButton("Delete Student Record");
        deleteStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        deleteStudentRecordButton.addActionListener(e -> deleteStudentRecord());
        deleteStudentRecordsPanel.add(deleteStudentRecordButton, gbc);

        gbc.gridx = 1;
        JButton clearInputFieldsButton = new JButton("Clear Input Fields");
        clearInputFieldsButton.setPreferredSize(new Dimension(200, 32));
        clearInputFieldsButton.addActionListener(e -> clearInputFields());
        deleteStudentRecordsPanel.add(clearInputFieldsButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JButton returnToUserOptionsButton = new JButton("Return to User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToUserOptionsButton.addActionListener(e -> navigationController.navigateTo(USER_OPTIONS));
        deleteStudentRecordsPanel.add(returnToUserOptionsButton, gbc);

        gbc.gridx = 1;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));
        deleteStudentRecordsPanel.add(logoutButton, gbc);

        return deleteStudentRecordsPanel;

    }

    @Override
    public void onNavigatedTo() {
        
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        clearInputFields();

    }

    private void deleteStudentRecord() {
        
        String rollNumber = rollNumberField.getText().trim();

        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "All fields required to delete a student record!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!rollNumber.matches("^\\d{3}$")) {
            JOptionPane.showMessageDialog(this,
                validRollNumberGuidelines + "Please enter a valid roll number",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!doesRollNumberExist(rollNumber)) {
            JOptionPane.showMessageDialog(this,
                "Please enter an existing roll number",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String deleteStudentRecordSQL = "DELETE FROM student WHERE roll_no = ?";

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentRecordSQL)) {

            preparedStatement.setString(1, rollNumber);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                JOptionPane.showMessageDialog(this,
                    "Student data was deleted successfully",
                    "Data Deletion Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Student data deletion failed",
                    "Data Deletion Failed",
                    JOptionPane.WARNING_MESSAGE);
            }
            clearInputFields();
            
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(this, 
                "Error:\n" + e.toString(),
                "MySQL Error",
                JOptionPane.WARNING_MESSAGE);
        }

    }

    private boolean doesRollNumberExist(String rollNumber) {

        String validateRollNumberExistenceSQL = "SELECT EXISTS(SELECT 1 FROM student WHERE roll_no = ?)";

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(validateRollNumberExistenceSQL))
        {

            preparedStatement.setString(1, rollNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) == 1;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error:\n" + e.toString(),
                "MySQL Error",
                JOptionPane.WARNING_MESSAGE
            );
        }

        return false;
    }

    @Override
    protected void clearInputFields() {
        rollNumberField.setText("");
        rollNumberField.requestFocus();
    }

}
