package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.item.ItemSearchGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserUpdateGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserWelcomeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class MenuPageGUI extends GUI {
    private JButton sökButton;
    private JButton mittKontoButton;
    private JButton LoggaUtButton;
    private JButton userButton;
    private JPanel menuPanel;
    private User loggedInUser;

    public String username;


    public MenuPageGUI(User loggedInUser, GUI previousGUI) {
        super(previousGUI, "MenuPageGUI");
        this.loggedInUser = loggedInUser;
        this.username = loggedInUser.getUsername();
        setupPanels();
        displayGUI();
    }
    @Override
    protected JButton[] setupButtons() {
        sökButton = new JButton("ItemSearchGUI");
        LoggaUtButton = new JButton("HomeScreenGUI");
        mittKontoButton = new JButton("MyAccountGUI");
        userButton = new JButton("UserGUI");

        LoggaUtButton.addActionListener(e -> {
            dispose();
            new HomeScreenGUI(this);
        });

        mittKontoButton.addActionListener(e -> {
            dispose();
            new UserUpdateGUI(this, LibraryManager.getCurrentUser());
        });

        sökButton.addActionListener(e -> {
            dispose();
            new ItemSearchGUI(this);
        });

        userButton.addActionListener(e -> {
            dispose();
            new UserGUI(LibraryManager.getCurrentUser(), this);
        });

        return new JButton[]{LoggaUtButton, mittKontoButton, sökButton, userButton};
    }

    @Override
    protected void setupPanels() {
        JLabel welcomeLabel = new JLabel("Välkommen, " + username + "!");
        menuPanel.add(welcomeLabel);
    }
}
