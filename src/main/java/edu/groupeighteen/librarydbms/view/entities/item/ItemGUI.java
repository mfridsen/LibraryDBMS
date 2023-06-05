package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.view.entities.rental.RentalGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 * <p>
 * this class displays all information about a single item object
 */
public class ItemGUI extends GUI
{
    // TODO- if LibraryManager.getCurrentUser != null and item.isAvailable
    //  Rental newRental = RentalHandler.createNewRental(LibraryManager.getCurrentUser.getUserID, item.getItemID)
    //  new RentalGUI för newRental

    private JPanel scrollPanePanel;



    public ItemGUI(GUI previousGUI, Item item)
    {
        super(previousGUI, "ItemGUI", item);
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {
        Item item = (Item) entity;

        if (item.isAvailable()) //If item can be rented...
        {
            if (LibraryManager.getCurrentUser() != null) //... and we are logged in
            {
                try
                {
                    Rental newRental = RentalHandler.createNewRental(LibraryManager.getCurrentUser().getUserID(),
                            item.getItemID());
                    dispose();
                    new RentalGUI(this, newRental);
                }
                catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
                {
                    e.printStackTrace(); //TODO-prio handle
                }
            }
            else //... and we are not logged in
            {
                //Open login GUI
            }
        }

        return new JButton[0];

    }

    protected void setupScrollPane()
    {
        Item item = (Item) entity;

        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather common data
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"Item ID", item.getItemID()});
        data.add(new Object[]{"Item Title", item.getTitle()});
        data.add(new Object[]{"Item Type", getItemTypeString(item.getType())});
        data.add(new Object[]{"Classification", item.getClassificationName()});
        data.add(new Object[]{"Author/Director", item.getAuthorFirstname() + " " + item.getAuthorLastname()});

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

    @Override
    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }

    public Item getItem()
    {

        return (Item) entity;
    }
}