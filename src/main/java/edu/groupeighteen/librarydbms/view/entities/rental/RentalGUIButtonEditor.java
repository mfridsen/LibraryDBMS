package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.buttons.EntityButtonEditor;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * This class is used to define the behaviour of the "View Rental" button in a JTable cell.
 * It extends the DefaultCellEditor class, and overrides necessary methods to provide the required functionality.
 * The editor will be used in the table displaying the list of rentals in the application.
 */
public class RentalGUIButtonEditor extends EntityButtonEditor
{
    /**
     * Constructs a RentalGUIButtonEditor object.
     * Sets up the action listener for the button and configures its visual settings.
     *
     * @param checkBox    a JCheckBox object which is sent to the parent constructor.
     * @param rental      a Rental object that this button corresponds to.
     * @param previousGUI the previous GUI screen.
     */
    public RentalGUIButtonEditor(JCheckBox checkBox, Rental rental, String label, GUI previousGUI)
    {
        super(checkBox, rental, label, previousGUI);
    }

    /**
     * Returns the current editing value from the cell.
     * If the button has been clicked (isPushed is true), it creates a new RentalGUI.
     *
     * @return the current editing value from the cell.
     */
    @Override
    public Object getCellEditorValue()
    {
        if (isPushed)
        {
            new RentalGUI(previousGUI, (Rental) entity);
        }
        isPushed = false;
        return label; // Return a new String so the original is not affected
    }
}