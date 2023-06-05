package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

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

    private final Item item;
    private JPanel scrollPanePanel;

    public ItemGUI(GUI previousGUI, Item item)
    {
        super(previousGUI, "ItemGUI");
        this.item = item;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {
        //Leads to ItemGUI
        return new JButton[0];

    }

    protected void setupScrollPane()
    {
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
        if (item instanceof Literature) {
            Literature literatureItem = (Literature) item;
            data.add(new Object[]{"ISBN", literatureItem.getISBN()});
            data.add(new Object[]{"Barcode", literatureItem.getBarcode()});
        }
        //Check if item is an instance of Film
        else if (item instanceof Film) {
            Film filmItem = (Film) item;
            data.add(new Object[]{"Age Rating", filmItem.getAgeRating()});
            data.add(new Object[]{"Country of Production", filmItem.getCountryOfProduction()});
            data.add(new Object[]{"List of Actors", String.join(", ", filmItem.getListOfActors())});
            data.add(new Object[]{"Barcode", filmItem.getBarcode()});
        }

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
        return item;
    }
}