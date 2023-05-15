package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;

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

    private JTable resultTable;

    public RentalSearchResultGUI(GUI previousGUI, JTable resultTable) {
        super(previousGUI, "RentalSearchResultGUI");
        this.resultTable = resultTable;
    }

    @Override
    protected void setupButtons() {

    }

    @Override
    protected void setupPanels() {

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