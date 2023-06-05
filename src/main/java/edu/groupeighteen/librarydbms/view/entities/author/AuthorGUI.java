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
public class AuthorGUI extends GUI
{
    //TODO- fält som ska visas i denna ordning:
    //  authorID, firstName, lastName
    // biography
    private final Author author;
    private JPanel scrollPanePanel;

    /**
     * @param
     * @param author
     */

    public AuthorGUI(GUI previousGUI, Author author)
    {
        super(previousGUI, "AuthorGUI", author);
        this.author = author;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    protected JButton[] setupButtons()
    {
        JButton updateButton = new JButton("Update Author");
        updateButton.addActionListener(e ->
        {
            dispose();
            new AuthorUpdateGUI(this, author);
        });
        JButton deleteButton = new JButton("Delete Author");
        deleteButton.addActionListener(e ->
        {
            dispose();
            new AuthorDeleteGUI(this, author);
        });
        return new JButton[]{deleteButton, updateButton};
    }

    private void setupScrollPane()
    {
        String[] columnNames = {"Property", "Value"};

        Object[][] data = {
                {"Author ID", author.getAuthorID()},
                {"Author Name", author.getAuthorFirstname()},

        };
        JTable authorUpdateTable = setupTable(columnNames, data);
        JScrollPane authorScrollPane = new JScrollPane();
        authorScrollPane.setViewportView(authorUpdateTable);
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(authorScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void setupPanels()
    {
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH);
    }

    public Author getAuthor()
    {
        return author;
    }
}
