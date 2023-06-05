package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class AuthorCreateGUI extends GUI
{
    private JTextField authornameField;
    private JPasswordField passwordField;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     */
    public AuthorCreateGUI(GUI previousGUI)
    {
        super(previousGUI, "AuthorCreateGUI", null);
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e ->
        {
            resetFields();
        });
        JButton proceedButton = new JButton("Create Author");
        proceedButton.addActionListener(e ->
        {
            createAuthor();
        });
        return new JButton[]{proceedButton};
    }

    @Override
    protected void setupPanels()
    {
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(2, 2));
        JLabel userNameLabel = new JLabel("Enter Username");
        authornameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Enter Password");
        passwordField = new JPasswordField(10);
        textFieldsPanel.add(userNameLabel);
        textFieldsPanel.add(authornameField);
        textFieldsPanel.add(passwordLabel);
        textFieldsPanel.add(authornameField);

        GUIPanel.add(textFieldsPanel, BorderLayout.NORTH);

    }

    private void resetFields()
    {
        authornameField.setText("");
        passwordField.setText("");
    }

    private void createAuthor()
    {
        String usernameStr = authornameField.getText();
        String passwordStr = Arrays.toString(passwordField.getPassword());

        if (usernameStr.isEmpty())
        {
            System.err.println("To create a new user you need to enter in a username. Username: " + usernameStr);
            resetFields();
            return;
        }
        if (passwordStr.isEmpty())
        {
            System.err.println("To create a new user you need to enter in an password. Password: " + passwordStr);
            resetFields();
            return;
        }
        //If successful, dispose, create a new user and open a new UserGUI for that user
        dispose();
        //User newUser = UserHandler.createNewUser(usernameStr, passwordStr);
        //new UserGUI(this, newUser);
    }
}
