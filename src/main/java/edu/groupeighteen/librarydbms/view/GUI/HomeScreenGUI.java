package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class HomeScreenGUI extends GUI {
//TODO-prio dela upp
    public JLabel welcomeLabel;
    public JPanel buttonPanel;
    public JButton loginHereButton;
    public JButton infoButton;
    public JButton skapakontoButton;

    public HomeScreenGUI(GUI previousGUI) {
        super(previousGUI, "HomeScreenGUI");
        setupPanels();
        displayGUI();
    }


    @Override
    protected JButton[] setupButtons() {
        buttonPanel = new JPanel();
        loginHereButton = new JButton("LoginScreenGUI");
        infoButton = new JButton("InfoPageGUI");
        skapakontoButton = new JButton("CreateAccountGUI");
        buttonPanel.add(loginHereButton);
        buttonPanel.add(infoButton);
        buttonPanel.add(skapakontoButton);

        loginHereButton.addActionListener(e -> {
            dispose(); // Close the current window
            new LoginScreenGUI(this); //Open the Log in page
        });

        infoButton.addActionListener(e -> {
            dispose();
            new InfoPageGUI(this);
        });

        skapakontoButton.addActionListener(e -> {
            dispose();
            new CreateAccountGUI(this);
        });

        return new JButton[]{loginHereButton, infoButton, skapakontoButton};
    }

    @Override
    protected void setupPanels() {
        welcomeLabel = new JLabel("Welcome to the library system");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        GUIPanel.add(panel, BorderLayout.NORTH);
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}

