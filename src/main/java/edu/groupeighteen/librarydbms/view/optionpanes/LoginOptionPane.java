package edu.groupeighteen.librarydbms.view.optionpanes;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.optionpanes
 * @contact matfir-1@student.ltu.se
 * @date 5/19/2023
 * <p>
 * LoginOptionPane class provides a graphical interface for user login.
 * It includes text fields for entering username and password, as well as buttons for login and going back.
 * After successful login, it retrieves the user and sets the current user in LibraryManager.
 */
public class LoginOptionPane extends OptionPane
{
    //TODO-prio better exception handling
    //TODO-prio restructure

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
     * Constructor for the LoginOptionPane class.
     * Initializes the interface components, sets their properties, and adds action listeners to the buttons.
     */
    public LoginOptionPane(GUI previousGUI)
    {
        super(previousGUI);

        this.usernameField = new JTextField();
        usernameField.setColumns(10);  // 20 is an example, adjust this to fit your needs

        this.passwordField = new JPasswordField();
        passwordField.setColumns(10);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        this.messageLabel = new JLabel();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create panels for username and password with their labels
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(passwordField);

        panel.add(usernamePanel);
        panel.add(passwordPanel);

        // Create a buttonPanel for login and back buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel);
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
                System.out.println("Login successful.");
                JOptionPane.getRootFrame().dispose();
            }
            else
            {
                System.err.println("Login failed");
            }
        });

        backButton.addActionListener(e -> JOptionPane.getRootFrame().dispose());

        JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{}, null);
    }
}