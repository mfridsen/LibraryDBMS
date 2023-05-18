package edu.groupeighteen.librarydbms.view.GUI.entities;

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
 *
 * This class is used to define the behaviour of the "View Rental" button in a JTable cell.
 * It extends the DefaultCellEditor class, and overrides necessary methods to provide the required functionality.
 * The editor will be used in the table displaying the list of rentals in the application.
 */
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private final String label;
    private boolean isPushed;
    private final Rental rental;
    private final GUI previousGUI;

    /**
     * Constructs a ButtonEditor object.
     * Sets up the action listener for the button and configures its visual settings.
     *
     * @param checkBox a JCheckBox object which is sent to the parent constructor.
     * @param rental a Rental object that this button corresponds to.
     * @param previousGUI the previous GUI screen.
     */
    public ButtonEditor(JCheckBox checkBox, Rental rental, GUI previousGUI) {
        super(checkBox);
        this.rental = rental;
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
     * If the button has been clicked (isPushed is true), it creates a new RentalGUI.
     *
     * @return the current editing value from the cell.
     */
    public Object getCellEditorValue() {
        if (isPushed) {
            new RentalGUI(previousGUI, rental);
        }
        isPushed = false;
        return label; // Return a new String so the original is not affected
    }

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