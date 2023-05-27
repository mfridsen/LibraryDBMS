package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.view.buttons.EntityButtonEditor;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherGUI;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherGUIButtonEditor extends EntityButtonEditor {
    public PublisherGUIButtonEditor(JCheckBox checkBox, Publisher publisher, GUI previousGUI) {
        super(checkBox, publisher, previousGUI);
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
            new PublisherGUI(previousGUI, (Publisher) entity);
        }
        isPushed = false;
        return label; // Return a new String so the original is not affected
    }
}
