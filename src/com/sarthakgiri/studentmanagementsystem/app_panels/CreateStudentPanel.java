import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateStudentPanel extends BasePanel{

    private String username, password, databaseUrl;
    private JTextField nameField, rollNumberField, departmentField, emailField, phoneField, marksField;
    
    // validation guidelines
    private static final String validNameGuidelines = "Valid name conditions:\n" +
                                                      "1. The name should contain at least a first and last name\n" +
                                                      "2. Each word in the name should start with a upper case letter\n";
    private static final String validEmailGuidelines = "Valid email conditions:\n" +
                                                      "1. The first part of the email (before '@' symbol) can be alphanumeric, and can contain the following characters: ._%+-\n" +
                                                      "2. The second part of the email (after '@' symbol) should follow these rules:\n\t" +
                                                      "a) The second level domain can be alphanumeric, along with these characters: .-\n\t" +
                                                      "b) The top level domain can only be alphabetic of length between 2 and 6 characters (inclusive)\n";
    private static final String validPhoneNumberGuidelines = "Valid phone number conditions:\n" +
                                                            "1. Should start with these two country codes: '+91' or '0'\n" +
                                                            "2. The 10 digit phone number should start with numbers 6 to 9\n";
    private static final String validRollNumberGuidelines = "Valid roll number conditions:\n" +
                                                            "1. The roll number should contain exactly 3 digits\n";
    private static final String validDepartmentNameGuidelines = "Valid department names guidelines:\n" +
                                                                "1. The name should only contain simple alphabetic letters and spaces\n";
    private static final String validMarksGuidelines = "Valid marks guidelines:\n" +
                                                       "1. The marks should be integers between 0 and 100 (inclusive)\n";

    @Override
    protected JPanel build() {

        JPanel createStudentRecordPanel = new JPanel(new GridBagLayout());
        createStudentRecordPanel.setBorder(BorderFactory.createTitledBorder("Create New Student Record"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        createStudentRecordPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        createStudentRecordPanel.add(nameField, gbc);

        // Roll Number field
        gbc.gridx = 0; gbc.gridy = 1;
        createStudentRecordPanel.add(new JLabel("Roll Number:"), gbc);
        gbc.gridx = 1;
        rollNumberField = new JTextField(20);
        createStudentRecordPanel.add(rollNumberField, gbc);

        // Department field
        gbc.gridx = 0; gbc.gridy = 2;
        createStudentRecordPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(20);
        createStudentRecordPanel.add(departmentField, gbc);

        // Email ID field
        gbc.gridx = 0; gbc.gridy = 3;
        createStudentRecordPanel.add(new JLabel("Email ID:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        createStudentRecordPanel.add(emailField, gbc);

        // Phone Number field
        gbc.gridx = 0; gbc.gridy = 4;
        createStudentRecordPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        createStudentRecordPanel.add(phoneField, gbc);

        // Marks field
        gbc.gridx = 0; gbc.gridy = 5;
        createStudentRecordPanel.add(new JLabel("Marks:"), gbc);
        gbc.gridx = 1;
        marksField = new JTextField(20);
        createStudentRecordPanel.add(marksField, gbc);

        // create student record button
        JPanel uiButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JPanel uiButtonPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JButton createStudentRecordButton = new JButton("Create Student Record");
        createStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        createStudentRecordButton.addActionListener(e -> createNewStudentRecord());

        // clear fields button;
        JButton clearInputFieldsButton = new JButton("Clear Input Fields");
        clearInputFieldsButton.setPreferredSize(new Dimension(200, 32));
        clearInputFieldsButton.addActionListener(e -> clearInputFields());

        // return to user options button
        JButton returnToUserOptionsButton = new JButton("Return To User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToUserOptionsButton.addActionListener(e -> navigationController.navigateTo(USER_OPTIONS));

        // logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));

        uiButtonPanel.add(createStudentRecordButton);
        uiButtonPanel.add(clearInputFieldsButton);
        uiButtonPanel2.add(returnToUserOptionsButton);
        uiButtonPanel2.add(logoutButton);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        createStudentRecordPanel.add(uiButtonPanel, gbc);

        gbc.gridy = 7;
        createStudentRecordPanel.add(uiButtonPanel2, gbc);

        return createStudentRecordPanel;

    }

    @Override
    public void onNavigatedTo() {
        
        username = navigationController.getData("username");
        password = navigationController.getData("password");
        databaseUrl = navigationController.getData("databaseUrl");

        clearInputFields();

    }

    private void createNewStudentRecord() {
        
        String name = nameField.getText().trim();
        String rollNo = rollNumberField.getText().trim();
        String department = departmentField.getText().trim();
        String emailID = emailField.getText().trim();
        String phoneNumber = phoneField.getText().trim();
        String marks = marksField.getText().trim();

        if (name.isEmpty() ||
            rollNo.isEmpty() ||
            department.isEmpty() ||
            emailID.isEmpty() ||
            phoneNumber.isEmpty() ||
            marks.isEmpty())
        {
            JOptionPane.showMessageDialog(this, 
                "All fields required to create a new record!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate name
        String regex = "(\\p{Upper}\\p{Lower}+\\s?){2,3}";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(name).matches() ||
            !rollNo.matches("^\\d{3}$") ||
            !department.matches("^[A-Za-z\\s]{2,100}$") ||
            !emailID.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$") ||
            !phoneNumber.matches("^((\\Q+91\\E)|0)?[6-9][0-9]{9}$") ||
            !marks.matches("^\\d{1,3}$"))
        {
            if (!pattern.matcher(name).matches()) {
                JOptionPane.showMessageDialog(this, 
                    validNameGuidelines + "Please enter a valid name", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);   
            }
            if (!rollNo.matches("^\\d{3}$")) {
                JOptionPane.showMessageDialog(this,
                    validRollNumberGuidelines + "Please enter a valid roll number",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            }
            if (!department.matches("^[A-Za-z\\s]{2,100}$")) {
                JOptionPane.showMessageDialog(this,
                    validDepartmentNameGuidelines + "Please enter a valid department name",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            }
            if (!emailID.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                JOptionPane.showMessageDialog(this, 
                    validEmailGuidelines + "Please enter a valid email ID", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
            }
            if (!phoneNumber.matches("^((\\Q+91\\E)|0)?[6-9][0-9]{9}$")) {
                JOptionPane.showMessageDialog(this, 
                    validPhoneNumberGuidelines + "Please enter a valid phone number", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
            }
            if (!marks.matches("^\\d{1,3}$")) {
                JOptionPane.showMessageDialog(this,
                    validMarksGuidelines + "Please enter valid marks",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            }
            return;
        }

        int marksInt = Integer.parseInt(marks);
        if (!(marksInt >= 0 && marksInt <= 100)) {
            JOptionPane.showMessageDialog(this,
                validMarksGuidelines + "Please enter valid marks",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        char gradeChar = generateGrade(marksInt);

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {

            insertNewStudentRecord(connection, name, rollNo, department, emailID, phoneNumber, marksInt, gradeChar);

            clearInputFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: MySQL server connection failed:\n" + e.toString(),
                "Server connection failed",
                JOptionPane.WARNING_MESSAGE
            );
        }
        
    }

    private void clearInputFields() {
        nameField.setText("");
        rollNumberField.setText("");
        departmentField.setText("");
        emailField.setText("");
        phoneField.setText("");
        marksField.setText("");
        nameField.requestFocus();
    }

    private void insertNewStudentRecord(Connection connection, String name, String rollNumber, String departmentName, String emailID, String phoneNumber, int marks, char grade) {

        String insertStudentDetailSQL = "INSERT INTO student (name, roll_no, department, email, phone, marks, grade) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStudentDetailSQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, rollNumber);
            preparedStatement.setString(3, departmentName);
            preparedStatement.setString(4, emailID);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setInt(6, marks);
            preparedStatement.setString(7, Character.toString(grade));
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, 
                "Student details created successfully", 
                "New student record created", 
                JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {

            String errorMessage = "Error: Data insertion failed:\n" + e.toString() + "\n";

            if (e.getMessage().toLowerCase().contains("duplicate")) {
                errorMessage += "The data you tried to enter already exists. Please try again.\n";
            } else if (e.getMessage().toLowerCase().contains("null")) {
                errorMessage += "The data cannot be null. Please try again.\n";
            }

            JOptionPane.showMessageDialog(this,
                errorMessage,
                "New student record creation failed",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private char generateGrade(int marks) {

        if (marks >= 90 && marks <= 100) {
            return 'A';
        } else if (marks >= 80 && marks <= 89) {
            return 'B';
        } else if (marks >= 70 && marks <= 79) {
            return 'C';
        } else if (marks >= 60 && marks <= 69) {
            return 'D';
        } else if (marks >= 50 && marks <= 59) {
            return 'E';
        } else {
            return 'F';
        }
    }

}
