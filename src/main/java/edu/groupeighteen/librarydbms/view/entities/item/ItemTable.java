package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-22
 * <p>
 * ItemTable is an extension of the JTable class to support custom
 * cell editors, which in this case are JButton instances with
 * specific click behavior defined by ItemGUIButtonEditor.
 * Each table row corresponds to an item, with two buttons - "View"
 * and "Rent" - each handled by a different ItemGUIButtonEditor instance.
 */
public class ItemTable extends JTable
{
    /**
     * Holds instances of ItemGUIButtonEditor, each associated
     * with a button in the table's cells.
     */
    private final List<ItemGUIButtonEditor> editors;

    /**
     * Constructs an ItemTable with the specified model, items, and previous GUI.
     *
     * @param model       the ItemTableModel that the JTable will use.
     * @param items       the list of Item objects for which "View" and "Rent" buttons are to be created.
     * @param previousGUI the GUI from which the ItemTable is created.
     */
    public ItemTable(ItemTableModel model, List<Item> items, GUI previousGUI)
    {
        super(model);
        this.editors = new ArrayList<>();
        for (Item item : items)
        {
            this.editors.add(new ItemGUIButtonEditor(new JCheckBox(), item, "View", previousGUI));
            this.editors.add(new ItemGUIButtonEditor(new JCheckBox(), item, "Rent", previousGUI));
        }
    }

    /**
     * Returns the appropriate cell editor for the given cell, which
     * is an ItemGUIButtonEditor if the cell is in the "View" or "Rent"
     * columns and the row index is less than the number of editors.
     * Otherwise, it returns the default cell editor.
     *
     * @param row    the row index of the cell being edited.
     * @param column the column index of the cell being edited.
     * @return the appropriate TableCellEditor for the given cell.
     */
    @Override
    public TableCellEditor getCellEditor(int row, int column)
    {
        int modelColumn = convertColumnIndexToModel(column);

        if (modelColumn >= 3 && modelColumn <= 4 && row < editors.size())
        {
            return editors.get(row * 2 + modelColumn - 3);
        }
        else
        {
            return super.getCellEditor(row, column);
        }
    }
}