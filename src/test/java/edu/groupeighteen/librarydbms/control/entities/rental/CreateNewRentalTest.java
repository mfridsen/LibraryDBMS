package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;
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
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the CreateNewRentalTest class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewRentalTest extends BaseRentalHandlerTest
{

    /**
     * Test case for createNewRental method with valid input.
     * <p>
     * For valid inputs (existing userID and itemID), the test should confirm:
     * <p>
     * The returned Rental is not null.
     * Each field of the returned Rental object matches expected value.
     * rentalDate is set to the current time (to the nearest second).
     * rentalDueDate is set to rentalDate plus allowed rental days at 20:00.
     * rentalReturnDate is null.
     * lateFee is 0.0.
     */
    @Test
    @Order(1)
    void testCreateNewRental_ValidInput()
    {
        System.out.println("\n1: Testing createNewRental method with valid input...");

        int validUserID = 3;
        int validItemID = 4;

        // Set up expected values based on the valid inputs
        String expectedUsername = "user3"; //PATRON
        String expectedTitle = "item4"; //OTHER_BOOKS, is allowed to rent out
        int expectedRentalID = 1; // Replace with the expected rental ID, if known
        int rentalDueDateDays = Item.getDefaultAllowedRentalDays(Item.ItemType.OTHER_BOOKS);
        LocalDateTime expectedDueDate = LocalDateTime.now().plusDays(rentalDueDateDays)
                .withHour(20).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        // Call the method under test
        Rental rental = null;
        try
        {
            rental = RentalHandler.createNewRental(validUserID, validItemID);
        }
        catch (Exception e)
        {
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
        try
        {
            rentedItem = ItemHandler.getItemByID(validItemID);
        }
        catch (Exception e)
        {
            fail("Unexpected exception thrown when retrieving rented item: " + e.getMessage());
        }
        assertNotNull(rentedItem, "Rented item should not be null");
        assertFalse(rentedItem.isAvailable(), "Rented item should not be available");

        // Verify that the User's current rentals count has been incremented
        User rentingUser = null;
        try
        {
            rentingUser = UserHandler.getUserByID(validUserID);
        }
        catch (Exception e)
        {
            fail("Unexpected exception thrown when retrieving renting user: " + e.getMessage());
        }
        assertNotNull(rentingUser, "Renting user should not be null");
        assertTrue(rentingUser.getCurrentRentals() > 0, "Renting user's current rentals count " +
                "should be greater than 0");

        // Verify that storedTitles still contains 1 count for "item1"
        int storedTitlesCount = ItemHandler.getStoredTitles().get(expectedTitle);
        assertEquals(1, storedTitlesCount, "Stored titles should still contain 1 count for " + expectedTitle);

        // Verify that availableTitles has 0 counts for "item1", but "item1" still exists in it
        int availableTitlesCount = ItemHandler.getAvailableTitles().get(expectedTitle);
        assertEquals(0, availableTitlesCount, "Available titles should have 0 counts for " + expectedTitle);
        assertTrue(ItemHandler.getAvailableTitles().containsKey(expectedTitle),
                "Available titles should still contain " + expectedTitle);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with an invalid userID.
     * <p>
     * This test attempts to create a new rental using an invalid user ID. The userID is invalid if it is
     * not a positive integer.
     * <p>
     * An InvalidIDException should be thrown with an appropriate error message.
     */
    @Test
    @Order(2)
    void testCreateNewRental_InvalidUserID()
    {
        System.out.println("\n2: Testing createNewRental method with invalid userID...");

        int invalidUserID = -1; // User IDs should be positive integers
        int validItemID = 1;

        Exception exception = assertThrows(InvalidIDException.class,
                () -> RentalHandler.createNewRental(invalidUserID, validItemID));

        String expectedMessage = "invalid userID";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with an invalid itemID.
     * <p>
     * This test attempts to create a new rental using an invalid item ID. The itemID is invalid if it is not a
     * positive integer.
     * <p>
     * An InvalidIDException should be thrown with an appropriate error message.
     */
    @Test
    @Order(3)
    void testCreateNewRental_InvalidItemID()
    {
        System.out.println("\n3: Testing createNewRental method with invalid itemID...");

        int invalidItemID = 0; // Item IDs should be positive integers
        int validUserID = 1;

        Exception exception = assertThrows(InvalidIDException.class,
                () -> RentalHandler.createNewRental(validUserID, invalidItemID));

        String expectedMessage = "invalid itemID";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with a nonexistent user.
     * <p>
     * This test attempts to create a new rental using a user ID that does not exist in the database.
     * A EntityNotFoundException should be thrown with an appropriate error message.
     */
    @Test
    @Order(4)
    void testCreateNewRental_NonexistentUser()
    {
        System.out.println("\n4: Testing createNewRental method with nonexistent user...");

        int nonexistentUserID = 9999; // This user ID does not exist in the database
        int validItemID = 1;

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> RentalHandler.createNewRental(nonexistentUserID, validItemID));

        String expectedMessage = "User with ID " + nonexistentUserID + " not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test the createNewRental method with a userID that doesn't exist in the database. This test should pass if
     * a EntityNotFoundException is thrown, as a rental cannot be created for a user that doesn't exist.
     */
    @Test
    @Order(5)
    void testCreateNewRental_SoftDeletedUser()
    {
        System.out.println("\n5: Testing createNewRental method with a user that doesn't exist...");

        int nonexistentUserID = 999; // assuming this ID does not exist in your database
        int validItemID = 1;

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> RentalHandler.createNewRental(nonexistentUserID, validItemID),
                "A EntityNotFoundException should be thrown when attempting to create a rental for a user that doesn't exist.");
        assertTrue(exception.getMessage().contains("User with ID " + nonexistentUserID + " not found."),
                "The exception message should indicate the nonexistence of the user.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with a nonexistent item.
     * <p>
     * This test attempts to create a new rental using an item ID that does not exist in the database.
     * An EntityNotFoundException should be thrown with an appropriate error message.
     */
    @Test
    @Order(6)
    void testCreateNewRental_NonexistentItem()
    {
        System.out.println("\n6: Testing createNewRental method with nonexistent item...");

        int validUserID = 3;
        int nonexistentItemID = 9999; // This item ID does not exist in the database

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> RentalHandler.createNewRental(validUserID, nonexistentItemID));

        String expectedMessage = "Item with ID " + nonexistentItemID + " not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test the createNewRental method with an itemID that doesn't exist in the database. This test should pass if
     * an EntityNotFoundException is thrown, as a rental cannot be created for an item that doesn't exist.
     */
    @Test
    @Order(7)
    void testCreateNewRental_SoftDeletedItem()
    {
        System.out.println("\n7: Testing createNewRental method with an item that doesn't exist...");

        int validUserID = 3;
        int nonexistentItemID = 999; // assuming this ID does not exist in your database

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> RentalHandler.createNewRental(validUserID, nonexistentItemID),
                "An EntityNotFoundException should be thrown when attempting to create a rental for an item that doesn't exist.");
        assertTrue(exception.getMessage().contains("Item with ID " + nonexistentItemID + " not found."),
                "The exception message should indicate the nonexistence of the item.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method when user tries to rent an item that's already rented out.
     */
    @Test
    @Order(8)
    void testCreateNewRental_ItemAlreadyRented()
    {
        System.out.println("\n8: Testing createNewRental method with an item that's already rented out...");

        try
        {
            int validUserID = 3;
            int validItemID = 4; //OTHER_BOOKS

            //Change item to be unavailable and update it
            Item unavailableItem = ItemHandler.getItemByID(validItemID);
            assertNotNull(unavailableItem);
            unavailableItem.setAvailable(false);
            ItemHandler.updateItem(unavailableItem);

            //Assert correct exception with correct message is thrown
            String title = unavailableItem.getTitle();
            Exception exception = assertThrows(EntityNotFoundException.class,
                    () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "Rental creation failed: No available copy of " + title + " found.";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

            //Rent item 2 (should be available)
            RentalHandler.createNewRental(validUserID, 6);

            //Assert correct exception with correct message is thrown when we attempt to rent item again
            exception = assertThrows(EntityNotFoundException.class, () -> RentalHandler.createNewRental(4, 6));
            expectedMessage = "Rental creation failed";
            actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
        catch (InvalidIDException | NullEntityException | RetrievalException | RentalNotAllowedException |
               EntityNotFoundException | InvalidTypeException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method when user tries to rent more items than allowed.
     */
    @Test
    @Order(9)
    void testCreateNewRental_MaxRentalsExceeded()
    {
        System.out.println("\n9: Testing createNewRental method with more rentals than allowed...");

        try
        {
            int validUserID = 3; //PATRON
            int validItemID = 3;

            RentalHandler.setVerbose(true);

            //Change users number of rentals to maximum allowed
            User maxRentalUser = UserHandler.getUserByID(validUserID);
            assertNotNull(maxRentalUser);
            maxRentalUser.setCurrentRentals(3);
            UserHandler.updateUser(maxRentalUser);

            //Tracer to find bug
            Item item3 = ItemHandler.getItemByID(validItemID);
            assertNotNull(item3);
            System.out.println("item3 available 1: " + item3.isAvailable());

            //Tracer to find second bug
            User user3 = UserHandler.getUserByID(validUserID);
            assertNotNull(user3);
            System.out.println("Username: " + user3.getUsername() +
                    ", allowedRentals: " + user3.getAllowedRentals() +
                    ", currentRentals: " + user3.getCurrentRentals() +
                    ", allowedToRent: " + user3.isAllowedToRent());

            //Assert correct exception with correct message is thrown
            Exception exception = assertThrows(RentalNotAllowedException.class,
                    () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "User not allowed to rent either due to already renting at maximum capacity " +
                    "or having a late fee.";
            String actualMessage = exception.getMessage();

            //Debug
            System.out.println(actualMessage);

            assertTrue(actualMessage.contains(expectedMessage));

            //Try with user 4 and items that are rentable
            assertDoesNotThrow(() -> RentalHandler.createNewRental(4, 6)); //STUDENT
            assertDoesNotThrow(() -> RentalHandler.createNewRental(4, 7));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(4, 8));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(4, 9));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(4, 10));

            //... where 11 should fail
            exception = assertThrows(RentalNotAllowedException.class,
                    () -> RentalHandler.createNewRental(4, 11)); //STUDENT
            expectedMessage = "User not allowed to rent either due to already renting at maximum capacity " +
                    "or having a late fee.";
            actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

            //Debug
            item3 = ItemHandler.getItemByID(3);
            assertNotNull(item3);
            System.out.println("item3 available 2: " + item3.isAvailable());
            //assertFalse(item3.isAvailable());

            RentalHandler.setVerbose(false);

        }
        catch (InvalidIDException | NullEntityException | RetrievalException | InvalidUserRentalsException |
               UpdateException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method when user tries to rent an item but they have unpaid late fees.
     */
    @Test
    @Order(10)
    void testCreateNewRental_UnpaidLateFees()
    {
        System.out.println("\n10: Testing createNewRental method when user has unpaid late fees...");

        try
        {
            int validUserID = 3;
            int validItemID = 3;

            //Get ourselves a poor little user object
            User lateFeeUser = UserHandler.getUserByID(validUserID);
            assertNotNull(lateFeeUser);

            //Assert user is allowed to rent before getting a late fee
            assertDoesNotThrow(() -> RentalHandler.createNewRental(validUserID, validItemID));

            //Set a positive late fee for user
            lateFeeUser.setLateFee(1);
            UserHandler.updateUser(lateFeeUser);

            //Assert correct exception with correct message is thrown
            Exception exception = assertThrows(RentalNotAllowedException.class,
                    () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "User not allowed to rent either due to already renting at " +
                    "maximum capacity or having a late fee.";
            String actualMessage = exception.getMessage();
            System.out.println(actualMessage);
            assertTrue(actualMessage.contains(expectedMessage));

        }
        catch (InvalidIDException | NullEntityException | InvalidLateFeeException | UpdateException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}