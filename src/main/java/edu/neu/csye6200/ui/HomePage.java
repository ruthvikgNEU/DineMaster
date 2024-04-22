package edu.neu.csye6200.ui;



import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private final JButton signInButton;
    private final JButton signUpButton;

    public HomePage() {
        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");

        signInButton.addActionListener(e -> new LoginDialog(this).setVisible(true));
        signUpButton.addActionListener(e -> new SignUpDialog(this).setVisible(true));

        setLayout(new FlowLayout());
        add(signInButton);
        add(signUpButton);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}