package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 * <p>
 * GUI for searching Items in the library database.
 * Extends the GUI class and provides interface to search for items.
 */
public class ItemSearchGUI extends GUI
{
    //TODO-prio look over exceptions

    /**
     * Table used for search queries.
     */
    private JTable itemSearchTable;

    /**
     * Panel containing the search fields.
     */
    private JPanel searchFieldsPanel;

    /**
     * Constructs a new ItemSearchGUI object.
     *
     * @param previousGUI The GUI that opened this GUI.
     */
    public ItemSearchGUI(GUI previousGUI)
    {
        super(previousGUI, "ItemSearchGUI", null);
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up buttons for the GUI.
     *
     * @return An array of JButtons for the GUI.
     */
    protected JButton[] setupButtons()
    {
        JButton resetButton = setupResetButton();
        JButton searchButton = setupSearchButton();
        return new JButton[]{resetButton, searchButton};
    }

    /**
     * Sets up the Reset button for the GUI.
     *
     * @return The reset button.
     */
    private JButton setupResetButton()
    {
        JButton resetCellsButton = new JButton("Reset");
        resetCellsButton.addActionListener(e ->
        {
            resetCells();
        });
        return resetCellsButton;
    }

    /**
     * Clears all input fields in the search table.
     */
    private void resetCells()
    {
        for (int row = 0; row < itemSearchTable.getRowCount(); row++)
        {
            for (int col = 0; col < itemSearchTable.getColumnCount(); col++)
            {
                if (col == 1)
                {
                    itemSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    /**
     * Sets up the Search button for the GUI.
     *
     * @return The search button.
     */
    private JButton setupSearchButton()
    {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e ->
        {
            List<Item> searchResultList = performSearch();
            if (!searchResultList.isEmpty())
            {
                dispose();
                new ItemSearchResultGUI(this, searchResultList);
            }
            else
            {
                System.err.println("No results found for search.");
            }
        });
        return searchButton;
    }

    /**
     * Performs a search for items in the database using parameters from the itemSearchTable.
     *
     * @return a List of Item objects that match the search parameters. If no matches are found, returns an empty list.
     * Iterates over each row of the itemSearchTable, getting the value at column 1 (search parameters) and running the
     * appropriate search function based on the row number (search type). Handles special case of author name search
     * separately after the switch statement.
     * The author search is performed using both first name and last name parameters. Both parameters are allowed to be null.
     * Exceptions related to invalid data are caught and logged, and the corresponding table cells are reset.
     */
    private List<Item> performSearch()
    {
        //TODO-prio create smaller helper methods
        List<Item> searchResultList = new ArrayList<>();

        for (int row = 0; row < itemSearchTable.getRowCount(); row++)
        {
            Object cellData = itemSearchTable.getValueAt(row, 1);

            //Setup values to be used in searches
            int itemID = 0;
            String authorFirstName = null;
            String authorLastName = null;

            if (cellData == null || cellData.toString().isEmpty())
            {
                continue;
            }

            try
            {
                switch (row)
                {
                    //Item ID
                    case 0 ->
                    {
                        itemID = Integer.parseInt(cellData.toString());
                        if (itemID > 0)
                        {
                            Item item = performItemIDSearch(itemID);
                            if (item != null)
                                searchResultList.add(item);
                            else
                                System.err.println("No item found for itemID: " + itemID);
                        }
                        else
                            System.err.println("Item IDs can't be less than 1. Item ID: " + itemID);
                    }
                    //Item title
                    case 1 ->
                    {
                        String title = cellData.toString();
                        searchResultList.addAll(performItemTitleSearch(title, row));
                    }
                    //Item classification
                    case 2 ->
                    {
                        String classificationName = cellData.toString();
                        searchResultList.addAll(performItemClassificationSearch(classificationName, row));
                    }
                    //Author first name
                    case 3 -> authorFirstName = cellData.toString();
                    //Author last name
                    case 4 -> authorLastName = cellData.toString();
                    //ISBN
                    case 5 ->
                    {
                        String ISBN = cellData.toString();
                        List<Item> items = ItemHandler.getItemsByISBN(ISBN);
                        if (!items.isEmpty())
                        {
                            searchResultList.addAll(items);
                        }
                        else
                        {
                            System.err.println("No item found for ISBN: " + ISBN);
                        }
                    }
                }
            }
            catch (NumberFormatException | InvalidISBNException nfe)
            {
                System.err.println("Wrong data type for field: " + itemSearchTable.getValueAt(row, 0));
                resetCells(); //TODO-prio make helper method
            }

            // Perform search by author names after reading both names from the table
            searchResultList.addAll(performAuthorSearch(authorFirstName, authorLastName, row));
        }

        return searchResultList;
    }

    /**
     * Attempts to retrieve an item based on a given item ID.
     *
     * @param itemID The ID of the item to search for.
     * @return The Item with the given ID, or null if not found.
     */
    private Item performItemIDSearch(int itemID)
    {
        Item item = null;

        try
        {
            item = ItemHandler.getItemByID(itemID);
        }
        catch (InvalidIDException | RetrievalException e)
        {
            ExceptionHandler.HandleFatalException(e);
        }

        return item;
    }

    /**
     * Attempts to retrieve items based on a given title.
     *
     * @param title The title of the items to search for.
     * @param row   The row in the table where the title is located.
     * @return A list of Items with the given title, or an empty list if none were found.
     */
    private List<Item> performItemTitleSearch(String title, int row)
    {
        List<Item> items = new ArrayList<>();

        try
        {
            items = ItemHandler.getItemsByTitle(title);
            if (items.isEmpty())
                System.err.println("No item found for title: " + title);
        }
        catch (InvalidTitleException e)
        {
            System.err.println("Wrong data type for field: " + itemSearchTable.getValueAt(row, 0));
            resetCells(); //TODO-prio make helper method
        }

        return items;
    }

    /**
     * Attempts to retrieve items based on a given classification.
     *
     * @param classificationName The classification of the items to search for.
     * @param row                The row in the table where the classification is located.
     * @return A list of Items with the given classification, or an empty list if none were found.
     */
    private List<Item> performItemClassificationSearch(String classificationName, int row)
    {
        List<Item> items = new ArrayList<>();

        try
        {
            items = ItemHandler.getItemsByClassification(classificationName);
            if (items.isEmpty())
                System.err.println("No item found for classificationName: " + classificationName);
        }
        catch (InvalidNameException e)
        {
            System.err.println("Wrong data type for field: " + itemSearchTable.getValueAt(row, 0));
            resetCells(); //TODO-prio make helper method
        }

        return items;
    }

    /**
     * Helper method to perform the search for author names in the database.
     *
     * @param authorFirstName The author's first name as a String.
     * @param authorLastName  The author's last name as a String.
     * @param row             The row index in the itemSearchTable where the author's name is located.
     * @return a List of Item objects that match the author's first name and/or last name. If no matches are found,
     * returns an empty list.
     * This method is called by performSearch() after author first and last names have been retrieved from the table.
     * Exceptions related to invalid data are caught and logged, and the corresponding table cells are reset.
     */
    private List<Item> performAuthorSearch(String authorFirstName, String authorLastName, int row)
    {
        List<Item> items = new ArrayList<>();

        if (authorFirstName != null || authorLastName != null)
        {
            try
            {
                items = ItemHandler.getItemsByAuthor(authorFirstName, authorLastName);
                if (items.isEmpty())
                    System.err.println("No item found for author: " + authorFirstName + " " + authorLastName);
            }
            catch (InvalidNameException e)
            {
                System.err.println("Wrong data type for field: " + itemSearchTable.getValueAt(row, 0));
                resetCells(); //TODO-prio make helper method
            }
        }

        return items;
    }

    /**
     * Sets up the scroll pane containing the item search table.
     */
    protected void setupScrollPane()
    {
        String[] columnNames = {"Property", "Search Value"};

        Object[][] data = {
                {"Item ID", ""},
                {"Title", ""},
                {"Classification", ""},
                {"Author/Director First Name", ""},
                {"Author/Director Last Name", ""},
                {"ISBN", ""}
        };

        itemSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(itemSearchTable);

        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up the panels for this GUI.
     */
    @Override
    protected void setupPanels()
    {
        GUIPanel.add(searchFieldsPanel);
    }
}
