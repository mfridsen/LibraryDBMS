package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.util.Arrays;

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
 * <p>
 * TODO completely overhaul this class to be a OptionPane or whatever they're called instead
 */
public class RentalDeleteGUI extends GUI
{

    //TODO-comment
    private final Rental rentalToDelete;
    private JPasswordField passwordField;

    public RentalDeleteGUI(GUI previousGUI, Rental rentalToDelete)
    {
        super(previousGUI, "RentalDeleteGUI", rentalToDelete);
        this.rentalToDelete = rentalToDelete;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons()
    {
        JButton confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e ->
        {
            //TODO-prio you shouldn't be able to access this GUI at all without being logged in (and staff)
            if (LibraryManager.getCurrentUser() != null)
            {
                try
                {
                    if (UserHandler.validate(LibraryManager.getCurrentUser(),
                            Arrays.toString(passwordField.getPassword())))
                    {
                        /*try {
                            RentalHandler.deleteRental(rentalToDelete);
                            //dispose();
                            //TODO-prio return to some other GUI, probably the LoginGUI
                        } catch (SQLException sqle) {
                            sqle.printStackTrace();
                        }*/
                    }
                }
                catch (UserValidationException uve)
                {
                    uve.printStackTrace();
                }
            }
        });
        return new JButton[]{confirmButton};
    }

    @Override
    protected void setupPanels()
    {
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        GUIPanel.add(passwordPanel);
    }
}