package view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.GUI.entities.rental.RentalSearchGUI;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/15/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalSearchGUI class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalSearchGUITest {

    public static void main(String[] args) {
        LibraryManager.setup();
        try {
            RentalHandler.createNewRental(1, 1);
            RentalHandler.createNewRental(2, 2);
            RentalHandler.createNewRental(3, 3);
            RentalHandler.createNewRental(4, 4);
            RentalHandler.createNewRental(5, 5);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        new RentalSearchGUI(null);
    }
}