package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class RentalSearchResultGUI extends GUI {
    //TODO-prio clear searchResultList of duplicates
    private final List<Rental> searchResultList;

    private JPanel searchResultPanel;

    public RentalSearchResultGUI(GUI previousGUI, List<Rental> searchResultList) {
        super(previousGUI, "RentalSearchResultGUI");
        this.searchResultList = searchResultList;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        return new JButton[]{};
    }

    private void setupScrollPane() {
        //Define the names of the columns for the table.
        String[] columnNames = {"Rental ID", "Username", "Item Title", "Rental Date"};

        //Ensure that searchResultList is not empty
        if (searchResultList != null && !searchResultList.isEmpty()) {
            //Gather the data for the table. Each row corresponds to a property of the rental.
            Object[][] data = new Object[searchResultList.size()][columnNames.length];

            //Populate data with searchResultList content
            for (int i = 0; i < searchResultList.size(); i++) {
                Rental rental = searchResultList.get(i);
                data[i][0] = rental.getRentalID();
                data[i][1] = rental.getUsername();
                data[i][2] = rental.getItemTitle();
                data[i][3] = rental.getRentalDate();
            }

            //Use the column names and data to create a new table with editable cells.
            JTable searchResultTable = setupTable(columnNames, data);

            //Create a new scroll pane and add the table to it.
            JScrollPane searchResultScrollPane = new JScrollPane();
            searchResultScrollPane.setViewportView(searchResultTable);

            //Create a new panel with a BorderLayout and add the scroll pane to the center of it.
            searchResultPanel = new JPanel(new BorderLayout());
            searchResultPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchResultPanel);
    }

    /*
        String[] columnNames = {"User ID", "Username", "First Name", "Last Name"};
        Object[][] data = {
            {1, "user1", "Anders", "Svensson"},
            {2, "user2", "Kalle", "Persson"},
            // More rows...
        };

        JTable table = new JTable(data, columnNames);

        // To make the table scrollable, add it to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Then add the JScrollPane to your panel or frame
        panel.add(scrollPane, BorderLayout.CENTER);
     */
}