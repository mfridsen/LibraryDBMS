package edu.groupeighteen.librarydbms.view.GUI.entities;

import edu.groupeighteen.librarydbms.model.entities.Rental;

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
public class RentalGUI extends JFrame {
    //This is used to go back to the previous frame
    private final JFrame previousGUI; //All GUI-classes should have this

    private final Rental rental;

    private JLabel[] labels; //Makes things a little cleaner
    private JLabel rentalIDLabel;
    private JLabel userIDLabel;
    private JLabel usernameLabel;
    private JLabel itemIDLabel;
    private JLabel itemTitleLabel;
    private JLabel rentalDateLabel;

    private JButton[] buttons; //Makes things a little cleaner
    private JButton previousGUIButton;
    private JButton rentalUpdateButton;
    private JButton rentalDeleteButton;

    private JPanel rentalGUIPanel;
    private JPanel buttonPanel;
    private JPanel labelPanel;


    public RentalGUI(JFrame previousFrame, Rental rental) {
        this.previousGUI = previousFrame;
        this.rental = rental;

        setupLabels();
        setupButtons();
        setupPanels();

        this.add(rentalGUIPanel);
        this.pack(); //Packs all the things
        this.setVisible(true); //We kinda need to be able to see it
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Can close the GUI via close button in the frame
        this.setLocationRelativeTo(null); //Places the GUI at the center of the screen
    }

    /**
     * Reads the data from the Rental object and sets labels accordingly.
     */
    private void setupLabels() {
        rentalIDLabel  = new JLabel("Rental ID:    " + rental.getRentalID());
        userIDLabel    = new JLabel("User ID:      " + rental.getUserID());
        usernameLabel  = new JLabel("Username:     " + rental.getUsername());
        itemIDLabel    = new JLabel("Item ID:      " + rental.getItemID());
        itemTitleLabel = new JLabel("Item Title:   " + rental.getTitle());
        rentalDateLabel = new JLabel("Rental Date:  " + rental.getRentalDate());

        labels = new JLabel[]{rentalIDLabel, userIDLabel, usernameLabel, itemIDLabel, itemTitleLabel, rentalDateLabel};
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    private void setupButtons() {
        previousGUIButton = new JButton("PreviousGUI"); //Leads to previousGUI
        //Add actionListener
        rentalUpdateButton = new JButton("RentalUpdateGUI"); //Leads to RentalUpdateGUI
        //Add actionListener
        rentalDeleteButton = new JButton("RentalDeleteGUI"); //Leads to RentalDeleteGUI
        //Add actionListener
        buttons = new JButton[]{previousGUIButton, rentalUpdateButton, rentalDeleteButton};
    }

    /**
     * Sets up the panel.
     */
    private void setupPanels() {
        rentalGUIPanel = new JPanel(new BorderLayout());

        //We want the buttons to be ordered horizontally, in a row
        buttonPanel = new JPanel();

        //We want the labels to be ordered vertically, in a column
        labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        //Add buttons
        for (JButton button : buttons)
            buttonPanel.add(button);

        //Add labels
        for (JLabel label : labels) {
            label.setAlignmentX(Component.LEFT_ALIGNMENT); //Align labels to the left
            labelPanel.add(label);
        }

        rentalGUIPanel.add(labelPanel, BorderLayout.WEST); //Add labelPanel to the left
        rentalGUIPanel.add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the bottom
    }

    /**
     * Returns the previous GUI in the form of a JFrame.
     * @return the previous GUI.
     */
    public JFrame getPreviousFrame() {
        return previousGUI;
    }

    /**
     * Returns the Rental object this class is displaying.
     * @return the Rental object.
     */
    public Rental getRental() {
        return rental;
    }
}