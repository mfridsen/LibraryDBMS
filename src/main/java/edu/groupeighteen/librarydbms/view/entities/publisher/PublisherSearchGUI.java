package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.control.entities.PublisherHandler;
import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherSearchResultGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherSearchGUI extends GUI {
    private JTable PublisherSearchTable;
    private JPanel searchFieldsPanel;

    public PublisherSearchGUI(GUI previousGUI) {
        super(previousGUI, "PublisherSearchGUI");
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
        for (int row = 0; row < PublisherSearchTable.getRowCount(); row++) {
            for (int col = 0; col < PublisherSearchTable.getColumnCount(); col++) {
                if (col == 1) {
                    PublisherSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    private JButton setupSearchButton() {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            List<Publisher> searchResultList = performSearch();
            if (!searchResultList.isEmpty()) {
                dispose();
                new PublisherSearchResultGUI(this, searchResultList);
            } else {
                System.err.println("No results found for search.");
            }
        });
        return searchButton;
    }

    private List<Publisher> performSearch() {
        List<Publisher> searchResultList = new ArrayList<>();

        for (int row = 0; row < PublisherSearchTable.getRowCount(); row++) {
            Object cellData = PublisherSearchTable.getValueAt(row, 1);

            if (cellData == null || cellData.toString().isEmpty()) {
                continue;
            }

            try {
                switch (row) {
                    case 0 -> {
                        int PublisherID = Integer.parseInt(cellData.toString());
                        Publisher Publisher = PublisherHandler.getPublisherByID(PublisherID);
                        if (Publisher != null) {
                            searchResultList.add(Publisher);
                        } else {
                            System.err.println("No Publisher found for PublisherID: " + PublisherID);
                        }
                    }
                    case 1 -> {
                        String title = cellData.toString();
                        //TODO-prio change
                        /*
                        Publisher Publisher = PublisherHandler.getPublisherByTitle(title);
                        if (Publisher != null) {
                            searchResultList.add(Publisher);
                        } else {
                            System.err.println("No Publisher found for title: " + title);
                        }

                         */
                    }
                }
            } catch (NumberFormatException | InvalidIDException nfe) {
                System.err.println("Wrong data type for field: " + PublisherSearchTable.getValueAt(row, 0));
            }
        }

        return searchResultList;
    }

    protected void setupScrollPane() {
        String[] columnNames = {"Property", "Search Value"};

        Object[][] data = {
                {"Publisher ID", ""},
                {"Title", ""},
        };

        PublisherSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(PublisherSearchTable);

        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchFieldsPanel);
    }
}
