package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.PublisherHandler;
import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.Publisher.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.Publisher.PublisherNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.Publisher.NullPublisherException;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherUpdateGUI extends GUI {
    //The Publisher object to update
    private final Publisher oldPublisher;
    //The new Publisher object
    private Publisher newPublisher;
    //We need the table to be a member variable in order to access its data via the buttons
    private JTable PublisherUpdateTable;
    //The panel containing the scroll pane which displays the Publisher data
    private JPanel scrollPanePanel;

    /**
     * Constructs a new PublisherUpdateGUI instance. This GUI allows the user to update the specified Publisher.
     *
     * @param previousGUI  The GUI that was previously displayed. This is used for navigation purposes.
     * @param PublisherToUpdate The Publisher object that the user wants to update. The initial fields of the
     *                     GUI will be populated with the details of this Publisher.
     */
    public PublisherUpdateGUI(GUI previousGUI, Publisher PublisherToUpdate) {
        super(previousGUI, "PublisherUpdateGUI for PublisherID = " + PublisherToUpdate.getPublisherID());
        this.oldPublisher = PublisherToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up and returns an array containing 'Reset' and 'Confirm Update' buttons.
     * <p>
     * The 'Reset' button is created by calling the 'setupResetButton' method. When clicked,
     * this button resets the values in the editable fields of the 'PublisherUpdateTable'.
     * <p>
     * The 'Confirm Update' button is created by calling the 'setupConfirmButton' method.
     * When clicked, this button attempts to collect new data from the 'PublisherUpdateTable',
     * create a new 'Publisher' object based on this data, and update the old Publisher with the new data.
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
     * of the third column in the 'PublisherUpdateTable'.
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
     * Resets all the cells in the third column of the 'PublisherUpdateTable' to an empty string.
     * <p>
     * Note: This method assumes the third column (index 2) of the table is the only column that needs to be reset.
     */
    private void resetCells() {
        for (int row = 0; row < PublisherUpdateTable.getRowCount(); row++) {
            for (int col = 0; col < PublisherUpdateTable.getColumnCount(); col++) {
                if (col == 2) { //Assuming the 3rd column is the editable column
                    PublisherUpdateTable.setValueAt("", row, col);
                }
            }
        }
    }

    /**
     * Sets up and returns a 'Confirm Update' button. When clicked, this button collects new data from the
     * 'PublisherUpdateTable', creates a new 'Publisher' object based on this data, and attempts to update the old
     * Publisher with this new data.
     * <p>
     * The new Publisher data is collected from the third column of the 'PublisherUpdateTable'. Only non-empty fields
     * are used to update the new Publisher. User ID and Publisher ID fields are parsed as integers, while Publisher Date
     * is parsed as a LocalDateTime. If any parsing fails due to incorrect input format, an error message is printed
     * to the system error stream.
     * <p>
     * After collecting and parsing the new data, the method tries to update the Publisher in the database using
     * 'PublisherHandler.updatePublisher'. If this is successful, the current GUI is disposed and a new PublisherGUI
     * displaying the updated Publisher is opened. If the update fails due to a SQL exception, the application
     * terminates with a status code of 1. If it fails due to illegal arguments, the error message is printed
     * to the system error stream, the data cells are reset, and the method exits.
     * <p>
     * Note: Better exception handling and testing for illegal arguments is needed.
     *
     * @return JButton The 'Confirm Update' button configured with the appropriate action listener.
     */
    private JButton setupConfirmButton() {
        //Updates PublisherToUpdate and opens a new PublisherGUI displaying the updated Publisher object
        JButton confirmUpdateButton = new JButton("Confirm Update");
        confirmUpdateButton.addActionListener(e -> {
            //Duplicate oldPublisher
            newPublisher = new Publisher(oldPublisher);

            //Get the new values from the table
            String userID = (String) PublisherUpdateTable.getValueAt(0, 2);
            String username = (String) PublisherUpdateTable.getValueAt(1, 2);
            String PublisherID = (String) PublisherUpdateTable.getValueAt(2, 2);
            String PublisherTitle = (String) PublisherUpdateTable.getValueAt(3, 2);
            //Update the PublisherToUpdate object. Only update if new value is not null or empty
            try {
                if (userID != null && !userID.isEmpty()) {
                    newPublisher.setPublisherID(Integer.parseInt(userID));
                }
                //No parsing required for username, it is a string
                if (username != null && !username.isEmpty()) {
                    newPublisher.setTitle(username);
                }
                if (PublisherID != null && !PublisherID.isEmpty()) {
                    newPublisher.setPublisherID(Integer.parseInt(PublisherID));
                }
                //No parsing required for PublisherTitle, it is a string
                if (PublisherTitle != null && !PublisherTitle.isEmpty()) {
                    newPublisher.setTitle(PublisherTitle);
                }
            } catch (NumberFormatException | InvalidIDException nfe) {
                System.err.println("One of the fields that requires a number received an invalid input. User ID:" + userID + ", Publisher ID: " + PublisherID);
            } catch (DateTimeParseException | InvalidTitleException dtpe) {
                System.err.println("The date field received an invalid input. Please ensure it is in the correct format.");
            }

            //Now call the update method
            try {
                PublisherHandler.updatePublisher(oldPublisher);
                dispose();
                new PublisherGUI(this, newPublisher);
            } catch (NullPublisherException | PublisherNotFoundException sqle) {
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
     * current details of the Publisher, and setting it into a panel for display.
     * <p>
     * The table is set up with three columns: "Property", "Old Value", and "New Value".
     * The "Property" column contains the names of the properties of the Publisher.
     * The "Old Value" column contains the current values of these properties.
     * The "New Value" column is initially empty and is where new values can be entered for updating the Publisher.
     * <p>
     * After the table is set up, it is added to a scroll pane, which in turn is added to a panel
     * that uses a BorderLayout. The scroll pane is placed in the center of this panel.
     */
    protected void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Old Value", "New Value"};

        //Gather the data for the table. Each row corresponds to a property of the Publisher.
        //The first column is the name of the property, the second column is its old value,
        //and the third column (which is initially empty) will hold the new value.
        Object[][] data = {
                {"Publisher ID", oldPublisher.getPublisherID(), ""},
                {"Publisher Title", oldPublisher.getTitle(), ""},
        };

        //Use the column names and data to create a new table with editable cells.
        PublisherUpdateTable = setupTableWithEditableCells(columnNames, data, 2);

        //Create a new scroll pane and add the table to it.
        JScrollPane PublisherScrollPane = new JScrollPane();
        PublisherScrollPane.setViewportView(PublisherUpdateTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(PublisherScrollPane, BorderLayout.CENTER);
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
