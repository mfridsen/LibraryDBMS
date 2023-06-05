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
 */
public class ItemTable extends JTable {
    private final List<ItemGUIButtonEditor> editors;

    /**
     * Constructs an ItemTable with the specified model, items, and previous GUI.
     *
     */
    public ItemTable(ItemTableModel model, List<Item> items, GUI previousGUI) {
        super(model);
        this.editors = new ArrayList<>();
        for (Item item : items) {
            this.editors.add(new ItemGUIButtonEditor(new JCheckBox(), item, "View", previousGUI));
            this.editors.add(new ItemGUIButtonEditor(new JCheckBox(), item, "Rent", previousGUI));
        }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        int modelColumn = convertColumnIndexToModel(column);

        if (modelColumn >= 3 && modelColumn <= 4 && row < editors.size()) {
            return editors.get(row * 2 + modelColumn - 3);
        } else {
            return super.getCellEditor(row, column);
        }
    }
}

