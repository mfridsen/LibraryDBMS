package edu.groupeighteen.librarydbms.view.entities.item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-22
 */
public class ItemTableModel extends DefaultTableModel
{
    public ItemTableModel(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == 4 ? JButton.class : super.getColumnClass(columnIndex);
    }
}