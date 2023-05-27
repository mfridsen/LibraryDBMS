package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherGUIButtonEditor;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherTableModel;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherTable extends JTable {

    private final List<PublisherGUIButtonEditor> editors;

    /**
     * Constructs a PublisherTable with the specified model, Publishers, and previous GUI.
     *
     */
    public PublisherTable(PublisherTableModel model, List<Publisher> Publishers, GUI previousGUI) {
        super(model);
        this.editors = new ArrayList<>();
        for (Publisher Publisher : Publishers) {
            this.editors.add(new PublisherGUIButtonEditor(new JCheckBox(), Publisher, previousGUI));
        }
    }
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        int modelColumn = convertColumnIndexToModel(column);

        if (modelColumn == 4 && row < editors.size()) {
            return editors.get(row);
        } else {
            return super.getCellEditor(row, column);
        }
    }
}
