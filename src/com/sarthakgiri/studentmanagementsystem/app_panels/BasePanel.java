import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class BasePanel extends JPanel implements PanelNames {

    protected NavigationController navigationController = NavigationController.getInstance();

    // validation guidelines
    protected static final String validNameGuidelines = "Valid name conditions:\n" +
                                                      "1. The name should contain at least a first and last name\n" +
                                                      "2. Each word in the name should start with a upper case letter\n" +
                                                      "3. There can be a maximum of 3 words in the name\n";
    protected static final String validEmailGuidelines = "Valid email conditions:\n" +
                                                      "1. The first part of the email (before '@' symbol) can be alphanumeric, and can contain the following characters: ._%+-\n" +
                                                      "2. The second part of the email (after '@' symbol) should follow these rules:\n\t" +
                                                      "a) The second level domain can be alphanumeric, along with these characters: .-\n\t" +
                                                      "b) The top level domain can only be alphabetic of length between 2 and 6 characters (inclusive)\n";
    protected static final String validPhoneNumberGuidelines = "Valid phone number conditions:\n" +
                                                            "1. Should start with these two country codes: '+91' or '0'\n" +
                                                            "2. The 10 digit phone number should start with numbers 6 to 9\n";
    protected static final String validRollNumberGuidelines = "Valid roll number conditions:\n" +
                                                            "1. The roll number should contain exactly 3 digits\n" +
                                                            "2. For a 1 digit roll number, append 2 zeros to the start\n" +
                                                            "3. For a 2 digit roll number, append 1 zero to the start\n";
    protected static final String validDepartmentNameGuidelines = "Valid department names guidelines:\n" +
                                                                "1. The name should only contain simple alphabetic letters and spaces\n";
    protected static final String validMarksGuidelines = "Valid marks guidelines:\n" +
                                                       "1. The marks should be an integer between 0 and 100 (inclusive)\n";
    protected static final String validColumnNamesGuidelines = "Valid column names:\n" +
                                                                "1. name\n" +
                                                                "2. roll_no\n" +
                                                                "3. department\n" +
                                                                "4. email\n" +
                                                                "5. phone\n" +
                                                                "6. marks\n"; 


    public BasePanel() {
        setLayout(new BorderLayout(10, 10));
        add(build(), BorderLayout.CENTER);
    }

    // Each panel implements its own UI
    protected abstract JPanel build();

    // called every time a page is shown (optional)
    public void onNavigatedTo() {}

    // clear input fields (optional)
    protected void clearInputFields() {}

    // generate grade
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
