package edu.groupeighteen.librarydbms.view.entities.rental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * This class extends DefaultTableModel and is used to create a table model for displaying rentals.
 * It provides a custom getColumnClass method to allow for a JButton in a table cell.
 */
public class RentalTableModel extends DefaultTableModel
{

    /**
     * Constructs a RentalTableModel with the specified data and column names.
     *
     * @param data        the two-dimensional array of data representing the table content
     * @param columnNames the array of column names for the table
     */
    public RentalTableModel(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }

    /**
     * Returns the class type of the column at the specified index.
     * If the columnIndex is 4, it returns JButton.class.
     * Otherwise, it delegates to the super class's getColumnClass method.
     *
     * @param columnIndex the index of the column
     * @return the class type of the column at the specified index
     */
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == 4 ? JButton.class : super.getColumnClass(columnIndex);
    }
}