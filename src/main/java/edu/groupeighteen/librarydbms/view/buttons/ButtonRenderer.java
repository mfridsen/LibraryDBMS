package edu.groupeighteen.librarydbms.view.buttons;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * This class extends JButton and implements the TableCellRenderer interface. It is used to create a custom renderer
 * that can display a JButton in a table cell.
 */
public class ButtonRenderer extends JButton implements TableCellRenderer
{

    /**
     * Returns this button as the renderer for the specified table cell.
     * The text of the button is set to the value of the cell.
     *
     * @param table      the JTable we're providing the renderer for.
     * @param value      the value of the cell to be rendered.
     * @param isSelected true if the cell is selected.
     * @param hasFocus   true if the cell has focus.
     * @param row        the row index of the cell being drawn.
     * @param column     the column index of the cell being drawn.
     * @return the component used for drawing the cell.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        this.setText((value == null) ? "" : value.toString());
        return this;
    }
}