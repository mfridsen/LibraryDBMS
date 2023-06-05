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
import edu.groupeighteen.librarydbms.view.buttons.ButtonRenderer;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSearchResultGUI extends GUI {
    // TODO- if LibraryManager.getCurrentUser != null and item.isAvailable
    //  Rental newRental = RentalHandler.createNewRental(LibraryManager.getCurrentUser.getUserID, item.getItemID)
    //  new RentalGUI för newRental

    private final List<Item> searchResultList;
    private JPanel searchResultPanel;

    public ItemSearchResultGUI(GUI previousGUI, List<Item> searchResultList) {
        super(previousGUI, "ItemSearchResultGUI", null);
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    private void clearDuplicates() {
        Set<Integer> seenItemIDs = new HashSet<>();
        searchResultList.removeIf(item -> !seenItemIDs.add(item.getItemID()));
    }

    @Override
    protected JButton[] setupButtons() {
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dispose();
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    private void setupScrollPane() {
        String[] columnNames = {"ID", "Title", "Classification", "View Item", "Rent Item"};

        if (searchResultList != null && !searchResultList.isEmpty()) {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++) {
                Item item = searchResultList.get(i);
                data[i][0] = item.getItemID();
                data[i][1] = item.getTitle();
                data[i][2] = item.getClassificationName();
                data[i][3] = "View"; // Text for the button
                data[i][4] = "Rent"; // Text for the button
            }

            ItemTable searchResultTable = new ItemTable(new ItemTableModel(data, columnNames), searchResultList, this);

            ButtonRenderer buttonRenderer = new ButtonRenderer();

            //View Item buttons
            searchResultTable.getColumn("View Item").setCellRenderer(buttonRenderer);
            for (Item item : searchResultList) {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item, "View", this);
                searchResultTable.getColumnModel().getColumn(3).setCellEditor(itemGUIButtonEditor);
            }

            //Rent Item buttons
            searchResultTable.getColumn("Rent Item").setCellRenderer(buttonRenderer);
            for (Item item : searchResultList) {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item, "Rent", this);
                searchResultTable.getColumnModel().getColumn(4).setCellEditor(itemGUIButtonEditor);
            }

            JScrollPane searchResultScrollPane = new JScrollPane();
            searchResultScrollPane.setViewportView(searchResultTable);
            searchResultPanel = new JPanel(new BorderLayout());
            searchResultPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchResultPanel);
    }
}