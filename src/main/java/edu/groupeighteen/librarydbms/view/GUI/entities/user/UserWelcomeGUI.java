package edu.groupeighteen.librarydbms.view.GUI.entities.user;
import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 * this class displays all information about a single user object
 */
public class UserWelcomeGUI extends GUI {
    private JLabel welcomeLabel;
    private JButton searchButton;
    private JButton tillbakaButton;
    private JPanel welcomePanel;
    private JFrame welcomeFrame;
    private User user;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     */
    public UserWelcomeGUI(GUI previousGUI, User user) {
        super(previousGUI, "UserWelcomeGUI");
        setupButtons();
        addButtonsToPanel(new JButton[]{searchButton, tillbakaButton});
        setupPanels();
        this.displayGUI();
    }

    public void WelcomeUserGUI() {
        welcomeLabel = new JLabel("Sök");
        searchButton = new JButton("UserSearchGUI");
        tillbakaButton = new JButton("Tillbaka");
        welcomePanel = new JPanel();
        welcomeFrame = new JFrame("UserGUI");

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(searchButton);
        welcomePanel.add(tillbakaButton);

        welcomeFrame.add(welcomePanel);
        welcomeFrame.pack();
        welcomeFrame.setVisible(true);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        searchButton.addActionListener(e -> {

        });
    }

    @Override
    protected JButton[] setupButtons() {
        return new JButton[0];
    }

    @Override
    protected void setupPanels() {

    }
}
