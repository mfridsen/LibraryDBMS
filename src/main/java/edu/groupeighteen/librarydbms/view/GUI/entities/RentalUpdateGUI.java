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
public class RentalUpdateGUI extends JFrame {
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



    private JButton confirmUpdateButton;
    private JButton cancelButton;

    public RentalUpdateGUI(JFrame previousGUI, Rental rental) {
        this.previousGUI = previousGUI;
        this.rental = rental;
    }
}