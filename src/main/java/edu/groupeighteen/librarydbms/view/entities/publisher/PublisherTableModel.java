package edu.groupeighteen.librarydbms.view.entities.publisher;

import javax.swing.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherTableModel {


    public PublisherTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 4 ? JButton.class : super.getColumnClass(columnIndex);
    }
}
