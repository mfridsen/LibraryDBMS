package edu.groupeighteen.librarydbms.view.entities.user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-19
 */
public class UserTableModel extends DefaultTableModel
{

    public UserTableModel(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == 4 ? JButton.class : super.getColumnClass(columnIndex);
    }
}
