package edu.groupeighteen.librarydbms.view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.rental.RentalSearchResultGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 *  * this class handles searching for users
 *   * leads to UserSearchResultGUI
 */
public class UserSearchGUI extends GUI {
    private JLabel usernameLabel;
    private JLabel userIDLabel;
    private JTextField usernameField;
    private JTextField userIDField;
    private JTable userSearchTable;
    private JPanel searchFieldsPanel;
    public UserSearchGUI(GUI previousGUI) {
        super(previousGUI, "UserSearchGUI");
        setupSearchPanel();
        setupPanels();
        displayGUI();
    }

    protected JButton[] setupButtons() {
        //Resets the editable cells //TODO-bug doesn't clear selected field
        JButton resetButton = setupResetButton();
        //Performs the search and opens a searchResultGUI
        JButton searchButton = setupSearchButton();
        return new JButton[]{resetButton, searchButton};
    }

    private JButton setupResetButton() {
        JButton resetCellsButton = new JButton("Reset");
        resetCellsButton.addActionListener(e -> {
            resetCells();
        });
        return resetCellsButton;
    }

    private void resetCells() {
        for (int row = 0; row < userSearchTable.getRowCount(); row++) {
            for (int col = 0; col < userSearchTable.getColumnCount(); col++) {
                if (col == 1) { //Assuming the 2nd column is the editable column
                    userSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    private JButton setupSearchButton() {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            //Perform the search
            List<User> searchResultList = performSearch();
            //If the search doesn't generate a result, we stay
            if (!searchResultList.isEmpty()) {
                dispose();
                new UserSearchResultGUI(this, searchResultList);
            } else System.err.println("No results found for search.");
        });
        return searchButton;
    }
    private List<User> performSearch() {
        List<Rental> searchResultList = new ArrayList<>();

        for (int row = 0; row < userSearchTable.getRowCount(); row++) {
            //Retrieve cell data
            Object cellData = userSearchTable.getValueAt(row, 1);

            //If data is null or empty, do nothing
            if (cellData == null || cellData.toString().isEmpty()) {
                continue;
            }

            //Attempt to parse the cell data and perform the search
            try {
                switch (row) {
                    //Rental ID
                    case 0 -> {
                        int UserID = Integer.parseInt(cellData.toString());
                        Rental rentalByID = RentalHandler.getRentalByID(UserID);
                        if (usernameField != null) {
                            searchResultList.add(rentalByID);
                        } else System.err.println("No user found for userID: " + UserID);
                    }
                    //User ID
                    case 1 -> {
                        int userID = Integer.parseInt(cellData.toString());
                        List<User> rentalByUserIDList = RentalHandler.getRentalsByUserID(userID);
                        if (!rentalByUserIDList.isEmpty()) {
                            searchResultList.addAll(ByUserIDList);
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
                System.err.println("Wrong data type for field: " + userSearchTable.getValueAt(row, 0));
            }
            catch (SQLException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            }
        }

        return searchResultList;
    }

    protected void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Search Value"};

        Object[][] data = {
                {"User ID", ""},
                {"Username", ""},
        };

        //Use the column names and data to create a new table with editable cells.
        userSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        //Create a new scroll pane and add the table to it.
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(userSearchTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchFieldsPanel);
    }
}

