import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecurePasswordApp {
    private JFrame frame;
    private JLabel titleLabel;
    private JPasswordField passwordField;
    private JProgressBar strengthProgressBar;
    private JButton submitButton;
    private JCheckBox showPasswordCheckbox;
    private JLabel errorLabel;
    private JLabel strengthTextLabel; // Add a JLabel for displaying the strength text.

    public SecurePasswordApp() {
        frame = new JFrame("Secure Password Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        titleLabel = new JLabel("Check the Strength of Your Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        passwordField = new JPasswordField(20);
        strengthProgressBar = new JProgressBar(0, 100);
        strengthProgressBar.setStringPainted(true);
        strengthProgressBar.setForeground(Color.GRAY);

        submitButton = new JButton("Check Password");
        submitButton.setBackground(new Color(66, 134, 244));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setEnabled(false);

        showPasswordCheckbox = new JCheckBox("Show Password");

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        // Add a JLabel for displaying the strength text.
        strengthTextLabel = new JLabel("Password strength: ");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(titleLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(new JLabel("Enter Your Password: "), constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(strengthProgressBar, constraints);

        constraints.gridx = 1;
        panel.add(submitButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(showPasswordCheckbox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(errorLabel, constraints);

        // Add the strength text label below the progress bar.
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        panel.add(strengthTextLabel, constraints);

        frame.add(panel);
        frame.setVisible(true);

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStrength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStrength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStrength();
            }

            private void updateStrength() {
                String password = new String(passwordField.getPassword());
                int strength = checkPasswordStrength(password);
                strengthProgressBar.setValue(strength);

                if (strength >= 80) {
                    strengthProgressBar.setForeground(Color.GREEN);
                    errorLabel.setText("");
                } else if (strength >= 40) {
                    strengthProgressBar.setForeground(Color.ORANGE);
                    errorLabel.setText("");
                } else {
                    strengthProgressBar.setForeground(Color.RED);
                    errorLabel.setText("Password is too weak.");
                }

                submitButton.setEnabled(strength >= 60);

                // Display the password strength as text.
                strengthTextLabel.setText("Password strength: " + strength + "%");
            }

            private int checkPasswordStrength(String password) {
                int length = password.length();
                boolean hasUpperCase = !password.equals(password.toLowerCase());
                boolean hasLowerCase = !password.equals(password.toUpperCase());
                boolean hasDigit = password.matches(".*\\d.*");
                boolean hasSpecialChar = !password.matches("[A-Za-z0-9]*");

                int strength = 0;

                if (length >= 8) {
                    strength += 20;
                } else {
                    errorLabel.setText("Password is too short.");
                }

                if (hasUpperCase) {
                    strength += 20;
                }

                if (hasLowerCase) {
                    strength += 20;
                }

                if (hasDigit) {
                    strength += 20;
                }

                if (hasSpecialChar) {
                    strength += 20;
                }

                return strength;
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                int strength = strengthProgressBar.getValue();
                String message = "Your password is strong.";
                if (strength < 60) {
                    message = "Your password is weak. Please choose a stronger one.";
                }
                JOptionPane.showMessageDialog(frame, message, "Password Strength",
                        JOptionPane.INFORMATION_MESSAGE);

                // Display the password strength as text.
                strengthTextLabel.setText("Password strength: " + strength + "%");
            }
        });

        showPasswordCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                passwordField.setEchoChar(checkBox.isSelected() ? '\0' : '*');
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SecurePasswordApp();
        });
    }
}
