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
 * LoginOptionPaneGUI class provides a graphical interface for user login.
 * It includes text fields for entering username and password, as well as buttons for login and going back.
 * After successful login, it retrieves the user and sets the current user in LibraryManager.
 */
public class LoginOptionPaneGUI
{
    //TODO-prio better exception handling

    /**
     * JTextField for inputting username.
     */
    private final JTextField usernameField;

    /**
     * JPasswordField for inputting password.
     */
    private final JPasswordField passwordField;

    /**
     * JLabel for displaying login messages.
     */
    private final JLabel messageLabel;

    /**
     * Constructor for the LoginOptionPaneGUI class.
     * Initializes the interface components, sets their properties, and adds action listeners to the buttons.
     */
    public LoginOptionPaneGUI()
    {
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

        loginButton.addActionListener(e ->
        {
            boolean loginSuccessful = false;
            try
            {
                loginSuccessful = UserHandler.login(usernameField.getText(), new String(passwordField.getPassword()));
            }
            catch (UserValidationException userValidationException)
            {
                userValidationException.printStackTrace();
            }
            if (loginSuccessful)
            {
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
                System.out.println("Login successful, you can now attempt to rent again.");
                JOptionPane.getRootFrame().dispose();
            }
            else
            {
                messageLabel.setText("Login failed");
            }
        });

        backButton.addActionListener(e ->
        {
            JOptionPane.getRootFrame().dispose();
        });

        JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{}, null);
    }
}