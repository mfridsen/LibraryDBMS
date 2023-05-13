package control;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit Test for the RentalHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalHandlerTest extends BaseHandlerTest {

    //TODO-future make all tests more verbose
    //TODO-future javadoc tests properly
    //TODO-prio change order of tests to match order of methods

    @BeforeEach
    @Override
    void setupAndReset() {
        super.setupAndReset();
        setupTestTablesAndData();
    }

    @Test
    @Order(1)
    void testSaveRental() {
        System.out.println("\n1: Testing saveRental method...");

        Rental rental = new Rental(1, 1, LocalDateTime.now());

        //Test valid saveRental
        int rentalID = 0;
        try {
            rentalID = RentalHandler.saveRental(rental);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        assertNotEquals(0, rentalID);

        //Test invalid saveRental
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(-1, 1, LocalDateTime.now())));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(1, -1, LocalDateTime.now())));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(1, 1, LocalDateTime.now().plusDays(1))));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(1, 1, null)));

        System.out.println("Test finished.");
    }

    @Test
    @Order(2)
    void testCreateNewRental() {
        System.out.println("\n2: Testing createNewRental method...");

        //Test valid createNewRental
        Rental rental = null;
        try {
            rental = RentalHandler.createNewRental(1, 1, LocalDateTime.now());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        assertNotNull(rental);

        //Test invalid createNewRental
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(-1, 1, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(1, -1, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(1, 1, LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(1, 1, null));

        System.out.println("Test finished.");
    }

    @Test
    @Order(3)
    void testGetAllRentals() {
        System.out.println("\n3: Testing getAllRentals method...");
        // Test the retrieval of all Rentals from the database
        System.out.println("Test finished.");
    }

    @Test
    @Order(4)
    void testGetRentalByID() {
        System.out.println("\n4: Testing getRentalByID method...");
        // Test the retrieval of a specific Rental by its rentalID
        System.out.println("Test finished.");
    }

    @Test
    @Order(5)
    void testGetRentalsByRentalDate() {
        System.out.println("\n5: Testing getRentalsByRentalDate method...");
        // Test the retrieval of Rentals by their rentalDate
        System.out.println("Test finished.");
    }

    @Test
    @Order(6)
    void testGetRentalsByRentalDay() {
        System.out.println("\n6: Testing getRentalsByRentalDay method...");
        // Test the retrieval of Rentals by their rentalDate
        System.out.println("Test finished.");
    }

    @Test
    @Order(7)
    void testGetRentalsByUserID() {
        System.out.println("\n7: Testing getRentalsByUserID method...");
        // Test the retrieval of Rentals by their userID
        System.out.println("Test finished.");
    }

    @Test
    @Order(8)
    void testGetRentalsByItemID() {
        System.out.println("\n8: Testing getRentalsByItemID method...");
        // Test the retrieval of Rentals by their itemID
        System.out.println("Test finished.");
    }

    @Test
    @Order(9)
    void testUpdateRental() {
        System.out.println("\n9: Testing updateRental method...");
        // Test the updating of a specific Rental in the database
        System.out.println("Test finished.");
    }

    @Test
    @Order(10)
    void testDeleteRental() {
        System.out.println("\n10: Testing deleteRental method...");
        // Test the deletion of a specific Rental from the database
        System.out.println("Test finished.");
    }
}