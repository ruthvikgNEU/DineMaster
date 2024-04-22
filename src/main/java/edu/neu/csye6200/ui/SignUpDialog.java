package edu.neu.csye6200.ui;

import edu.neu.csye6200.handler.CsvFileHandlerFactory;
import edu.neu.csye6200.model.User;
import edu.neu.csye6200.utils.Constants;
import edu.neu.csye6200.utils.Utility;
import edu.neu.csye6200.model.Employee;
import edu.neu.csye6200.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SignUpDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField birthdayField;
    private JTextField emailField;
    private JButton signUpButton;
    private JButton backButton;
    private CsvFileHandlerFactory csvFileHandlerFactory;
    private JRadioButton employeeButton;
    private JRadioButton customerButton;
    private ButtonGroup roleGroup;
    private String userType;
    private User newUser;

    public SignUpDialog(MainFrame parent) {
        super(parent, "Sign Up", true);
        setupDialog();
    }

    public SignUpDialog(HomePage parent) {
        super(parent, "Sign Up", true);
        setupDialog();
    }

    private void setupDialog() {
        // Set preferred size for the dialog
        setPreferredSize(new Dimension(450, 450));
        csvFileHandlerFactory = CsvFileHandlerFactory.getInstance();

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        nameField = new JTextField();
        ageField = new JTextField();
        birthdayField = new JTextField();
        emailField = new JTextField();
        signUpButton = new JButton("Sign Up");
        // backButton = new JButton("Back");

        employeeButton = new JRadioButton("Staff");
        customerButton = new JRadioButton("Customer");
        roleGroup = new ButtonGroup();

        roleGroup.add(employeeButton);
        roleGroup.add(customerButton);

        employeeButton.addActionListener(e -> userType = "Staff");
        customerButton.addActionListener(e -> userType = "Customer");

        JButton backButton = new JButton("Back");
        backButton.setBounds(20, 390, 100, 25);
        backButton.addActionListener(e -> {
            // Close the current dialog
            System.out.println("pressed the back button in signup");
            dispose();
            // Re-open the MainFrame
            new MainFrame().setVisible(true);
        });
        add(backButton);

        signUpButton.addActionListener(e -> attemptSignUp());

        setLayout(null);
        addComponentsWithAbsolutePositioning();
        pack();
        setLocationRelativeTo(null); // Center the dialog on the screen

        // Initialize the remaining fields
        userType = null;
        newUser = null;
    }

    private void addComponentsWithAbsolutePositioning() {
        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(20, 60, 100, 25);
        usernameField.setBounds(140, 60, 200, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 110, 100, 25);
        passwordField.setBounds(140, 110, 200, 25);

        JLabel nameFieldLabel = new JLabel("Name:");
        nameFieldLabel.setBounds(20, 160, 100, 25);
        nameField.setBounds(140, 160, 200, 25);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(20, 210, 100, 25);
        ageField.setBounds(140, 210, 200, 25);

        JLabel birthdayLabel = new JLabel("Birthday:");
        birthdayLabel.setBounds(20, 260, 100, 25);
        birthdayField.setBounds(140, 260, 200, 25);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 310, 100, 25);
        emailField.setBounds(140, 310, 200, 25);

        employeeButton.setBounds(20, 360, 100, 25);
        customerButton.setBounds(140, 360, 100, 25);

        signUpButton.setBounds(260, 360, 100, 25);

        add(nameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(nameFieldLabel);
        add(nameField);
        add(ageLabel);
        add(ageField);
        add(birthdayLabel);
        add(birthdayField);
        add(emailLabel);
        add(emailField);
        add(employeeButton);
        add(customerButton);
        add(signUpButton);
    }

    private void attemptSignUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();
        String email = emailField.getText();
        String birthday = birthdayField.getText();

        // Check if name contains any numerical values
        if (name.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Name should not contain numerical values.");
            return;
        }

        // Check if email contains @ symbol
        if (!email.contains("@") && !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email in invalid.");
            return;
        }

        // Check if birthday is in MM/DD/YYYY format
        if (!birthday.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Birthday should be in MM/DD/YYYY format.");
            return;
        }

        if ("Staff".equals(userType)) {
            newUser = new Employee(username, Utility.encryptPassword(password), userType);
            csvFileHandlerFactory.writeUserDetails(Constants.DATABASE_FILE_PATH, Arrays.asList(newUser));
        } else {
            newUser = new Customer(username, Utility.encryptPassword(password), userType);
            csvFileHandlerFactory.writeUserDetails(Constants.CUSTOMER_FILE_PATH, Arrays.asList(newUser));
        }

        // Only call performRegistration if a new user has been created
        if (newUser != null && getParent() instanceof MainFrame) {
            ((MainFrame) getParent()).performRegistration(this);
        }

    }

    public boolean isRegistered() {
        // Check if the user has filled in all the required fields and clicked the "Register" button
        return newUser != null;
    }

    public User getNewUser() {
        return this.newUser;
    }
}