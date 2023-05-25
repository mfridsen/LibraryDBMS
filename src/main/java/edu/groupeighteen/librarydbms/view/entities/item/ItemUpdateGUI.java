package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.item.ItemNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.item.NullItemException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-21
 */
public class ItemUpdateGUI extends GUI {
    //The item object to update
    private final Item olditem;
    //The new item object
    private Item newitem;
    //We need the table to be a member variable in order to access its data via the buttons
    private JTable itemUpdateTable;
    //The panel containing the scroll pane which displays the item data
    private JPanel scrollPanePanel;

    /**
     * Constructs a new itemUpdateGUI instance. This GUI allows the user to update the specified item.
     *
     * @param previousGUI  The GUI that was previously displayed. This is used for navigation purposes.
     * @param itemToUpdate The item object that the user wants to update. The initial fields of the
     *                     GUI will be populated with the details of this item.
     */
    public ItemUpdateGUI(GUI previousGUI, Item itemToUpdate) {
        super(previousGUI, "itemUpdateGUI for itemID = " + itemToUpdate.getItemID());
        this.olditem = itemToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up and returns an array containing 'Reset' and 'Confirm Update' buttons.
     * <p>
     * The 'Reset' button is created by calling the 'setupResetButton' method. When clicked,
     * this button resets the values in the editable fields of the 'itemUpdateTable'.
     * <p>
     * The 'Confirm Update' button is created by calling the 'setupConfirmButton' method.
     * When clicked, this button attempts to collect new data from the 'itemUpdateTable',
     * create a new 'item' object based on this data, and update the old item with the new data.
     *
     * @return JButton[] An array containing the 'Reset' and 'Confirm Update' buttons.
     */
    @Override
    protected JButton[] setupButtons() {
        JButton resetCellsButton = setupResetButton();
        JButton confirmUpdateButton = setupConfirmButton();
        return new JButton[]{resetCellsButton, confirmUpdateButton};
    }

    /**
     * Sets up and returns a 'Reset' button. When clicked, this button will clear the data in all cells
     * of the third column in the 'itemUpdateTable'.
     * <p>
     * Note: There is a known issue where the selected field is not cleared upon button click.
     *
     * @return JButton The reset button configured with the appropriate action listener.
     */
    private JButton setupResetButton() {
        //Clears the data cells
        JButton resetCellsButton = new JButton("Reset");
        //Reset the editable cells //TODO-bug doesn't clear selected field
        resetCellsButton.addActionListener(e -> {
            resetCells();
        });
        return resetCellsButton;
    }

    /**
     * Resets all the cells in the third column of the 'itemUpdateTable' to an empty string.
     * <p>
     * Note: This method assumes the third column (index 2) of the table is the only column that needs to be reset.
     */
    private void resetCells() {
        for (int row = 0; row < itemUpdateTable.getRowCount(); row++) {
            for (int col = 0; col < itemUpdateTable.getColumnCount(); col++) {
                if (col == 2) { //Assuming the 3rd column is the editable column
                    itemUpdateTable.setValueAt("", row, col);
                }
            }
        }
    }

    /**
     * Sets up and returns a 'Confirm Update' button. When clicked, this button collects new data from the
     * 'itemUpdateTable', creates a new 'item' object based on this data, and attempts to update the old
     * item with this new data.
     * <p>
     * The new item data is collected from the third column of the 'itemUpdateTable'. Only non-empty fields
     * are used to update the new item. User ID and Item ID fields are parsed as integers, while item Date
     * is parsed as a LocalDateTime. If any parsing fails due to incorrect input format, an error message is printed
     * to the system error stream.
     * <p>
     * After collecting and parsing the new data, the method tries to update the item in the database using
     * 'itemHandler.updateitem'. If this is successful, the current GUI is disposed and a new itemGUI
     * displaying the updated item is opened. If the update fails due to a SQL exception, the application
     * terminates with a status code of 1. If it fails due to illegal arguments, the error message is printed
     * to the system error stream, the data cells are reset, and the method exits.
     * <p>
     * Note: Better exception handling and testing for illegal arguments is needed.
     *
     * @return JButton The 'Confirm Update' button configured with the appropriate action listener.
     */
    private JButton setupConfirmButton() {
        //Updates itemToUpdate and opens a new itemGUI displaying the updated item object
        JButton confirmUpdateButton = new JButton("Confirm Update");
        confirmUpdateButton.addActionListener(e -> {
            //Duplicate olditem
            newitem = new Item(olditem);

            //Get the new values from the table
            String userID = (String) itemUpdateTable.getValueAt(0, 2);
            String username = (String) itemUpdateTable.getValueAt(1, 2);
            String itemID = (String) itemUpdateTable.getValueAt(2, 2);
            String itemTitle = (String) itemUpdateTable.getValueAt(3, 2);
            //Update the itemToUpdate object. Only update if new value is not null or empty
            try {
                if (userID != null && !userID.isEmpty()) {
                    newitem.setItemID(Integer.parseInt(userID));
                }
                //No parsing required for username, it is a string
                if (username != null && !username.isEmpty()) {
                    newitem.setTitle(username);
                }
                if (itemID != null && !itemID.isEmpty()) {
                    newitem.setItemID(Integer.parseInt(itemID));
                }
                //No parsing required for itemTitle, it is a string
                if (itemTitle != null && !itemTitle.isEmpty()) {
                    newitem.setTitle(itemTitle);
                }
            } catch (NumberFormatException | InvalidIDException nfe) {
                System.err.println("One of the fields that requires a number received an invalid input. User ID:" + userID + ", Item ID: " + itemID);
            } catch (DateTimeParseException | InvalidTitleException dtpe) {
                System.err.println("The date field received an invalid input. Please ensure it is in the correct format.");
            }

            //Now call the update method
            try {
                ItemHandler.updateItem(olditem);
                dispose();
                new ItemGUI(this, newitem);
            } catch (NullItemException | ItemNotFoundException sqle) {
                sqle.printStackTrace();
                LibraryManager.exit(1);
            } catch (IllegalArgumentException ile) {
                System.err.println(ile.getMessage()); //TODO-test //TODO-exception handle better
                resetCells();
            }
        });
        return confirmUpdateButton;
    }

    /**
     * Sets up the scroll pane for the GUI, including creating a table with the
     * current details of the item, and setting it into a panel for display.
     * <p>
     * The table is set up with three columns: "Property", "Old Value", and "New Value".
     * The "Property" column contains the names of the properties of the item.
     * The "Old Value" column contains the current values of these properties.
     * The "New Value" column is initially empty and is where new values can be entered for updating the item.
     * <p>
     * After the table is set up, it is added to a scroll pane, which in turn is added to a panel
     * that uses a BorderLayout. The scroll pane is placed in the center of this panel.
     */
    protected void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Old Value", "New Value"};

        //Gather the data for the table. Each row corresponds to a property of the item.
        //The first column is the name of the property, the second column is its old value,
        //and the third column (which is initially empty) will hold the new value.
        Object[][] data = {
                {"Item ID", olditem.getItemID(), ""},
                {"Item Title", olditem.getTitle(), ""},
        };

        //Use the column names and data to create a new table with editable cells.
        itemUpdateTable = setupTableWithEditableCells(columnNames, data, 2);

        //Create a new scroll pane and add the table to it.
        JScrollPane itemScrollPane = new JScrollPane();
        itemScrollPane.setViewportView(itemUpdateTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(itemScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up the panels for the GUI.
     * <p>
     * In this method, the scroll pane panel created in setupScrollPane() is added
     * to the north area (top) of the GUIPanel.
     */
    @Override
    protected void setupPanels() {
        //Add the scroll pane panel to the north area (top) of the GUIPanel.
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }
}
