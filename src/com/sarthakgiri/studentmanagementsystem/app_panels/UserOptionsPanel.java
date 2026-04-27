import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

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
        createStudentRecordButton.addActionListener(e -> navigationController.navigateTo(CREATE_STUDENT));
        userOptionsPanel.add(createStudentRecordButton, gbc);

        // Read Student Option
        gbc.gridy = 1; 
        JButton displayAllStudentButton = new JButton("Read Student Records");
        displayAllStudentButton.setPreferredSize(new Dimension(200, 32));
        displayAllStudentButton.addActionListener(e -> navigationController.navigateTo(READ_STUDENT));
        userOptionsPanel.add(displayAllStudentButton, gbc);

        // Update Student Option
        gbc.gridy = 2;
        JButton updateStudentRecordButton = new JButton("Update Student Record");
        updateStudentRecordButton.setPreferredSize(new Dimension(200, 32));
        updateStudentRecordButton.addActionListener(e -> navigationController.navigateTo(UPDATE_STUDENT));
        userOptionsPanel.add(updateStudentRecordButton, gbc);

        // logout button
        gbc.gridy = 3; 
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(50, 32));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));
        userOptionsPanel.add(logoutButton, gbc);
        
        return userOptionsPanel;

    }
    
}
