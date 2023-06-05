package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-19
 */
public class UserTable extends JTable
{

    /**
     * The list of UserGUIButtonEditor instances associated with this UserTable.
     */

    private final List<UserGUIButtonEditor> editors;

    /**
     * Constructs a UserTable with the specified model, users, and previous GUI.
     *
     * @param model       the UserTableModel used as the table model
     * @param users       the list of User objects to be displayed in the table
     * @param previousGUI the previous GUI instance
     */

    public UserTable(UserTableModel model, List<User> users, GUI previousGUI)
    {
        super(model);
        this.editors = new ArrayList<>();
        for (User user : users)
        {
            //this.editors.add(new UserGUIButtonEditor(new JCheckBox(), user, previousGUI));
        }
    }

    /**
     * Returns an appropriate editor for the cell specified by the given row and column indices.
     *
     * @param row    the row of the cell to edit.
     * @param column the column of the cell to edit.
     * @return if the specified cell is in the 'View User' column, a UserGUIButtonEditor is returned
     * that opens a UserGUI for the User object associated with the row.
     * Otherwise, the result of the superclass's getCellEditor method is returned.
     */

    @Override
    public TableCellEditor getCellEditor(int row, int column)
    {
        int modelColumn = convertColumnIndexToModel(column);

        if (modelColumn == 2 && row < editors.size())
        {
            return editors.get(row);
        }
        else
        {
            return super.getCellEditor(row, column);
        }
    }
}

