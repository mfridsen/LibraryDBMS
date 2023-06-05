package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 */
public class UserUpdateGUI extends GUI
{
    private final User oldUser;
    private User newUser;
    private JTable userUpdateTable;
    private JPanel scrollPanePanel;


    public UserUpdateGUI(GUI previousGUI, User userToUpdate)
    {
        super(previousGUI, "UserUpdateGUI for userID = " + userToUpdate.getUserID(), userToUpdate);
        this.oldUser = userToUpdate;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    protected JButton[] setupButtons()
    {
        JButton resetCellsButton = setupResetButton();
        JButton confirmUpdateButton = setupConfirmButton();
        return new JButton[]{resetCellsButton, confirmUpdateButton};
    }

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

    private void resetCells()
    {
        for (int row = 0; row < userUpdateTable.getRowCount(); row++)
        {
            for (int col = 0; col < userUpdateTable.getColumnCount(); col++)
            {
                if (col == 2)
                { //Assuming the 3rd column is the editable column
                    userUpdateTable.setValueAt("", row, col);
                }
            }
        }
    }

    private JButton setupConfirmButton()
    {
        //Updates rentalToUpdate and opens a new RentalGUI displaying the updated Rental object
        JButton confirmUpdateButton = new JButton("Confirm Update");
        confirmUpdateButton.addActionListener(e ->
        {
            //Duplicate oldRental
            newUser = new User(oldUser);

            //Get the new values from the table
            String userID = (String) userUpdateTable.getValueAt(0, 2);
            String username = (String) userUpdateTable.getValueAt(1, 2);

            //Update the rentalToUpdate object. Only update if new value is not null or empty
            try
            {
                if (userID != null && !userID.isEmpty())
                {
                    //newUser.setUserID(Integer.parseInt(userID));
                }
                //No parsing required for username, it is a string
                if (username != null && !username.isEmpty())
                {
                    //newUser.setUsername(username);
                }

                //No parsing required for itemTitle, it is a string
            }
            catch (NumberFormatException nfe)
            {
                System.err.println(
                        "One of the fields that requires a number received an invalid input. User ID:" + userID);
            }
            catch (DateTimeParseException dtpe)
            {
                System.err.println(
                        "The date field received an invalid input. Please ensure it is in the correct format.");
            }

            //Now call the update method
            try
            {
                //UserHandler.updateUser(newUser);
                dispose();
                new UserGUI(this, newUser);
            }
            catch (IllegalArgumentException ile)
            {
                System.err.println(ile.getMessage()); //TODO-test //TODO-exception handle better
                resetCells();
            }
        });
        return confirmUpdateButton;
    }


    private void setupScrollPane()
    {
        String[] columnNames = {"Property", "Old Value", "New Value"};
        Object[][] data = {
                {"User ID", oldUser.getUserID(), ""},
                {"Username", oldUser.getUsername(), ""},
        };
        userUpdateTable = setupTableWithEditableCells(columnNames, data, 2);

        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);

        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);

    }


    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);

    }


}