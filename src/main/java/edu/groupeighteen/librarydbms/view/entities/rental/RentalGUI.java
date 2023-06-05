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
 *
 * This class displays all data for a given Rental Object. It also contains buttons to perform relevant CRUD
 * operations on the object.
 *
 * Brought to you by enough nicotine to kill a large horse.
 *
 * @see GUI
 * @see Rental
 */
public class RentalGUI extends GUI {
    //TODO- fält som ska visas i denna ordning:
    //  RentalID, UserID, ItemID, RentalDate
    //  userName, itemTitle, rentalDueDate, rentalReturnDate
    //  active, overdue, lateFee
    //TODO-comment
    //TODO-test
    //The rental object to display
    private final Rental rental;
    //The panel containing the scroll pane which displays the Rental data
    private JPanel scrollPanePanel;

    /**
     *
     * @param previousGUI
     * @param rental
     */
    public RentalGUI(GUI previousGUI, Rental rental) {
        super(previousGUI, "RentalGUI", rental);
        this.rental = rental;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    @Override
    protected JButton[] setupButtons() {
        //Leads to RentalUpdateGUI
        JButton rentalUpdateButton = new JButton("RentalUpdateGUI");
        //Add actionListener
        rentalUpdateButton.addActionListener(e -> {
            dispose();
            new RentalUpdateGUI(this, rental);
        });

        //Leads to RentalDeleteGUI
        JButton rentalDeleteButton = new JButton("RentalDeleteGUI");
        //Add actionListener
        rentalDeleteButton.addActionListener(e -> {
            dispose();
            new RentalDeleteGUI(this, rental);
        });

        return new JButton[]{rentalUpdateButton, rentalDeleteButton};
    }

    /**
     * Sets up the scroll pane.
     */
    protected void setupScrollPane() {
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Rental ID", rental.getRentalID()},
                {"User ID", rental.getUserID()},
                {"Username", rental.getUsername()},
                {"Item ID", rental.getItemID()},
                {"Item Title", rental.getItemTitle()},
                {"Rental Date", rental.getRentalDate()}
        };

        JTable rentalUpdateTable = setupTable(columnNames, data);
        //Create the scroll pane
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(rentalUpdateTable);
        //Create panel and add scroll pane to it
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up the main panel.
     */
    @Override
    protected void setupPanels() {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH); //Add scrollPanePanel to the top
    }

    /**
     * Returns the Rental object this class is displaying.
     * @return the Rental object.
     */
    public Rental getRental() {
        return rental;
    }
}