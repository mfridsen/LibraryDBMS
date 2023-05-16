package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 */
public class UserUpdateGUI extends GUI {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private User user;

    public UserUpdateGUI(GUI previousGUI, User user) {
        super(previousGUI, "UserUpdateGUI");
        this.user = user;
        setupPanels();
        displayGUI();
    }


    @Override
    protected JButton[] setupButtons() {
        JButton changeButton = new JButton("Change");

        changeButton.addActionListener(e -> {
            dispose();
            LibraryManager.getCurrentUser().setUsername(usernameField.getText());
            LibraryManager.getCurrentUser().setPassword(Arrays.toString(passwordField.getPassword()));
            try {
                UserHandler.updateUser(LibraryManager.getCurrentUser());
                new MenuPageGUI(LibraryManager.getCurrentUser(),this);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);//TODO-Exception
            }

        });
        return new JButton[]{changeButton};
    }

    @Override
    protected void setupPanels() {
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        JPanel accountPanel = new JPanel();

        accountPanel.add(usernameLabel);
        accountPanel.add(usernameField);
        accountPanel.add(passwordLabel);
        accountPanel.add(passwordField);

        GUIPanel.add(accountPanel);
    }
}

