import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class UserOptionsPanel extends BasePanel {

    private JButton displayAllStudentButton;

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
        displayAllStudentButton = new JButton("Read Student Records");
        displayAllStudentButton.setPreferredSize(new Dimension(200, 32));
        displayAllStudentButton.addActionListener(e -> navigationController.navigateTo(READ_STUDENT));
        userOptionsPanel.add(displayAllStudentButton, gbc);
        
        return userOptionsPanel;

    }
    
}
