package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.Publisher.PublisherNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserNotFoundException;
import edu.groupeighteen.librarydbms.view.entities.rental.RentalGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherCreateGUI extends GUI {
    private JTextField userIDField;
    private JTextField PublisherIDField;

    /**
     * Constructs a new RentalCreateGUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     */
    public PublisherCreateGUI(GUI previousGUI) {
        super(previousGUI, "PublisherCreateGUI");
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
            createPublisher();
        });

        return new JButton[]{resetButton, createButton};
    }

    /**
     * Resets the text fields for user ID and Publisher ID to be empty.
     */
    private void resetFields() {
        userIDField.setText("");
        PublisherIDField.setText("");
    }

    /**
     * Attempts to create a new Rental.
     * If the user ID or Publisher ID fields are empty, or if they cannot be parsed as integers,
     * an error message is printed and the method returns early. If the fields are valid,
     * a new Rental is created and a new RentalGUI is opened for that rental.
     */
    private void createPublisher() {
        String userIDStr = userIDField.getText();
        String PublisherIDStr = PublisherIDField.getText();

        //Check if one or both fields are empty
        if (userIDStr.isEmpty()) {
            System.err.println("To create a new Publisher you need to enter in a user ID. User ID: " + userIDStr);
            resetFields();
            return;
        }
        if (PublisherIDStr.isEmpty()) {
            System.err.println("To create a new Publisher you need to enter in an Publisher ID. Publisher ID: " + PublisherIDStr);
            resetFields();
            return;
        }

        int userID = 0;
        int PublisherID = 0;

        //Attempt to parse the user ID and Publisher ID as integers
        try {
            userID = Integer.parseInt(userIDStr);
        } catch (NumberFormatException nfe) {
            //If the parsing fails, print an error message
            System.err.println("Publisher creation failed: user ID not an int. User ID: " + userIDStr);
            resetFields();
            return;
        }
        try {
            PublisherID = Integer.parseInt(PublisherIDStr);
        } catch (NumberFormatException nfe) {
            //If the parsing fails, print an error message
            System.err.println("Publisher creation failed: Publisher ID not an int. Publisher ID: " + PublisherIDStr);
            resetFields();
            return;
        }

        //If successful, dispose, create a new Publisher and open a new PublisherGUI for that Publisher
        /*try {
            dispose();
            Rental newRental = RentalHandler.createNewRental(userID, PublisherID);
            new RentalGUI(this, newRental);
        } catch (UserNotFoundException | PublisherNotFoundException | RentalNotAllowedException | InvalidIDException sqle) {
            System.err.println("Error connecting to database: " + sqle.getMessage()); //TODO-exception
            sqle.printStackTrace();
            LibraryManager.exit(1);
        }*/
    }

    /**
     * Overrides the setupPanels method from the GUI class.
     * Sets up the panel containing the text fields for user ID and Publisher ID.
     */
    @Override
    protected void setupPanels() {
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(2, 2));
        JLabel userIDLabel = new JLabel("Enter Publisher ID:");
        userIDField = new JTextField(10);
        JLabel PublisherIDLabel = new JLabel("Enter Publisher ID:");
        PublisherIDField = new JTextField(10);
        textFieldsPanel.add(userIDLabel);
        textFieldsPanel.add(userIDField);
        textFieldsPanel.add(PublisherIDLabel);
        textFieldsPanel.add(PublisherIDField);

        GUIPanel.add(textFieldsPanel, BorderLayout.NORTH);
    }

}
