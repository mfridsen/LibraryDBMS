package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 *
 * this class handles searching for items
 * leads to ItemSearchResultGUI
 */

public class ItemSearchGUI extends GUI {
    //TODO-prio add search-by-day and search-by-dates
    private JPanel searchFieldsPanel;
    private JTable itemSearchTable;

    /**
     * Constructs a new itemSearchGUI object.
     * The constructor first calls the constructor of the superclass, GUI,
     * passing in the previous GUI and the title of this GUI.
     * It then sets up the scroll pane and panels, and finally displays the GUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     */
    public ItemSearchGUI(GUI previousGUI) {
        super(previousGUI, "ItemSearchGUI");
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Overrides the setupButtons() method of the superclass.
     * Sets up two buttons: a 'Reset' button and a 'Search' button.
     * The 'Reset' button will clear the data in the editable cells.
     * The 'Search' button will perform a search based on the data in the editable cells
     * and open a itemSearchResultGUI with the results of the search.
     *
     * Note: There is a known issue where the selected field is not cleared upon 'Reset' button click.
     *
     * @return An array containing the 'Reset' button and the 'Search' button.
     */
    @Override
    protected JButton[] setupButtons() {
        //Resets the editable cells //TODO-bug doesn't clear selected field
        JButton resetButton = setupResetButton();
        //Performs the search and opens a searchResultGUI
        JButton searchButton = setupSearchButton();
        return new JButton[]{resetButton, searchButton};
    }

    /**
     * Sets up and returns a 'Reset' button. When clicked, this button will clear the data in all cells
     * of the third column in the 'itemSearchTable'.
     *
     * Note: There is a known issue where the selected field is not cleared upon button click.
     *
     * @return JButton The reset button configured with the appropriate action listener.
     */
    private JButton setupResetButton(){
        JButton resetCellsButton = new JButton("Reset");
        resetCellsButton.addActionListener(e -> {
            resetCells();
        });
        return resetCellsButton;
    }

    /**
     * Resets all the cells in the second column of the 'itemSearchTable' to an empty string.
     *
     * Note: This method assumes the second column (index 1) of the table is the only column that needs to be reset.
     */
    private void resetCells() {
        for (int row = 0; row < itemSearchTable.getRowCount(); row++) {
            for (int col = 0; col < itemSearchTable.getColumnCount(); col++) {
                if (col == 1) { //Assuming the 2nd column is the editable column
                    itemSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    /**
     * Sets up and returns a 'Search' button. When clicked, this button will perform a search
     * based on the data in the editable cells. If the search generates any results, the current
     * GUI will be disposed and a new itemSearchResultGUI will be created with the results of the search.
     * If no results are found, an error message will be printed to the system error stream.
     *
     * @return JButton The search button configured with the appropriate action listener.
     */
    private JButton setupSearchButton() {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            //Perform the search
            List<Item> searchResultList = performSearch();
            //If the search doesn't generate a result, we stay
            if (!searchResultList.isEmpty()) {
                dispose();
                new ItemSearchResultGUI(this, searchResultList);
                ItemHandler.printItemList(searchResultList);
            } else {
                System.err.println("No results found for search.");
                resetCells();
            }
        });
        return searchButton;
    }

    //TODO-test
    /**
     * Performs a search based on the data in the editable cells of the itemSearchTable.
     * Each row in the table corresponds to a different search parameter (item ID, user ID, username, item ID,
     * item title, item date).
     * If the cell data for a row is not null or empty, an attempt will be made to parse it into the appropriate type
     * (int or String or LocalDateTime)
     * and perform the corresponding search. The search results are then added to a list of items.
     *
     * If a search doesn't generate any results, a message will be printed to the system error stream.
     * If the cell data cannot be parsed to the correct type, a message will be printed to the system error stream and the search will continue with the next row.
     * If a SQLException occurs during a search, the stack trace will be printed and the program will exit with a status of 1.
     *
     * @return List<item> The list of items that match the search parameters.
     */
    private List<Item> performSearch() {
        List<Item> searchResultList = new ArrayList<>();

        for (int row = 0; row < itemSearchTable.getRowCount(); row++) {
            //Retrieve cell data
            Object cellData = itemSearchTable.getValueAt(row, 1);

            //If data is null or empty, do nothing
            if (cellData == null || cellData.toString().isEmpty()) {
                continue;
            }

            //Attempt to parse the cell data and perform the search
            try {
                switch (row) {
                    //item ID
                    case 0 -> {
                        int itemID = Integer.parseInt(cellData.toString());
                        Item itemByID = ItemHandler.getItemByID(itemID);
                        if (itemByID != null) {
                            searchResultList.add(itemByID);
                        } else System.err.println("No item found for itemID: " + itemID);
                    }
                    //User ID
                    case 1 -> {
                        int userID = Integer.parseInt(cellData.toString());
                        List<Item> itemByUserIDList = ItemHandler.getItemByTitle(userID);
                        if (!itemByUserIDList.isEmpty()) {
                            searchResultList.addAll(itemByUserIDList);
                        } else System.err.println("No items found for userID: " + userID);
                    }

                }
                //Item ID
                case 3 -> {
                    int itemID = Integer.parseInt(cellData.toString());
                    List<Item> itemByItemIDList = ItemHandler.getitemsByItemID(itemID);
                    if (!itemByItemIDList.isEmpty()) {
                        searchResultList.addAll(itemByItemIDList);
                    } else System.err.println("No items found for itemID: " + itemID);
                }
                //Item Title
                case 4 -> {
                    String itemTitle = cellData.toString();
                    List<Item> itemByItemTitleList = ItemHandler.getStoredTitles(itemTitle);
                    if (!itemByItemTitleList.isEmpty()) {
                        searchResultList.addAll(itemByItemTitleList);
                    } else System.err.println("No items found for item title: " + itemTitle);
                }

            } catch (NumberFormatException | DateTimeParseException nfe) {
                //The cell data could not be parsed to an int or a date, do nothing
                System.err.println("Wrong data type for field: " + itemSearchTable.getValueAt(row, 0));
            }
            catch (SQLException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            }
        }

        return searchResultList;
    }

    /**
     * Sets up the scroll pane that contains the item search table.
     * The table includes columns for "Property" and "Search Value", where each row corresponds to a different item
     * property. The first column contains the property names and the second column is editable for inputting
     * search values.
     *
     * The item search table is then placed within a scroll pane, which is added to the 'searchFieldsPanel' with a
     * BorderLayout.
     *
     * The properties are as follows: "item ID", "User ID", "Username", "Item ID", "Item Title", and "item Date".
     */
    protected void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Search Value"};

        //Gather the data for the table. Each row corresponds to a property of the item.
        //The first column is the name of the property, the second column (which is initially empty)
        //will hold the new value.
        Object[][] data = {
                {"User ID", ""},
                {"Username", ""},
                {"Item ID", ""},
                {"Item Title", ""},
        };

        //Use the column names and data to create a new table with editable cells.
        itemSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        //Create a new scroll pane and add the table to it.
        JScrollPane itemScrollPane = new JScrollPane();
        itemScrollPane.setViewportView(itemSearchTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(itemScrollPane, BorderLayout.CENTER);
    }

    /**
     * Configures the panels for the GUI.
     * Currently, this includes adding the 'searchFieldsPanel' to 'GUIPanel'.
     */
    @Override
    protected void setupPanels() {
        GUIPanel.add(searchFieldsPanel);
    }
