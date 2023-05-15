package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;
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
public class UserSearchGUI extends GUI {
    private JLabel usernameLabel;
    private JLabel userIDLabel;
    private JTextField usernameField;
    private JTextField userIDField;
    private JButton searchButton;
    private JPanel searchPanel;
    public UserSearchGUI(GUI previousGUI) {
        super(previousGUI, "UserSearchGUI");
        setupButtons();
        addButtonsToPanel(new JButton[]{searchButton});
        setupSearchPanel();
        setupPanels();
        this.displayGUI();
    }

    protected JButton[] setupButtons(){
        searchButton = new JButton("UserSearchResultGUI");
        searchButton.addActionListener(e -> {
            dispose();
            new UserSearchResultGUI(this, Integer.parseInt(userIDField.getText()), usernameField.getText());
        });
        return new JButton[]{searchButton};
    }

    private void setupSearchPanel() {
        usernameLabel = new JLabel("Search username: ");
        usernameField = new JTextField(10);
        userIDLabel = new JLabel("Search userID: ");
        userIDField = new JTextField(5);
        searchPanel = new JPanel();
        searchPanel.add(usernameLabel);
        searchPanel.add(usernameField);
        searchPanel.add(userIDLabel);
        searchPanel.add(userIDField);
    }

    @Override
    protected void setupPanels(){
        GUIPanel = new JPanel(new BorderLayout());
        GUIPanel.add(searchPanel, BorderLayout.NORTH);
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}
