package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidAgeRatingException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.NullEntityException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidItemTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-21
 */
public class ItemUpdateGUI extends GUI
{
    //TODO-prio improve exception handling
    //TODO-prio fields can be set more or less as one wishes, not good

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
    public ItemUpdateGUI(GUI previousGUI, Item itemToUpdate)
    {
        super(previousGUI, "itemUpdateGUI for itemID = " + itemToUpdate.getItemID(), itemToUpdate);
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
    protected JButton[] setupButtons()
    {
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
    private JButton setupResetButton()
    {
        //Clears the data cells
        JButton resetCellsButton = new JButton("Reset");
        //Reset the editable cells //TODO-bug doesn't clear selected field
        resetCellsButton.addActionListener(e ->
        {
            resetCells();
        });
        return resetCellsButton;
    }

    /**
     * Resets all the cells in the third column of the 'itemUpdateTable' to an empty string.
     * <p>
     * Note: This method assumes the third column (index 2) of the table is the only column that needs to be reset.
     */
    private void resetCells()
    {
        for (int row = 0; row < itemUpdateTable.getRowCount(); row++)
        {
            for (int col = 0; col < itemUpdateTable.getColumnCount(); col++)
            {
                if (col == 2)
                { //Assuming the 3rd column is the editable column
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
    private JButton setupConfirmButton()
    {
        //Recast entity into item
        Item oldItem = (Item) entity;

        //Reset error message
        messageLabel.setText("");

        //Updates itemToUpdate and opens a new itemGUI displaying the updated item object
        JButton confirmUpdateButton = new JButton("Confirm Update");
        confirmUpdateButton.addActionListener(e ->
        {
            Item newItem;

            //Duplicate oldItem
            if (oldItem instanceof Literature)
            {
                newItem = retrieveAndSetLiteratureObject((Literature) oldItem);
            }
            else
            {
                newItem = retrieveAndSetFilmObject((Film) oldItem);
            }

            // Now call the update method
            if (newItem != null)
            {
                try
                {
                    ItemHandler.updateItem(newItem);
                    dispose();
                    new ItemGUI(this, newItem);
                }
                catch (NullEntityException | EntityNotFoundException sqle)
                {
                    sqle.printStackTrace();
                    LibraryManager.exit(1);
                }
                catch (IllegalArgumentException ile)
                {
                    System.err.println(ile.getMessage()); //TODO-test //TODO-exception handle better
                    resetCells();
                }
            }
            else resetCells();
        });
        return confirmUpdateButton;
    }

    /**
     * This method retrieves a Literature object and sets its properties based on the values
     * entered into the table. If an invalid value is entered, an error message will be displayed.
     *
     * @param oldItem the original Literature object
     * @return the updated Literature object, null if any errors were encountered
     */
    private Literature retrieveAndSetLiteratureObject(Literature oldItem)
    {
        Literature newItem = new Literature(oldItem);

        //Used to collect error messages for display
        StringBuilder messageBuilder = new StringBuilder();

        //Get the new values from the table and update the itemToUpdate object.
        //Only update if new value is not null or empty
        try
        {
            String itemTitle = (String) itemUpdateTable.getValueAt(1, 2);
            if (itemTitle != null && !itemTitle.isEmpty())
                newItem.setTitle(itemTitle);
        }
        catch (InvalidTitleException e)
        {
            messageBuilder.append("Invalid title: ").append(e.getMessage()).append("\n");
        }

        //Type
        try
        {
            String itemType = (String) itemUpdateTable.getValueAt(2, 2);
            if (itemType != null && !itemType.isEmpty())
                newItem.setType(Item.ItemType.valueOf(itemType));
        }
        catch (InvalidItemTypeException | IllegalArgumentException e)
        {
            messageBuilder.append("Invalid itemType: ").append(e.getMessage()).append("\n");
        }

        //Classification
        try
        {
            String classificationName = (String) itemUpdateTable.getValueAt(3, 2);
            if (classificationName != null && !classificationName.isEmpty())
                newItem.setClassificationName(classificationName);
        }
        catch (Exception e)
        {
            messageBuilder.append("Invalid classification name: ").append(e.getMessage()).append("\n");
        }

        //Classification
        String classificationName = (String) itemUpdateTable.getValueAt(3, 2);
        if (classificationName != null && !classificationName.isEmpty())
            newItem.setClassificationName(classificationName);

        //Author first name
        String authorFirstName = (String) itemUpdateTable.getValueAt(4, 2);
        if (authorFirstName != null && !authorFirstName.isEmpty())
            newItem.setAuthorFirstname(authorFirstName);

        //Author last name
        String authorLastName = (String) itemUpdateTable.getValueAt(5, 2);
        if (authorLastName != null && !authorLastName.isEmpty())
            newItem.setAuthorLastname(authorLastName);

        //ISBN
        try
        {
            String ISBN = (String) itemUpdateTable.getValueAt(6, 2);
            if (ISBN != null && !ISBN.isEmpty())
                newItem.setISBN(ISBN);
        }
        catch (InvalidISBNException e)
        {
            messageBuilder.append("Invalid ISBN: ").append(e.getMessage()).append("\n");
        }

        //Barcode
        try
        {
            String barcode = (String) itemUpdateTable.getValueAt(7, 2);
            if (barcode != null && !barcode.isEmpty())
                newItem.setBarcode(barcode);
        }
        catch (InvalidBarcodeException e)
        {
            messageBuilder.append("Invalid barcode: ").append(e.getMessage()).append("\n");
        }

        //Available
        String availableStr = (String) itemUpdateTable.getValueAt(8, 2);
        if (availableStr != null && !availableStr.isEmpty())
        {
            boolean available = Boolean.parseBoolean(availableStr);
            newItem.setAvailable(available);
        }

        // Build error message
        String errorMessage = messageBuilder.toString();
        messageLabel.setText(errorMessage);

        // If there were validation errors, return null
        if (!errorMessage.isEmpty())
        {
            return null;
        }

        return newItem;
    }

    /**
     * This method retrieves a Film object and sets its properties based on the values
     * entered into the table. If an invalid value is entered, an error message will be displayed.
     *
     * @param oldItem the original Film object
     * @return the updated Film object, null if any errors were encountered
     */
    private Film retrieveAndSetFilmObject(Film oldItem)
    {
        Film newItem = new Film(oldItem);

        //Used to collect error messages for display
        StringBuilder messageBuilder = new StringBuilder();

        //Get the new values from the table and update the itemToUpdate object.
        //Only update if new value is not null or empty
        try
        {
            String itemTitle = (String) itemUpdateTable.getValueAt(1, 2);
            if (itemTitle != null && !itemTitle.isEmpty())
                newItem.setTitle(itemTitle);
        }
        catch (InvalidTitleException e)
        {
            messageBuilder.append("Invalid title: ").append(e.getMessage()).append("\n");
        }

        //Type
        try
        {
            String itemType = (String) itemUpdateTable.getValueAt(2, 2);
            if (itemType != null && !itemType.isEmpty())
                newItem.setType(Item.ItemType.valueOf(itemType));
        }
        catch (InvalidItemTypeException e)
        {
            messageBuilder.append("Invalid itemType: ").append(e.getMessage()).append("\n");
        }

        //Classification
        try
        {
            String classificationName = (String) itemUpdateTable.getValueAt(3, 2);
            if (classificationName != null && !classificationName.isEmpty())
                newItem.setClassificationName(classificationName);
        }
        catch (Exception e)
        {
            messageBuilder.append("Invalid classification name: ").append(e.getMessage()).append("\n");
        }

        //Author first name
        String authorFirstName = (String) itemUpdateTable.getValueAt(4, 2);
        if (authorFirstName != null && !authorFirstName.isEmpty())
            newItem.setAuthorFirstname(authorFirstName);

        //Author last name
        String authorLastName = (String) itemUpdateTable.getValueAt(5, 2);
        if (authorLastName != null && !authorLastName.isEmpty())
            newItem.setAuthorLastname(authorLastName);

        //Age rating
        Object ageRatingValue = itemUpdateTable.getValueAt(6, 2);
        //System.out.println("Age rating value: " + ageRatingValue); //TODO-debug
        if (ageRatingValue != null && ageRatingValue.toString().matches("\\d+")) {
            try {
                int ageRating = Integer.parseInt(ageRatingValue.toString());
                newItem.setAgeRating(ageRating);
            } catch (InvalidAgeRatingException e) {
                messageBuilder.append("Invalid age rating: ").append(e.getMessage()).append("\n");
            }
        } else if (ageRatingValue != null && !ageRatingValue.toString().isEmpty()) {
            messageBuilder.append("Invalid age rating: Not a number.\n");
        }


        //Country of production
        try
        {
            String countryOfProduction = (String) itemUpdateTable.getValueAt(7, 2);
            if (countryOfProduction != null && !countryOfProduction.isEmpty())
                newItem.setCountryOfProduction(countryOfProduction);
        }
        catch (InvalidNameException e)
        {
            messageBuilder.append("Invalid country of production: ").append(e.getMessage()).append("\n");
        }

        //List of actors
        String listOfActors = (String) itemUpdateTable.getValueAt(8, 2);
        if (listOfActors != null && !listOfActors.isEmpty())
            newItem.setListOfActors(listOfActors);

        //Barcode
        try
        {
            String barcode = (String) itemUpdateTable.getValueAt(9, 2);
            if (barcode != null && !barcode.isEmpty())
                newItem.setBarcode(barcode);
        }
        catch (InvalidBarcodeException e)
        {
            messageBuilder.append("Invalid barcode: ").append(e.getMessage()).append("\n");
        }

        //Available
        Object availableValue = itemUpdateTable.getValueAt(10, 2);
        if (availableValue instanceof Boolean) {
            newItem.setAvailable((Boolean) availableValue);
        }

        // Build error message
        String errorMessage = messageBuilder.toString();
        messageLabel.setText(errorMessage);

        // If there were validation errors, return null
        if (!errorMessage.isEmpty())
        {
            return null;
        }

        return newItem;
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
    protected void setupScrollPane()
    {
        //Recast entity into item
        Item olditem = (Item) entity;

        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Old Value", "New Value"};

        //Gather common data
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"Item ID", olditem.getItemID(), ""});
        data.add(new Object[]{"Item Title", olditem.getTitle(), ""});
        data.add(new Object[]{"Item Type", getItemTypeString(olditem.getType()), ""});
        data.add(new Object[]{"Classification", olditem.getClassificationName(), ""});
        data.add(new Object[]{"Author/Director First Name", olditem.getAuthorFirstname()});
        data.add(new Object[]{"Author/Director Last Name", olditem.getAuthorLastname()});

        //Check if item is an instance of Literature
        if (olditem instanceof Literature)
        {
            Literature literatureItem = (Literature) olditem;
            data.add(new Object[]{"ISBN", literatureItem.getISBN(), ""});
        }
        //Check if item is an instance of Film
        else if (olditem instanceof Film)
        {
            Film filmItem = (Film) olditem;
            data.add(new Object[]{"Age Rating", filmItem.getAgeRating(), ""});
            data.add(new Object[]{"Country of Production", filmItem.getCountryOfProduction(), ""});
            data.add(new Object[]{"List of Actors", String.join(", ", filmItem.getListOfActors()), ""});
        }

        //Add final row(s)
        data.add(new Object[]{"Barcode", olditem.getBarcode(), ""});
        data.add(new Object[]{"Available", olditem.isAvailable(), ""});

        //Convert list to an array for final data structure
        Object[][] dataArray = data.toArray(new Object[0][]);

        //Use the column names and data to create a new table with editable cells.
        itemUpdateTable = setupTableWithEditableCells(columnNames, dataArray, 2);

        //Create a new scroll pane and add the table to it.
        JScrollPane itemScrollPane = new JScrollPane();
        itemScrollPane.setViewportView(itemUpdateTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(messageLabel, BorderLayout.NORTH);
        scrollPanePanel.add(itemScrollPane, BorderLayout.CENTER);
    }

    /**
     * Get the string representation of an Item type.
     *
     * @param type the type of the Item.
     * @return the string representation of the type.
     */
    private String getItemTypeString(Item.ItemType type)
    {
        String typeString = null;
        switch (type)
        {
            case REFERENCE_LITERATURE -> typeString = "Reference Literature";
            case MAGAZINE -> typeString = "Magazine";
            case FILM -> typeString = "Film";
            case COURSE_LITERATURE -> typeString = "Course Literature";
            case OTHER_BOOKS -> typeString = "Book";
        }
        return typeString;
    }

    /**
     * Sets up the panels for the GUI.
     * <p>
     * In this method, the scroll pane panel created in setupScrollPane() is added
     * to the north area (top) of the GUIPanel.
     */
    @Override
    protected void setupPanels()
    {
        //Add the scroll pane panel to the north area (top) of the GUIPanel.
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }
}