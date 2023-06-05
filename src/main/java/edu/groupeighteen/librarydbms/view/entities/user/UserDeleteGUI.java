package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserDeleteGUI extends GUI
{
    private final User usertoDelete;
    private JButton confirmButton;
    private JPasswordField passwordField;


    public UserDeleteGUI(GUI previousGUI, User usertoDelete)
    {
        super(previousGUI, "UserDeleteGUI", usertoDelete);
        this.usertoDelete = usertoDelete;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {
        confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e ->
        {
            dispose();

            if (LibraryManager.getCurrentUser() != null)
            {
                try
                {
                    if (UserHandler.validate(LibraryManager.getCurrentUser(),
                            Arrays.toString(passwordField.getPassword())))
                    {
                        //UserHandler.deleteUser(usertoDelete);
                        //dispose();
                        //TODO-prio return to some other GUI, probably the LoginGUI
                    }
                }
                catch (UserValidationException userNullException)
                {
                    userNullException.printStackTrace();
                }
            }
            //delete user
            //previous gui = null
            //return to appropriate gui
        });
        return new JButton[]{confirmButton};
    }

    @Override
    protected void setupPanels()
    {
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Delete user: " + usertoDelete.getUsername() + "?");
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        GUIPanel.add(passwordPanel);
    }
}
