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
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class RentalUpdateGUI extends GUI {
    //The rental object to update
    private Rental rentalToUpdate;
    //We need the table to be a member variable in order to access its data via the buttons
    private JTable rentalUpdateTable;
    //The panel containing the scroll pane which displays the Rental data
    private JPanel scrollPanePanel;

    /**
     *
     * @param previousGUI
     * @param rentalToUpdate
     */
    public RentalUpdateGUI(GUI previousGUI, Rental rentalToUpdate) {
        super(previousGUI, "RentalUpdateGUI");
        this.rentalToUpdate = rentalToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        //Clears the data cells
        JButton resetCellsButton = new JButton("Reset");
        //Reset the editable fields //TODO-bug doesn't clear selected field
        resetCellsButton.addActionListener(e -> {
            for (int row = 0; row < rentalUpdateTable.getRowCount(); row++) {
                for (int col = 0; col < rentalUpdateTable.getColumnCount(); col++) {
                    if (col == 2) { // Assuming the 3rd column is the editable column
                        rentalUpdateTable.setValueAt("", row, col);
                    }
                }
            }
        });

        //Updates rentalToUpdate and opens a new RentalGUI displaying the updated Rental object
        JButton confirmUpdateButton = new JButton("RentalGUI"); //TODO-future Change to "Confirm Update"
        confirmUpdateButton.addActionListener(e -> {
            // Get the new values from the table
            String userID = (String) rentalUpdateTable.getValueAt(1, 2);
            String username = (String) rentalUpdateTable.getValueAt(2, 2);
            String itemID = (String) rentalUpdateTable.getValueAt(3, 2);
            String itemTitle = (String) rentalUpdateTable.getValueAt(4, 2);
            String rentalDate = (String) rentalUpdateTable.getValueAt(5, 2);

            // Update the rentalToUpdate object
            // Only update if new value is not null or empty
            try {
                if (userID != null && !userID.isEmpty()) {
                    rentalToUpdate.setUserID(Integer.parseInt(userID));
                }
                // No parsing required for username, it is a string
                if (username != null && !username.isEmpty()) {
                    rentalToUpdate.setUsername(username);
                }
                if (itemID != null && !itemID.isEmpty()) {
                    rentalToUpdate.setItemID(Integer.parseInt(itemID));
                }
                // No parsing required for itemTitle, it is a string
                if (itemTitle != null && !itemTitle.isEmpty()) {
                    rentalToUpdate.setTitle(itemTitle);
                }
                if (rentalDate != null && !rentalDate.isEmpty()) {
                    rentalToUpdate.setRentalDate(LocalDateTime.parse(rentalDate));
                }
            } catch (NumberFormatException nfe) {
                System.err.println("One of the fields that requires a number received an invalid input.");
            } catch (DateTimeParseException dtpe) {
                System.err.println("The date field received an invalid input. Please ensure it is in the correct format.");
            }

            // Now you can call the update method
            try {
                RentalHandler.updateRental(rentalToUpdate); //TODO-prio doesn't update properly
                dispose();
                new RentalGUI(this, rentalToUpdate);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            }
        });

        return new JButton[]{resetCellsButton, confirmUpdateButton};
    }

    /**
     * Sets up the scroll pane.
     */
    protected void setupScrollPane() {
        // Define column names
        String[] columnNames = {"Property", "Old Value", "New Value"};

        // Gather data
        Object[][] data = {
                {"Rental ID", rentalToUpdate.getRentalID(), ""},
                {"User ID", rentalToUpdate.getUserID(), ""},
                {"Username", rentalToUpdate.getUsername(), ""},
                {"Item ID", rentalToUpdate.getItemID(), ""},
                {"Item Title", rentalToUpdate.getTitle(), ""},
                {"Rental Date", rentalToUpdate.getRentalDate(), ""}
        };

        //Create the table
        this.rentalUpdateTable = setupTableWithEditableCells(columnNames, data);
        //Create scroll pane and add table to it
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(rentalUpdateTable);
        //Create panel and add scroll pane to it
        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH); //Add scrollPanePanel to the top
    }
}