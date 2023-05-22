package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.gui.EntityButtonEditor;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-22
 */
public class ItemGUIButtonEditor extends EntityButtonEditor {
    public ItemGUIButtonEditor(JCheckBox checkBox, Item item, GUI previousGUI) {
        super(checkBox, item, previousGUI);
    }

    /**
     * Returns the current editing value from the cell.
     * If the button has been clicked (isPushed is true), it creates a new RentalGUI.
     *
     * @return the current editing value from the cell.
     */
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            new ItemGUI(previousGUI, (Item) entity);
        }
        isPushed = false;
        return label; // Return a new String so the original is not affected
    }
}
