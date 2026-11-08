
package com.ngari.gui;

import com.ngari.model.User;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    private static final Color PRIMARY_BLUE =
            new Color(23, 105, 170);

    private static final Color WHITE = Color.WHITE;

    public WelcomeFrame(User user) {

        // Configure the window
        setTitle("Login Successful");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setPreferredSize(new Dimension(420, 80));

        JLabel headingLabel = new JLabel("WELCOME");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(WHITE);

        headerPanel.add(headingLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Welcome message
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(WHITE);
        contentPanel.setLayout(new BoxLayout(
                contentPanel, BoxLayout.Y_AXIS
        ));

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(35, 20, 30, 20)
        );

        JLabel welcomeLabel = new JLabel(
                "Hello, " + user.getUsername() + "!"
        );

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 23));
        welcomeLabel.setForeground(new Color(40, 50, 60));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(
                "You have successfully logged in."
        );

        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.GRAY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(messageLabel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Display the window
        add(mainPanel);
        setVisible(true);
    }
}
