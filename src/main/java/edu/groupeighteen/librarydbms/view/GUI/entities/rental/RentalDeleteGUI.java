package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/14/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class RentalDeleteGUI extends GUI {
    private final Rental rentalToDelete;
    private JPanel enterPasswordPanel;
    private JButton confirmButton;

    public RentalDeleteGUI(GUI previousGUI, Rental rentalToDelete) {
        super(previousGUI, "RentalDeleteGUI");
        this.rentalToDelete = rentalToDelete;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e -> {

        });
        return new JButton[]{};
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(enterPasswordPanel);
    }
}