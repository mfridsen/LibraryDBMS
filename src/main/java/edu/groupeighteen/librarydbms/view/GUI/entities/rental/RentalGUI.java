package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

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
public class RentalGUI extends GUI {
    private final Rental rental;

    private JLabel[] labels;
    private JLabel rentalIDLabel;
    private JLabel userIDLabel;
    private JLabel usernameLabel;
    private JLabel itemIDLabel;
    private JLabel itemTitleLabel;
    private JLabel rentalDateLabel;

    private JScrollPane rentalScrollPane;

    private JButton previousGUIButton;
    private JButton rentalUpdateButton;
    private JButton rentalDeleteButton;

    private JPanel scrollPanePanel;

    public RentalGUI(GUI previousGUI, Rental rental) {
        super(previousGUI, "RentalGUI");
        this.rental = rental;

        //setupLabels();

        setupButtons();
        addButtonsToPanel(new JButton[]{previousGUIButton, rentalUpdateButton, rentalDeleteButton});
        setupScrollPane();
        setupPanels();

        this.displayGUI();
    }

    /**
     * Sets up the buttons in this class and adds ActionListeners to them, implementing their actionPerformed methods.
     */
    private void setupButtons() {
        //Leads to previousGUI
        previousGUIButton = new JButton("PreviousGUI");
        //Add actionListener
        previousGUIButton.addActionListener(e -> {
            if (previousGUI == null) {
                System.err.println("No previous GUI to return to!");
            } else {
                dispose();
                previousGUI.displayGUI();
            }
        });

        //Leads to RentalUpdateGUI
        rentalUpdateButton = new JButton("RentalUpdateGUI");
        //Add actionListener
        rentalUpdateButton.addActionListener(e -> {
            dispose();
            new RentalUpdateGUI(this, rental);
        });

        //Leads to RentalDeleteGUI
        rentalDeleteButton = new JButton("RentalDeleteGUI");
        //Add actionListener
        rentalDeleteButton.addActionListener(e -> {
            dispose();
            new RentalDeleteGUI(this, rental);
        });
    }

    /**
     * Sets up the scroll pane.
     */
    private void setupScrollPane() {
        //Define column names
        String[] columnNames = {"Property", "Value"};

        //Gather data
        Object[][] data = {
                {"Rental ID", rental.getRentalID()},
                {"User ID", rental.getUserID()},
                {"Username", rental.getUsername()},
                {"Item ID", rental.getItemID()},
                {"Item Title", rental.getTitle()},
                {"Rental Date", rental.getRentalDate()}
        };

        rentalScrollPane = setupTableScrollPane(columnNames, data);
    }

    /**
     * Sets up the main panel.
     */
    private void setupPanels() {
        GUIPanel = new JPanel(new BorderLayout()); //To achieve the preferred layout, BorderLayout is needed
        scrollPanePanel = new JPanel();
        scrollPanePanel.add(rentalScrollPane, BorderLayout.CENTER);
        GUIPanel.add(scrollPanePanel, BorderLayout.NORTH); //Add scrollPanePanel to the top
        GUIPanel.add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the bottom
    }

    /**
     * Returns the Rental object this class is displaying.
     * @return the Rental object.
     */
    public Rental getRental() {
        return rental;
    }
}