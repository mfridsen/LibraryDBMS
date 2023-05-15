package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

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

public class UserSearchResultGUI extends GUI {
    private JPanel searchPanel;
    private JFrame searchFrame;
    private JButton lånaButton;
    private JButton visaobjektButton;
    private int userID;
    private String username;

    public UserSearchResultGUI(GUI previousGUI, int userID, String username) {
        super(previousGUI, "UserSearchResultGUI");
        this.userID = userID;
        this.username = username;
    }

    @Override
    protected void setupButtons() {

    }

    @Override
    protected void setupPanels() {

    }

    public void userResults() {
        searchPanel = new JPanel();
        searchFrame = new JFrame("UserSearchResultGUI");
        lånaButton = new JButton("Låna");
        visaobjektButton = new JButton("Visa objekt");

        JLabel resultatLabel;
        /*
        if (username != null && containsIgnoreCase(username)) {
            resultatLabel = new JLabel("Resultat: " + username);
        } else {
            resultatLabel = new JLabel("Inget resultat hittades.");
        }

         */
        resultatLabel = new JLabel("Inget resultat hittades.");

        searchPanel.add(resultatLabel);
        searchPanel.add(lånaButton);
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

    public void searchUserID(int userID){

    }

    public void searchUsername(String username){

    }
}
