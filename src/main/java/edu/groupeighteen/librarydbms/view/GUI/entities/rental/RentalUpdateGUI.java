package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
    private final Rental rentalToUpdate;
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
        this.displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        //Clears the text fields
        JButton clearFieldsButton = new JButton("Clear Fields");
        clearFieldsButton.addActionListener(e -> {
            //TODO-prio implement
        });

        //Updates rentalToUpdate and opens a new RentalGUI displaying the updated Rental object
        JButton confirmUpdateButton = new JButton("RentalGUI"); //TODO-future Change to "Confirm Update"
        confirmUpdateButton.addActionListener(e -> {
            //TODO-prio implement
        });

        return new JButton[]{clearFieldsButton, confirmUpdateButton};
    }

    /**
     * Sets up the scroll pane with columns displaying Needs to be called by setupPanels.
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
        //<Entity>GUIs need a JScrollPane to display all their data in a good format
        JScrollPane rentalScrollPane = setupScrollPaneTableWithButtons(columnNames, data);
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH); //Add scrollPanePanel to the top
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the bottom
    }
}