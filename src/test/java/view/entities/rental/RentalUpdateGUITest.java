package view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.view.entities.rental.RentalUpdateGUI;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/15/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalUpdateGUI class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
public class RentalUpdateGUITest {

    public static void main(String[] args) {
        LibraryManager.setup();
        /*
        try {
            new RentalUpdateGUI(null, RentalHandler.createNewRental(1, 1, LocalDateTime.now()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

         */
    }
}