package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserDeleteGUI extends GUI {
    private final User usertoDelete;
    private JButton confirmButton;
    private JPasswordField passwordField;


    public UserDeleteGUI(User usertoDelete, GUI previousGUI) {
        super(previousGUI, "UserDeleteGUI");
        this.usertoDelete = usertoDelete;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e -> {
            dispose();

            if (LibraryManager.getCurrentUser() != null) {
                if (UserHandler.validateUser(LibraryManager.getCurrentUser(),
                        Arrays.toString(passwordField.getPassword()))) {
                    try {
                        UserHandler.deleteUser(usertoDelete);
                        //dispose();
                        //TODO-prio return to some other GUI, probably the LoginGUI
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }
            }
            //delete user
            //previous gui = null
            //return to appropriate gui
        });
        return new JButton[]{confirmButton};
    }

    @Override
    protected void setupPanels() {
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Delete user: " + usertoDelete.getUsername() + "?");
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        GUIPanel.add(passwordPanel);
    }
}
