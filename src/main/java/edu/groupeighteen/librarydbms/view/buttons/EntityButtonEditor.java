package edu.groupeighteen.librarydbms.view.buttons;

import edu.groupeighteen.librarydbms.model.entities.Entity;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.gui
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * ButtonEditor is a custom table cell editor that uses a JButton.
 * Each JButton instance in the 'View Entity' column of a EntityTable is associated with a specific Entity object.
 * When a JButton is clicked, a new EntityGUI is opened for the associated Entity.
 */
public abstract class EntityButtonEditor extends DefaultCellEditor
{
    protected final String label;
    protected final GUI previousGUI;
    protected JButton button;
    protected boolean isPushed;
    protected Entity entity;

    /**
     * Creates a new ButtonEditor.
     *
     * @param checkBox    a JCheckBox that the superclass's constructor requires.
     * @param entity      the Entity object that the button will open a EntityGUI for when clicked.
     * @param previousGUI the GUI from which the EntityGUI will be opened.
     */
    public EntityButtonEditor(JCheckBox checkBox, Entity entity, String label, GUI previousGUI)
    {
        super(checkBox);
        this.entity = entity;
        this.previousGUI = previousGUI;
        this.label = label;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * Returns a JButton as the cell editor component.
     *
     * @param table      the JTable that is asking the editor to edit.
     * @param value      the value of the cell to be edited.
     * @param isSelected true if the cell is to be rendered with highlighting.
     * @param row        the row of the cell being edited.
     * @param column     the column of the cell being edited.
     * @return the JButton as the cell editor component.
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column)
    {
        isPushed = true;
        return button;
    }

    /**
     * Returns the value contained in the editor.
     * If the JButton has been clicked, opens a new EntityGUI for the associated Entity.
     *
     * @return the text displayed on the JButton.
     */
    public abstract Object getCellEditorValue();

    /**
     * Indicates whether the editing should stop.
     *
     * @return true to indicate that editing has stopped.
     */
    public boolean stopCellEditing()
    {
        isPushed = false;
        return super.stopCellEditing();
    }

    /**
     * Notifies all listeners that the editing has stopped.
     */
    protected void fireEditingStopped()
    {
        super.fireEditingStopped();
    }
}
