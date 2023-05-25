package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 * this class displays all information about a single item object
 */
public class ItemGUI extends GUI {
    // TODO- if LibraryManager.getCurrentUser != null and item.isAvailable
    //  Rental newRental = RentalHandler.createNewRental(LibraryManager.getCurrentUser.getUserID, item.getItemID)
    //  new RentalGUI för newRental

    //TODO- fält som ska visas i denna ordning:
    //  itemID, type, title, barcode, isbn
    //  genre, author name, publisher name
        private Item item;
        private JPanel scrollPanePanel;

    public ItemGUI(GUI preiousGUI, Item item){
        super(preiousGUI, "ItemGUI");
        this.item = item;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        //Leads to ItemGUI
        return new JButton[0];

    }

    protected void setupScrollPane(){
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Item ID", item.getItemID()},
                {"Item Title", item.getTitle()},
        };

        JTable userUpdateTable = setupTable(columnNames, data);
        //Create the scroll pane
        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);
        //Create panel and add scroll pane to it
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }
    public Item getItem(){
        return item;
    }
}