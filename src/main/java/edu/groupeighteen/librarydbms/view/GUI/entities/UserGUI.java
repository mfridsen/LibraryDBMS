package edu.groupeighteen.librarydbms.view.GUI.entities;

import edu.groupeighteen.librarydbms.model.entities.User;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserGUI {
    private User user;
    private JLabel userIDLabel;
    private JLabel usernameLabel;

    private JLabel passwordLabel;



    private JButton tillbakaButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel UserGUIPanel;
    private JFrame UserGUIFrame;


    public UserGUI(User user) {
        this.user = user;
        this.userIDLabel = new JLabel("userID: " + user.getUserID());
    }
}
