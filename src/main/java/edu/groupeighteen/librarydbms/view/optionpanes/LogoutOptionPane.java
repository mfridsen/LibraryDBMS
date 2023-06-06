package edu.groupeighteen.librarydbms.view.optionpanes;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.HomeScreenGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.optionpanes
 * @contact matfir-1@student.ltu.se
 * @date 5/19/2023
 * <p>
 * This class represents the Logout option pane in the application's Graphical User Interface (GUI).
 * The LogoutOptionPane is a pop-up dialog that prompts the user to confirm or cancel their logout request.
 */
public class LogoutOptionPane extends OptionPane
{
    private JPanel buttonPanel;

    /**
     * Constructor for the LogoutOptionPane. Sets up panels and buttons, and displays the option pane.
     *
     * @param previousGUI The GUI that was displayed before this one.
     */
    public LogoutOptionPane(GUI previousGUI)
    {
        super(previousGUI);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel logoutLabel = new JLabel("Log Out?");
        logoutLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
        panel.add(logoutLabel); // Add to main panel

        setupButtonPanel();

        // Add buttonPanel to main panel
        panel.add(buttonPanel);

        // Implement button behavior here...

        JOptionPane.showOptionDialog(null, panel, "Logout", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{}, null);
    }

    /**
     * Sets up the button panel to be displayed on the LogoutOptionPane. This panel includes
     * a Cancel button that, when clicked, discards the logout request and returns to the previous GUI,
     * and a Confirm button that, when clicked, logs out the current user and returns to the HomeScreenGUI.
     */
    private void setupButtonPanel()
    {
        // Button panel with FlowLayout to hold Cancel and Confirm buttons
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(e ->
        {
            JOptionPane.getRootFrame().dispose();
            previousGUI.displayGUI();
        });

        JButton confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e ->
        {
            //Dispose previous GUI
            previousGUI.dispose();
            //Log out user
            LibraryManager.setCurrentUser(null);
            //Go to Home Screen, null previousGUI because otherwise things will get messy
            new HomeScreenGUI(previousGUI);
        });

        // Add buttons to buttonPanel
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
    }
}