package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.item.ItemSearchGUI;
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
public class MenuPageGUI {
    private JButton sökButton;
    private JButton mittKontoButton;
    private JButton LoggaUtButton;
    private JButton tillbakaButton;
    private JButton userButton;
    private JFrame menuFrame;
    private JPanel menuPanel;
    private User loggedInUser;

    public String username;


    public MenuPageGUI(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.username = loggedInUser.getUsername();
    }



    public void menuGUI(){
        menuFrame = new JFrame("MenuPageGUI");
        menuPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");
        sökButton = new JButton("ItemSearchGUI");
        LoggaUtButton = new JButton("HomeScreenGUI");
        mittKontoButton = new JButton("MyAccountGUI");
        userButton = new JButton("UserGUI");

        JLabel welcomeLabel = new JLabel("Välkommen, " + username + "!");
        menuPanel.add(welcomeLabel);
        menuPanel.add(tillbakaButton);
        menuPanel.add(LoggaUtButton);
        menuPanel.add(mittKontoButton);
        menuPanel.add(sökButton);
        menuPanel.add(userButton);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new LoginScreenGUI();
            }
        });
        LoggaUtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new HomeScreenGUI();
            }
        });
        mittKontoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new UserUpdateGUI(LibraryManager.getCurrentUser());
            }
        });
        sökButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
               ItemSearchGUI search = new ItemSearchGUI();
               search.searchGUI();

            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                UserWelcomeGUI userWelcomeGUI = new UserWelcomeGUI();
                userWelcomeGUI.WelcomeUserGUI();
            }
        });
        menuFrame.add(menuPanel);
        menuFrame.pack();
        menuFrame.setVisible(true);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
