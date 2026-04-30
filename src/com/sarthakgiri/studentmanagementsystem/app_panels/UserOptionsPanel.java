import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class contains all the code for displaying - in a screen - the various user operations available in the Student Management System
 */
public class UserOptionsPanel extends BasePanel {

    @Override
    protected JPanel build() {
        
        JPanel userOptionsPanel = new JPanel(new GridBagLayout());
        userOptionsPanel.setBorder(BorderFactory.createTitledBorder("Student Management System Options"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Create Student Option
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JButton createStudentRecordButton = new JButton("Create Student Record");
        createStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        createStudentRecordButton.addActionListener(_ -> navigationController.navigateTo(CREATE_STUDENT));
        userOptionsPanel.add(createStudentRecordButton, gbc);

        // Read Student Option
        gbc.gridy = 1; 
        JButton displayAllStudentButton = new JButton("Read Student Records");
        displayAllStudentButton.setPreferredSize(new Dimension(200, 32));
        displayAllStudentButton.addActionListener(_ -> navigationController.navigateTo(READ_STUDENT));
        userOptionsPanel.add(displayAllStudentButton, gbc);

        // Update Student Option
        gbc.gridy = 2;
        JButton updateStudentRecordButton = new JButton("Update Student Record");
        updateStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        updateStudentRecordButton.addActionListener(_ -> navigationController.navigateTo(UPDATE_STUDENT));
        userOptionsPanel.add(updateStudentRecordButton, gbc);

        // Delete Student Option
        gbc.gridy = 3;
        JButton deleteStudentRecordButton = new JButton("Delete Student Record");
        deleteStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        deleteStudentRecordButton.addActionListener(_ -> navigationController.navigateTo(DELETE_STUDENT));
        userOptionsPanel.add(deleteStudentRecordButton, gbc);

        // Search Student Option
        gbc.gridy = 4;
        JButton searchStudentRecordButton = new JButton("Search Student Record");
        searchStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        searchStudentRecordButton.addActionListener(_ -> navigationController.navigateTo(SEARCH_STUDENT));
        userOptionsPanel.add(searchStudentRecordButton, gbc);

        // Student Statistic Option
        gbc.gridy = 5;
        JButton studentStatisticButton = new JButton("Student Statistics");
        studentStatisticButton.setPreferredSize(new Dimension(200, 32));
        studentStatisticButton.addActionListener(_ -> navigationController.navigateTo(STUDENT_STATS));
        userOptionsPanel.add(studentStatisticButton, gbc);

        // logout button
        gbc.gridy = 6; 
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(50, 32));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        userOptionsPanel.add(logoutButton, gbc);
        
        return userOptionsPanel;

    }
    
}
