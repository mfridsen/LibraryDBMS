package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-25
 */
public class PublisherGUI {
    //TODO- fält som ska visas i denna ordning:
    //  publisherID, name, email

    private Publisher Publisher;
    private JPanel scrollPanePanel;

    public PublisherGUI(GUI preiousGUI, Publisher Publisher){
        super(preiousGUI, "PublisherGUI");
        this.Publisher = Publisher;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        //Leads to PublisherGUI
        return new JButton[0];

    }

    protected void setupScrollPane(){
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Publisher ID", Publisher.getPublisherID()},
                {"Publisher Name", Publisher.getPublisherName()},
                {"Publisher Email", Publisher.getEmail()}
        };

        JTable userUpdateTable = setupTable(columnNames, data);
        //Create the scroll pane
        JScrollPane userScrollPane = new JScrollPane();
        userScrollPane.setViewportView(userUpdateTable);
        //Create panel and add scroll pane to it
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(userScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels() {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }
    public Publisher getPublisher(){
        return Publisher;
    }
}
