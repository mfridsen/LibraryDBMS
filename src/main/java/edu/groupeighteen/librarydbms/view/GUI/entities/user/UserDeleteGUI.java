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
    private JButton cancelButton;
    private JButton confirmButton;
    private JLabel deleteUser;

    public UserDeleteGUI(User user, GUI previousGUI) {
        super(previousGUI, "UserDeleteGUI");
        this.user = user;
        this.deleteUser = new JLabel("Delete user: " + user.getUsername() + "?");
        setupButtons();
        addButtonsToPanel(new JButton[]{confirmButton, cancelButton});
        setupPanels();
        this.displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        return new JButton[0];
    }

    @Override
    protected void setupPanels() {

    }
}
