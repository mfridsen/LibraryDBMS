package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.DeletionException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.NullEntityException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-22
 */
public class ItemDeleteGUI extends GUI {
    //TODO-comment
    private final Item itemToDelete;
    private JPasswordField passwordField;

    public ItemDeleteGUI(GUI previousGUI, Item itemToDelete) {
        super(previousGUI, "ItemDeleteGUI");
        this.itemToDelete = itemToDelete;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        JButton confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e -> {
            //TODO-prio you shouldn't be able to access this GUI at all without being logged in (and staff)
            if (LibraryManager.getCurrentUser() != null) {
                try {
                    if (UserHandler.validate(LibraryManager.getCurrentUser(),
                            Arrays.toString(passwordField.getPassword()))) {
                        ItemHandler.deleteItem(itemToDelete);
                        //dispose();
                        //TODO-prio return to some other GUI, probably the LoginGUI
                    }
                } catch (DeletionException | UserValidationException nullEntityException) {
                    nullEntityException.printStackTrace();
                }
            }
        });
        return new JButton[]{confirmButton};
    }

    @Override
    protected void setupPanels() {
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        GUIPanel.add(passwordPanel);
    }
}

