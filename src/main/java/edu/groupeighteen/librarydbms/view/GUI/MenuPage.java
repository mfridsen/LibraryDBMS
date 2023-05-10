package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.view.GUI.entities.ItemSearchGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.UserGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class MenuPage {
    public JButton sökButton;
    public JButton mittKontoButton;
    public JButton LoggaUtButton;
    public JButton tillbakaButton;
    public JButton userButton;
    public JFrame menuFrame;
    public JPanel menuPanel;

    public String username;


    public MenuPage(String username) {
        this.username = username;
    }



    public void menuGUI(){
        menuFrame = new JFrame("Meny");
        menuPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");
        sökButton = new JButton("Sök");
        LoggaUtButton = new JButton("Logga Ut");
        mittKontoButton = new JButton("Mitt Konto");
        userButton = new JButton("User");

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
                new LoginScreen();
            }
        });
        LoggaUtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new HomeScreen();
            }
        });
        mittKontoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                MyAccount.alterPersonalInfo accountInfo = new MyAccount.alterPersonalInfo();
                accountInfo.changeInfoGUI();
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
                UserGUI userGUI = new UserGUI();
                userGUI.WelcomeUserGUI();
            }
        });
        menuFrame.add(menuPanel);
        menuFrame.pack();
        menuFrame.setVisible(true);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


}
