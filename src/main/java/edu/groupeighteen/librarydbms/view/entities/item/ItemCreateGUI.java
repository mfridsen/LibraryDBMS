package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.ItemNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserNotFoundException;
import edu.groupeighteen.librarydbms.view.entities.rental.RentalGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.item
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class ItemCreateGUI extends GUI {
    private JTextField userIDField;
    private JTextField itemIDField;

    /**
     * Constructs a new RentalCreateGUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     */
    public ItemCreateGUI(GUI previousGUI) {
        super(previousGUI, "ItemCreateGUI");
        setupPanels();
        displayGUI();
    }

    /**
     * Overrides the setupButtons method from the GUI class.
     * Sets up the "Reset Fields" and "Create Rental" buttons.
     *
     * @return An array of the two JButtons created in this method.
     */
    @Override
    protected JButton[] setupButtons() {
        JButton resetButton = new JButton("Reset Fields");
        resetButton.addActionListener(e -> {
            resetFields();
        });

        JButton createButton = new JButton("Create Rental");
        createButton.addActionListener(e -> {
            createItem();
        });

        return new JButton[]{resetButton, createButton};
    }

    /**
     * Resets the text fields for user ID and item ID to be empty.
     */
    private void resetFields() {
        userIDField.setText("");
        itemIDField.setText("");
    }

    /**
     * Attempts to create a new Rental.
     * If the user ID or item ID fields are empty, or if they cannot be parsed as integers,
     * an error message is printed and the method returns early. If the fields are valid,
     * a new Rental is created and a new RentalGUI is opened for that rental.
     */
    private void createItem() {
        String userIDStr = userIDField.getText();
        String itemIDStr = itemIDField.getText();

        //Check if one or both fields are empty
        if (userIDStr.isEmpty()) {
            System.err.println("To create a new item you need to enter in a user ID. User ID: " + userIDStr);
            resetFields();
            return;
        }
        if (itemIDStr.isEmpty()) {
            System.err.println("To create a new item you need to enter in an item ID. Item ID: " + itemIDStr);
            resetFields();
            return;
        }

        int userID = 0;
        int itemID = 0;

        //Attempt to parse the user ID and item ID as integers
        try {
            userID = Integer.parseInt(userIDStr);
        } catch (NumberFormatException nfe) {
            //If the parsing fails, print an error message
            System.err.println("Item creation failed: user ID not an int. User ID: " + userIDStr);
            resetFields();
            return;
        }
        try {
            itemID = Integer.parseInt(itemIDStr);
        } catch (NumberFormatException nfe) {
            //If the parsing fails, print an error message
            System.err.println("Item creation failed: item ID not an int. Item ID: " + itemIDStr);
            resetFields();
            return;
        }

        //If successful, dispose, create a new item and open a new ItemGUI for that item
        try {
            dispose();
            Rental newRental = RentalHandler.createNewRental(userID, itemID);
            new RentalGUI(this, newRental);
        } catch (UserNotFoundException | ItemNotFoundException | RentalNotAllowedException | InvalidIDException sqle) {
            System.err.println("Error connecting to database: " + sqle.getMessage()); //TODO-exception
            sqle.printStackTrace();
            LibraryManager.exit(1);
        }
    }

    /**
     * Overrides the setupPanels method from the GUI class.
     * Sets up the panel containing the text fields for user ID and item ID.
     */
    @Override
    protected void setupPanels() {
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(2, 2));
        JLabel userIDLabel = new JLabel("Enter Item ID:");
        userIDField = new JTextField(10);
        JLabel itemIDLabel = new JLabel("Enter item ID:");
        itemIDField = new JTextField(10);
        textFieldsPanel.add(userIDLabel);
        textFieldsPanel.add(userIDField);
        textFieldsPanel.add(itemIDLabel);
        textFieldsPanel.add(itemIDField);

        GUIPanel.add(textFieldsPanel, BorderLayout.NORTH);
    }
}
