package model.entities;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the Rental class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalTest {

    /**
     * This test case validates the behavior of the Rental constructor for creation with valid input.
     * It checks whether the rental ID, user ID, item ID, and rental date are correctly set.
     * It also checks whether the username, item title, rental due date, rental return date, and late fee are initialized as expected.
     */
    @Test
    @Order(1)
    void testRentalConstructor_ForCreation_ValidInput() {
        System.out.println("\n1: Testing Rental constructor for creation with valid input...");

        // Inputs for the constructor
        int userID = 1;
        int itemID = 1;

        // Call the constructor
        Rental rental = new Rental(userID, itemID);

        // Check the values of the object
        assertEquals(0, rental.getRentalID(), "Rental ID should be 0 for a newly created rental.");
        assertEquals(userID, rental.getUserID(), "User ID should match the input.");
        assertEquals(itemID, rental.getItemID(), "Item ID should match the input.");

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(now, rental.getRentalDate(), "Rental date should be approximately the current time.");

        assertNull(rental.getUsername(), "Username should be null for a newly created rental.");
        assertNull(rental.getItemTitle(), "Item title should be null for a newly created rental.");
        assertNull(rental.getRentalDueDate(), "Rental due date should be null for a newly created rental.");
        assertNull(rental.getRentalReturnDate(), "Rental return date should be null for a newly created rental.");
        assertEquals(0.0, rental.getLateFee(), "Late fee should be 0.0 for a newly created rental.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test case validates the behavior of the Rental constructor for creation with invalid input.
     * It checks whether the constructor correctly throws an exception when the user ID or item ID are not valid.
     */
    @Test
    @Order(2)
    void testRentalConstructor_ForCreation_InvalidInput() {
        System.out.println("\n2: Testing Rental constructor for creation with invalid input...");

        // Inputs for the constructor
        int invalidUserID = 0;
        int invalidItemID = -1;

        // Test invalid user ID
        assertThrows(IllegalArgumentException.class, () -> new Rental(invalidUserID, 1),
                "Constructor should throw an IllegalArgumentException when the user ID is not valid.");

        // Test invalid item ID
        assertThrows(IllegalArgumentException.class, () -> new Rental(1, invalidItemID),
                "Constructor should throw an IllegalArgumentException when the item ID is not valid.");

        System.out.println("\nTEST FINISHED.");
    }


    /**
     * This test case validates the behavior of the Rental constructor when provided with valid data from the database.
     * It checks whether all fields of the Rental object are correctly initialized based on the input parameters.
     */
    @Test
    @Order(3)
    void testRentalConstructor_FromDatabase_ValidInput() {
        System.out.println("\n3: Testing Rental constructor with data retrieved from the database...");

        // Inputs for the constructor
        int rentalID = 1;
        int userID = 1;
        int itemID = 1;
        LocalDateTime rentalDate = LocalDateTime.now().minusDays(5).truncatedTo(ChronoUnit.SECONDS);
        String username = "TestUser";
        String itemTitle = "TestItem";
        LocalDateTime rentalDueDate = LocalDateTime.now().plusDays(5).withHour(16).withMinute(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime rentalReturnDate = null;
        double lateFee = 0.0;

        Rental rental = new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee);

        // Test all fields
        assertEquals(rentalID, rental.getRentalID(), "RentalID not set correctly.");
        assertEquals(userID, rental.getUserID(), "UserID not set correctly.");
        assertEquals(itemID, rental.getItemID(), "ItemID not set correctly.");
        assertEquals(rentalDate, rental.getRentalDate(), "RentalDate not set correctly.");
        assertEquals(username, rental.getUsername(), "Username not set correctly.");
        assertEquals(itemTitle, rental.getItemTitle(), "ItemTitle not set correctly.");
        assertEquals(rentalDueDate, rental.getRentalDueDate(), "RentalDueDate not set correctly.");
        assertNull(rental.getRentalReturnDate(), "RentalReturnDate should be null.");
        assertEquals(lateFee, rental.getLateFee(), "LateFee not set correctly.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test case validates the behavior of the Rental constructor when provided with invalid data from the database.
     * It checks whether appropriate exceptions are thrown for each type of invalid input.
     */
    @Test
    @Order(4)
    void testRentalConstructor_FromDatabase_InvalidInput() {
        System.out.println("\n4: Testing Rental constructor with data retrieved from the database and invalid input...");

        // Invalid inputs for the constructor
        int rentalID = -1;
        int userID = -1;
        int itemID = -1;
        LocalDateTime rentalDate = LocalDateTime.now().plusDays(1); // In future
        String username = "";
        String itemTitle = "";
        LocalDateTime rentalDueDate = LocalDateTime.now().minusDays(1); // In past
        LocalDateTime rentalReturnDate = LocalDateTime.now().minusDays(10); // Before rentalDate
        double lateFee = -0.01; // Negative fee

        // Testing invalid rentalID
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when rentalID was invalid.");

        // Testing invalid userID
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when userID was invalid.");

        // Testing invalid itemID
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when itemID was invalid.");

        // Testing invalid rentalDate
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when rentalDate was invalid.");

        // Testing invalid username
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when username was invalid.");

        // Testing invalid itemTitle
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when itemTitle was invalid.");

        // Testing invalid rentalDueDate
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when rentalDueDate was invalid.");

        // Testing invalid rentalReturnDate
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when rentalReturnDate was invalid.");

        // Testing invalid lateFee
        assertThrows(IllegalArgumentException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, lateFee), "Rental constructor did not throw exception when lateFee was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test method verifies the correct functionality of the copy constructor of the Rental class.
     * The copy constructor should create a new Rental instance with the same property values as the original.
     * We test it by creating a new Rental object, then using the copy constructor to make a copy,
     * and finally verifying that all properties in the copied Rental are identical to the original.
     */
    @Test
    @Order(5)
    void testRentalConstructor_CopyRental() {
        System.out.println("\n5: Testing Rental copy constructor...");

        // Create a rental object
        LocalDateTime now = LocalDateTime.now();
        Rental originalRental = new Rental(1, 2, 3, now, "username", "itemTitle", now.plusDays(7), null, 0.0);

        // Use the copy constructor
        Rental copyRental = new Rental(originalRental);

        // Check that all fields are identical
        assertEquals(originalRental.getRentalID(), copyRental.getRentalID());
        assertEquals(originalRental.getUserID(), copyRental.getUserID());
        assertEquals(originalRental.getItemID(), copyRental.getItemID());
        assertEquals(originalRental.getRentalDate(), copyRental.getRentalDate());
        assertEquals(originalRental.getUsername(), copyRental.getUsername());
        assertEquals(originalRental.getItemTitle(), copyRental.getItemTitle());
        assertEquals(originalRental.getRentalDueDate(), copyRental.getRentalDueDate());
        assertEquals(originalRental.getRentalReturnDate(), copyRental.getRentalReturnDate());
        assertEquals(originalRental.getLateFee(), copyRental.getLateFee());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    void testSetRentalID() {
        System.out.println("\n6: Testing setRentalID...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalID(-1));
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalID(0));
        rental.setRentalID(1);
        assertEquals(1, rental.getRentalID());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testSetUserID() {
        System.out.println("\n7: Testing setUserID...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setUserID(-1));
        assertThrows(IllegalArgumentException.class, () -> rental.setUserID(0));
        rental.setUserID(1);
        assertEquals(1, rental.getUserID());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetItemID() {
        System.out.println("\n8: Testing setItemID...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setItemID(-1));
        assertThrows(IllegalArgumentException.class, () -> rental.setItemID(0));
        rental.setItemID(1);
        assertEquals(1, rental.getItemID());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetRentalDate() {
        System.out.println("\n9: Testing setRentalDate...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalDate(null));
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalDate(LocalDateTime.now().plusSeconds(1)));
        rental.setRentalDate(LocalDateTime.now());
        // Assuming your test completes within a second, this should pass.
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), rental.getRentalDate());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testSetUsername() {
        System.out.println("\n10: Testing setUsername...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setUsername(null));
        assertThrows(IllegalArgumentException.class, () -> rental.setUsername(""));
        rental.setUsername("testUser");
        assertEquals("testUser", rental.getUsername());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testSetItemTitle() {
        System.out.println("\n11: Testing setItemTitle...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setItemTitle(null));
        assertThrows(IllegalArgumentException.class, () -> rental.setItemTitle(""));
        rental.setItemTitle("testTitle");
        assertEquals("testTitle", rental.getItemTitle());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testSetRentalDueDate() {
        System.out.println("\n12: Testing setRentalDueDate...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalDueDate(null));
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalDueDate(LocalDateTime.now().minusSeconds(1)));
        rental.setRentalDueDate(LocalDateTime.now().plusDays(1));
        assertEquals(LocalDateTime.now().plusDays(1).withHour(16).withMinute(0).truncatedTo(ChronoUnit.SECONDS), rental.getRentalDueDate());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testSetRentalReturnDate() {
        System.out.println("\n13: Testing setRentalReturnDate...");
        Rental rental = new Rental(1, 1);
        rental.setRentalDate(LocalDateTime.now().minusDays(1)); // Set RentalDate to make RentalReturnDate setting possible
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalReturnDate(LocalDateTime.now().minusDays(2))); // Return date before RentalDate
        rental.setRentalReturnDate(LocalDateTime.now());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), rental.getRentalReturnDate());
        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testSetLateFee() {
        System.out.println("\n14: Testing setLateFee...");
        Rental rental = new Rental(1, 1);
        assertThrows(IllegalArgumentException.class, () -> rental.setLateFee(-0.01));
        rental.setLateFee(0.0);
        assertEquals(0.0, rental.getLateFee());
        rental.setLateFee(1.0);
        assertEquals(1.0, rental.getLateFee());
        System.out.println("\nTEST FINISHED.");
    }
}