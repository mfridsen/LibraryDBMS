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

    private final Rental rentalToUpdate;

    private JButton clearFieldsButton;
    private JButton confirmUpdateButton;

    public RentalUpdateGUI(GUI previousGUI, Rental rentalToUpdate) {
        super(previousGUI, "RentalUpdateGUI");
        this.rentalToUpdate = rentalToUpdate;
        addButtonsToPanel(new JButton[]{clearFieldsButton, confirmUpdateButton});
        setupScrollPane();
        setupPanels();
        this.displayGUI();
    }

    @Override
    protected void setupButtons() {

    }

    @Override
    protected void setupPanels() {

    }

    private void setupScrollPane() {
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

        // Create table model with data and column names
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only the third column editable
                return column == 2;
            }
        };

        // Create table with the model
        JTable table = new JTable(tableModel);

        // Make the table use text fields for the third column
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));

        // Add table to a scroll pane in case it gets too big
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scroll pane to the panel
        GUIPanel.add(scrollPane, BorderLayout.CENTER);
    }
}