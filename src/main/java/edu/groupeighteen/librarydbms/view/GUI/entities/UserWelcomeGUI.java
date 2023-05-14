package edu.groupeighteen.librarydbms.view.GUI.entities;
import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;

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
public class UserWelcomeGUI {
    private JLabel welcomeLabel;
    private JButton searchButton;
    private JButton tillbakaButton;
    private JPanel welcomePanel;
    private JFrame welcomeFrame;
    private User user;

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

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose();
                MenuPageGUI menuPage = new MenuPageGUI(LibraryManager.getCurrentUser());
                menuPage.menuGUI();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose();
               UserSearchGUI userSearchGUI = new UserSearchGUI();
               userSearchGUI.SearchGUI();
            }
        });
    }

    public static void main(String[] args) {
        UserWelcomeGUI userWelcomeGUI = new UserWelcomeGUI();
        userWelcomeGUI.WelcomeUserGUI();
    }
}
