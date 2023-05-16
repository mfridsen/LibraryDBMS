package edu.groupeighteen.librarydbms.view.GUI.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 * this class displays all information about a single item object
 */
public class ItemGUI extends GUI {
        private Item item;
        private JLabel itemIDLabel;
        private JLabel itemLabel;
        private JLabel ISBNLabel;
        private JButton updateButton;
        private JButton deleteButton;
        private JPanel ItemGUIPanel;
        private JFrame ItemGUIFrame;

    public ItemGUI(Item item, GUI preiousGUI){
        super(preiousGUI, "ItemGUI");
        this.item = item;
    this.itemIDLabel = new JLabel("itemID: " + item.getItemID());
        setupButtons();
        addButtonsToPanel(new JButton[]{updateButton, deleteButton});
        setupPanels();
        this.displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        return new JButton[0];
    }

    @Override
    protected void setupPanels() {

    }
}