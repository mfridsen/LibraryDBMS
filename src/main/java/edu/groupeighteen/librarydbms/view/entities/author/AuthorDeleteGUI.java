package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class AuthorDeleteGUI extends GUI
{
    private final Author authortoDelete;
    private JButton confirmButton;
    private JPasswordField passwordField;


    public AuthorDeleteGUI(GUI previousGUI, Author authortoDelete)
    {
        super(previousGUI, "UserDeleteGUI", authortoDelete);
        this.authortoDelete = authortoDelete;
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
                /*try {
                    if (UserHandler.validate(LibraryManager.getCurrentUser(),
                            Arrays.toString(passwordField.getPassword()))) {
                        //UserHandler.deleteUser(authortoDelete);
                        //dispose();
                        //TODO-prio return to some other GUI, probably the LoginGUI
                    }
                } catch (InvalidPasswordException | NullUserException userNullException) {
                    userNullException.printStackTrace();
                }*/
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
        JLabel passwordLabel = new JLabel("Delete author: " + authortoDelete.getAuthorFirstname() + "?");
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        GUIPanel.add(passwordPanel);
    }
}
