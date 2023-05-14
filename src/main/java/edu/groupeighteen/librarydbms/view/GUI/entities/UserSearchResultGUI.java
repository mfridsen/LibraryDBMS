package edu.groupeighteen.librarydbms.view.GUI.entities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 *  * this class displays results for a search performed in the UserSearchGUI
 *  leads to UserGUI
 */

public class UserSearchResultGUI {
    private JPanel searchPanel;
    private JFrame searchFrame;
    private JButton lånaButton;
    private JButton tillbakaButton;
    private JButton visaobjektButton;
    private String searchedUser;
    private String[] userArray;

    public UserSearchResultGUI(String searchedUser, String[] userArray) {
        this.searchedUser = searchedUser;
        this.userArray = userArray;
    }

    public void userResults() {
        searchPanel = new JPanel();
        searchFrame = new JFrame("UserSearchResultGUI");
        lånaButton = new JButton("Låna");
        tillbakaButton = new JButton("Tillbaka");
        visaobjektButton = new JButton("Visa objekt");

        JLabel resultatLabel;
        if (searchedUser != null && containsIgnoreCase(searchedUser, userArray)) {
            resultatLabel = new JLabel("Resultat: " + searchedUser);
        } else {
            resultatLabel = new JLabel("Inget resultat hittades.");
        }
        searchPanel.add(resultatLabel);
        searchPanel.add(lånaButton);
        searchPanel.add(tillbakaButton);
        searchPanel.add(visaobjektButton);

        searchFrame.getContentPane().add(searchPanel); // Add the panel to the content pane
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchFrame.pack();
        searchFrame.setVisible(true);
    }

    private boolean containsIgnoreCase(String search, String[] source) {
        for (String item : source) {
            if (item.equalsIgnoreCase(search)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Test code
        String[] userArray = { "User1", "User2", "User3", "User4", "User5" };
        UserSearchResultGUI userSearchResultGUI = new UserSearchResultGUI("User3", userArray);
        userSearchResultGUI.userResults();
    }
}
