package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 */
public class UserCreateGUI extends GUI
{
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     */
    public UserCreateGUI(GUI previousGUI)
    {
        super(previousGUI, "UserCreateGUI", null);
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
        JButton proceedButton = new JButton("Create User");
        proceedButton.addActionListener(e ->
        {
            createUser();
        });
        return new JButton[]{proceedButton};
    }

    @Override
    protected void setupPanels()
    {
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(2, 2));
        JLabel userNameLabel = new JLabel("Enter Username");
        usernameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Enter Password");
        passwordField = new JPasswordField(10);
        textFieldsPanel.add(userNameLabel);
        textFieldsPanel.add(usernameField);
        textFieldsPanel.add(passwordLabel);
        textFieldsPanel.add(usernameField);

        GUIPanel.add(textFieldsPanel, BorderLayout.NORTH);

    }

    private void resetFields()
    {
        usernameField.setText("");
        passwordField.setText("");
    }

    private void createUser()
    {
        String usernameStr = usernameField.getText();
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

