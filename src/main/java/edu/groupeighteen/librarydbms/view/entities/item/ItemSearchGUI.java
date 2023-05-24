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
        JButton resetButton = setupResetButton();
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
                if (col == 1) {
                    itemSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    private JButton setupSearchButton() {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            List<Item> searchResultList = performSearch();
            if (!searchResultList.isEmpty()) {
                dispose();
                new ItemSearchResultGUI(this, searchResultList);
            } else {
                System.err.println("No results found for search.");
            }
        });
        return searchButton;
    }

    private List<Item> performSearch() {
        List<Item> searchResultList = new ArrayList<>();

        for (int row = 0; row < itemSearchTable.getRowCount(); row++) {
            Object cellData = itemSearchTable.getValueAt(row, 1);

            if (cellData == null || cellData.toString().isEmpty()) {
                continue;
            }

            try {
                switch (row) {
                    case 0 -> {
                        int itemID = Integer.parseInt(cellData.toString());
                        Item item = ItemHandler.getItemByID(itemID);
                        if (item != null) {
                            searchResultList.add(item);
                        } else {
                            System.err.println("No item found for itemID: " + itemID);
                        }
                    }
                    case 1 -> {
                        String title = cellData.toString();
                        Item item = ItemHandler.getItemByTitle(title);
                        if (item != null) {
                            searchResultList.add(item);
                        } else {
                            System.err.println("No item found for title: " + title);
                        }
                    }
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Wrong data type for field: " + itemSearchTable.getValueAt(row, 0));
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            }
        }

        return searchResultList;
    }

    protected void setupScrollPane() {
        String[] columnNames = {"Property", "Search Value"};

        Object[][] data = {
                {"Item ID", ""},
                {"Title", ""},
        };

        itemSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(itemSearchTable);

        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchFieldsPanel);
    }
}
