package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserDeleteGUI extends GUI {
    private User user;
    private JButton confirmButton;
    private JLabel deleteUser;

    public UserDeleteGUI(User user, GUI previousGUI) {
        super(previousGUI, "UserDeleteGUI");
        this.user = user;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e -> {
            dispose();
            //delete user
            //previous gui = null
            //return to appropriate gui
        });
        return new JButton[]{confirmButton};
    }

    @Override
    protected void setupPanels() {
        this.deleteUser = new JLabel("Delete user: " + user.getUsername() + "?");

    }
}
