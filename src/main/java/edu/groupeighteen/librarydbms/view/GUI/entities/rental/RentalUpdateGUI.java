package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/14/2023
 *
 * This class represents a graphical user interface (GUI) for updating a Rental object.
 * The class extends the generic GUI class, and provides a specific implementation for
 * managing the update process of a rental.
 *
 * The GUI consists of a table for displaying the current details of a rental, and fields
 * for entering new values for these details. The GUI also includes buttons for resetting
 * the fields and for confirming the update.
 *
 * The class holds a reference to the rental that is to be updated, as well as a reference
 * to the new, updated rental. It also keeps a reference to the table displaying the rental details,
 * and to the panel containing this table.
 *
 * The GUI is set up by first setting up the scroll pane and panels, and then displaying the GUI.
 *
 * The class offers the following main functionalities:
 * - Setting up buttons for resetting the fields and confirming the update.
 * - Setting up a scroll pane for displaying the rental details.
 * - Setting up panels for the GUI.
 *
 * @see GUI
 * @see Rental
 */
public class RentalUpdateGUI extends GUI {
    //The rental object to update
    private final Rental oldRental;
    //The new rental object
    private Rental newRental;
    //We need the table to be a member variable in order to access its data via the buttons
    private JTable rentalUpdateTable;
    //The panel containing the scroll pane which displays the Rental data
    private JPanel scrollPanePanel;

    /**
     * Constructs a new RentalUpdateGUI instance. This GUI allows the user to update the specified rental.
     *
     * @param previousGUI The GUI that was previously displayed. This is used for navigation purposes.
     * @param rentalToUpdate The Rental object that the user wants to update. The initial fields of the
     *                       GUI will be populated with the details of this rental.
     */
    public RentalUpdateGUI(GUI previousGUI, Rental rentalToUpdate) {
        super(previousGUI, "RentalUpdateGUI for rentalID = " + rentalToUpdate.getRentalID());
        this.oldRental = rentalToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons for the GUI.
     *
     * This method creates two buttons:
     * 1. "Reset" button: Clears the data cells in the rentalUpdateTable. Currently, it only clears the cells in the third column (0-indexed).
     *    There is a known bug where it does not clear the currently selected field.
     * 2. "RentalGUI" button: This duplicates the rentalToUpdate object, gets the new values from the rentalUpdateTable, and updates the rentalToUpdate object.
     *    It then calls the updateRental method from the RentalHandler to update the rental. If successful, it disposes of the current GUI and opens a new RentalGUI displaying the updated Rental object.
     *    If a SQLException occurs during the update, the system will exit with a status of 1.
     *    Note: The button's name is planned to be changed to "Confirm Update" in the future.
     *
     * @return An array of the two created JButton objects: [resetCellsButton, confirmUpdateButton].
     * @throws NumberFormatException If any of the fields that require a number receive an invalid input.
     * @throws DateTimeParseException If the date field receives an input that does not match the required date format.
     */
    protected JButton[] setupButtons() {
        //Clears the data cells
        JButton resetCellsButton = new JButton("Reset");
        //Reset the editable fields //TODO-bug doesn't clear selected field
        resetCellsButton.addActionListener(e -> {
            for (int row = 0; row < rentalUpdateTable.getRowCount(); row++) {
                for (int col = 0; col < rentalUpdateTable.getColumnCount(); col++) {
                    if (col == 2) { //Assuming the 3rd column is the editable column
                        rentalUpdateTable.setValueAt("", row, col);
                    }
                }
            }
        });

        //Updates rentalToUpdate and opens a new RentalGUI displaying the updated Rental object
        JButton confirmUpdateButton = new JButton("Confirm Update");
        confirmUpdateButton.addActionListener(e -> {
            //Duplicate rentalToUpdate
            newRental = new Rental(oldRental);

            //Get the new values from the table
            String userID = (String) rentalUpdateTable.getValueAt(0, 2);
            String username = (String) rentalUpdateTable.getValueAt(1, 2);
            String itemID = (String) rentalUpdateTable.getValueAt(2, 2);
            String itemTitle = (String) rentalUpdateTable.getValueAt(3, 2);
            String rentalDate = (String) rentalUpdateTable.getValueAt(4, 2);

            //Update the rentalToUpdate object
            //Only update if new value is not null or empty
            try {
                if (userID != null && !userID.isEmpty()) {
                    newRental.setUserID(Integer.parseInt(userID));
                }
                //No parsing required for username, it is a string
                if (username != null && !username.isEmpty()) {
                    newRental.setUsername(username);
                }
                if (itemID != null && !itemID.isEmpty()) {
                    newRental.setItemID(Integer.parseInt(itemID));
                }
                //No parsing required for itemTitle, it is a string
                if (itemTitle != null && !itemTitle.isEmpty()) {
                    newRental.setTitle(itemTitle);
                }
                if (rentalDate != null && !rentalDate.isEmpty()) {
                    newRental.setRentalDate(LocalDateTime.parse(rentalDate));
                }
            } catch (NumberFormatException nfe) {
                System.err.println("One of the fields that requires a number received an invalid input.");
            } catch (DateTimeParseException dtpe) {
                System.err.println("The date field received an invalid input. Please ensure it is in the correct format.");
            }

            //Now you can call the update method
            try {
                RentalHandler.updateRental(oldRental, newRental);
                dispose();
                new RentalGUI(this, newRental);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            }
        });

        return new JButton[]{resetCellsButton, confirmUpdateButton};
    }

    /**
     * Sets up the scroll pane for the GUI, including creating a table with the
     * current details of the rental, and setting it into a panel for display.
     *
     * The table is set up with three columns: "Property", "Old Value", and "New Value".
     * The "Property" column contains the names of the properties of the rental.
     * The "Old Value" column contains the current values of these properties.
     * The "New Value" column is initially empty and is where new values can be entered for updating the rental.
     *
     * After the table is set up, it is added to a scroll pane, which in turn is added to a panel
     * that uses a BorderLayout. The scroll pane is placed in the center of this panel.
     */
    protected void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Old Value", "New Value"};

        //Gather the data for the table. Each row corresponds to a property of the rental.
        //The first column is the name of the property, the second column is its old value,
        //and the third column (which is initially empty) will hold the new value.
        Object[][] data = {
                {"User ID", oldRental.getUserID(), ""},
                {"Username", oldRental.getUsername(), ""},
                {"Item ID", oldRental.getItemID(), ""},
                {"Item Title", oldRental.getTitle(), ""},
                {"Rental Date", oldRental.getRentalDate(), ""}
        };

        //Use the column names and data to create a new table with editable cells.
        rentalUpdateTable = setupTableWithEditableCells(columnNames, data);

        //Create a new scroll pane and add the table to it.
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(rentalUpdateTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up the panels for the GUI.
     *
     * In this method, the scroll pane panel created in setupScrollPane() is added
     * to the north area (top) of the GUIPanel.
     */
    protected void setupPanels() {
        //Add the scroll pane panel to the north area (top) of the GUIPanel.
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }
}