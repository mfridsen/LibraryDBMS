package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class LoginScreenGUI extends GUI {
    public JLabel usernameLabel;
    public JTextField usernameField;
    public JLabel passwordLabel;
    public JPasswordField passwordField;
    public JPanel LoginPanel;
    public JButton proceedButton;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     */
    public LoginScreenGUI(GUI previousGUI) {
        super(previousGUI, "LoginScreenGUI");
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        proceedButton = new JButton("MenuPageGUI");

        proceedButton.addActionListener(e -> {
            dispose();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                if (UserHandler.login(username, password)) {
                    LibraryManager.setCurrentUser(UserHandler.getUserByUsername(username));
                    new MenuPageGUI(LibraryManager.getCurrentUser(), this);
                } else {
                    // show error message or do nothing
                    new LoginErrorGUI(this);// TODO-prio change from null to this

                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);// TODO-Exception
            }

        });
        return new JButton[]{proceedButton};
    }

    @Override
    protected void setupPanels() {
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        LoginPanel = new JPanel();
        LoginPanel.add(usernameLabel);
        LoginPanel.add(usernameField);
        LoginPanel.add(passwordLabel);
        LoginPanel.add(passwordField);
        GUIPanel.add(LoginPanel);
    }
}
