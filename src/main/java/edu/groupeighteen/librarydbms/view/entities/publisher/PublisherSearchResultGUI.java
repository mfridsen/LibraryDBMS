package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.view.LoginScreenGUI;
import edu.groupeighteen.librarydbms.view.buttons.ButtonRenderer;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherGUIButtonEditor;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherTable;
import edu.groupeighteen.librarydbms.view.entities.Publisher.PublisherTableModel;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-25
 */
public class PublisherSearchResultGUI {
    //TODO- fält som ska visas i denna ordning:
    //  publisherID, name, email

    private final List<Publisher> searchResultList;
    private JPanel searchResultPanel;

    public PublisherSearchResultGUI(GUI previousGUI, List<Publisher> searchResultList) {
        super(previousGUI, "PublisherSearchResultGUI");
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    private void clearDuplicates() {
        Set<Integer> seenPublisherIDs = new HashSet<>();
        searchResultList.removeIf(Publisher -> !seenPublisherIDs.add(Publisher.getPublisherID()));
    }

    @Override
    protected JButton[] setupButtons() {
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dispose();
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    private void setupScrollPane() {
        String[] columnNames = {"PublisherID", "Publisher Title", "View Rental"};

        if (searchResultList != null && !searchResultList.isEmpty()) {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++) {
                Publisher Publisher = searchResultList.get(i);
                data[i][0] = Publisher.getPublisherID();
                data[i][1] = Publisher.getPublisherName();
                data[i][2] = Publisher.getEmail();
                data[i][3] = "View";  // Text for the button
            }

            PublisherTable searchResultTable = new PublisherTable(new PublisherTableModel(data, columnNames), searchResultList, this);

            ButtonRenderer buttonRenderer = new ButtonRenderer();
            searchResultTable.getColumn("View Rental").setCellRenderer(buttonRenderer);
            for (Publisher Publisher : searchResultList) {
                PublisherGUIButtonEditor PublisherGUIButtonEditor = new PublisherGUIButtonEditor(new JCheckBox(), Publisher, this);
                searchResultTable.getColumnModel().getColumn(2).setCellEditor(PublisherGUIButtonEditor);
            }

            JScrollPane searchResultScrollPane = new JScrollPane();
            searchResultScrollPane.setViewportView(searchResultTable);
            searchResultPanel = new JPanel(new BorderLayout());
            searchResultPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(searchResultPanel);
    }
}
