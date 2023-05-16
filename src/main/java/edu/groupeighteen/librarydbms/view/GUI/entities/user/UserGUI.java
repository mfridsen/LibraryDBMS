package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserGUI extends GUI {
    private User user;
    private JLabel userIDLabel;
    private JLabel usernameLabel;

    private JLabel passwordLabel;



    private JButton tillbakaButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel UserGUIPanel;
    private JFrame UserGUIFrame;


    public UserGUI(User user, GUI prevoiusGUI) {
        super(prevoiusGUI, "UserGUI");
        this.user = user;
        this.userIDLabel = new JLabel("userID: " + user.getUserID());
        setupButtons();
        addButtonsToPanel(new JButton[]{deleteButton, updateButton});
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
