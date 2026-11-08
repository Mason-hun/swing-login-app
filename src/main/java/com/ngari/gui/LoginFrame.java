
package com.ngari.gui;

import com.ngari.dao.UserDAO;
import com.ngari.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginFrame extends JFrame {

    // Application colours
    private static final Color PRIMARY_BLUE = new Color(23, 105, 170);
    private static final Color WHITE = Color.WHITE;
    private static final Color DARK_TEXT = new Color(40, 50, 60);

    public LoginFrame() {

        // Configure the window
        setTitle("User Login");
        setSize(420, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create the main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        // Create the header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setPreferredSize(new Dimension(420, 90));

        JLabel titleLabel = new JLabel("USER LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);

        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create the login form
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(DARK_TEXT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username input
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(DARK_TEXT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password input
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(PRIMARY_BLUE);
        loginButton.setForeground(WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        loginButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components to the form
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(7));

        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(7));

        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));

        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Handle login button clicks
        loginButton.addActionListener(event -> {

            String username = usernameField.getText().trim();
            char[] passwordCharacters = passwordField.getPassword();

            // Validate input
            if (username.isEmpty() || passwordCharacters.length == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter both username and password.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                Arrays.fill(passwordCharacters, '\0');
                return;
            }

            // Convert password for the existing DAO method
            String password = new String(passwordCharacters);
            Arrays.fill(passwordCharacters, '\0');

            // Disable button during authentication
            loginButton.setEnabled(false);
            loginButton.setText("PLEASE WAIT...");

            // Authenticate in the background
            SwingWorker<User, Void> worker = new SwingWorker<>() {

                @Override
                protected User doInBackground() throws Exception {

                    UserDAO userDAO = new UserDAO();

                    return userDAO.authenticate(username, password);
                }

                @Override
                protected void done() {

                    try {

                        User user = get();

                        if (user != null) {

                            JOptionPane.showMessageDialog(
                                    LoginFrame.this,
                                    "Login successful!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            dispose();
                            new WelcomeFrame(user);

                        } else {

                            JOptionPane.showMessageDialog(
                                    LoginFrame.this,
                                    "Invalid username or password.",
                                    "Login Failed",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            passwordField.setText("");
                            passwordField.requestFocusInWindow();

                            loginButton.setEnabled(true);
                            loginButton.setText("LOGIN");
                        }

                    } catch (Exception e) {

                        JOptionPane.showMessageDialog(
                                LoginFrame.this,
                                "An error occurred during login.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        e.printStackTrace();

                        loginButton.setEnabled(true);
                        loginButton.setText("LOGIN");

                    } finally {

                        // Clear the password field after processing
                        passwordField.setText("");
                    }
                }
            };

            worker.execute();
        });

        // Add the main container to the window
        add(mainPanel);

        // Display the completed window
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
