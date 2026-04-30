import java.awt.Dimension;
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
import javax.swing.JTextField;

/**
 * This class contains all the code for updating any column in a student record based on user input in a screen
 */
public class UpdateStudentPanel extends BasePanel {

    private String username, password, databaseUrl;
    private JTextField columnNameField, newColumnDataField, rollNumberField;

    @Override
    protected JPanel build() {
        
        JPanel updateStudentRecordPanel = new JPanel(new GridBagLayout());
        updateStudentRecordPanel.setBorder(BorderFactory.createTitledBorder("Update Existing Student Record"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        updateStudentRecordPanel.add(new JLabel("Field to update:"), gbc);
        gbc.gridx = 1;
        columnNameField = new JTextField(20);
        updateStudentRecordPanel.add(columnNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        updateStudentRecordPanel.add(new JLabel("New student data:"), gbc);
        gbc.gridx = 1;
        newColumnDataField = new JTextField(20);
        updateStudentRecordPanel.add(newColumnDataField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        updateStudentRecordPanel.add(new JLabel("Existing roll number:"), gbc);
        gbc.gridx = 1;
        rollNumberField = new JTextField(20);
        updateStudentRecordPanel.add(rollNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JButton updateStudentRecordButton = new JButton("Update Student Record");
        updateStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        updateStudentRecordButton.addActionListener(_ -> updateStudentRecord());
        updateStudentRecordPanel.add(updateStudentRecordButton, gbc);

        gbc.gridx = 1;
        JButton clearInputFieldsButton = new JButton("Clear Input Fields");
        clearInputFieldsButton.setPreferredSize(new Dimension(200, 32));
        clearInputFieldsButton.addActionListener(_ -> clearInputFields());
        updateStudentRecordPanel.add(clearInputFieldsButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JButton returnToUserOptionsButton = new JButton("Return to User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToUserOptionsButton.addActionListener(_ -> navigationController.navigateTo(USER_OPTIONS));
        updateStudentRecordPanel.add(returnToUserOptionsButton, gbc);

        gbc.gridx = 1;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        updateStudentRecordPanel.add(logoutButton, gbc);

        return updateStudentRecordPanel;

    }

    @Override
    public void onNavigatedTo() {
        
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        clearInputFields();
    }

    /**
     * Validates user input and calls the function to update student record in the database
     */
    private void updateStudentRecord() {
        
        String columnName = columnNameField.getText().trim();
        String newColumnData = newColumnDataField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();

        if (columnName.isEmpty() || newColumnData.isEmpty() || rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "All fields required to update a student record!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pattern tableColumnChoice = Pattern.compile(
            "\\b(name|roll_no|department|email|phone|marks)\\b",
            Pattern.CASE_INSENSITIVE
        );

        boolean isValid = true;

        if (!tableColumnChoice.matcher(columnName).matches()) {
            JOptionPane.showMessageDialog(this, 
                validColumnNamesGuidelines + "Please enter a valid column name", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            isValid = false;
        } else {
            switch (columnName.toLowerCase()) {
                case "name":
                    // validate name
                    String regex = "(\\p{Upper}\\p{Lower}+\\s?){2,3}";
                    Pattern namePattern = Pattern.compile(regex);
                    if (!namePattern.matcher(newColumnData).matches()) {
                        JOptionPane.showMessageDialog(this, 
                            validNameGuidelines + "Please enter a valid name", 
                            "Validation Error", 
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;  
                    }
                    break;
                case "roll_no":
                    if (!newColumnData.matches("^\\d{3}$")) {
                        JOptionPane.showMessageDialog(this,
                            validRollNumberGuidelines + "Please enter a valid roll number",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;
                    }
                    break;
                case "department":
                    if (!newColumnData.matches("^[A-Za-z\\s]{2,100}$")) {
                        JOptionPane.showMessageDialog(this,
                            validDepartmentNameGuidelines + "Please enter a valid department name",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;
                    }
                    break;
                case "email":
                    if (!newColumnData.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                        JOptionPane.showMessageDialog(this, 
                            validEmailGuidelines + "Please enter a valid email ID", 
                            "Validation Error", 
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;
                    }
                    break;
                case "phone":
                    if (!newColumnData.matches("^((\\Q+91\\E)|0)?[6-9][0-9]{9}$")) {
                        JOptionPane.showMessageDialog(this, 
                            validPhoneNumberGuidelines + "Please enter a valid phone number", 
                            "Validation Error", 
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;
                    }
                    break;
                case "marks":
                    if (!newColumnData.matches("^\\d{1,3}$")) {
                        JOptionPane.showMessageDialog(this,
                            validMarksGuidelines + "Please enter valid marks",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;
                    } else if (!(Integer.parseInt(newColumnData) >= 0 && Integer.parseInt(newColumnData) <= 100)) {
                        JOptionPane.showMessageDialog(this,
                            "Please enter marks between 0 and 100",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                        isValid = false;
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(this,
                        "Invalid column name",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                    isValid = false;
            }

            if (!rollNumber.matches("^\\d{3}$")) {
                JOptionPane.showMessageDialog(this,
                    validRollNumberGuidelines + "Please enter a valid roll number",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE); 
                isValid = false;             
            } else if (!doesRollNumberExist(rollNumber)) {
                JOptionPane.showMessageDialog(this,
                    "Please enter an existing roll number",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                isValid = false;
            }
        }

        if (isValid) {
            updateStudentRecordData(columnName.toLowerCase(), newColumnData, rollNumber);
        }        

    }

    /**
     * Validates the existence of a roll number in the database
     * @param rollNumber the value to validate
     * @return boolean 'true' if the value exists in the database
     */
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
                "Error:\n" + e,
                "MySQL Error",
                JOptionPane.WARNING_MESSAGE
            );
        }

        return false;
    }

    /**
     * Updates the value in a specific column of a student record in the database
     * @param columnName the column whose value has to be updated
     * @param newColumnData the new value
     * @param rollNumber used to locate the student record that has to be updated
     */
    private void updateStudentRecordData(String columnName, String newColumnData, String rollNumber) {
        
        String updateStudentRecordSQL = "UPDATE student SET " + columnName + " = ? WHERE roll_no = ?";
        String updateStudentMarksAndGradeSQL = "UPDATE student SET marks = ?, grade = ? WHERE roll_no = ?";

        int rowAffected;

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(updateStudentRecordSQL);
            PreparedStatement preparedStatement2 = connection.prepareStatement(updateStudentMarksAndGradeSQL))
        {

            if (columnName.equals("marks")) {
                int marks = Integer.parseInt(newColumnData); 
                char grade = generateGrade(marks);

                preparedStatement2.setInt(1, marks);
                preparedStatement2.setString(2, String.valueOf(grade));
                preparedStatement2.setString(3, rollNumber);

                rowAffected = preparedStatement2.executeUpdate();
            } else {

                preparedStatement.setString(1, newColumnData);
                preparedStatement.setString(2, rollNumber);
                
                rowAffected = preparedStatement.executeUpdate();
            }

            if (rowAffected > 0) {
                JOptionPane.showMessageDialog(this,
                    "Student data was updated successfully",
                    "Data Update Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Student data update failed",
                    "Data Update Failed",
                    JOptionPane.WARNING_MESSAGE);
            }
            clearInputFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error:\n" + e,
                "MySQL Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    @Override
    protected void clearInputFields() {
        columnNameField.setText("");
        newColumnDataField.setText("");
        rollNumberField.setText("");
        columnNameField.requestFocus();
    }

}
