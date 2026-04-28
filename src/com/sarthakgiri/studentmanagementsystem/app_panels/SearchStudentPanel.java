import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SearchStudentPanel extends BasePanel {

    @Override
    protected JPanel build() {
        
        JPanel searchOptionsPanel = new JPanel(new GridBagLayout());
        searchOptionsPanel.setBorder(BorderFactory.createTitledBorder("Student Search Options"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JButton studentNameSearchButton = new JButton("Search By Name");
        studentNameSearchButton.setPreferredSize(new Dimension(200, 32));
        studentNameSearchButton.addActionListener(e -> navigationController.navigateTo(NAME_SEARCH));
        searchOptionsPanel.add(studentNameSearchButton, gbc);

        gbc.gridy = 1;
        JButton studentRollNumberSearchButton = new JButton("Search By Roll Number");
        studentRollNumberSearchButton.setPreferredSize(new Dimension(200, 32));
        studentRollNumberSearchButton.addActionListener(e -> navigationController.navigateTo(ROLL_NUMBER_SEARCH));
        searchOptionsPanel.add(studentRollNumberSearchButton, gbc);

        gbc.gridy = 2;
        JButton studentDepartmentSearchButton = new JButton("Search By Department");
        studentDepartmentSearchButton.setPreferredSize(new Dimension(200, 32));
        studentDepartmentSearchButton.addActionListener(e -> navigationController.navigateTo(DEPARTMENT_SEARCH));
        searchOptionsPanel.add(studentDepartmentSearchButton, gbc);

        gbc.gridy = 3;
        JButton studentMarksSearchButton = new JButton("Search By Marks");
        studentMarksSearchButton.setPreferredSize(new Dimension(200, 32));
        studentMarksSearchButton.addActionListener(e -> navigationController.navigateTo(MARKS_RANGE_SEARCH));
        searchOptionsPanel.add(studentMarksSearchButton, gbc);

        gbc.gridy = 4;
        JButton returnToUserOptionsButton = new JButton("Return to User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToUserOptionsButton.addActionListener(e -> navigationController.navigateTo(USER_OPTIONS));
        searchOptionsPanel.add(returnToUserOptionsButton, gbc);

        gbc.gridy = 5;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(e -> navigationController.navigateTo(LOGIN));
        searchOptionsPanel.add(logoutButton, gbc); 

        return searchOptionsPanel;

    }

}
