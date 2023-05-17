package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 */
public class UserUpdateGUI extends GUI {
    private final User oldUser;
    private User newUser;
    private User user;

    private JTextField usernameField;
    private JTable userUpdateTable;
    private JPasswordField passwordField;
    private JPanel scrollPanePanel;


    public UserUpdateGUI(GUI previousGUI, User userToUpdate) {
        super(previousGUI, "UserUpdateGUI" + userToUpdate.getUserID());
        this.oldUser = userToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    private void setupScrollPane() {
        String[] columNames = {"Old Name", "New Name"};
        Object[][] data = {
                {"User ID", oldUser.getUserID(), ""},
                {"Username", oldUser.getUsername(), ""},
        };
        userUpdateTable = setupTableWithEditableCells(columnNames, data, 2);

        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);


    }

    protected JButton[] setupButtons() {
        JButton changeButton = new JButton("Change");

        changeButton.addActionListener(e -> {
            dispose();
            LibraryManager.getCurrentUser().setUsername(usernameField.getText());
            LibraryManager.getCurrentUser().setPassword(Arrays.toString(passwordField.getPassword()));
            try {
                UserHandler.updateUser(LibraryManager.getCurrentUser());
                new MenuPageGUI(LibraryManager.getCurrentUser(), this);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);//TODO-Exception
            }

        });
        return new JButton[]{changeButton};

    }

    private void resetCells() {
        for (int row = 0; row < userUpdateTable.getRowCount(); row++) {
            for (int col = 0; col < userUpdateTable.getColumnCount(); col++) {
                if (col == 2) { //Assuming the 3rd column is the editable column
                    userUpdateTable.setValueAt("", row, col);
                }

            }


            protected void setupPanels () {
                JLabel usernameLabel = new JLabel("Username:");
                usernameField = new JTextField(10);
                JLabel passwordLabel = new JLabel("Password:");
                passwordField = new JPasswordField(10);
                JPanel accountPanel = new JPanel();

                accountPanel.add(usernameLabel);
                accountPanel.add(usernameField);
                accountPanel.add(passwordLabel);
                accountPanel.add(passwordField);

                GUIPanel.add(accountPanel);
            }
        }
    }
}