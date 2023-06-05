package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * The RentalCreateGUI class is a subclass of GUI that provides a graphical user interface
 * for creating a new Rental object. It contains two input fields for entering a user ID and an item ID.
 */
public class RentalCreateGUI extends GUI
{
    private JTextField userIDField;
    private JTextField itemIDField;

    /**
     * Constructs a new RentalCreateGUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     */
    public RentalCreateGUI(GUI previousGUI)
    {
        super(previousGUI, "RentalCreateGUI", null);
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
    protected JButton[] setupButtons()
    {
        JButton resetButton = new JButton("Reset Fields");
        resetButton.addActionListener(e ->
        {
            resetFields();
        });

        JButton createButton = new JButton("Create Rental");
        createButton.addActionListener(e ->
        {
            createRental();
        });

        return new JButton[]{resetButton, createButton};
    }

    /**
     * Resets the text fields for user ID and item ID to be empty.
     */
    private void resetFields()
    {
        userIDField.setText("");
        itemIDField.setText("");
    }

    /**
     * Attempts to create a new Rental.
     * If the user ID or item ID fields are empty, or if they cannot be parsed as integers,
     * an error message is printed and the method returns early. If the fields are valid,
     * a new Rental is created and a new RentalGUI is opened for that rental.
     */
    private void createRental()
    {
        String userIDStr = userIDField.getText();
        String itemIDStr = itemIDField.getText();

        //Check if one or both fields are empty
        if (userIDStr.isEmpty())
        {
            System.err.println("To create a new Rental you need to enter in a user ID. User ID: " + userIDStr);
            resetFields();
            return;
        }
        if (itemIDStr.isEmpty())
        {
            System.err.println("To create a new Rental you need to enter in an item ID. Item ID: " + itemIDStr);
            resetFields();
            return;
        }

        int userID = 0;
        int itemID = 0;

        //Attempt to parse the user ID and item ID as integers
        try
        {
            userID = Integer.parseInt(userIDStr);
        }
        catch (NumberFormatException nfe)
        {
            //If the parsing fails, print an error message
            System.err.println("Rental creation failed: user ID not an int. User ID: " + userIDStr);
            resetFields();
            return;
        }
        try
        {
            itemID = Integer.parseInt(itemIDStr);
        }
        catch (NumberFormatException nfe)
        {
            //If the parsing fails, print an error message
            System.err.println("Rental creation failed: item ID not an int. Item ID: " + itemIDStr);
            resetFields();
            return;
        }

        //If successful, dispose, create a new rental and open a new RentalGUI for that rental
        /*try {
            dispose();
            Rental newRental = RentalHandler.createNewRental(userID, itemID, LocalDateTime.now());
            new RentalGUI(this, newRental);
        } catch (SQLException sqle) {
            System.err.println("Error connecting to database: " + sqle.getMessage()); //TODO-exception
            sqle.printStackTrace();
            LibraryManager.exit(1);
        }*/
    }

    /**
     * Overrides the setupPanels method from the GUI class.
     * Sets up the panel containing the text fields for user ID and item ID.
     */
    @Override
    protected void setupPanels()
    {
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(2, 2));
        JLabel userIDLabel = new JLabel("Enter user ID:");
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