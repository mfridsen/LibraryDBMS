package edu.groupeighteen.librarydbms.view.GUI.entities;

import edu.groupeighteen.librarydbms.model.entities.Rental;

import javax.swing.*;

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
public class RentalGUI {
    private final Rental rental;

    private JLabel rentalIDLabel;
    private JLabel userIDLabel;
    private JLabel usernameLabel;
    private JLabel itemIDLabel;
    private JLabel itemTitleLabel;
    private JLabel rentalDateLabel;

    private JButton backButton;
    private JButton updateButton;
    private JButton deleteButton;

    private JPanel RentalGUIPanel;
    private JFrame RentalGUIFrame;

    public RentalGUI(Rental rental) {
        this.rental = rental;
        this.rentalIDLabel  = new JLabel("Rental ID:    " + rental.getRentalID());
        this.userIDLabel    = new JLabel("User ID:      " + rental.getUserID());
        this.usernameLabel  = new JLabel("Username:     " + rental.getUsername());
        this.itemIDLabel    = new JLabel("Item ID:      " + rental.getItemID());
        this.itemTitleLabel = new JLabel("Item Title:   " + rental.getTitle());
        this.rentalDateLabel = new JLabel("Rental Date:  " + rental.getRentalDate());
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public Rental getRental() {
        return rental;
    }
}