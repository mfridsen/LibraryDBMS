package edu.groupeighteen.librarydbms.view.entities.author;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class AuthorTableModel extends DefaultTableModel
{
    public AuthorTableModel(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == 4 ? JButton.class : super.getColumnClass(columnIndex);
    }
}
