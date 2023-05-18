package edu.groupeighteen.librarydbms.view.GUI.entities;

import edu.groupeighteen.librarydbms.model.entities.Entity;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.rental.RentalGUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public abstract class EntityButtonEditor extends DefaultCellEditor {
    protected JButton button;
    protected final String label;
    protected boolean isPushed;
    protected final Entity entity;
    protected final GUI previousGUI;

    /**
     * Constructs an EntityButtonEditor object.
     * Sets up the action listener for the button and configures its visual settings.
     *
     * @param checkBox a JCheckBox object which is sent to the parent constructor.
     * @param entity an Entity object that this button corresponds to.
     * @param previousGUI the previous GUI screen.
     */
    public EntityButtonEditor(JCheckBox checkBox, Entity entity, GUI previousGUI) {
        super(checkBox);
        this.entity = entity;
        this.previousGUI = previousGUI;
        label = "View";
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * Prepares the button (editor) to be shown in the table cell.
     * It is invoked when a cell is being edited and the editing cell can be scrolled.
     *
     * @param table the table where the editing is taking place.
     * @param value the value of the cell to be edited.
     * @param isSelected whether the cell is selected.
     * @param row the row index of the cell being edited.
     * @param column the column index of the cell being edited.
     * @return the component to be displayed while the cell is being edited.
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        isPushed = true;
        return button;
    }

    /**
     * Returns the current editing value from the cell.
     * If the button has been clicked (isPushed is true), it creates a new EntityGUI.
     *
     * Has to be implemented for each child class.
     *
     * @return the current editing value from the cell.
     */
    public abstract Object getCellEditorValue();
    /*{
        if (isPushed) {
            new RentalGUI(previousGUI, rental);
        }
        isPushed = false;
        return label;
    }*/

    /**
     * Notifies the table that editing has stopped in the current cell, and resets isPushed to false.
     *
     * @return true if editing was stopped; false otherwise.
     */
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.
     * The event instance is lazily created using the parameters passed into the fire method.
     */
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}