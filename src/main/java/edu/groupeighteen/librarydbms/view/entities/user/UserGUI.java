package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 */
public class UserGUI extends GUI
{
    //TODO- fält som ska visas i denna ordning:
    //  userID, userName, type,
    //  firstName, lastName, Email
    //  allowedRentals, currentRentals, allowedToRent, lateFee
    private final User user;
    private JPanel scrollPanePanel;

    /**
     * @param
     * @param user
     */

    public UserGUI(GUI previousGUI, User user)
    {
        super(previousGUI, "UserGUI", user);
        this.user = user;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    protected JButton[] setupButtons()
    {
        JButton updateButton = new JButton("Update User");
        updateButton.addActionListener(e ->
        {
            dispose();
            new UserUpdateGUI(this, user);
        });
        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e ->
        {
            dispose();
            new UserDeleteGUI(this, user);
        });
        return new JButton[]{deleteButton, updateButton};
    }

    private void setupScrollPane()
    {
        String[] columnNames = {"Property", "Value"};

        Object[][] data = {
                {"User ID", user.getUserID()},
                {"Username", user.getUsername()},

        };
        JTable userUpdateTable = setupTable(columnNames, data);
        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }

    public User getUser()
    {
        return user;
    }
}
