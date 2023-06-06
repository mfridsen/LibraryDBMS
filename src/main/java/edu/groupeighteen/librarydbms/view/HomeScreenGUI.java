package edu.groupeighteen.librarydbms.view;

import edu.groupeighteen.librarydbms.view.entities.item.ItemSearchGUI;
import edu.groupeighteen.librarydbms.view.entities.user.UserCreateGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;
import edu.groupeighteen.librarydbms.view.optionpanes.LoginOptionPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 * <p>
 * This class represents the Home screen of the application's Graphical User Interface (GUI).
 * The Home screen is the first screen that the user sees and provides options to login,
 * create an account, view info, and search.
 */
public class HomeScreenGUI extends GUI
{

    /**
     * Constructor for the HomeScreenGUI. Sets up panels and displays the GUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     * Can be null if this is the first screen being displayed.
     */
    public HomeScreenGUI(GUI previousGUI)
    {
        super(previousGUI, "HomeScreenGUI", null);
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons to be displayed on the GUI. Each button is associated with an action listener
     * that performs a specific action when the button is clicked.
     *
     * @return An array of buttons to be displayed on the GUI.
     */
    @Override
    protected JButton[] setupButtons()
    {
        JButton loginButton = new JButton("Log In");
        JButton createAccountButton = new JButton("Create Account");
        JButton infoButton = new JButton("Info");
        JButton searchButton = new JButton("Search");

        loginButton.addActionListener(e ->
        {
            new LoginOptionPane(this); //Open the login pop-up
        });

        infoButton.addActionListener(e ->
        {
            dispose();
            new InfoPageGUI(this); //Open the info page
        });

        createAccountButton.addActionListener(e ->
        {
            dispose();
            new UserCreateGUI(this); //Open a UserCreateGUI
        });

        searchButton.addActionListener(e ->
        {
            dispose();
            new ItemSearchGUI(this); //Open an ItemSearchGUI
        });

        return new JButton[]{loginButton, createAccountButton, searchButton, infoButton};
    }

    /**
     * Sets up the panels to be displayed on the GUI. This includes a welcome label
     * and a panel containing the buttons set up in setupButtons.
     */
    @Override
    protected void setupPanels()
    {
        JLabel welcomeLabel = new JLabel("Welcome to LibraryDBMS v1.337");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        GUIPanel.add(panel, BorderLayout.NORTH);
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}