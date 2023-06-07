package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * The 'RentalSearchGUI' class extends from the 'GUI' superclass and provides a graphical interface
 * for users to search for rentals. It allows users to input search parameters, execute the search,
 * and display the results.
 * <p>
 * It includes fields for a panel 'searchFieldsPanel' and a table 'rentalSearchTable' that are used
 * to gather search parameters and display them in a structured manner.
 * <p>
 * The class overrides 'setupButtons()' and 'setupPanels()' methods from the superclass 'GUI' to create a
 * specialized interface for rental searches. It also provides additional methods for setting up
 * specific buttons ('setupResetButton()' and 'setupSearchButton()'), resetting the data in the search table
 * ('resetCells()'), performing the search ('performSearch()'), and setting up the scroll pane that
 * contains the search table ('setupScrollPane()').
 * <p>
 * Usage:
 * new RentalSearchGUI(previousGUI);
 * <p>
 * Note:
 * The 'performSearch()' method attempts to parse cell data into appropriate types (int, String, LocalDateTime)
 * and performs the corresponding search, adding any results to a list of Rentals. If the cell data cannot be
 * parsed to the correct type, or if a SQLException occurs during a search, error messages will be printed to
 * the system error stream and the program may exit with a status of 1.
 * @see Rental
 * @see GUI
 */
public class RentalSearchGUI extends GUI
{
    //TODO- fält som ska visas i denna ordning:
    //  RentalID, RentalDate
    //  userName, itemTitle, rentalDueDate, rentalReturnDate
    //  active, overdue, lateFee
    //TODO-prio add search-by-day and search-by-dates
    private JPanel searchFieldsPanel;
    private JTable rentalSearchTable;

    /**
     * Constructs a new RentalSearchGUI object.
     * The constructor first calls the constructor of the superclass, GUI,
     * passing in the previous GUI and the title of this GUI.
     * It then sets up the scroll pane and panels, and finally displays the GUI.
     *
     * @param previousGUI The GUI that was displayed before this one.
     */
    public RentalSearchGUI(GUI previousGUI)
    {
        super(previousGUI, "RentalSearchGUI", null);
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Overrides the setupButtons() method of the superclass.
     * Sets up two buttons: a 'Reset' button and a 'Search' button.
     * The 'Reset' button will clear the data in the editable cells.
     * The 'Search' button will perform a search based on the data in the editable cells
     * and open a RentalSearchResultGUI with the results of the search.
     * <p>
     * Note: There is a known issue where the selected field is not cleared upon 'Reset' button click.
     *
     * @return An array containing the 'Reset' button and the 'Search' button.
     */
    @Override
    protected JButton[] setupButtons()
    {
        //Resets the editable cells //TODO-bug doesn't clear selected field
        JButton resetButton = setupResetButton();
        //Performs the search and opens a searchResultGUI
        JButton searchButton = setupSearchButton();
        return new JButton[]{resetButton, searchButton};
    }

    /**
     * Sets up and returns a 'Reset' button. When clicked, this button will clear the data in all cells
     * of the third column in the 'rentalSearchTable'.
     * <p>
     * Note: There is a known issue where the selected field is not cleared upon button click.
     *
     * @return JButton The reset button configured with the appropriate action listener.
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
     * Resets all the cells in the second column of the 'rentalSearchTable' to an empty string.
     * <p>
     * Note: This method assumes the second column (index 1) of the table is the only column that needs to be reset.
     */
    private void resetCells()
    {
        for (int row = 0; row < rentalSearchTable.getRowCount(); row++)
        {
            for (int col = 0; col < rentalSearchTable.getColumnCount(); col++)
            {
                if (col == 1)
                { //Assuming the 2nd column is the editable column
                    rentalSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    /**
     * Sets up and returns a 'Search' button. When clicked, this button will perform a search
     * based on the data in the editable cells. If the search generates any results, the current
     * GUI will be disposed and a new RentalSearchResultGUI will be created with the results of the search.
     * If no results are found, an error message will be printed to the system error stream.
     *
     * @return JButton The search button configured with the appropriate action listener.
     */
    private JButton setupSearchButton()
    {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e ->
        {
            //Perform the search
            List<Rental> searchResultList = performSearch();
            //If the search doesn't generate a result, we stay
            if (!searchResultList.isEmpty()) {
                dispose();
                new RentalSearchResultGUI(this, searchResultList);
                RentalHandler.printRentalList(searchResultList);
            } else {
                System.err.println("No results found for search.");
                resetCells();
            }
        });
        return searchButton;
    }

    //TODO-test

    /**
     * Performs a search based on the data in the editable cells of the rentalSearchTable.
     * Each row in the table corresponds to a different search parameter (rental ID, user ID, username, item ID,
     * item title, rental date).
     * If the cell data for a row is not null or empty, an attempt will be made to parse it into the appropriate type
     * (int or String or LocalDateTime)
     * and perform the corresponding search. The search results are then added to a list of Rentals.
     * <p>
     * If a search doesn't generate any results, a message will be printed to the system error stream.
     * If the cell data cannot be parsed to the correct type, a message will be printed to the system error stream and the search will continue with the next row.
     * If a SQLException occurs during a search, the stack trace will be printed and the program will exit with a status of 1.
     *
     * @return List<Rental> The list of Rentals that match the search parameters.
     */
    private List<Rental> performSearch()
    {
        List<Rental> searchResultList = new ArrayList<>();

        for (int row = 0; row < rentalSearchTable.getRowCount(); row++)
        {
            //Retrieve cell data
            Object cellData = rentalSearchTable.getValueAt(row, 1);

            //If data is null or empty, do nothing
            if (cellData == null || cellData.toString().isEmpty())
            {
                continue;
            }

            //Attempt to parse the cell data and perform the search
            try {
                switch (row) {
                    //Rental ID
                    case 0 -> {
                        int rentalID = Integer.parseInt(cellData.toString());
                        Rental rentalByID = RentalHandler.getRentalByID(rentalID);
                        if (rentalByID != null) {
                            searchResultList.add(rentalByID);
                        } else System.err.println("No rental found for rentalID: " + rentalID);
                    }
                    //User ID
                    case 1 -> {
                        int userID = Integer.parseInt(cellData.toString());
                        List<Rental> rentalByUserIDList = RentalHandler.getRentalsByUserID(userID);
                        if (!rentalByUserIDList.isEmpty()) {
                            searchResultList.addAll(rentalByUserIDList);
                        } else System.err.println("No rentals found for userID: " + userID);
                    }
                    //Username
                    case 2 -> {
                        String username = cellData.toString();
                        List<Rental> rentalByUsernameList = RentalHandler.getRentalsByUsername(username);
                        if (!rentalByUsernameList.isEmpty()) {
                            searchResultList.addAll(rentalByUsernameList);
                        } else System.err.println("No rentals found for username: " + username);
                    }
                    //Item ID
                    case 3 -> {
                        int itemID = Integer.parseInt(cellData.toString());
                        List<Rental> rentalByItemIDList = RentalHandler.getRentalsByItemID(itemID);
                        if (!rentalByItemIDList.isEmpty()) {
                            searchResultList.addAll(rentalByItemIDList);
                        } else System.err.println("No rentals found for itemID: " + itemID);
                    }
                    //Item Title
                    case 4 -> {
                        String itemTitle = cellData.toString();
                        List<Rental> rentalByItemTitleList = RentalHandler.getRentalsByItemTitle(itemTitle);
                        if (!rentalByItemTitleList.isEmpty()) {
                            searchResultList.addAll(rentalByItemTitleList);
                        } else System.err.println("No rentals found for item title: " + itemTitle);
                    }
                    //Rental date, assuming the date is stored as a String in the format "yyyy-MM-dd"
                    case 5 -> {
                        LocalDateTime rentalDate = LocalDateTime.parse(cellData.toString());
                        List<Rental> rentalByDateList = RentalHandler.getRentalsByRentalDate(rentalDate);
                        if (!rentalByDateList.isEmpty()) {
                            searchResultList.addAll(rentalByDateList);
                        } else System.err.println("No rentals found for rental date: " + rentalDate);
                    }
                }
            } catch (NumberFormatException | DateTimeParseException nfe) {
                //The cell data could not be parsed to an int or a date, do nothing
                System.err.println("Wrong data type for field: " + rentalSearchTable.getValueAt(row, 0));
            }
            catch (InvalidNameException | InvalidDateException | InvalidIDException | InvalidTitleException e)
            {
                e.printStackTrace();
                LibraryManager.exit(1);
            }
        }

        return searchResultList;
    }

    /**
     * Sets up the scroll pane that contains the rental search table.
     * The table includes columns for "Property" and "Search Value", where each row corresponds to a different rental
     * property. The first column contains the property names and the second column is editable for inputting
     * search values.
     * <p>
     * The rental search table is then placed within a scroll pane, which is added to the 'searchFieldsPanel' with a
     * BorderLayout.
     * <p>
     * The properties are as follows: "Rental ID", "User ID", "Username", "Item ID", "Item Title", and "Rental Date".
     */
    protected void setupScrollPane()
    {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Search Value"};

        //Gather the data for the table. Each row corresponds to a property of the rental.
        //The first column is the name of the property, the second column (which is initially empty)
        //will hold the new value.
        Object[][] data = {
                {"Rental ID ", ""},
                {"User ID", ""},
                {"Username", ""},
                {"Item ID", ""},
                {"Item Title", ""},
                {"Rental Date", ""}
        };

        //Use the column names and data to create a new table with editable cells.
        rentalSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        //Create a new scroll pane and add the table to it.
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(rentalSearchTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    /**
     * Configures the panels for the GUI.
     * Currently, this includes adding the 'searchFieldsPanel' to 'GUIPanel'.
     */
    @Override
    protected void setupPanels()
    {
        GUIPanel.add(searchFieldsPanel);
    }
}