package edu.groupeighteen.librarydbms.view.GUI.entities;
import edu.groupeighteen.librarydbms.view.GUI.MenuPage;

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
public class UserGUI {
    private JLabel welcomeLabel;
    private JButton searchButton;
    private JButton tillbakaButton;
    private JPanel welcomePanel;
    private JFrame welcomeFrame;

    public void WelcomeUserGUI() {
        welcomeLabel = new JLabel("Välkommen");
        searchButton = new JButton("Sök");
        tillbakaButton = new JButton("Tillbaka");
        welcomePanel = new JPanel();
        welcomeFrame = new JFrame("Välkommen");

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
                MenuPage menuPage = new MenuPage("hej");
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
        UserGUI userGUI = new UserGUI();
        userGUI.WelcomeUserGUI();
    }
}
