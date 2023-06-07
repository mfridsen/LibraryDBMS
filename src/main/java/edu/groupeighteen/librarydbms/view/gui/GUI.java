package edu.groupeighteen.librarydbms.view.gui;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.entities.Entity;
import edu.groupeighteen.librarydbms.view.HomeScreenGUI;
import edu.groupeighteen.librarydbms.view.optionpanes.LogoutOptionPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/14/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public abstract class GUI extends JFrame
{
    //This is used to go back to the previous GUI
    protected final GUI previousGUI;
    //All GUI objects will have at least one 'main' panel, to which we can add other panels, such as the buttonPanel
    protected JPanel GUIPanel;
    //All GUI objects will have a button panel at the bottom
    protected JPanel buttonPanel;
    //All GUI objects (except the absolutely first in the stack, but whatever) will have a "Back" button
    protected JButton previousGUIButton;
    protected JButton logoutButton;

    //Most GUIs are Entity-related
    protected Entity entity;

    /**
     * JLabel for displaying login messages.
     */
    protected JLabel messageLabel;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     * @param previousGUI the previous GUI object.
     * @param title       the title of this GUI object.
     */
    public GUI(GUI previousGUI, String title, Entity entity)
    {
        this.previousGUI = previousGUI;
        this.setTitle(title);
        this.entity = entity;
        messageLabel = new JLabel();
        GUIPanel = new JPanel(new BorderLayout()); //To achieve the preferred layout, BorderLayout is needed
        buttonPanel = new JPanel(); //The previous button will always be added to the button panel
        setupPreviousGUIButton();
        setupLogoutButton();
        buttonPanel.add(previousGUIButton);
        if (!(logoutButton == null)) buttonPanel.add(logoutButton);
        addButtonsToPanel(setupButtons());
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the bottom
    }

    /**
     * GUIs will need a logout button, whenever a User is logged in.
     */
    protected void setupLogoutButton()
    {
        if (LibraryManager.getCurrentUser() != null)
        {
            logoutButton = new JButton("Log Out");
            logoutButton.addActionListener(e ->
            {
                new LogoutOptionPane(this);
            });
        }
    }

    /**
     * All GUI classes will need to implement this method according to their individual layouts.
     */
    protected abstract JButton[] setupButtons();

    /**
     * All GUI classes will have at least two panels, and as such need to implement this method.
     */
    protected abstract void setupPanels();

    /**
     * Performs all of the basic operations needed to display a GUI (JFrame).
     */
    public void displayGUI()
    {
        this.add(GUIPanel);
        this.pack(); //Packs all the things
        this.setVisible(true); //We kinda need to be able to see it
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Can close the GUI via close button in the frame
        this.setLocationRelativeTo(null); //Places the GUI at the center of the screen
    }

    /**
     * Adds an array of JButtons to a the button JPanel.
     * Since no Layout is being used, the default FlowLayout will order the buttons in a horizontal row.
     *
     * @param buttons the array of JButtons to add to the buttonPanel.
     */
    protected void addButtonsToPanel(JButton[] buttons)
    {
        //We want the buttons to be ordered horizontally, in a row
        for (JButton button : buttons)
            buttonPanel.add(button);
    }

    /**
     * Since all GUIs will have a "Back" button that functions exactly the same, we might as well just
     * set it up here.
     */
    private void setupPreviousGUIButton()
    {
        previousGUIButton = new JButton("Back");
        previousGUIButton.addActionListener(e ->
        {
            if (previousGUI == null)
            {
                System.err.println("DEBUG: No previous GUI to return to!");
                dispose();
                new HomeScreenGUI(null);
            }
            else
            {
                dispose();
                previousGUI.displayGUI();
            }
        });
    }

    /**
     * Creates a JTable with named columns and fills it with data, then returns the JTable.
     *
     * @param columnNames a String array containing the names of the columns.
     * @param data        a two-dimensional Object array containing the data to fill in the columns.
     * @return a JTable ready to add to a JScrollPane.
     */
    protected JTable setupTable(String[] columnNames, Object[][] data)
    {
        //Create table with data and column names
        JTable table = new JTable(data, columnNames);

        //Make the table uneditable
        table.setDefaultEditor(Object.class, null);

        return table;
    }

    //TODO-comment rework comment

    /**
     * Creates a JTable with named columns and fills it with data, as well as adding a column with
     * editable cells where new data can be entered.
     *
     * @param columnNames a String array containing the names of the columns.
     * @param data        a two-dimensional Object array containing the data to fill in the columns.
     * @param editableCol the column which is desired to be editable.
     * @return a JTable ready to add to a JScrollPane.
     */
    protected JTable setupTableWithEditableCells(String[] columnNames, Object[][] data, int editableCol)
    {
        // Create table model with data and column names
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                // Make only the desired column editable
                return column == editableCol;
            }
        };
        // Create table with the model
        JTable table = new JTable(tableModel);
        // Make the table use text fields for the third column
        table.getColumnModel().getColumn(editableCol).setCellEditor(new DefaultCellEditor(new JTextField()));
        return table;
    }

    /**
     * Adds an array of JLabels to a JPanel and then returns that panel.
     * Uses a BoxLayout to align the labels vertically and on the left.
     * This panel needs to be added to another JPanel using a BorderLayout.WEST in order to align properly.
     *
     * @param labels the array of JLabels to add to the panel.
     * @return the new JPanel, with labels installed.
     */
    protected JPanel addLabelsToPanel(JLabel[] labels)
    {
        JPanel labelPanel = new JPanel();
        //We want the labels to be ordered vertically, in a column
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        for (JLabel label : labels)
        {
            label.setAlignmentX(Component.LEFT_ALIGNMENT); //Align labels to the left
            labelPanel.add(label);
        }
        return labelPanel;
    }

    public GUI getPreviousGUI()
    {
        return previousGUI;
    }
}