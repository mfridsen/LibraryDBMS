package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserDeleteGUI {
    private User user;
    private JButton cancelButton;
    private JButton confirmButton;
    private JLabel deleteUser;

    public UserDeleteGUI(User user) {
        this.user = user;
        this.deleteUser = new JLabel("Delete user: " + user.getUsername() + "?");
    }
}
