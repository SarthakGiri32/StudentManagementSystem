import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends BasePanel {

    private JTextField usernameField, passwordField;
    private String correctUsername, correctPassword;

    @Override
    protected JPanel build() {
        
        JPanel userLoginFields = new JPanel(new GridBagLayout());
        userLoginFields.setBorder(BorderFactory.createTitledBorder("MySQL Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // username field
        gbc.gridx = 0; gbc.gridy = 0;
        userLoginFields.add(new JLabel("MySQL Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        userLoginFields.add(usernameField, gbc);

        // password field
        gbc.gridx = 0; gbc.gridy = 1;
        userLoginFields.add(new JLabel("MySQL Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JTextField(20);
        userLoginFields.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(50, 32));

        loginButton.addActionListener(e -> loginToMySQL());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        userLoginFields.add(loginButton, gbc);

        return userLoginFields;
        
    }

    private void setUserCredentials() {

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("resources\\mysqlusercredentials.properties")) {

            properties.load(fileInputStream);

            navigationController.putData("username", properties.getProperty("username"));
            navigationController.putData("password", properties.getProperty("password"));
            navigationController.putData("databaseUrl", properties.getProperty("databaseUrl"));
            
        } catch (IOException e) {
            e.toString();
        }
    }

    @Override
    public void onNavigatedTo() {

        setUserCredentials();
        clearInputFields();
        
        correctUsername = navigationController.getData("username");
        correctPassword = navigationController.getData("password");

    }

    private void loginToMySQL() {
        
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (correctUsername.equals(username) && correctPassword.equals(password)) {
            JOptionPane.showMessageDialog(this, 
                "Successfully logged into MySQL", 
                "Login Success", 
                JOptionPane.INFORMATION_MESSAGE);

            navigationController.navigateTo(USER_OPTIONS);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid Username or Password",
                "MySQL login failed",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInputFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
}
