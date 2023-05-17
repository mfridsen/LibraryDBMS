package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
    private JButton lånaButton;
    private JButton visaobjektButton;
    private List<User> searchResultList;

    public UserSearchResultGUI(GUI previousGUI, List<User> searchResultList) {
        super(previousGUI, "UserSearchResultGUI");
        this.searchResultList = searchResultList;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        lånaButton = new JButton("Låna");
        visaobjektButton = new JButton("Visa objekt");

        return null;
    }

    @Override
    protected void setupPanels() {
        searchPanel = new JPanel();
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
