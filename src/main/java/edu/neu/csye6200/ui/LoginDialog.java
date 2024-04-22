package edu.neu.csye6200.ui;

import edu.neu.csye6200.handler.CsvFileHandlerFactory;
import edu.neu.csye6200.handler.DbHandler;
import edu.neu.csye6200.model.User;
import edu.neu.csye6200.utils.Utility;
import edu.neu.csye6200.utils.Constants;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    // private JButton backButton;
    private boolean isAuthenticated = false;
    private DbHandler dbHandler;
    private JRadioButton employeeButton;
    private JRadioButton customerButton;
    private ButtonGroup roleGroup;
    private String userType;
    private CsvFileHandlerFactory csvFileHandlerFactory;

    public LoginDialog(MainFrame parent) {
        super(parent, "Login", true);
        setupDialog();
    }

    public LoginDialog(HomePage parent) {
        super(parent, "Login", true);
        setupDialog();
    }

    private void setupDialog() {
        // Set preferred size for the dialog
        setPreferredSize(new Dimension(350, 300));
        dbHandler = DbHandler.getInstance();

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        // backButton = new JButton("Login");

        employeeButton = new JRadioButton("Staff");
        customerButton = new JRadioButton("Customer");
        roleGroup = new ButtonGroup();

        roleGroup.add(employeeButton);
        roleGroup.add(customerButton);

        employeeButton.addActionListener(e -> userType = "Staff");
        customerButton.addActionListener(e -> userType = "Customer");

        JButton backButton = new JButton("Back");
        backButton.setBounds(20, 230, 100, 25); // Set the bounds for the back button
        backButton.addActionListener(e -> {
            // Close the current dialog
            dispose();
            // Re-open the MainFrame
            new MainFrame().setVisible(true);
        });

        add(backButton);

        loginButton.addActionListener(e -> attemptLogin());

        setLayout(null);
        addComponentsWithAbsolutePositioning();
        pack();
        setLocationRelativeTo(null); // Center the dialog on the screen

        // Initialize the remaining fields
        isAuthenticated = false;
        userType = null;
    }

    private void addComponentsWithAbsolutePositioning() {
        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(20, 70, 80, 30);
        usernameField.setBounds(100, 70, 190, 30);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(20, 120, 80, 30);
        passwordField.setBounds(100, 120, 190, 30);

        employeeButton.setBounds(20, 180, 100, 25);
        customerButton.setBounds(140, 180, 100, 25);

        loginButton.setBounds(260, 180, 100, 25);

        add(lblUser);
        add(usernameField);
        add(lblPass);
        add(passwordField);
        add(employeeButton);
        add(customerButton);
        add(loginButton);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        System.err.println("userType: " + userType);
        if (userType.equals("Staff")) {
            User user = dbHandler.getUserFromUserName(username);
            if (user != null && user.getEncryptedPassword().equals(Utility.encryptPassword(password))) {
                isAuthenticated = true;
            }
        } else if (userType.equals("Customer")) {
            csvFileHandlerFactory = CsvFileHandlerFactory.getInstance();
            List<User> users = csvFileHandlerFactory.readUserDetails(Constants.CUSTOMER_FILE_PATH);
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getEncryptedPassword().equals(Utility.encryptPassword(password))) {
                    isAuthenticated = true;
                    break;
                }
            }
        }
        System.err.println("isAuthenticated: " + isAuthenticated);

        if (isAuthenticated) {
            dispose(); // Close the dialog properly
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            // Clear the password field for retry
            passwordField.setText("");
        }
    }

    public String getUserType() {
        return userType;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}