package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.LoginScreenGUI;
import edu.groupeighteen.librarydbms.view.buttons.ButtonRenderer;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 * <p>
 * This class extends the GUI class and represents the GUI for displaying a list of items.
 */
public class ItemSearchResultGUI extends GUI
{
    /**
     * The list of Item objects to be displayed.
     */
    private final List<Item> searchResultList;

    /**
     * The panel in which the search results are displayed.
     */
    private JPanel searchResultPanel;

    /**
     * Constructs a new ItemSearchResultGUI.
     *
     * @param previousGUI      the GUI instance from which this GUI was opened.
     * @param searchResultList the list of Item objects to be displayed.
     */
    public ItemSearchResultGUI(GUI previousGUI, List<Item> searchResultList)
    {
        super(previousGUI, "ItemSearchResultGUI", null);
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Removes any duplicate items from the search result list.
     * An item is considered duplicate if it shares the same item ID
     * with another item in the list. In case of duplicates, all but one
     * instance will be removed.
     */
    private void clearDuplicates()
    {
        Set<Integer> seenItemIDs = new HashSet<>();
        searchResultList.removeIf(item -> !seenItemIDs.add(item.getItemID()));
    }

    /**
     * Sets up the buttons and their corresponding action listeners.
     * This method creates a "Home" button with an action listener that disposes the current GUI
     * and creates a new LoginScreenGUI instance.
     *
     * @return an array of JButton objects containing the "Home" button
     */
    @Override
    protected JButton[] setupButtons()
    {
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e ->
        {
            dispose();
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    /**
     * Sets up the scroll pane and populates it with data from the search result list.
     * If the search result list is not null and not empty, it creates a custom table model,
     * sets a custom cell renderer and editor for the last two columns, and adds the table to the scroll pane.
     * The scroll pane is then added to the search result panel.
     */
    private void setupScrollPane()
    {
        String[] columnNames = {"ID", "Title", "Classification", "View Item", "Rent Item"};

        if (searchResultList != null && !searchResultList.isEmpty())
        {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++)
            {
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
            for (Item item : searchResultList)
            {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item, "View", this);
                searchResultTable.getColumnModel().getColumn(3).setCellEditor(itemGUIButtonEditor);
            }

            //Rent Item buttons
            searchResultTable.getColumn("Rent Item").setCellRenderer(buttonRenderer);
            for (Item item : searchResultList)
            {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item, "Rent", this);
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
    protected void setupPanels()
    {
        GUIPanel.add(searchResultPanel);
    }
}