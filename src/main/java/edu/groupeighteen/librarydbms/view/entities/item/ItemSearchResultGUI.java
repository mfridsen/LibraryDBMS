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

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSearchResultGUI extends GUI {
    private final List<Item> searchResultList;
    private JPanel searchResultPanel;

    public ItemSearchResultGUI(GUI previousGUI, List<Item> searchResultList) {
        super(previousGUI, "ItemSearchResultGUI");
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
        String[] columnNames = {"ItemID", "Item Title", "View Rental"};

        if (searchResultList != null && !searchResultList.isEmpty()) {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++) {
                Item item = searchResultList.get(i);
                data[i][0] = item.getItemID();
                data[i][1] = item.getTitle();
                data[i][2] = "View";  // Text for the button
            }

            ItemTable searchResultTable = new ItemTable(new ItemTableModel(data, columnNames), searchResultList, this);

            ButtonRenderer buttonRenderer = new ButtonRenderer();
            searchResultTable.getColumn("View Rental").setCellRenderer(buttonRenderer);
            for (Item item : searchResultList) {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item, this);
                searchResultTable.getColumnModel().getColumn(2).setCellEditor(itemGUIButtonEditor);
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
