package edu.groupeighteen.librarydbms.view.GUI.entities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 *  * this class handles searching for users
 *   * leads to UserSearchResultGUI
 */
public class UserSearchGUI {
    private JTextField usernameField;
    private JButton searchButton;
    private JButton tillbakaButton;
    private JPanel searchPanel;
    private JFrame searchFrame;

    private String[] userArray = { "Kalle Karlsson", "Johan Lund", "Max Zorin", "James Bond", "Axl Rose" };

    public void SearchGUI() {
        JLabel searchLabel = new JLabel("Sök användare");
        usernameField = new JTextField(10);
        searchButton = new JButton("UserSearchResultGUI");
        tillbakaButton = new JButton("Tillbaka");
        searchPanel = new JPanel();
        searchFrame = new JFrame("UserSearchGUI");

        searchPanel.add(searchLabel);
        searchPanel.add(usernameField);
        searchPanel.add(searchButton);
        searchPanel.add(tillbakaButton);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFrame.dispose();
                UserWelcomeGUI userWelcomeGUI = new UserWelcomeGUI();
                userWelcomeGUI.WelcomeUserGUI();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchedUsername = usernameField.getText();
                searchFrame.dispose();
                UserSearchResultGUI userSearchResultGUI = new UserSearchResultGUI(searchedUsername, userArray);
                userSearchResultGUI.userResults();
                //hej
            }
        });
    }

    public static void main(String[] args) {
        UserSearchGUI searchGUI = new UserSearchGUI();
        searchGUI.SearchGUI();
    }
}
