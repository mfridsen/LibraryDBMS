package control;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    //TODO-comment
    @Test
    @Order(1)
    void testSaveRental() {
        System.out.println("\n1: Testing saveRental method...");

        Rental testRental = new Rental(1, 1, LocalDateTime.now());

        //Test valid saveRental
        int rentalID = 0;
        try {
            rentalID = RentalHandler.saveRental(testRental);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        assertTrue(rentalID > 0, "Saving a valid rental should return a valid rental ID.");

        //Test invalid saveRental
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(-1, 1, LocalDateTime.now())));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(1, -1, LocalDateTime.now())));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(1, 1, LocalDateTime.now().plusDays(1))));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.saveRental(new Rental(1, 1, null)));

        System.out.println("Test finished.");
    }

    //TODO-comment
    @Test
    @Order(2)
    void testCreateNewRental() {
        System.out.println("\n2: Testing createNewRental method...");
        int userID = 1;
        int itemID = 1;
        LocalDateTime rentalDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); //Need to round before test
        Rental testRental = null;

        //Test valid createNewRental
        try {
            testRental = RentalHandler.createNewRental(userID, itemID, rentalDate);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        assertNotNull(testRental, "Creating a valid rental should return a valid rental object.");
        assertTrue(testRental.getRentalID() > 0, "The new rental should have a valid rental ID.");
        assertEquals(userID, testRental.getUserID(), "The new rental's userID should match the provided userID.");
        assertEquals(itemID, testRental.getItemID(), "The new rental's itemID should match the provided itemID.");
        assertEquals(rentalDate, testRental.getRentalDate(), "The new rental's rentalDate should match the provided rentalDate.");

        //Test invalid createNewRental
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(-1, 1, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(1, -1, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(1, 1, LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.createNewRental(1, 1, null));

        System.out.println("Test finished.");
    }

    //TODO-comment
    @Test
    @Order(3)
    void testGetAllRentals() {
        System.out.println("\n3: Testing getAllRentals method...");

        //TODO-prio implement

        System.out.println("Test finished.");
    }

    /**
     * This test creates a new rental at the start, and then checks that the getRentalByID method returns a Rental
     * object with the same details as the created rental. It also checks that an IllegalArgumentException is thrown
     * when trying to retrieve a rental with an invalid ID.
     */
    @Test
    @Order(4)
    void testGetRentalByID() {
        System.out.println("\n4: Testing getRentalByID method...");

        //Setup: create a new rental
        Rental rental = null;
        try {
            rental = RentalHandler.createNewRental(1, 1, LocalDateTime.now());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during setup: " + e.getMessage());
        }

        //Test valid getRentalByID
        Rental retrievedRental = null;
        try {
            retrievedRental = RentalHandler.getRentalByID(rental.getRentalID());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        assertNotNull(retrievedRental);
        assertEquals(rental.getRentalID(), retrievedRental.getRentalID());
        assertEquals(rental.getUserID(), retrievedRental.getUserID());
        assertEquals(rental.getItemID(), retrievedRental.getItemID());
        assertEquals(rental.getRentalDate(), retrievedRental.getRentalDate());

        //Test invalid getRentalByID
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalByID(-1));

        System.out.println("Test finished.");
    }

    /**
     * This test method first creates a few new rentals, two with the current date and time and one with a future date.
     * Then it tests the getRentalsByRentalDate method by asking for rentals from the current date, expecting to find
     * the two rentals it just created. It also checks for rentals from a date in the past, where it expects to find none.
     * Finally, it tests the method with invalid inputs, expecting it to throw an IllegalArgumentException.
     */
    @Test
    @Order(5)
    void testGetRentalsByRentalDate() {
        System.out.println("\n5: Testing getRentalsByRentalDate method...");

        LocalDateTime testDate1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime testDate2 = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime testDate3 = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime testDate4 = LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS);

        // Create new rentals to test with
        try {
            Rental rental1 = RentalHandler.createNewRental(1, 1, testDate1);
            Rental rental2 = RentalHandler.createNewRental(2, 2, testDate1);
            Rental rental3 = RentalHandler.createNewRental(3, 3, testDate3); // This rental has a different date

            // Test valid getRentalsByRentalDate
            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(rental1.getRentalDate());
            assertNotNull(rentals);
            assertEquals(2, rentals.size()); // There should be two rentals with the same date

            // Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertFalse(rentals.contains(rental3));

            // Test with a rentalDate that no rentals have
            rentals = RentalHandler.getRentalsByRentalDate(testDate4);
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        // Test invalid getRentalsByRentalDate
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByRentalDate(testDate2));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByRentalDate(null));

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
    void testGetRentalsByTimePeriod() {
        System.out.println("\n7: Testing getRentalsByTimePeriod method...");
        // Test the retrieval of Rentals by their rentalDate
        System.out.println("Test finished.");
    }

    @Test
    @Order(8)
    void testGetRentalsByUserID() {
        System.out.println("\n8: Testing getRentalsByUserID method...");
        // Test the retrieval of Rentals by their userID
        System.out.println("Test finished.");
    }

    @Test
    @Order(9)
    void testGetRentalsByItemID() {
        System.out.println("\n9: Testing getRentalsByItemID method...");
        // Test the retrieval of Rentals by their itemID
        System.out.println("Test finished.");
    }

    @Test
    @Order(10)
    void testUpdateRental() {
        System.out.println("\n10: Testing updateRental method...");
        // Test the updating of a specific Rental in the database
        System.out.println("Test finished.");
    }

    @Test
    @Order(11)
    void testDeleteRental() {
        System.out.println("\n11: Testing deleteRental method...");
        // Test the deletion of a specific Rental from the database
        System.out.println("Test finished.");
    }
}