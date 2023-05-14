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
public class GUI extends JFrame {
    //This is used to go back to the previous GUI
    protected final GUI previousGUI;
    protected JPanel GUIPanel;
    protected JPanel[] panels;

    public GUI(GUI previousGUI) {
        this.previousGUI = previousGUI;
    }

    public void displayGUI(String title) {
        this.setTitle(title);
        this.add(GUIPanel);
        this.pack(); //Packs all the things
        this.setVisible(true); //We kinda need to be able to see it
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Can close the GUI via close button in the frame
        this.setLocationRelativeTo(null); //Places the GUI at the center of the screen
    }

    /**
     * Adds an array of JButtons to a JPanel and then returns that panel.
     * Since no Layout is being used, the default FlowLayout will order the buttons in a horizontal row.
     * @param buttons
     * @return
     */
    protected JPanel addButtonsToPanel(JButton[] buttons) {
        JPanel buttonPanel = new JPanel();
        //We want the buttons to be ordered horizontally, in a row
        for (JButton button : buttons)
            buttonPanel.add(button);
        return buttonPanel;
    }

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