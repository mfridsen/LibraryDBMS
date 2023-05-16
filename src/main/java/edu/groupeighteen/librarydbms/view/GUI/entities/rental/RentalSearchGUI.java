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
public class RentalSearchGUI extends GUI {
    private JPanel searchFieldsPanel;

    public RentalSearchGUI(GUI previousGUI) {
        super(previousGUI, "RentalSearchGUI");
    }

    @Override
    protected JButton[] setupButtons() {
        return new JButton[]{};
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchFieldsPanel);
    }
}