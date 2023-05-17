package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserGUI extends GUI {
    private final User user;
    private JPanel scrollPanePanel;

    /**
     *
     * @param
     * @param user
     */

    public UserGUI(User user, GUI prevoiusGUI) {
        super(prevoiusGUI, "UserGUI");
        this.user = user;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    protected JButton[] setupButtons() {
        JButton updateButton = new JButton("Update User");
        updateButton.addActionListener(e -> {
            dispose();
            new UserUpdateGUI(this, user);
        });
        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e -> {
            dispose();
            new UserDeleteGUI(user, this);
        });
        return new JButton[]{deleteButton, updateButton};
    }

    private void setupScrollPane() {
        String[] columnNames = {};

        Object[][] data = {
                {"User ID", user.getUserID()},
                {"Username", user.getUsername()},

        };
        JTable userUpdateTable = setupScrollPaneTable(columnNames, data);
        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);


    }

    @Override
    protected void setupPanels() {
        JLabel userIDLabel = new JLabel("userID: " + user.getUserID());

    }
    public User getUser(){
        return user;
    }
}
