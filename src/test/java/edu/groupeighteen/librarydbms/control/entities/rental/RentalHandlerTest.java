
package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
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
import org.junit.jupiter.api.*;

import java.time.LocalDate;
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
 * <p>
 * Unit Test for the RentalHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalHandlerTest extends BaseHandlerTest
{

    //TODO-future make all tests more verbose
    //TODO-prio change order of tests to match order of methods

    //TODO-PRIO IMPLEMENT TESTS FOR CREATING A RENTAL WITH DELETED USER AND ITEM

    @Override
    @BeforeEach
    protected void setupAndReset()
    {
        super.setupAndReset();
        ItemHandler.setup(); //Fills maps with items
        UserHandler.setup(); //Fills list with users
        DatabaseHandler.setVerbose(false); //Get that thing to shut up
    }

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

        int validUserID = 1;
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

        int validUserID = 1;
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
            int validUserID = 1;
            int validItemID = 1;

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
            RentalHandler.createNewRental(validUserID, 2);

            //Assert correct exception with correct message is thrown when we attempt to rent item again
            exception = assertThrows(EntityNotFoundException.class, () -> RentalHandler.createNewRental(2, 2));
            expectedMessage = "Rental creation failed";
            actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

        }
        catch (InvalidIDException | EntityNotFoundException | RentalNotAllowedException | NullEntityException | RetrievalException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
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
            int validUserID = 1;
            int validItemID = 1;

            RentalHandler.setVerbose(true);

            //Change users number of rentals to maximum allowed
            User maxRentalUser = UserHandler.getUserByID(validUserID);
            assertNotNull(maxRentalUser);
            maxRentalUser.setCurrentRentals(User.DEFAULT_ALLOWED_RENTALS);
            UserHandler.updateUser(maxRentalUser);

            //Tracer to find bug
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            System.out.println("item1 available 1: " + item1.isAvailable());

            //Assert correct exception with correct message is thrown
            Exception exception = assertThrows(RentalNotAllowedException.class,
                                               () -> RentalHandler.createNewRental(validUserID, validItemID));
            String expectedMessage = "User not allowed to rent either due to already renting at maximum capacity " +
                    "or having a late fee.";
            String actualMessage = exception.getMessage();

            //Debug
            System.out.println(actualMessage);

            assertTrue(actualMessage.contains(expectedMessage));

            //Try with user 2 and items 1-6...
            assertDoesNotThrow(() -> RentalHandler.createNewRental(2, 1));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(2, 2));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(2, 3));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(2, 4));
            assertDoesNotThrow(() -> RentalHandler.createNewRental(2, 5));

            //... where 6 should fail
            exception = assertThrows(RentalNotAllowedException.class,
                                     () -> RentalHandler.createNewRental(2, 6));
            expectedMessage = "User not allowed to rent either due to already renting at maximum capacity " +
                    "or having a late fee.";
            actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

            //Debug
            item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            System.out.println("item1 available 1: " + item1.isAvailable());
            assertFalse(item1.isAvailable());

            RentalHandler.setVerbose(false);

        }
        catch (InvalidIDException | NullEntityException | InvalidNameException | RetrievalException | InvalidUserRentalsException | EntityNotFoundException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
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
            int validUserID = 1;
            int validItemID = 1;

            //Get ourselves a poor little user object
            User lateFeeUser = UserHandler.getUserByID(validUserID);
            assertNotNull(lateFeeUser);

            //Assert user is allowed to rent before getting a late fee
            assertDoesNotThrow(() -> RentalHandler.createNewRental(validUserID, 2));

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
        catch (InvalidIDException | NullEntityException | InvalidNameException | InvalidLateFeeException | EntityNotFoundException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //GET ALL ----------------------------------------------------------------------------------------------------------

    /**
     * Test case for the getAllRentals method with an empty database.
     * <p>
     * This test verifies that the getAllRentals method correctly returns an empty list when there are no rentals
     * in the database.
     * <p>
     * It clears the rentals table in the database, calls the getAllRentals method, and compares the expected empty
     * list with the actual result.
     * <p>
     * If the actual result matches the expected empty list, the test passes.
     */
    @Test
    @Order(11)
    void testGetAllRentals_EmptyRentalsTable()
    {
        System.out.println("\n11: Testing getAllRentals method with an empty database...");

        // Clear the rentals table in the database
        DatabaseHandler.executeCommand("DELETE FROM rentals");

        List<Rental> expectedRentals = Collections.emptyList();
        List<Rental> actualRentals;
        actualRentals = RentalHandler.getAllRentals();
        assertEquals(expectedRentals, actualRentals);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test method validates the functionality of the getAllRentals method in the RentalHandler class when the
     * database is populated.
     * <p>
     * The method first creates 5 new rentals. Then it calls the getAllRentals method to retrieve all rentals from
     * the database.
     * <p>
     * It checks if the returned list of rentals is not null and if the number of retrieved rentals matches the number
     * of created rentals.
     * <p>
     * If the list is null or the sizes do not match, the test fails.
     * <p>
     * If an exception is thrown during the process (EntityNotFoundException, EntityNotFoundException,
     * RentalNotAllowedException, or InvalidIDException), the test also fails.
     */
    @Test
    @Order(12)
    void testGetAllRentals_PopulatedRentalsTable_OneRental()
    {
        System.out.println("\n12: Testing getAllRentals method with some rentals in the database...");

        try
        {
            //Create 1 rental
            RentalHandler.createNewRental(1, 1);

            //Retrieve all rentals
            List<Rental> rentals = RentalHandler.getAllRentals();

            //Check if the number of rentals retrieved matches the number of rentals created
            assertNotNull(rentals, "The retrieved rentals list should not be null.");
            assertEquals(1, rentals.size(), "The number of retrieved rentals does not match the " +
                    "number of created rentals.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test method validates the functionality of the getAllRentals method in the RentalHandler class when the
     * database is populated.
     * <p>
     * The method first creates 5 new rentals. Then it calls the getAllRentals method to retrieve all rentals from
     * the database.
     * <p>
     * It checks if the returned list of rentals is not null and if the number of retrieved rentals matches the number
     * of created rentals.
     * <p>
     * If the list is null or the sizes do not match, the test fails.
     * <p>
     * If an exception is thrown during the process (EntityNotFoundException, EntityNotFoundException,
     * RentalNotAllowedException, or InvalidIDException), the test also fails.
     */
    @Test
    @Order(13)
    void testGetAllRentals_PopulatedRentalsTable_MultipleRentals()
    {
        System.out.println("\n13: Testing getAllRentals method with some rentals in the database...");

        try
        {
            // Create 5 rentals, should get IDs 1-5
            for (int i = 1; i <= 5; i++)
                RentalHandler.createNewRental(i, i);

            //Retrieve all rentals
            List<Rental> rentals = RentalHandler.getAllRentals();

            //Check if the number of rentals retrieved matches the number of rentals created
            assertNotNull(rentals, "The retrieved rentals list should not be null.");
            assertEquals(5, rentals.size(), "The number of retrieved rentals does not match the " +
                    "number of created rentals.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //GET BY ID --------------------------------------------------------------------------------------------------------

    /**
     * This is a test for the method 'getRentalByID' in the class 'RentalHandler'.
     * <p>
     * The purpose of this test is to validate that the method correctly throws an 'InvalidIDException' when given
     * invalid rental IDs.
     * <p>
     * Initially, the test creates 5 rentals with rental IDs ranging from 1 to 5. Following this, it attempts to fetch
     * rentals with IDs 0 and -1, which should trigger the 'InvalidIDException' as these IDs are not valid.
     */
    @Test
    @Order(14)
    void testGetRentalByID_InvalidRentalID()
    {
        System.out.println("\n14: Testing getRentalByID method with an invalid rentalID...");

        try
        {
            // Create 5 rentals, should get IDs 1-5
            for (int i = 1; i <= 5; i++)
                RentalHandler.createNewRental(i, i);

            //These should result in exceptions
            assertThrows(InvalidIDException.class, () -> RentalHandler.getRentalByID(0));
            assertThrows(InvalidIDException.class, () -> RentalHandler.getRentalByID(-1));

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This is a test for the method 'getRentalByID' in the class 'RentalHandler'.
     * <p>
     * The purpose of this test is to confirm that the method correctly returns null when trying to fetch a rental
     * with a non-existent rental ID.
     * <p>
     * Initially, the test creates 5 rentals with rental IDs ranging from 1 to 5. Following this, it attempts to
     * fetch rentals with IDs 6 to 10, which should return null as no rentals with these IDs exist.
     */
    @Test
    @Order(15)
    void testGetRentalByID_NonExistentRentalID()
    {
        System.out.println("\n15: Testing getRentalByID method with non-existent rentalID...");

        try
        {
            // Create 5 rentals, should get IDs 1-5
            for (int i = 1; i <= 5; i++)
                RentalHandler.createNewRental(i, i);

            // These should return null as no rental with these IDs exist
            for (int i = 6; i <= 10; i++)
                assertNull(RentalHandler.getRentalByID(i), "Expected null for non-existent rental ID " + i);

        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This is a test for the method 'getRentalByID' in the class 'RentalHandler'.
     * <p>
     * The purpose of this test is to confirm that the method correctly retrieves rentals with valid rental IDs and all
     * the fields of the retrieved rentals are as expected.
     * <p>
     * The test creates 5 rentals with rental IDs ranging from 1 to 5 and then attempts to fetch each of them.
     * <p>
     * For each fetched rental, the test asserts that the object is not null and all its fields
     * (rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, and lateFee)
     * match the expected values.
     */
    @Test
    @Order(16)
    void testGetRentalByID_ValidRentalID()
    {
        System.out.println("\n16: Testing getRentalByID method with valid rentalID...");

        try
        {
            for (int i = 0; i < 5; i++)
            {
                // Create rental
                RentalHandler.createNewRental(i + 1, i + 1);
                Rental rental = RentalHandler.getRentalByID(i + 1);

                // Verify non-nullness
                assertNotNull(rental, "Expected Rental object for rental ID " + i + 1);

                // Verify fields
                assertEquals(i + 1, rental.getRentalID());
                assertEquals(i + 1, rental.getUserID());
                assertEquals(i + 1, rental.getItemID());
                assertNotNull(rental.getRentalDate());
                assertEquals("user" + (i + 1), rental.getUsername());
                assertEquals("item" + (i + 1), rental.getItemTitle());
                assertEquals(rental.getRentalDate().plusDays(14).truncatedTo(ChronoUnit.SECONDS).withHour(20)
                                     .withMinute(0).withSecond(0), rental.getRentalDueDate());
                assertNull(rental.getRentalReturnDate());
                assertEquals(0.0, rental.getLateFee(), 0.001);
            }
        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //UPDATE -----------------------------------------------------------------------------------------------------------

    /**
     * Test case for updateRental method when the rental to update is null.
     * <p>
     * This test checks if a NullEntityException is correctly thrown when the rental is null.
     */
    @Test
    @Order(17)
    public void testUpdateRental_NullRental()
    {
        System.out.println("\n17: Testing updateRental method with a null rental...");

        // Call the updateRental method
        Exception exception = assertThrows(UpdateException.class, () -> RentalHandler.updateRental(null));

        // Check that the exception has the right message and cause
        assertTrue(exception.getMessage().contains("Rental Update failed:"));
        assertTrue(exception.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when the rental to update does not exist in the database.
     * <p>
     * This test checks if a EntityNotFoundException is correctly thrown when the rental does not exist.
     */
    @Test
    @Order(18)
    public void testUpdateRental_NonExistingRental()
    {
        System.out.println("\n18: Testing updateRental method with a non-existing rental...");

        try
        {
            // Create a non-existing rental
            Rental nonExistingRental = new Rental(1, 1);
            nonExistingRental.setRentalID(1); //Needs a valid ID (> 0)

            // Call the updateRental method
            Exception exception = assertThrows(UpdateException.class,
                                               () -> RentalHandler.updateRental(nonExistingRental));

            // Check that the exception has the right message and cause
            assertTrue(exception.getMessage().contains("Rental Update failed:"));
            assertTrue(exception.getCause() instanceof EntityNotFoundException);
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when the rental to update has an invalid rentalID.
     * <p>
     * This test checks if an InvalidIDException is correctly thrown when the rentalID is invalid.
     */
    @Test
    @Order(19)
    public void testUpdateRental_InvalidRentalID()
    {
        System.out.println("\n19: Testing updateRental method with an invalid RentalID...");

        try
        {
            // Create a rental with an invalid ID. ID is going to be 0 by default, which is invalid
            Rental invalidRental = new Rental(1, 1);

            // Call the updateRental method
            Exception exception = assertThrows(UpdateException.class,
                                               () -> RentalHandler.updateRental(invalidRental));

            // Check that the exception has the right message and cause
            assertTrue(exception.getMessage().contains("Rental Update failed:"));
            assertTrue(exception.getCause() instanceof InvalidIDException);
        }
        catch (ConstructionException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when only the due date of the rental is updated.
     * <p>
     * This test checks if the due date is correctly updated in the database.
     */
    @Test
    @Order(20)
    public void testUpdateRental_ChangeDueDate()
    {
        System.out.println("\n20: Testing updateRental method by only changing the DueDate...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();
            LocalDateTime originalReturnDate = rentalToUpdate.getRentalReturnDate();
            double originalLateFee = rentalToUpdate.getLateFee();

            // Change the dueDate
            LocalDateTime newDueDate = LocalDateTime.now().plusDays(7).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            rentalToUpdate.setRentalDueDate(newDueDate);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(newDueDate, updatedRental.getRentalDueDate());
            assertEquals(originalReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(originalLateFee, updatedRental.getLateFee());
        }
        catch (InvalidDateException | UpdateException | InvalidIDException
                | EntityNotFoundException | RentalNotAllowedException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when only the return date of the rental is updated.
     * <p>
     * This test checks if the return date is correctly updated in the database.
     */
    @Test
    @Order(21)
    public void testUpdateRental_ChangeReturnDate()
    {
        System.out.println("\n21: Testing updateRental method by only changing the ReturnDate...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();
            LocalDateTime originalDueDate = rentalToUpdate.getRentalDueDate();
            double originalLateFee = rentalToUpdate.getLateFee();

            // Change the returnDate
            LocalDateTime newReturnDate = LocalDateTime.now().plusDays(5).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            rentalToUpdate.setRentalReturnDate(newReturnDate);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(originalDueDate, updatedRental.getRentalDueDate());
            assertEquals(newReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(originalLateFee, updatedRental.getLateFee());
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException
                | InvalidDateException | UpdateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when only the late fee of the rental is updated.
     * <p>
     * This test checks if the late fee is correctly updated in the database.
     */
    @Test
    @Order(22)
    public void testUpdateRental_ChangeLateFee()
    {
        System.out.println("\n22: Testing updateRental method by only changing the LateFee...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();
            LocalDateTime originalDueDate = rentalToUpdate.getRentalDueDate();
            LocalDateTime originalReturnDate = rentalToUpdate.getRentalReturnDate();

            // Change the lateFee
            double newLateFee = 50.0;
            rentalToUpdate.setLateFee(newLateFee);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(originalDueDate, updatedRental.getRentalDueDate());
            assertEquals(originalReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(newLateFee, updatedRental.getLateFee());
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException
                | InvalidLateFeeException | UpdateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when the due date, return date, and late fee of the rental are updated.
     * <p>
     * This test checks if the due date, return date, and late fee are correctly updated in the database.
     */
    @Test
    @Order(23)
    public void testUpdateRental_ChangeAllFields()
    {
        System.out.println("\n23: Testing updateRental method by changing all mutable fields...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();

            // Change all mutable fields
            LocalDateTime newDueDate = LocalDateTime.now().plusDays(7).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime newReturnDate = LocalDateTime.now().plusDays(5).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            double newLateFee = 50.0;
            rentalToUpdate.setRentalDueDate(newDueDate);
            rentalToUpdate.setRentalReturnDate(newReturnDate);
            rentalToUpdate.setLateFee(newLateFee);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(newDueDate, updatedRental.getRentalDueDate());
            assertEquals(newReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(newLateFee, updatedRental.getLateFee());
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException
                | InvalidDateException | InvalidLateFeeException | UpdateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    //SOFT DELETE ------------------------------------------------------------------------------------------------------

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A valid Rental object is passed as argument.
     * The method is expected to perform successfully, and the database should no longer contain the softly deleted Rental.
     */
    @Test
    @Order(24)
    void testSoftDeleteRental_ValidRental()
    {
        System.out.println("\n24: Testing softDeleteRental method with a valid rental...");

        try
        {
            // Create and save a new rental
            Rental rentalToDelete = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToDelete);

            // Softly delete the rental
            try
            {
                RentalHandler.deleteRental(rentalToDelete);
            }
            catch (DeleteException e)
            {
                fail("An unexpected exception occurred: " + e.getMessage());
            }

            // Retrieve the rental from the database
            Rental retrievedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNotNull(retrievedRental);

            // Verify the rental is softly deleted
            assertTrue(retrievedRental.isDeleted(), "The rental should be softly deleted.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A null Rental object is passed as argument.
     * The method is expected to throw a DeleteException.
     */
    @Test
    @Order(25)
    void testSoftDeleteRental_NullRental()
    {
        System.out.println("\n25: Testing softDeleteRental method with a null rental...");

        // Attempt to softly delete a null rental
        Exception e = assertThrows(DeleteException.class, () -> RentalHandler.deleteRental(null),
                                   "A DeleteException should be thrown when attempting to softly delete a null rental.");
        assertTrue(e.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
                   "The cause of the DeleteException should be a NullEntityException.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A Rental object that doesn't exist in the database is passed as argument.
     * The method is expected to throw a DeleteException.
     */
    @Test
    @Order(26)
    void testSoftDeleteRental_NonExistentRental()
    {
        System.out.println("\n26: Testing softDeleteRental method with a non-existent rental...");

        try
        {
            // Create a rental that doesn't exist in the database
            Rental nonExistentRental = new Rental(1, 1);
            nonExistentRental.setRentalID(1); //Make sure the rental has a valid ID

            // Attempt to softly delete the non-existent rental
            Exception e = assertThrows(DeleteException.class, () -> RentalHandler.deleteRental(nonExistentRental),
                                       "A DeleteException should be thrown when attempting to softly delete a non-existent rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                       "The cause of the DeleteException should be a EntityNotFoundException.");
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#undoDeleteRental(Rental)}.
     * Case: A softly deleted Rental object is passed as argument.
     * The method is expected to perform successfully, and the database should contain the recovered Rental.
     */
    @Test
    @Order(27)
    void testSoftDeleteRental_AlreadySoftDeletedRental()
    {
        System.out.println(
                "\n27: Testing softDeleteRental method with a rental that has already been softly deleted...");

        try
        {
            // Create a new rental, softly delete it, and then try to softly delete it again
            Rental rentalToDelete = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToDelete);
            RentalHandler.deleteRental(rentalToDelete);
            RentalHandler.deleteRental(rentalToDelete);

            // Verify that the rental is marked as deleted in the database
            Rental deletedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNotNull(deletedRental);
            assertTrue(deletedRental.isDeleted(), "The rental should still be marked as deleted after a s" +
                    "econd soft delete.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException | DeleteException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //UNDO SOFT DELETE -------------------------------------------------------------------------------------------------

    /**
     * Test method for {@link RentalHandler#undoDeleteRental(Rental)}.
     * Case: A valid Rental object that was softly deleted is passed as argument.
     * The method is expected to perform successfully, and the database should contain the recovered Rental, no longer marked as deleted.
     */
    @Test
    @Order(28)
    void testUndoSoftDeleteRental_ValidRental()
    {
        System.out.println("\n28: Testing undoSoftDeleteRental method with a valid, softly deleted rental...");

        try
        {
            // Create a new rental, softly delete it, and then undo the soft delete
            Rental rentalToRecover = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToRecover);
            RentalHandler.deleteRental(rentalToRecover);
            RentalHandler.undoDeleteRental(rentalToRecover);

            // Verify that the rental is not marked as deleted in the database
            Rental recoveredRental = RentalHandler.getRentalByID(rentalToRecover.getRentalID());
            assertNotNull(recoveredRental);
            assertFalse(recoveredRental.isDeleted(),
                        "The rental should not be marked as deleted after undoing the soft delete.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException
                | DeleteException | RecoveryException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#undoDeleteRental(Rental)}.
     * Case: A null Rental object is passed as argument.
     * The method is expected to throw a UpdateException.
     */
    @Test
    @Order(29)
    void testUndoSoftDeleteRental_NullRental()
    {
        System.out.println("\n29: Testing undoSoftDeleteRental method with a null rental...");

        // Attempt to undo a soft delete on a null rental
        Exception e = assertThrows(RecoveryException.class,
                                   () -> RentalHandler.undoDeleteRental(null),
                                   "A RecoveryException should be thrown when attempting to undo a soft " +
                                           "delete on a null rental.");
        assertTrue(e.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
                   "The cause of the RecoveryException should be a NullEntityException.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#undoDeleteRental(Rental)}.
     * Case: A Rental object that wasn't softly deleted is passed as argument.
     * The method is expected to perform successfully without changing the Rental.
     */
    @Test
    @Order(30)
    void testUndoSoftDeleteRental_NotSoftlyDeletedRental()
    {
        System.out.println("\n30: Testing undoSoftDeleteRental method with a rental that was not softly deleted...");

        try
        {
            // Create a new rental and attempt to undo a soft delete on it
            Rental rental = RentalHandler.createNewRental(1, 1);
            assertNotNull(rental);
            RentalHandler.undoDeleteRental(rental);

            // Verify that the rental is not marked as deleted in the database
            Rental rentalInDB = RentalHandler.getRentalByID(rental.getRentalID());
            assertNotNull(rentalInDB);
            assertFalse(rentalInDB.isDeleted(),
                        "The rental should not be marked as deleted if it was never softly deleted.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException | RecoveryException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    //HARD DELETE ------------------------------------------------------------------------------------------------------

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A valid Rental object is passed as argument.
     * The method is expected to perform successfully, and the database should no longer contain the deleted Rental.
     */
    @Test
    @Order(31)
    void testDeleteRental_ValidRental()
    {
        System.out.println("\n31: Testing deleteRental method with a valid rental...");

        try
        {
            // Create a new rental and delete it
            Rental rentalToDelete = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToDelete);
            RentalHandler.hardDeleteRental(rentalToDelete);

            // Verify that the rental no longer exists in the database
            Rental deletedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNull(deletedRental, "The rental should be null after being deleted.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException | DeleteException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A null Rental object is passed as argument.
     * The method is expected to throw a DeleteException.
     */
    @Test
    @Order(32)
    void testDeleteRental_NullRental()
    {
        System.out.println("\n32: Testing deleteRental method with a null rental...");

        // Attempt to delete a null rental
        Exception e = assertThrows(DeleteException.class,
                                   () -> RentalHandler.hardDeleteRental(null),
                                   "A DeleteException should be thrown " +
                                           "when attempting to delete a null rental.");
        assertTrue(e.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
                   "The cause of the DeleteException should be a NullEntityException.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A Rental object that doesn't exist in the database is passed as argument.
     * The method is expected to throw a DeleteException.
     */
    @Test
    @Order(33)
    void testDeleteRental_NotExistingRental()
    {
        System.out.println("\n33: Testing deleteRental method with a rental that doesn't exist...");

        try
        {
            // Attempt to delete a rental that doesn't exist in the database
            Rental nonExistentRental = new Rental(1, 1);
            nonExistentRental.setRentalID(1); //Needs a valid ID
            Exception e = assertThrows(DeleteException.class,
                                       () -> RentalHandler.hardDeleteRental(nonExistentRental),
                                       "A DeleteException should be thrown when attempting to delete a non-existent rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                       "The cause of the DeleteException should be a EntityNotFoundException.");
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A Rental object that was softly deleted is passed as argument.
     * The method is expected to perform successfully, and the database should no longer contain the deleted Rental.
     */
    @Test
    @Order(34)
    void testDeleteRental_SoftlyDeletedRental()
    {
        System.out.println("\n34: Testing deleteRental method with a rental that was softly deleted...");

        try
        {
            // Create a new rental, softly delete it, and then hard delete it
            Rental rentalToDelete = RentalHandler.createNewRental(1, 1);
            assertNotNull(rentalToDelete);
            RentalHandler.deleteRental(rentalToDelete);
            RentalHandler.hardDeleteRental(rentalToDelete);

            // Verify that the rental no longer exists in the database
            Rental deletedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNull(deletedRental, "The rental should be null after being deleted, " +
                    "even if it was softly deleted before.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException | DeleteException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A Rental object that was hard deleted is passed as argument.
     * An exception of type DeleteException should be thrown, with its cause being a EntityNotFoundException, since the rental
     * has been hard deleted and doesn't exist in the database anymore.
     */
    @Test
    @Order(35)
    void testSoftDeleteRental_AlreadyHardDeletedRental()
    {
        System.out.println("\n35: Testing softDeleteRental method with a rental that has already been hard deleted...");

        try
        {
            // Create a new rental, hard delete it, and then try to softly delete it
            Rental rentalToDelete = RentalHandler.createNewRental(1, 1);
            RentalHandler.hardDeleteRental(rentalToDelete);

            // Attempt to softly delete the hard deleted rental
            Exception e = assertThrows(DeleteException.class,
                                       () -> RentalHandler.deleteRental(rentalToDelete),
                                       "A DeleteException should be thrown when attempting to softly delete a " +
                                               "hard deleted rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                       "The cause of the DeleteException should be a EntityNotFoundException.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException
                | DeleteException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#undoDeleteRental(Rental)}.
     * Case: A Rental object that was hard deleted is passed as argument.
     * An exception of type RecoveryException should be thrown, with its cause being a EntityNotFoundException, since the rental
     * has been hard deleted and doesn't exist in the database anymore.
     */
    @Test
    @Order(36)
    void testUndoSoftDeleteRental_HardDeletedRental()
    {
        System.out.println("\n36: Testing undoSoftDeleteRental method with a rental that was hard deleted...");

        try
        {
            // Create a new rental, hard delete it, and then try to undo a soft delete on it
            Rental rentalToRecover = RentalHandler.createNewRental(1, 1);
            RentalHandler.hardDeleteRental(rentalToRecover);

            // Attempt to undo a soft delete on the hard deleted rental
            Exception e = assertThrows(RecoveryException.class,
                                       () -> RentalHandler.undoDeleteRental(rentalToRecover),
                                       "A RecoveryException should be thrown when attempting to undo a soft delete " +
                                               "on a hard deleted rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                       "The cause of the RecoveryException should be a EntityNotFoundException.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeleteException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //GET BY DATE ------------------------------------------------------------------------------------------------------

    /**
     * Tests the getRentalsByRentalDate method when supplied with a null rental date.
     * This test should fail with an InvalidDateException, as rental dates cannot be null.
     */
    @Test
    @Order(37)
    void testGetRentalsByRentalDate_InvalidDate()
    {
        System.out.println("\n37: Testing getRentalsByRentalDate method with invalid date...");

        LocalDateTime invalidDate = null;

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDate(invalidDate),
                     "Expected InvalidDateException to be thrown, but it didn't");

        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDate(futureDate),
                     "Expected InvalidDateException to be thrown, but it didn't");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the getRentalsByRentalDate method when supplied with a rental date that doesn't match any existing rentals.
     * This test should pass if the method correctly returns an empty list, indicating no rentals were found.
     */
    @Test
    @Order(38)
    void testGetRentalsByRentalDate_NoRentalsFound()
    {
        System.out.println("\n3: Testing getRentalsByRentalDate method with a date that doesn't match any rentals...");

        LocalDateTime dateWithNoRentals = LocalDateTime.now().minusDays(
                1); //Assuming no rentals are made 1 day in the past

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(dateWithNoRentals);

            assertNotNull(rentals, "The returned list should not be null");
            assertTrue(rentals.isEmpty(),
                       "The list should be empty as no rentals should be found for the provided date.");
        }
        catch (InvalidDateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the getRentalsByRentalDate method when supplied with a rental date matching one rental.
     * This test should pass if the method correctly returns a list containing the one matching rental.
     */
    @Test
    @Order(39)
    void testGetRentalsByRentalDate_OneRentalFound()
    {
        System.out.println("\n39: Testing getRentalsByRentalDate method with a date matching a single rental...");

        // Assuming a rental was made today
        LocalDateTime dateWithOneRental = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        try
        {
            RentalHandler.createNewRental(1, 1);

            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(dateWithOneRental);

            assertNotNull(rentals, "The returned list should not be null");
            assertEquals(1, rentals.size(), "The list should contain one rental.");
        }
        catch (InvalidIDException | EntityNotFoundException
                | RentalNotAllowedException | InvalidDateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the getRentalsByRentalDate method when supplied with a rental date matching multiple rentals.
     * This test should pass if the method correctly returns a list containing all the matching rentals.
     */
    @Test
    @Order(40)
    void testGetRentalsByRentalDate_MultipleRentalsFound()
    {
        System.out.println("\n40: Testing getRentalsByRentalDate method with a date matching multiple rentals...");

        // Assuming multiple rentals were made today
        LocalDateTime dateWithMultipleRentals = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        try
        {
            // Create 5 rentals
            createAndSaveRentalsWithDifferentDates(5, 0);

            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(dateWithMultipleRentals);

            assertNotNull(rentals, "The returned list should not be null");
            assertEquals(5, rentals.size(), "The list should contain five rentals.");

            // Verifying the contents of the returned list
            for (int i = 0; i < 5; i++)
            {
                Rental rental = rentals.get(i);
                assertNotNull(rental, "The rental at index " + i + " should not be null.");
                assertEquals(i + 1, rental.getRentalID(),
                             "The rental ID of the rental at index " + i + " should be " + (i + 1));
            }
        }
        catch (InvalidDateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    //GET BY DAY -------------------------------------------------------------------------------------------------------

    /**
     * Tests the method getRentalsByRentalDay() when the rental day provided is null.
     * Expects an InvalidDateException.
     */
    @Test
    @Order(41)
    void testGetRentalsByRentalDay_NullRentalDay()
    {
        System.out.println("\n41: Testing getRentalsByRentalDay method with null rental day...");

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDay(null),
                     "getRentalsByRentalDay should throw InvalidDateException when rentalDay is null");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when the rental day provided is in the future.
     * Expects an InvalidDateException.
     */
    @Test
    @Order(42)
    void testGetRentalsByRentalDay_FutureRentalDay()
    {
        System.out.println("\n42: Testing getRentalsByRentalDay method with future rental day...");

        LocalDate futureDate = LocalDate.now().plusDays(1);

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDay(futureDate),
                     "getRentalsByRentalDay should throw InvalidDateException when rentalDay is in the future");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are no existing rentals.
     * Expects the returned list to be empty.
     */
    @Test
    @Order(43)
    void testGetRentalsByRentalDay_NoExistingRentals()
    {
        System.out.println("\n43: Testing getRentalsByRentalDay method with no existing rentals...");

        LocalDate rentalDay = LocalDate.now();

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(0, rentals.size(),
                         "Returned rental list should be empty when there are no rentals on the rental day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are existing rentals, but none on the desired date.
     * Expects the returned list to be empty.
     */
    @Test
    @Order(44)
    void testGetRentalsByRentalDay_NoRentalsOnDesiredDate()
    {
        System.out.println(
                "\n44: Testing getRentalsByRentalDay method with rentals existing, but none on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(5, 5);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(0, rentals.size(),
                         "Returned rental list should be empty when there are no rentals on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are 5 existing rentals and 1 on the desired date.
     * Expects the returned list to contain one rental.
     */
    @Test
    @Order(45)
    void testGetRentalsByRentalDay_OneRentalOnDesiredDate()
    {
        System.out.println(
                "\n45: Testing getRentalsByRentalDay method with 5 existing rentals and 1 on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(5, 4);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(1, rentals.size(),
                         "Returned rental list should contain one rental when only one rental is on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are 5 existing rentals and 3 on the desired date.
     * Expects the returned list to contain three rentals.
     */
    @Test
    @Order(46)
    void testGetRentalsByRentalDay_ThreeRentalsOnDesiredDate()
    {
        System.out.println(
                "\n46: Testing getRentalsByRentalDay method with 5 existing rentals and 3 on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(6, 3);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(3, rentals.size(),
                         "Returned rental list should contain three rentals when three rentals are on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are 5 existing rentals and all are on the desired date.
     * Expects the returned list to contain all five rentals.
     */
    @Test
    @Order(47)
    void testGetRentalsByRentalDay_AllRentalsOnDesiredDate()
    {
        System.out.println(
                "\n47: Testing getRentalsByRentalDay method with 5 existing rentals and all on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(5, 0);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(5, rentals.size(),
                         "Returned rental list should contain five rentals when all rentals are on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are rentals having same date but different times.
     * Expects the returned list to contain all rentals regardless of different times.
     */
    @Test
    @Order(48)
    void testGetRentalsByRentalDay_SameDateDifferentTimes()
    {
        System.out.println(
                "\n48: Testing getRentalsByRentalDay method with rentals having same date but different times...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different times on the same date.
        createAndSaveRentalsWithDifferentTimes(5);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(5, rentals.size(),
                         "Returned rental list should contain five rentals when all rentals are on the desired day, regardless of different times");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when the rental day is before any existing rentals.
     * Expects the returned list to be empty.
     */
    @Test
    @Order(49)
    void testGetRentalsByRentalDay_BeforeExistingRentals()
    {
        System.out.println(
                "\n49: Testing getRentalsByRentalDay method with a rental day before any existing rentals...");

        LocalDate rentalDay = LocalDate.now().minusDays(5);

        // Assuming method to create and save rentals with dates after the rentalDay.
        createAndSaveRentalsWithDifferentDates(5, 0);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(0, rentals.size(),
                         "Returned rental list should be empty when the rental day is before any existing rentals");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    /**
     * Creates a specific number of Rental instances and saves them to the database with different rental dates.
     * Each rental date is offset by an increasing number of days.
     *
     * @param numOfRentals  The number of Rental instances to be created.
     * @param offsetRentals The number of Rental instances whose rental dates should be offset.
     */
    private void createAndSaveRentalsWithDifferentDates(int numOfRentals, int offsetRentals)
    {
        try
        {
            LocalDateTime now = LocalDateTime.now();

            //Create numOfRentals number of rentals
            for (int i = 1; i <= numOfRentals; i++)
                RentalHandler.createNewRental(i, i);

            //Change rentalDates on desired amount of rentals
            for (int i = 1; i <= offsetRentals; i++)
            {
                Rental rental = RentalHandler.getRentalByID(i);
                assertNotNull(rental);

                //This will give different days to all the offset rentals
                LocalDateTime offsetDate = now.minusDays(i);

                String query = "UPDATE rentals SET rentalDate = ? WHERE rentalID = ?";
                String[] params = {
                        String.valueOf(offsetDate),
                        String.valueOf(rental.getRentalID())
                };

                DatabaseHandler.executePreparedUpdate(query, params);
            }

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates a specific number of Rental instances and saves them to the database with different rental dates.
     * Each rental date is offset by an increasing number of days.
     *
     * @param numOfRentals The number of Rental instances to be created.
     */
    private void createAndSaveRentalsWithDifferentTimes(int numOfRentals)
    {
        try
        {
            LocalDateTime now = LocalDateTime.now();

            // Create numOfRentals number of rentals
            for (int i = 1; i <= numOfRentals; i++)
            {
                RentalHandler.createNewRental(i, i);
            }

            // Change rentalTimes on each rental
            for (int i = 1; i <= numOfRentals; i++)
            {
                Rental rental = RentalHandler.getRentalByID(i);
                assertNotNull(rental);

                // This will give different times to all the rentals, by offsetting each one by an increasing number of hours and minutes
                LocalDateTime offsetTime = now.minusMinutes(i);

                String query = "UPDATE rentals SET rentalDate = ? WHERE rentalID = ?";
                String[] params = {
                        String.valueOf(offsetTime),
                        String.valueOf(rental.getRentalID())
                };

                DatabaseHandler.executePreparedUpdate(query, params);
            }

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            e.printStackTrace();
        }
    }

}