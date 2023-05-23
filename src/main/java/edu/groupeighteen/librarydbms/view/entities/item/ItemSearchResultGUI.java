package edu.groupeighteen.librarydbms.view.entities.item;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 *
 * this class displays results for a search performed in the ItemSearchGUI
 * leads to ItemGUI
 */

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.LoginScreenGUI;
import edu.groupeighteen.librarydbms.view.gui.ButtonRenderer;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSearchResultGUI extends GUI {
    private final List<Item> searchResultList;

    /**
     * The panel in which the search results are displayed.
     */
    private JPanel searchResultPanel;

    /**
     * Constructs a new RentalSearchResultGUI.
     *
     * @param previousGUI      the GUI instance from which this GUI was opened.
     * @param searchResultList the list of Rental objects to be displayed.
     */
    public ItemSearchResultGUI(GUI previousGUI, List<Item> searchResultList) {
        super(previousGUI, "ItemSearchResultGUI");
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Removes any duplicate rentals from the search result list.
     * A rental is considered duplicate if it shares the same rental ID
     * with another rental in the list. In case of duplicates, all but one
     * instance will be removed.
     */
    private void clearDuplicates() {
        Set<Integer> seenRentalIDs = new HashSet<>();
        searchResultList.removeIf(item -> !seenRentalIDs.add(item.getItemID()));
    }

    /**
     * Sets up the buttons and their corresponding action listeners.
     * This method creates a "Home" button with an action listener that disposes the current GUI
     * and creates a new LoginScreenGUI instance.
     *
     * @return an array of JButton objects containing the "Home" button
     */
    @Override
    protected JButton[] setupButtons() {
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dispose(); //TODO-prio probably correct but needs discussion
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    /**
     * Sets up the scroll pane and populates it with data from the search result list.
     * If the search result list is not null and not empty, it creates a custom table model,
     * sets a custom cell renderer and editor for the last column, and adds the table to the scroll pane.
     * The scroll pane is then added to the search result panel.
     */
    private void setupScrollPane() {
        // Add a column for the buttons to the column names array
        String[] columnNames = {"ItemID", "Item Title"};

        if (searchResultList != null && !searchResultList.isEmpty()) {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++) {
                Item item = searchResultList.get(i);
                data[i][1] = item.getTitle();
                data[i][2] = item.getItemID();
                data[i][4] = "View";  // Text for the button
            }

            // Use the custom table model when creating the table
            ItemTable searchResultTable = new ItemTable(new ItemTableModel(data, columnNames), searchResultList, this);

            // Set the custom cell renderer and editor for the last column
            ButtonRenderer buttonRenderer = new ButtonRenderer();
            searchResultTable.getColumn("View Rental").setCellRenderer(buttonRenderer);
            for (Item item : searchResultList) {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item, this);
                searchResultTable.getColumnModel().getColumn(4).setCellEditor(itemGUIButtonEditor);
            }

            JScrollPane searchResultScrollPane = new JScrollPane();
            searchResultScrollPane.setViewportView(searchResultTable);
            searchResultPanel = new JPanel(new BorderLayout());
            searchResultPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        }
    }

    /**
     * Sets up the panels for this GUI.
     * Adds the searchResultPanel to the GUIPanel.
     */
    @Override
    protected void setupPanels() {
        GUIPanel.add(searchResultPanel);
    }
}
