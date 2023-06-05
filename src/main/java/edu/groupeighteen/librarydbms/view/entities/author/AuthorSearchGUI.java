package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class AuthorSearchGUI extends GUI
{
    private JTable authorSearchTable;
    private JPanel searchFieldsPanel;

    public AuthorSearchGUI(GUI previousGUI)
    {
        super(previousGUI, "AuthorSearchGUI", null);
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    protected JButton[] setupButtons()
    {
        //Resets the editable cells //TODO-bug doesn't clear selected field
        JButton resetButton = setupResetButton();
        //Performs the search and opens a searchResultGUI
        JButton searchButton = setupSearchButton();
        return new JButton[]{resetButton, searchButton};
    }

    private JButton setupResetButton()
    {
        JButton resetCellsButton = new JButton("Reset");
        resetCellsButton.addActionListener(e ->
        {
            resetCells();
        });
        return resetCellsButton;
    }

    private void resetCells()
    {
        for (int row = 0; row < authorSearchTable.getRowCount(); row++)
        {
            for (int col = 0; col < authorSearchTable.getColumnCount(); col++)
            {
                if (col == 1)
                { //Assuming the 2nd column is the editable column
                    authorSearchTable.setValueAt("", row, col);
                }
            }
        }
    }

    private JButton setupSearchButton()
    {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e ->
        {
            //Perform the search
            List<Author> searchResultList = performSearch();
            //If the search doesn't generate a result, we stay
            if (!searchResultList.isEmpty())
            {
                dispose();
                new AuthorSearchResultGUI(this, searchResultList);
            }
            else System.err.println("No results found for search.");
        });
        return searchButton;
    }

    private List<Author> performSearch()
    {
        List<Author> searchResultList = new ArrayList<>();

        for (int row = 0; row < authorSearchTable.getRowCount(); row++)
        {
            //Retrieve cell data
            Object cellData = authorSearchTable.getValueAt(row, 1);

            //If data is null or empty, do nothing
            if (cellData == null || cellData.toString().isEmpty())
            {
                continue;
            }

            //Attempt to parse the cell data and perform the search
            /*try {
                switch (row) {
                    //User ID
                    case 0 -> {
                        int userID = Integer.parseInt(cellData.toString());
                        User user = UserHandler.getUserByID(userID);
                        if (!(user == null)) {
                            searchResultList.add(user);
                        } else System.err.println("No user found for userID: " + userID);
                    }
                    //Username
                    case 1 -> {
                        String username = cellData.toString();
                        User user = UserHandler.getUserByUsername(username);
                        if (!(user == null)) {
                            searchResultList.add(user);
                        } else System.err.println("No user found for username: " + username);
                    }
                }
            } catch (NumberFormatException nfe) {
                //The cell data could not be parsed to an int or a date, do nothing
                System.err.println("Wrong data type for field: " + userSearchTable.getValueAt(row, 0));
            }

             */
        }

        return searchResultList;
    }

    protected void setupScrollPane()
    {
        //Define the names of the columns for the table.
        String[] columnNames = {"Property", "Search Value"};

        Object[][] data = {
                {"User ID", ""},
                {"Username", ""},
        };

        //Use the column names and data to create a new table with editable cells.
        authorSearchTable = setupTableWithEditableCells(columnNames, data, 1);

        //Create a new scroll pane and add the table to it.
        JScrollPane rentalScrollPane = new JScrollPane();
        rentalScrollPane.setViewportView(authorSearchTable);

        //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
        searchFieldsPanel = new JPanel(new BorderLayout());
        searchFieldsPanel.add(rentalScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels()
    {
        GUIPanel.add(searchFieldsPanel);
    }
}
