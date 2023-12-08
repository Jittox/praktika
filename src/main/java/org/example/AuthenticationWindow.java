package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthenticationWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AuthenticationWindow() {
        // Set up the authentication window
        setTitle(" ");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel usernameLabel = new JLabel("Логин:");
        JLabel passwordLabel = new JLabel("Пароль:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Авторизироваться");

        // Set layout to GridLayout
        setLayout(new GridLayout(3, 2));

        // Add components to the window
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label as a placeholder
        add(loginButton);

        // Create an action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your login logic here
            }
        });

        // Make the window visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthenticationWindow());
    }
}
