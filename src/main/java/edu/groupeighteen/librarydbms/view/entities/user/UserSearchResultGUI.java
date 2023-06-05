package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
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
 * @date 2023-05-10
 * <p>
 * * this class displays results for a search performed in the UserSearchGUI
 * leads to UserGUI
 */

public class UserSearchResultGUI extends GUI
{
    //TODO- fält som ska visas i denna ordning:
    //  userID, userName, type,
    //  firstName, lastName, Email
    //  lateFee
    /**
     * The list of User objects to be displayed.
     */
    private final List<User> searchResultList;

    /**
     * The panel in which the search results are displayed.
     */
    private JPanel searchResultPanel;

    /**
     * Constructs a new UserSearchResultGUI.
     *
     * @param previousGUI      the GUI instance from which this GUI was opened.
     * @param searchResultList the list of User objects to be displayed.
     */
    public UserSearchResultGUI(GUI previousGUI, List<User> searchResultList)
    {
        super(previousGUI, "UserSearchResultGUI", null);
        this.searchResultList = searchResultList;
        clearDuplicates();
        setupScrollPane();
        setupPanels();
        displayGUI();
    }

    /**
     * Removes any duplicate users from the search result list.
     * A user is considered duplicate if it shares the same user ID
     * with another user in the list. In case of duplicates, all but one
     * instance will be removed.
     */
    private void clearDuplicates()
    {
        Set<Integer> seenUserIDs = new HashSet<>();
        searchResultList.removeIf(user -> !seenUserIDs.add(user.getUserID()));
    }

    /**
     * Sets up the buttons and their corresponding action listeners.
     * This method creates a "Home" button with an action listener that disposes the current GUI
     * and creates a new LoginScreenGUI instance.
     *
     * @return an array of JButton objects containing the "Home" button
     */
    @Override
    protected JButton[] setupButtons()
    {
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e ->
        {
            dispose(); //TODO-prio probably correct but needs discussion
            new LoginScreenGUI(this);
        });
        return new JButton[]{homeButton};
    }

    /**
     * Sets up the scroll pane and populates it with data from the search result list.
     * If the search result list is not null and not empty, it creates a custom table model,
     * sets a custom cell renderer and editor for the last column, and adds the table to the scroll pane.
     * The scroll pane is then added to the search result panel.
     */
    private void setupScrollPane()
    {
        // Add a column for the buttons to the column names array
        String[] columnNames = {"User ID", "Username", "View User"};

        if (searchResultList != null && !searchResultList.isEmpty())
        {
            Object[][] data = new Object[searchResultList.size()][columnNames.length];
            for (int i = 0; i < searchResultList.size(); i++)
            {
                User user = searchResultList.get(i);
                data[i][0] = user.getUserID();
                data[i][1] = user.getUsername();
                data[i][2] = "View";  // Text for the button
            }

            // Use the custom table model when creating the table
            UserTable searchResultTable = new UserTable(new UserTableModel(data, columnNames), searchResultList, this);

            // Set the custom cell renderer and editor for the last column
            ButtonRenderer buttonRenderer = new ButtonRenderer();
            searchResultTable.getColumn("View User").setCellRenderer(buttonRenderer);
            for (User user : searchResultList)
            {
                UserGUIButtonEditor userGUIButtonEditor = new UserGUIButtonEditor(new JCheckBox(), user, "View", this);
                searchResultTable.getColumnModel().getColumn(2).setCellEditor(userGUIButtonEditor);
            }

            JScrollPane searchResultScrollPane = new JScrollPane();
            searchResultScrollPane.setViewportView(searchResultTable);
            searchResultPanel = new JPanel(new BorderLayout());
            searchResultPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        }
    }

    /**
     * Sets up the panels for this GUI.
     * Adds the searchResultPanel to the GUIPanel.
     */
    @Override
    protected void setupPanels()
    {
        GUIPanel.add(searchResultPanel);
    }
}