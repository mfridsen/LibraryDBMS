package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.LoginScreenGUI;
import edu.groupeighteen.librarydbms.view.buttons.ButtonRenderer;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * This class extends GUI to create a specialized graphical interface for displaying a list of rental search results.
 * Each row in the table represents a Rental object and includes a button to view the details of the rental.
 */
public class RentalSearchResultGUI extends GUI
{
    /**
     * The list of Rental objects to be displayed.
     */
    private final List<Rental> searchResultList;

    /**
     * The panel in which the search results are displayed.
     */
    private JPanel searchResultPanel;

    /**
     * Constructs a new RentalSearchResultGUI.
     *
     * @param previousGUI      the GUI instance from which this GUI was opened.
     * @param searchResultList the list of Rental objects to be displayed.
     */
    public RentalSearchResultGUI(GUI previousGUI, List<Rental> searchResultList)
    {
        super(previousGUI, "RentalSearchResultGUI", null);
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Removes any duplicate rentals from the search result list.
     * A rental is considered duplicate if it shares the same rental ID
     * with another rental in the list. In case of duplicates, all but one
     * instance will be removed.
     */
    private void clearDuplicates()
    {
        Set<Integer> seenRentalIDs = new HashSet<>();
        searchResultList.removeIf(rental -> !seenRentalIDs.add(rental.getRentalID()));
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
            dispose(); //TODO-prio should open a MyAccountGUI depending on what type of User is logged on
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    /**
     * Sets up the scroll pane and populates it with data from the search result list.
     * If the search result list is not null and not empty, it creates a custom table model,
     * sets a custom cell renderer and editor for the last column, and adds the table to the scroll pane.
     * The scroll pane is then added to the search result panel.
     */
    private void setupScrollPane()
    {
        // Add a column for the buttons to the column names array
        String[] columnNames = {"Rental ID", "Username", "Item Title", "Rental Date", "View Rental"};

        if (searchResultList != null && !searchResultList.isEmpty())
        {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];

            for (int i = 0; i < searchResultList.size(); i++)
            {
                Rental rental = searchResultList.get(i);
                data[i][0] = rental.getRentalID();
                data[i][1] = rental.getUsername();
                data[i][2] = rental.getItemTitle();
                data[i][3] = rental.getRentalDate();
                data[i][4] = "View";  // Text for the button
            }

            // Use the custom table model when creating the table
            RentalTable searchResultTable = new RentalTable(new RentalTableModel(data, columnNames),
                    searchResultList, this);

            // Set the custom cell renderer and editor for the last column
            ButtonRenderer buttonRenderer = new ButtonRenderer();
            searchResultTable.getColumn("View Rental").setCellRenderer(buttonRenderer);

            for (Rental rental : searchResultList)
            {
                RentalGUIButtonEditor rentalGUIButtonEditor = new RentalGUIButtonEditor(new JCheckBox(), rental,
                        "View", this);
                searchResultTable.getColumnModel().getColumn(4).setCellEditor(rentalGUIButtonEditor);
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