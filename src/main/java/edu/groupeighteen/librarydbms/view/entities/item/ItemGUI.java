package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalReturnException;
import edu.groupeighteen.librarydbms.view.entities.rental.RentalGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;
import edu.groupeighteen.librarydbms.view.optionpanes.LoginOptionPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 * <p>
 * ItemGUI class extends GUI class and provides a graphical interface for displaying and managing items.
 * It allows users to view item properties, including specific fields for Literature and Film items.
 * Users can also rent an item if it's available and they are logged in.
 */
public class ItemGUI extends GUI
{
    //TODO-prio better exception handling

    /**
     * JPanel for containing the scrollPane.
     */
    private JPanel scrollPanePanel;

    /**
     * Constructor for the ItemGUI class.
     *
     * @param previousGUI the GUI from which this GUI is accessed.
     * @param item        the Item entity to be managed.
     */
    public ItemGUI(GUI previousGUI, Item item)
    {
        super(previousGUI, "ItemGUI", item);
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Setup the buttons for the GUI.
     *
     * @return array of JButtons used in this GUI.
     */
    @Override
    protected JButton[] setupButtons()
    {
        //Recast entity into item
        Item item = (Item) entity;

        JButton rentButton = new JButton("Rent Item");

        rentButton.addActionListener(e ->
        {
            if (item.isAvailable()) //If item can be rented...
            {
                if (LibraryManager.getCurrentUser() != null) //... and we are logged in
                {
                    try
                    {
                        createAndOpenNewRental(item);
                    }
                    catch (EntityNotFoundException | InvalidIDException | InvalidTypeException fatalException) //This SHOULD NOT happen
                    {
                        ExceptionHandler.HandleFatalException("Rental creation failed fatally due to: " +
                                fatalException.getCause().getClass().getName() + ", Message: " +
                                fatalException.getMessage(), fatalException);
                    }
                    catch (RentalNotAllowedException rentalNotAllowedException) //This is perfectly fine
                    {
                        rentalNotAllowedException.printStackTrace(); //TODO-prio handle non-fatal
                    }
                }
                else //... and we are not logged in
                {
                    new LoginOptionPane(this);
                }
            }
            else //.. item can't be rented
            {
                System.err.println("Item can't be rented.");
            }
        });

        JButton returnButton = new JButton("Return Item");
        returnButton.addActionListener(e ->
        {
            if (!item.isAvailable())
            {
                List<Rental> rentals = RentalHandler.getRentalsByItemID(item.getItemID());
                if (rentals.isEmpty())
                {
                    System.err.println("Couldn't find rental for item which is rented out.");
                    LibraryManager.exit(1);
                }
                else if (rentals.size() > 1)
                {
                    System.err.println("There should only be one rental for this item");
                    LibraryManager.exit(1);
                }
                else
                {
                    dispose();
                    //Lets just send the user back to the ItemHandlerGUI
                    new ItemHandlerGUI(null);
                }
            }
            else
            {
                System.err.println("Item is not rented out.");
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e ->
        {
            dispose();
            new ItemUpdateGUI(this, item);
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e ->
        {
            dispose();
            if (LibraryManager.getCurrentUser() == null)
                new LoginOptionPane(this);
            new ItemDeleteGUI(this, item);
        });

        return new JButton[]{rentButton, returnButton, updateButton, deleteButton};
    }

    /**
     * Creates a new Rental for the specified Item and opens the RentalGUI for the new rental.
     *
     * @param item the Item to be rented.
     * @throws EntityNotFoundException   if the User or Item entities cannot be found.
     * @throws RentalNotAllowedException if the rental is not allowed.
     * @throws InvalidIDException        if the ID of the User or Item is invalid.
     */
    private void createAndOpenNewRental(Item item)
    throws EntityNotFoundException, RentalNotAllowedException, InvalidIDException, InvalidTypeException
    {
        Rental newRental = RentalHandler.createNewRental(LibraryManager.getCurrentUser().getUserID(),
                item.getItemID());
        RentalHandler.printRentalList(RentalHandler.getAllRentals()); //TODO-prio debug print
        newRental.printReceipt();
        dispose();
        new RentalGUI(this, newRental);
    }

    /**
     * Setup the scrollPane for the GUI to display Item data.
     */
    protected void setupScrollPane()
    {
        //Recast entity into item
        Item item = (Item) entity;

        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather common data
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"Item ID", item.getItemID()});
        data.add(new Object[]{"Item Title", item.getTitle()});
        data.add(new Object[]{"Item Type", getItemTypeString(item.getType())});
        data.add(new Object[]{"Classification", item.getClassificationName()});
        data.add(new Object[]{"Author/Director First Name", item.getAuthorFirstname()});
        data.add(new Object[]{"Author/Director Last Name", item.getAuthorLastname()});

        //Check if item is an instance of Literature
        if (item instanceof Literature)
        {
            Literature literatureItem = (Literature) item;
            data.add(new Object[]{"ISBN", literatureItem.getISBN()});

        }
        //Check if item is an instance of Film
        else if (item instanceof Film)
        {
            Film filmItem = (Film) item;
            data.add(new Object[]{"Age Rating", filmItem.getAgeRating()});
            data.add(new Object[]{"Country of Production", filmItem.getCountryOfProduction()});
            data.add(new Object[]{"List of Actors", String.join(", ", filmItem.getListOfActors())});
        }

        //Add final row(s)
        data.add(new Object[]{"Barcode", item.getBarcode()});
        data.add(new Object[]{"Deleted", item.isDeleted()});
        data.add(new Object[]{"Available", item.isAvailable()});

        //Convert list to an array for final data structure
        Object[][] dataArray = data.toArray(new Object[0][]);

        JTable userUpdateTable = setupTable(columnNames, dataArray);
        //Create the scroll pane
        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);
        //Create panel and add scroll pane to it
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);
    }

    /**
     * Get the string representation of an Item type.
     *
     * @param type the type of the Item.
     * @return the string representation of the type.
     */
    private String getItemTypeString(Item.ItemType type)
    {
        String typeString = null;
        switch (type)
        {
            case REFERENCE_LITERATURE -> typeString = "Reference Literature";
            case MAGAZINE -> typeString = "Magazine";
            case FILM -> typeString = "Film";
            case COURSE_LITERATURE -> typeString = "Course Literature";
            case OTHER_BOOKS -> typeString = "Book";
        }
        return typeString;
    }

    /**
     * Setup the panels for the GUI.
     */
    @Override
    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }

    /**
     * Get the Item entity managed by this GUI.
     *
     * @return the Item entity.
     */
    public Item getItem()
    {
        return (Item) entity;
    }
}