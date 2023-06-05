package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/14/2023
 * <p>
 * This class represents a graphical user interface (GUI) for updating a Rental object.
 * The class extends the generic GUI class, and provides a specific implementation for
 * managing the update process of a rental.
 * <p>
 * The GUI consists of a table for displaying the current details of a rental, and fields
 * for entering new values for these details. The GUI also includes buttons for resetting
 * the fields and for confirming the update.
 * <p>
 * The class holds a reference to the rental that is to be updated, as well as a reference
 * to the new, updated rental. It also keeps a reference to the table displaying the rental details,
 * and to the panel containing this table.
 * <p>
 * The GUI is set up by first setting up the scroll pane and panels, and then displaying the GUI.
 * <p>
 * The class offers the following main functionalities:
 * - Setting up buttons for resetting the fields and confirming the update.
 * - Setting up a scroll pane for displaying the rental details.
 * - Setting up panels for the GUI.
 * @see GUI
 * @see Rental
 */
public class RentalUpdateGUI extends GUI
{
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
     * @param previousGUI    The GUI that was previously displayed. This is used for navigation purposes.
     * @param rentalToUpdate The Rental object that the user wants to update. The initial fields of the
     *                       GUI will be populated with the details of this rental.
     */
    public RentalUpdateGUI(GUI previousGUI, Rental rentalToUpdate)
    {
        super(previousGUI, "RentalUpdateGUI for rentalID = " + rentalToUpdate.getRentalID(), rentalToUpdate);
        this.oldRental = rentalToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up and returns an array containing 'Reset' and 'Confirm Update' buttons.
     * <p>
     * The 'Reset' button is created by calling the 'setupResetButton' method. When clicked,
     * this button resets the values in the editable fields of the 'rentalUpdateTable'.
     * <p>
     * The 'Confirm Update' button is created by calling the 'setupConfirmButton' method.
     * When clicked, this button attempts to collect new data from the 'rentalUpdateTable',
     * create a new 'Rental' object based on this data, and update the old rental with the new data.
     *
     * @return JButton[] An array containing the 'Reset' and 'Confirm Update' buttons.
     */
    @Override
    protected JButton[] setupButtons()
    {
        JButton resetCellsButton = setupResetButton();
        JButton confirmUpdateButton = setupConfirmButton();
        return new JButton[]{resetCellsButton, confirmUpdateButton};
    }

    /**
     * Sets up and returns a 'Reset' button. When clicked, this button will clear the data in all cells
     * of the third column in the 'rentalUpdateTable'.
     * <p>
     * Note: There is a known issue where the selected field is not cleared upon button click.
     *
     * @return JButton The reset button configured with the appropriate action listener.
     */
    private JButton setupResetButton()
    {
        //Clears the data cells
        JButton resetCellsButton = new JButton("Reset");
        //Reset the editable cells //TODO-bug doesn't clear selected field
        resetCellsButton.addActionListener(e ->
        {
            resetCells();
        });
        return resetCellsButton;
    }

    /**
     * Resets all the cells in the third column of the 'rentalUpdateTable' to an empty string.
     * <p>
     * Note: This method assumes the third column (index 2) of the table is the only column that needs to be reset.
     */
    private void resetCells()
    {
        for (int row = 0; row < rentalUpdateTable.getRowCount(); row++)
        {
            for (int col = 0; col < rentalUpdateTable.getColumnCount(); col++)
            {
                if (col == 2)
                { //Assuming the 3rd column is the editable column
                    rentalUpdateTable.setValueAt("", row, col);
                }
            }
        }
    }

    /**
     * Sets up and returns a 'Confirm Update' button. When clicked, this button collects new data from the
     * 'rentalUpdateTable', creates a new 'Rental' object based on this data, and attempts to update the old
     * rental with this new data.
     * <p>
     * The new rental data is collected from the third column of the 'rentalUpdateTable'. Only non-empty fields
     * are used to update the new rental. User ID and Item ID fields are parsed as integers, while Rental Date
     * is parsed as a LocalDateTime. If any parsing fails due to incorrect input format, an error message is printed
     * to the system error stream.
     * <p>
     * After collecting and parsing the new data, the method tries to update the rental in the database using
     * 'RentalHandler.updateRental'. If this is successful, the current GUI is disposed and a new RentalGUI
     * displaying the updated rental is opened. If the update fails due to a SQL exception, the application
     * terminates with a status code of 1. If it fails due to illegal arguments, the error message is printed
     * to the system error stream, the data cells are reset, and the method exits.
     * <p>
     * Note: Better exception handling and testing for illegal arguments is needed.
     *
     * @return JButton The 'Confirm Update' button configured with the appropriate action listener.
     */
    private JButton setupConfirmButton()
    {
        //Updates rentalToUpdate and opens a new RentalGUI displaying the updated Rental object
        JButton confirmUpdateButton = new JButton("Confirm Update");
        confirmUpdateButton.addActionListener(e ->
        {
            //Duplicate oldRental
            newRental = new Rental(oldRental);

            //Get the new values from the table
            String userID = (String) rentalUpdateTable.getValueAt(0, 2);
            String username = (String) rentalUpdateTable.getValueAt(1, 2);
            String itemID = (String) rentalUpdateTable.getValueAt(2, 2);
            String itemTitle = (String) rentalUpdateTable.getValueAt(3, 2);
            String rentalDate = (String) rentalUpdateTable.getValueAt(4, 2);

            //Update the rentalToUpdate object. Only update if new value is not null or empty
            /*
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
                    newRental.setItemTitle(itemTitle);
                }
                if (rentalDate != null && !rentalDate.isEmpty()) {
                    newRental.setRentalDate(LocalDateTime.parse(rentalDate));
                }
            } catch (NumberFormatException nfe) {
                System.err.println("One of the fields that requires a number received an invalid input. User ID:" + userID + ", Item ID: " + itemID);
            } catch (DateTimeParseException dtpe) {
                System.err.println("The date field received an invalid input. Please ensure it is in the correct format.");
            }*/

            //Now call the update method
            /*try {
                RentalHandler.updateRental(oldRental, newRental);
                dispose();
                new RentalGUI(this, newRental);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            } catch (IllegalArgumentException ile) {
                System.err.println(ile.getMessage()); //TODO-test //TODO-exception handle better
                resetCells();
            }*/
        });
        return confirmUpdateButton;
    }

    /**
     * Sets up the scroll pane for the GUI, including creating a table with the
     * current details of the rental, and setting it into a panel for display.
     * <p>
     * The table is set up with three columns: "Property", "Old Value", and "New Value".
     * The "Property" column contains the names of the properties of the rental.
     * The "Old Value" column contains the current values of these properties.
     * The "New Value" column is initially empty and is where new values can be entered for updating the rental.
     * <p>
     * After the table is set up, it is added to a scroll pane, which in turn is added to a panel
     * that uses a BorderLayout. The scroll pane is placed in the center of this panel.
     */
    protected void setupScrollPane()
    {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Old Value", "New Value"};

        //Gather the data for the table. Each row corresponds to a property of the rental.
        //The first column is the name of the property, the second column is its old value,
        //and the third column (which is initially empty) will hold the new value.
        Object[][] data = {
                {"User ID", oldRental.getUserID(), ""},
                {"Username", oldRental.getUsername(), ""},
                {"Item ID", oldRental.getItemID(), ""},
                {"Item Title", oldRental.getItemTitle(), ""},
                {"Rental Date", oldRental.getRentalDate(), ""}
        };

        //Use the column names and data to create a new table with editable cells.
        rentalUpdateTable = setupTableWithEditableCells(columnNames, data, 2);

        //Create a new scroll pane and add the table to it.
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(rentalUpdateTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up the panels for the GUI.
     * <p>
     * In this method, the scroll pane panel created in setupScrollPane() is added
     * to the north area (top) of the GUIPanel.
     */
    @Override
    protected void setupPanels()
    {
        //Add the scroll pane panel to the north area (top) of the GUIPanel.
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }
}