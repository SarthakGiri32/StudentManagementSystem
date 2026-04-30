import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class contains all the code for displaying the student statistic options in a screen
 */
public class StudentStatisticOptionsPanel extends BasePanel {

    @Override
    protected JPanel build() {
        
        JPanel statisticOptionsPanel = new JPanel(new GridBagLayout());
        statisticOptionsPanel.setBorder(BorderFactory.createTitledBorder("Student Statistic Options"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JButton totalCountAndMarksRangeButton = new JButton("Total Student Count and Highest And Lowest Marks");
        totalCountAndMarksRangeButton.setPreferredSize(new Dimension(400, 32));
        totalCountAndMarksRangeButton.addActionListener(_ -> navigationController.navigateTo(STUDENT_COUNT_AND_MARKS));
        statisticOptionsPanel.add(totalCountAndMarksRangeButton, gbc);

        gbc.gridy = 1;
        JButton studentCountByDepartmentButton = new JButton("Student Count By Department");
        studentCountByDepartmentButton.setPreferredSize(new Dimension(400, 32));
        studentCountByDepartmentButton.addActionListener(_ -> navigationController.navigateTo(STUDENT_DEPARTMENT_COUNT));
        statisticOptionsPanel.add(studentCountByDepartmentButton, gbc);

        gbc.gridy = 2;
        JButton returnToUserOptionsButton = new JButton("Return to User Options");
        returnToUserOptionsButton.setPreferredSize(new Dimension(200, 32));
        returnToUserOptionsButton.addActionListener(_ -> navigationController.navigateTo(USER_OPTIONS));
        statisticOptionsPanel.add(returnToUserOptionsButton, gbc);

        gbc.gridy = 3;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 32));
        logoutButton.addActionListener(_ -> navigationController.navigateTo(LOGIN));
        statisticOptionsPanel.add(logoutButton, gbc);

        return statisticOptionsPanel;

    }

}
