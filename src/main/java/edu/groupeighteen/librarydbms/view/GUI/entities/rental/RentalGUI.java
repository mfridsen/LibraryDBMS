package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
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
public class RentalGUI extends GUI {
    private final Rental rental;

    private JLabel[] labels;
    private JLabel rentalIDLabel;
    private JLabel userIDLabel;
    private JLabel usernameLabel;
    private JLabel itemIDLabel;
    private JLabel itemTitleLabel;
    private JLabel rentalDateLabel;

    private JTable rentalTable;
    private JScrollPane rentalScrollPane;

    private JButton[] buttons;
    private JButton previousGUIButton;
    private JButton rentalUpdateButton;
    private JButton rentalDeleteButton;

    private JPanel buttonPanel;
    private JPanel labelPanel;

    public RentalGUI(GUI previousGUI, Rental rental) {
        super(previousGUI, "RentalGUI");
        this.rental = rental;

        //setupLabels();
        setupTable();
        setupButtons();
        setupPanels();

        this.displayGUI();
    }

    /**
     * Reads the data from the Rental object and sets labels accordingly.
     */
    private void setupLabels() {
        rentalIDLabel  = new JLabel("Rental ID:    " + rental.getRentalID());
        userIDLabel    = new JLabel("User ID:      " + rental.getUserID());
        usernameLabel  = new JLabel("Username:     " + rental.getUsername());
        itemIDLabel    = new JLabel("Item ID:      " + rental.getItemID());
        itemTitleLabel = new JLabel("Item Title:   " + rental.getTitle());
        rentalDateLabel = new JLabel("Rental Date:  " + rental.getRentalDate());

        labels = new JLabel[]{rentalIDLabel, userIDLabel, usernameLabel, itemIDLabel, itemTitleLabel, rentalDateLabel};
    }

    //TODO-prio move to GUI class
    private void setupTable() {
        // Define column names
        String[] columnNames = {"Property", "Value"};

        // Gather data
        Object[][] data = {
                {"Rental ID", rental.getRentalID()},
                {"User ID", rental.getUserID()},
                {"Username", rental.getUsername()},
                {"Item ID", rental.getItemID()},
                {"Item Title", rental.getTitle()},
                {"Rental Date", rental.getRentalDate()}
        };

        // Create table with data and column names
        rentalTable = new JTable(data, columnNames);

        // Make the table uneditable
        rentalTable.setDefaultEditor(Object.class, null);

        // Add table to a scroll pane in case it gets too big
        rentalScrollPane = new JScrollPane(rentalTable);
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    private void setupButtons() {
        //Leads to previousGUI
        previousGUIButton = new JButton("PreviousGUI");
        //Add actionListener
        previousGUIButton.addActionListener(e -> {
            if (previousGUI == null) {
                System.err.println("No previous GUI to return to!");
            } else {
                dispose();
                previousGUI.displayGUI();
            }
        });

        //Leads to RentalUpdateGUI
        rentalUpdateButton = new JButton("RentalUpdateGUI");
        //Add actionListener
        rentalUpdateButton.addActionListener(e -> {
            dispose();
            new RentalUpdateGUI(this, rental);
        });

        //Leads to RentalDeleteGUI
        rentalDeleteButton = new JButton("RentalDeleteGUI");
        //Add actionListener
        rentalDeleteButton.addActionListener(e -> {
            dispose();
            new RentalDeleteGUI(this, rental);
        });

        buttons = new JButton[]{previousGUIButton, rentalUpdateButton, rentalDeleteButton};
    }

    /**
     * Sets up the panel.
     */
    private void setupPanels() {
        //To achieve the preferred layout, BorderLayout is needed
        GUIPanel = new JPanel(new BorderLayout());
        buttonPanel = addButtonsToPanel(buttons);
        //labelPanel = addLabelsToPanel(labels);
        labelPanel = new JPanel();
        labelPanel.add(rentalScrollPane, BorderLayout.CENTER);
        //GUIPanel.add(labelPanel, BorderLayout.WEST); //Add labelPanel to the left
        GUIPanel.add(rentalTable, BorderLayout.NORTH);
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the bottom
    }

    /**
     * Returns the Rental object this class is displaying.
     * @return the Rental object.
     */
    public Rental getRental() {
        return rental;
    }
}