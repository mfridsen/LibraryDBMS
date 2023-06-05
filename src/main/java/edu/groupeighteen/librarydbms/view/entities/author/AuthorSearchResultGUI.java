package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.view.LoginScreenGUI;
import edu.groupeighteen.librarydbms.view.buttons.ButtonRenderer;
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
public class AuthorSearchResultGUI extends GUI
{
    //TODO- fält som ska visas i denna ordning:
    //  authorID, firstName, lastName

    private final List<Author> searchResultList;
    private JPanel searchResultPanel;

    public AuthorSearchResultGUI(GUI previousGUI, List<Author> searchResultList)
    {
        super(previousGUI, "AuthorSearchResultGUI", null);
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    private void clearDuplicates()
    {
        Set<Integer> seenAuthorIDs = new HashSet<>();
        searchResultList.removeIf(Author -> !seenAuthorIDs.add(Author.getAuthorID()));
    }

    @Override
    protected JButton[] setupButtons()
    {
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e ->
        {
            dispose();
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    private void setupScrollPane()
    {
        String[] columnNames = {"AuthorID", "Author firstName", "Author lastName", "View Rental"};

        if (searchResultList != null && !searchResultList.isEmpty())
        {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++)
            {
                Author Author = searchResultList.get(i);
                data[i][0] = Author.getAuthorID();
                data[i][1] = Author.getAuthorFirstname();
                data[i][2] = Author.getAuthorLastName();
                data[i][3] = "View";  // Text for the button
            }

            AuthorTable searchResultTable = new AuthorTable(new AuthorTableModel(data, columnNames), searchResultList,
                    this);

            ButtonRenderer buttonRenderer = new ButtonRenderer();
            searchResultTable.getColumn("View Rental").setCellRenderer(buttonRenderer);
            for (Author Author : searchResultList)
            {
                AuthorGUIButtonEditor AuthorGUIButtonEditor = new AuthorGUIButtonEditor(new JCheckBox(), Author,
                        "View", this);
                searchResultTable.getColumnModel().getColumn(2).setCellEditor(AuthorGUIButtonEditor);
            }

            JScrollPane searchResultScrollPane = new JScrollPane();
            searchResultScrollPane.setViewportView(searchResultTable);
            searchResultPanel = new JPanel(new BorderLayout());
            searchResultPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void setupPanels()
    {
        GUIPanel.add(searchResultPanel);
    }
}
