package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 */
public class UserCreateGUI extends GUI {
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     */
    public UserCreateGUI(GUI previousGUI) {
        super(previousGUI, "CreateAccountGUI");
        setupButtons();
        setupPanels();
        this.displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        JButton proceedButton = new JButton("Skapa");
        proceedButton.addActionListener(e -> {
            dispose();
            try {
                User newUser = UserHandler.createNewUser(usernameField.getText(), passwordField.getPassword().toString());
                UserWelcomeGUI userWelcomeGUI = new UserWelcomeGUI(this, newUser);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return new JButton[]{proceedButton};
    }

    @Override
    protected void setupPanels() {
        JLabel usernameLabel = new JLabel("Skapa Användarnamn:");
        usernameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Skapa Lösenord:");
        passwordField = new JPasswordField(10);
        JLabel emailLabel = new JLabel("Skapa Email");
        JTextField emailField = new JTextField(10);
        JPanel createAccountPanel = new JPanel();

        createAccountPanel.add(usernameLabel);
        createAccountPanel.add(usernameField);
        createAccountPanel.add(passwordLabel);
        createAccountPanel.add(passwordField);
        createAccountPanel.add(emailLabel);
        createAccountPanel.add(emailField);

        GUIPanel.add(createAccountPanel, BorderLayout.NORTH);
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}

