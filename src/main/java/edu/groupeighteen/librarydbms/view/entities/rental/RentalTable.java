package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * The RentalTable class is a custom JTable that overrides the getCellEditor method
 * to provide different cell editors for different rows in the 'View Rental' column.
 */
public class RentalTable extends JTable
{

    /**
     * The list of RentalGUIButtonEditor instances associated with this RentalTable.
     */
    private final List<RentalGUIButtonEditor> editors;

    /**
     * Constructs a RentalTable with the specified model, rentals, and previous GUI.
     *
     * @param model       the RentalTableModel used as the table model
     * @param rentals     the list of Rental objects to be displayed in the table
     * @param previousGUI the previous GUI instance
     */
    public RentalTable(RentalTableModel model, List<Rental> rentals, GUI previousGUI)
    {
        super(model);
        this.editors = new ArrayList<>();
        for (Rental rental : rentals)
        {
            this.editors.add(new RentalGUIButtonEditor(new JCheckBox(), rental, "View", previousGUI));
        }
    }

    /**
     * Returns an appropriate editor for the cell specified by the given row and column indices.
     *
     * @param row    the row of the cell to edit.
     * @param column the column of the cell to edit.
     * @return if the specified cell is in the 'View Rental' column, a RentalGUIButtonEditor is returned
     * that opens a RentalGUI for the Rental object associated with the row.
     * Otherwise, the result of the superclass's getCellEditor method is returned.
     */
    @Override
    public TableCellEditor getCellEditor(int row, int column)
    {
        int modelColumn = convertColumnIndexToModel(column);

        if (modelColumn == 4 && row < editors.size())
        {
            return editors.get(row);
        }
        else
        {
            return super.getCellEditor(row, column);
        }
    }
}