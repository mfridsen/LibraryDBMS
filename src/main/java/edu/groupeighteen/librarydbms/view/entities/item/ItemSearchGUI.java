package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
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
    private JTable itemSearchTable;
    private JPanel searchFieldsPanel;
    public ItemSearchGUI(GUI previousGUI) {
        super(previousGUI, "ItemSearchGUI");
        setupScrollPane();
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
        for (int row = 0; row < itemSearchTable.getRowCount(); row++) {
            for (int col = 0; col < itemSearchTable.getColumnCount(); col++) {
                if (col == 1) { //Assuming the 2nd column is the editable column
                    itemSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    private JButton setupSearchButton() {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            //Perform the search
            List<Item> searchResultList = performSearch();
            //If the search doesn't generate a result, we stay
            if (!searchResultList.isEmpty()) {
                dispose();
                new ItemSearchResultGUI(this, searchResultList);
            } else System.err.println("No results found for search.");
        });
        return searchButton;
    }
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
                    //Item ID
                    case 0 -> {
                        int itemID = Integer.parseInt(cellData.toString());
                        Item item = ItemHandler.getItemByID(itemID);
                        if (!(item == null)) {
                            searchResultList.add(item);
                        } else System.err.println("No item found for itemID: " + itemID);
                    }
                    //Item Title
                    case 1 -> {
                        String title = cellData.toString();
                        Item item = ItemHandler.getItemByTitle(title);
                        if (!(item == null)) {
                            searchResultList.add(item);
                        } else System.err.println("No item found for title: " + title);
                    }
                }
            } catch (NumberFormatException nfe) {
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

    protected void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Search Value"};

        Object[][] data = {
                {"Item ID", ""},
                {"Title", ""},
        };

        //Use the column names and data to create a new table with editable cells.
        itemSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        //Create a new scroll pane and add the table to it.
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(itemSearchTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchFieldsPanel);
    }
}

