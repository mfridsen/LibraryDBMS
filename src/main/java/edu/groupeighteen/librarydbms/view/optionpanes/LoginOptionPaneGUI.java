package edu.groupeighteen.librarydbms.view.optionpanes;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;

import javax.swing.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.optionpanes
 * @contact matfir-1@student.ltu.se
 * @date 5/19/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class LoginOptionPaneGUI {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;

    public LoginOptionPaneGUI() {
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");
        this.messageLabel = new JLabel();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);
        panel.add(messageLabel);

        loginButton.addActionListener(e -> {
            boolean loginSuccessful = false;
            try
            {
                loginSuccessful = UserHandler.login(usernameField.getText(), new String(passwordField.getPassword()));
            }
            catch (UserValidationException userValidationException)
            {
                userValidationException.printStackTrace();
            }
            if (loginSuccessful) {
                User user = null;
                try
                {
                    user = UserHandler.getUserByUsername(usernameField.getText());
                }
                catch (InvalidNameException invalidNameException)
                {
                    invalidNameException.printStackTrace();
                }
                LibraryManager.setCurrentUser(user);
                JOptionPane.getRootFrame().dispose();
            } else {
                messageLabel.setText("Login failed");
            }
        });

        backButton.addActionListener(e -> {
            JOptionPane.getRootFrame().dispose();
        });

        JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
    }
}