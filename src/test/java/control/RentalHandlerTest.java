
package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
    //TODO-prio change order of tests to match order of methods


    @Override
    @BeforeEach
    void setupAndReset() {
        super.setupAndReset();
        ItemHandler.setup();
        UserHandler.setup();
    }

    /**
     * Test case for createNewRental method with valid input.
     *
     * For valid inputs (existing userID and itemID), the test should confirm:
     * 
     *     The returned Rental is not null.
     *     Each field of the returned Rental object matches expected value.
     *     rentalDate is set to the current time (to the nearest second).
     *     rentalDueDate is set to rentalDate plus allowed rental days at 20:00.
     *     rentalReturnDate is null.
     *     lateFee is 0.0.
     */
    @Test
    @Order(1)
    void testCreateNewRental_ValidInput() {
        System.out.println("\n1: Testing createNewRental method with valid input...");

        int validUserID = 1;
        int validItemID = 1;

        // Set up expected values based on the valid inputs
        String expectedUsername = "user1";
        String expectedTitle = "item1";
        int expectedRentalID = 1; // Replace with the expected rental ID, if known
        LocalDateTime expectedDueDate = LocalDateTime.now().plusDays(14)
                .withHour(20).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        // Call the method under test
        Rental rental = null;
        try {
            rental = RentalHandler.createNewRental(validUserID, validItemID);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.getCause().printStackTrace();
        }

        // Verify that the resulting rental matches the expected values
        assertNotNull(rental, "Rental should not be null");
        assertEquals(expectedRentalID, rental.getRentalID(), "Rental ID should match expected value");
        assertEquals(expectedUsername, rental.getUsername(), "Username should match expected value");
        assertEquals(expectedTitle, rental.getItemTitle(), "Title should match expected value");
        assertEquals(expectedDueDate, rental.getRentalDueDate(), "Due date should match expected value");

        // Verify that the Item status has been updated to not available
        Item rentedItem = null;
        try {
            rentedItem = ItemHandler.getItemByID(validItemID);
        } catch (Exception e) {
            fail("Unexpected exception thrown when retrieving rented item: " + e.getMessage());
        }
        assertNotNull(rentedItem, "Rented item should not be null");
        assertFalse(rentedItem.isAvailable(), "Rented item should not be available");

        // Verify that the User's current rentals count has been incremented
        User rentingUser = null;
        try {
            rentingUser = UserHandler.getUserByID(validUserID);
        } catch (Exception e) {
            fail("Unexpected exception thrown when retrieving renting user: " + e.getMessage());
        }
        assertNotNull(rentingUser, "Renting user should not be null");
        assertTrue(rentingUser.getCurrentRentals() > 0, "Renting user's current rentals count should be greater than 0");

        System.out.println("\nTEST FINISHED.");
    }


    /**
     * Test case for createNewRental method with an invalid userID.
     *
     * This test attempts to create a new rental using an invalid user ID. The userID is invalid if it is not a positive integer.
     * An InvalidIDException should be thrown with an appropriate error message.
     */
    @Test
    @Order(2)
    void testCreateNewRental_InvalidUserID() {
        System.out.println("\n2: Testing createNewRental method with invalid userID...");

        int invalidUserID = -1; // User IDs should be positive integers
        int validItemID = 1;

        Exception exception = assertThrows(InvalidIDException.class, () -> RentalHandler.createNewRental(invalidUserID, validItemID));

        String expectedMessage = "invalid userID";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with an invalid itemID.
     *
     * This test attempts to create a new rental using an invalid item ID. The itemID is invalid if it is not a positive integer.
     * An InvalidIDException should be thrown with an appropriate error message.
     */
    @Test
    @Order(3)
    void testCreateNewRental_InvalidItemID() {
        System.out.println("\n3: Testing createNewRental method with invalid itemID...");

        int invalidItemID = 0; // Item IDs should be positive integers
        int validUserID = 1;

        Exception exception = assertThrows(InvalidIDException.class, () -> RentalHandler.createNewRental(validUserID, invalidItemID));

        String expectedMessage = "invalid itemID";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with a nonexistent user.
     *
     * This test attempts to create a new rental using a user ID that does not exist in the database.
     * A UserNotFoundException should be thrown with an appropriate error message.
     */
    @Test
    @Order(4)
    void testCreateNewRental_NonexistentUser() {
        System.out.println("\n4: Testing createNewRental method with nonexistent user...");

        int nonexistentUserID = 9999; // This user ID does not exist in the database
        int validItemID = 1;

        Exception exception = assertThrows(UserNotFoundException.class, () -> RentalHandler.createNewRental(nonexistentUserID, validItemID));

        String expectedMessage = "User with ID " + nonexistentUserID + " not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with a nonexistent item.
     *
     * This test attempts to create a new rental using an item ID that does not exist in the database.
     * An ItemNotFoundException should be thrown with an appropriate error message.
     */
    @Test
    @Order(5)
    void testCreateNewRental_NonexistentItem() {
        System.out.println("\n5: Testing createNewRental method with nonexistent item...");

        int validUserID = 1;
        int nonexistentItemID = 9999; // This item ID does not exist in the database

        Exception exception = assertThrows(ItemNotFoundException.class, () -> RentalHandler.createNewRental(validUserID, nonexistentItemID));

        String expectedMessage = "Item with ID " + nonexistentItemID + " not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method when user tries to rent an item that's already rented out.
     */
    @Test
    @Order(6)
    void testCreateNewRental_ItemAlreadyRented() {
        System.out.println("\n6: Testing createNewRental method with an item that's already rented out...");

        try {
            int validUserID = 1;
            int validItemID = 1;

            //Change item to be unavailable and update it
            Item unavailableItem = ItemHandler.getItemByID(validItemID);
            assertNotNull(unavailableItem);
            unavailableItem.setAvailable(false);
            ItemHandler.updateItem(unavailableItem);

            //Assert correct exception with correct message is thrown
            String title = unavailableItem.getTitle();
            Exception exception = assertThrows(ItemNotFoundException.class, () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "Rental creation failed: No available copy of " + title + " found.";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

        } catch (InvalidIDException | ItemNullException | ItemNotFoundException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method when user tries to rent more items than allowed.
     */
    @Test
    @Order(7)
    void testCreateNewRental_MaxRentalsExceeded() {
        System.out.println("\n7: Testing createNewRental method with more rentals than allowed...");

        try {
            int validUserID = 1;
            int validItemID = 1;

            //Change users number of rentals to maximum allowed
            User maxRentalUser = UserHandler.getUserByID(validUserID);
            assertNotNull(maxRentalUser);
            maxRentalUser.setCurrentRentals(User.DEFAULT_ALLOWED_RENTALS);
            UserHandler.updateUser(maxRentalUser);

            //Assert correct exception with correct message is thrown
            Exception exception = assertThrows(RentalNotAllowedException.class, () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "User not allowed to rent due to already renting to capacity.";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

        } catch (InvalidIDException | InvalidRentalException | UserNullException | InvalidUsernameException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method when user tries to rent an item but they have unpaid late fees.
     */
    @Test
    @Order(8)
    void testCreateNewRental_UnpaidLateFees() {
        System.out.println("\n8: Testing createNewRental method when user has unpaid late fees...");

        try {
            int validUserID = 1;
            int validItemID = 1;

            //Set a positive late fee for user
            User lateFeeUser = UserHandler.getUserByID(validUserID);
            assertNotNull(lateFeeUser);
            lateFeeUser.setLateFee(1);
            UserHandler.updateUser(lateFeeUser);

            //Assert correct exception with correct message is thrown
            Exception exception = assertThrows(RentalNotAllowedException.class, () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "User not allowed to rent due to having a late fee.";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

        } catch (InvalidIDException | UserNullException | InvalidUsernameException | InvalidLateFeeException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the getAllRentals method with an empty database.
     *
     * This test verifies that the getAllRentals method correctly returns an empty list when there are no rentals
     * in the database.
     *
     * It clears the rentals table in the database, calls the getAllRentals method, and compares the expected empty
     * list with the actual result.
     *
     * If the actual result matches the expected empty list, the test passes.
     */
    @Test
    @Order(9)
    void testGetAllRentals_EmptyRentalsTable() throws UserNotFoundException, InvalidIDException {
        System.out.println("\n9: Testing getAllRentals method with an empty database...");

        // Clear the rentals table in the database
        DatabaseHandler.executeCommand("DELETE FROM Rentals");

        List<Rental> expectedRentals = Collections.emptyList();
        List<Rental> actualRentals;
        actualRentals = RentalHandler.getAllRentals();
        assertEquals(expectedRentals, actualRentals);

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testGetAllRentals_PopulatedRentalsTable() {
        System.out.println("\n10: Testing getAllRentals method with some rentals in the database...");

        try {
            //Create some rentals
            RentalHandler.createNewRental(1, 1);
            RentalHandler.createNewRental(2, 2);
            RentalHandler.createNewRental(3, 3);
            RentalHandler.createNewRental(4, 4);
            RentalHandler.createNewRental(5, 5);

            //Retrieve all rentals
            List<Rental> rentals = RentalHandler.getAllRentals();

            //Check if the number of rentals retrieved matches the number of rentals created
            assertNotNull(rentals, "The retrieved rentals list should not be null.");
            assertEquals(5, rentals.size(), "The number of retrieved rentals does not match the number of created rentals.");
        } catch (UserNotFoundException | ItemNotFoundException | RentalNotAllowedException | InvalidIDException e) {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testGetAllRentals_NonexistentUserOrItem() {
        System.out.println("\n9: Testing getAllRentals method with a non-existent user or item...");

        // Your test code here...

        System.out.println("\nTEST FINISHED.");
    }



}
