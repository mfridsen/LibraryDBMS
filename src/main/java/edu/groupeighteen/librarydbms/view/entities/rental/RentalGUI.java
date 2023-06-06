package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalReturnException;
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
 * This class represents the Rental GUI in the application's Graphical User Interface (GUI).
 * It displays details about a specific rental and provides options to return, update, or delete the rental.
 * @see GUI
 * @see Rental
 */
public class RentalGUI extends GUI
{
    //The rental object to display
    private final Rental rental;
    //The panel containing the scroll pane which displays the Rental data
    private JPanel scrollPanePanel;

    /**
     * Constructor for the RentalGUI. Sets up panels and buttons, and displays the GUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     * @param rental The Rental object to display.
     */
    public RentalGUI(GUI previousGUI, Rental rental)
    {
        super(previousGUI, "RentalGUI", rental);
        this.rental = rental;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons to be displayed on the RentalGUI. These include:
     * - Return Rental: Allows the user to return the rental.
     * - Update Rental: Leads to the RentalUpdateGUI where the user can update the rental details.
     * - Delete Rental: Leads to the RentalDeleteGUI where the user can delete the rental.
     *
     * @return An array of JButtons to be added to the GUI.
     */
    @Override
    protected JButton[] setupButtons()
    {
        JButton rentalReturnButton = new JButton("Return Rental");
        rentalReturnButton.addActionListener(e ->
        {
            try
            {
                RentalHandler.returnRental(rental);
                dispose();
                RentalHandler.printRentalList(RentalHandler.getAllRentals()); //TODO-prio debug print
                new RentalGUI(null, rental);
            }
            catch (RentalReturnException ex)
            {
                System.err.println(ex.getMessage()); //TODO-prio look over handling
            }
        });

        //Leads to RentalUpdateGUI
        JButton rentalUpdateButton = new JButton("Update Rental");
        //Add actionListener
        rentalUpdateButton.addActionListener(e ->
        {
            dispose();
            new RentalUpdateGUI(this, rental);
        });

        //Leads to RentalDeleteGUI
        JButton rentalDeleteButton = new JButton("Delete Rental");
        //Add actionListener
        rentalDeleteButton.addActionListener(e ->
        {
            dispose();
            new RentalDeleteGUI(this, rental);
        });

        return new JButton[]{rentalReturnButton, rentalUpdateButton, rentalDeleteButton};
    }

    /**
     * Sets up the scroll pane that displays the rental details in a table format.
     * The scroll pane is added to a panel, which is later added to the GUI.
     */
    protected void setupScrollPane()
    {
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Rental ID", rental.getRentalID()},
                {"User ID", rental.getUserID()},
                {"Username", rental.getUsername()},
                {"Item ID", rental.getItemID()},
                {"Item Title", rental.getItemTitle()},
                {"Rental Date", rental.getRentalDate()},
                {"Rental Due Date", rental.getRentalDueDate()},
                {"Rental Return Date", rental.getRentalReturnDate()}
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
     * Adds the scrollPanePanel, which contains the scroll pane with the rental details, to the GUIPanel.
     */
    @Override
    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH); //Add scrollPanePanel to the top
    }

    /**
     * Returns the Rental object being displayed.
     *
     * @return The Rental object.
     */
    public Rental getRental()
    {
        return rental;
    }
}