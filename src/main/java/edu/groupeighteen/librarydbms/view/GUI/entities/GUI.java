package edu.groupeighteen.librarydbms.view.GUI.entities;

import javax.swing.*;
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
public abstract class GUI extends JFrame {
    //This is used to go back to the previous GUI
    protected final GUI previousGUI;
    //All GUI objects will have at least one 'main' panel, to which we can add other panels, such as the buttonPanel
    protected JPanel GUIPanel;
    //All GUI objects will have a button panel at the bottom
    protected JPanel buttonPanel;
    //All GUI objects (except the absolutely first in the stack, but whatever) will have a "Back" button
    protected JButton previousGUIButton;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     * @param previousGUI the previous GUI object.
     * @param title the title of this GUI object.
     */
    public GUI(GUI previousGUI, String title) {
        this.previousGUI = previousGUI;
        this.setTitle(title);
        //The previous button will always be added to the button panel
        buttonPanel = new JPanel();
        setupPreviousGUIButton();
        buttonPanel.add(previousGUIButton);
        setupButtons();
    }

    protected abstract void setupButtons();

    /**
     * Performs all of the basic operations needed to display a GUI (JFrame).
     */
    public void displayGUI() {
        this.add(GUIPanel);
        this.pack(); //Packs all the things
        this.setVisible(true); //We kinda need to be able to see it
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Can close the GUI via close button in the frame
        this.setLocationRelativeTo(null); //Places the GUI at the center of the screen
    }

    /**
     * Adds an array of JButtons to a the button JPanel.
     * Since no Layout is being used, the default FlowLayout will order the buttons in a horizontal row.
     * @param buttons the array of JButtons to add to the buttonPanel.
     */
    protected void addButtonsToPanel(JButton[] buttons) {
        //We want the buttons to be ordered horizontally, in a row
        for (JButton button : buttons)
            buttonPanel.add(button);
    }

    /**
     * Since all GUIs will have a "Back" button that functions exactly the same, we might as well just
     * set it up here.
     */
    private void setupPreviousGUIButton() {
        previousGUIButton = new JButton("Previous GUI");
        previousGUIButton.addActionListener(e -> {
            if (previousGUI == null) {
                System.err.println("No previous GUI to return to!");
            } else {
                dispose();
                previousGUI.displayGUI();
            }
        });
    }

    /**
     * Creates a JTable with named columns and fills it with data. Then makes the table uneditable,
     * adds it to a JScrollPane and returns that scroll pane.
     * @param columnNames a String array containing the names of the columns.
     * @param data a two-dimensional Object array containing the data to fill in the columns.
     * @return a JScrollPane ready to add to a JPanel to display the table.
     */
    protected JScrollPane setupTableScrollPane(String[] columnNames, Object[][] data) {
        //Create table with data and column names
        JTable table = new JTable(data, columnNames);

        //Make the table uneditable
        table.setDefaultEditor(Object.class, null);

        //Add table to a scroll pane in case it gets too big, and return it
        return new JScrollPane(table);
    }

    /**
     * Adds an array of JLabels to a JPanel and then returns that panel.
     * Uses a BoxLayout to align the labels vertically and on the left.
     * This panel needs to be added to another JPanel using a BorderLayout.WEST in order to align properly.
     * @param labels the array of JLabels to add to the panel.
     * @return the new JPanel, with labels installed.
     */
    protected JPanel addLabelsToPanel(JLabel[] labels) {
        JPanel labelPanel = new JPanel();
        //We want the labels to be ordered vertically, in a column
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        for (JLabel label : labels) {
            label.setAlignmentX(Component.LEFT_ALIGNMENT); //Align labels to the left
            labelPanel.add(label);
        }
        return labelPanel;
    }

    public GUI getPreviousGUI() {
        return previousGUI;
    }
}