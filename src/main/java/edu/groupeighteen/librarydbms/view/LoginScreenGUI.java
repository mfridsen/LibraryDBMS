package edu.groupeighteen.librarydbms.view;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class LoginScreenGUI extends GUI
{
    public JLabel usernameLabel;
    public JTextField usernameField;
    public JLabel passwordLabel;
    public JPasswordField passwordField;
    public JPanel LoginPanel;
    public JButton proceedButton;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     */
    public LoginScreenGUI(GUI previousGUI)
    {
        super(previousGUI, "LoginScreenGUI", null);
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {
        proceedButton = new JButton("MenuPageGUI");

        proceedButton.addActionListener(e ->
        {
            dispose();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            /*if (UserHandler.login(username, password)) {
                LibraryManager.setCurrentUser(UserHandler.getUserByUsername(username));
                //new MenuPageGUI(LibraryManager.getCurrentUser(), this);
            } else {
                // show error message or do nothing
                new LoginErrorGUI(this);// TODO-prio change from null to this

            }*/

        });
        return new JButton[]{proceedButton};
    }

    @Override
    protected void setupPanels()
    {
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
