package view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

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
        /*try {
            RentalHandler.createNewRental(1, 1);
            RentalHandler.createNewRental(2, 2);
            RentalHandler.createNewRental(3, 3);
            RentalHandler.createNewRental(4, 4);
            RentalHandler.createNewRental(5, 5);
        } catch (SQLException | UserNotFoundException sqle) {
            sqle.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
        new RentalSearchGUI(null);

         */
    }
}