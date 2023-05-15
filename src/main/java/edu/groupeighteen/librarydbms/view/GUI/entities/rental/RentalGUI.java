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
 *
 * This class displays all data for a given Rental Object. It also contains buttons to perform relevant CRUD
 * operations on the object.
 *
 * Brought to you by enough nicotine to kill a large horse.
 */
public class RentalGUI extends GUI {
    private final Rental rental;

    private JScrollPane rentalScrollPane;

    private JButton rentalUpdateButton;
    private JButton rentalDeleteButton;

    public RentalGUI(GUI previousGUI, Rental rental) {
        super(previousGUI, "RentalGUI");
        this.rental = rental;
        //First we set up the buttons
        setupButtons();
        //Then we add them to the button panel
        addButtonsToPanel(new JButton[]{rentalUpdateButton, rentalDeleteButton});
        //Setup scroll pane
        setupScrollPane();
        //Add all panels
        setupPanels();
        this.displayGUI();
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    protected void setupButtons() {
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
    }

    /**
     * Sets up the scroll pane.
     */
    private void setupScrollPane() {
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Rental ID", rental.getRentalID()},
                {"User ID", rental.getUserID()},
                {"Username", rental.getUsername()},
                {"Item ID", rental.getItemID()},
                {"Item Title", rental.getTitle()},
                {"Rental Date", rental.getRentalDate()}
        };

        rentalScrollPane = setupTableScrollPane(columnNames, data);
    }

    /**
     * Sets up the main panel.
     */
    private void setupPanels() {
        GUIPanel = new JPanel(new BorderLayout()); //To achieve the preferred layout, BorderLayout is needed
        JPanel scrollPanePanel = new JPanel();
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH); //Add scrollPanePanel to the top
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