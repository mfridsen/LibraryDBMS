package edu.groupeighteen.librarydbms.view;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-05
 */
public class LoginErrorGUI extends GUI
{
    private JLabel errorMessageLabel;
    private JButton okButton;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     * @param previousGUI the previous GUI object.
     */
    public LoginErrorGUI(GUI previousGUI)
    {
        super(previousGUI, "LoginErrorGUI", null);
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {
        okButton = new JButton("OK");

        okButton.addActionListener(e ->
        {
            dispose();
            new LoginScreenGUI(this);
        });

        return new JButton[]{okButton};
    }

    @Override
    protected void setupPanels()
    {
        JPanel panel = new JPanel();
        errorMessageLabel = new JLabel("Error! Enter the correct username and password");

        panel.setLayout(new GridLayout(2, 1));
        panel.add(errorMessageLabel);
        GUIPanel.add(panel);
    }
}

