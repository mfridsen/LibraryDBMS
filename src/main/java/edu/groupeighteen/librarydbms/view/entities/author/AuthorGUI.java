package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-25
 */
public class AuthorGUI {
    //TODO- fält som ska visas i denna ordning:
    //  authorID, firstName, lastName
    // biography
    private Author Author;
    private JPanel scrollPanePanel;


    public AuthorGUI(GUI preiousGUI, Author Author){
        super(preiousGUI, "AuthorGUI");
        this.Author = Author;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        //Leads to AuthorGUI
        return new JButton[0];

    }

    protected void setupScrollPane(){
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Author ID", Author.getAuthorID()},
                {"Author firstName", Author.getAuthorFirstname()},
                {"Author lastName",  Author.getAuthorLastName()},
                {"Author Biography", Author.getBiography()}
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
    public Author getAuthor(){
        return Author;
    }
}
