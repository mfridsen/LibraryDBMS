package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class AuthorUpdateGUI extends GUI
{
    private final Author oldAuthor;
    private Author newAuthor;
    private JTable authorUpdateTable;
    private JPanel scrollPanePanel;


    public AuthorUpdateGUI(GUI previousGUI, Author authorToUpdate)
    {
        super(previousGUI, "AuthorUpdateGUI for authorID = " + authorToUpdate.getAuthorID(), authorToUpdate);
        this.oldAuthor = authorToUpdate;
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
        for (int row = 0; row < authorUpdateTable.getRowCount(); row++)
        {
            for (int col = 0; col < authorUpdateTable.getColumnCount(); col++)
            {
                if (col == 2)
                { //Assuming the 3rd column is the editable column
                    authorUpdateTable.setValueAt("", row, col);
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
            newAuthor = new Author(oldAuthor);

            //Get the new values from the table
            String userID = (String) authorUpdateTable.getValueAt(0, 2);
            String username = (String) authorUpdateTable.getValueAt(1, 2);

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
                new AuthorGUI(this, newAuthor);
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
                {"Author ID", oldAuthor.getAuthorID(), ""},
                {"Author Name", oldAuthor.getAuthorFirstname(), ""},
        };
        authorUpdateTable = setupTableWithEditableCells(columnNames, data, 2);

        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(authorUpdateTable);

        scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);

    }


    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);

    }


}
