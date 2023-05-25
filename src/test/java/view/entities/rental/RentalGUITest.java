package view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.ItemNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserNotFoundException;
import edu.groupeighteen.librarydbms.view.entities.rental.RentalGUI;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/14/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalGUI class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
public class RentalGUITest {

    public static void main(String[] args) {
        //Need to setup everything before GUI
        LibraryManager.setup();

        try {
            new RentalGUI(null, RentalHandler.createNewRental(1, 1));
        } catch (ItemNotFoundException | UserNotFoundException | RentalNotAllowedException | InvalidIDException e) {
            e.printStackTrace();
        }
    }
}