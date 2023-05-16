package edu.groupeighteen.librarydbms.view.GUI.entities.item;

import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.item
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class ItemCreateGUI extends GUI {

    public ItemCreateGUI(GUI previousGUI) {
        super(previousGUI, "ItemCreateGUI");
        setupButtons();
        addButtonsToPanel(new JButton[]{});
        setupPanels();
        this.displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        return null;
    }

    @Override
    protected void setupPanels() {

    }
}