import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * This is the parent class inherited by all screen (panel) classes used in the app
 */
public abstract class BasePanel extends JPanel implements PanelNames {

    // will be used for handling navigation between the various screens of the app
    protected NavigationController navigationController = NavigationController.getInstance();

    // validation guidelines, which will used to display warning messages for invalid user input
    protected static final String validNameGuidelines = """
            Valid name conditions:
            1. The name should contain at least a first and last name
            2. Each word in the name should start with a upper case letter
            3. There can be a maximum of 3 words in the name
            """;
    protected static final String validEmailGuidelines = """
            Valid email conditions:
            1. The first part of the email (before '@' symbol) can be alphanumeric, and can contain the following characters: ._%+-
            2. The second part of the email (after '@' symbol) should follow these rules:
            \t\
            a) The second level domain can be alphanumeric, along with these characters: .-
            \t\
            b) The top level domain can only be alphabetic of length between 2 and 6 characters (inclusive)
            """;
    protected static final String validPhoneNumberGuidelines = """
            Valid phone number conditions:
            1. Should start with these two country codes: '+91' or '0'
            2. The 10 digit phone number should start with numbers 6 to 9
            """;
    protected static final String validRollNumberGuidelines = """
            Valid roll number conditions:
            1. The roll number should contain exactly 3 digits
            2. For a 1 digit roll number, append 2 zeros to the start
            3. For a 2 digit roll number, append 1 zero to the start
            """;
    protected static final String validDepartmentNameGuidelines = """
            Valid department names guidelines:
            1. The name should only contain simple alphabetic letters and spaces
            """;
    protected static final String validMarksGuidelines = """
            Valid marks guidelines:
            1. The marks should be an integer between 0 and 100 (inclusive)
            """;
    protected static final String validColumnNamesGuidelines = """
            Valid column names:
            1. name
            2. roll_no
            3. department
            4. email
            5. phone
            6. marks
            """;

    /**
     * Constructor used to initialize the base layout of any screen in the app
     */
    public BasePanel() {
        setLayout(new BorderLayout(10, 10));
        add(build(), BorderLayout.CENTER);
    }

    /**
     * Each panel implements its own UI
     * @return a base JPanel object containing all the elements displayed in a particular screen
     */
    protected abstract JPanel build();

    /**
     * called every time a screen is shown (optional), and can be used to perform some startup code after navigating to a screen
     */
    public void onNavigatedTo() {}

    /**
     * clear input fields (optional)
     */
    protected void clearInputFields() {}

    /**
     *  generate a student's grade for a particular 'marks' value
     * @param marks the value for which a student's grade is being calculated
     * @return a student grade
     */
    protected char generateGrade(int marks) {

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
