
package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.UserNotFoundException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

        Rental rental;
        try {
            rental = RentalHandler.createNewRental(validUserID, validItemID);
            // Check that the Rental object is not null and that it has a correct rentalID
            assertNotNull(rental, "The returned Rental object should not be null");
            assertTrue(rental.getRentalID() > 0);

            // Check that each field has been set correctly
            assertEquals(validUserID, rental.getUserID(), "UserID should match the input userID");
            assertEquals(validItemID, rental.getItemID(), "ItemID should match the input itemID");

            // Check that the username and itemTitle match the expected values
            User user = UserHandler.getUserByID(validUserID);
            Item item = ItemHandler.getItemByID(validItemID);

            assertNotNull(user);
            assertNotNull(item);
            assertEquals(user.getUsername(), rental.getUsername(), "Username should match the username of the user with the input userID");
            assertEquals(item.getTitle(), rental.getItemTitle(), "Item title should match the title of the item with the input itemID");

            // Check that rentalDate has been set to the current time (to the nearest second)
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            assertEquals(now, rental.getRentalDate().truncatedTo(ChronoUnit.SECONDS), "rentalDate should be set to the current time (to the nearest second)");

            // Check that rentalDueDate has been set to rentalDate plus allowed rental days at 20:00
            int allowedRentalDays = ItemHandler.getAllowedRentalDaysByID(validItemID);
            LocalDateTime dueDate = rental.getRentalDate().plusDays(allowedRentalDays).withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).withNano(0);
            assertEquals(dueDate, rental.getRentalDueDate(), "rentalDueDate should be set to rentalDate plus allowed rental days at 20:00");

            // Check that rentalReturnDate is null and lateFee is 0.0
            assertNull(rental.getRentalReturnDate(), "rentalReturnDate should be null for a new rental");
            assertEquals(0.0, rental.getLateFee(), "lateFee should be 0.0 for a new rental");

            System.out.println("\nTEST FINISHED.");

        } catch (UserNotFoundException | ItemNotFoundException e) {
            e.printStackTrace();
            fail("Test should be able to retrieve user or item with correct IDs. userID: " + validUserID + ", itemID: " + validItemID);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            fail("Exception occurred during interaction with database: : " + sqle.getMessage());
        }
    }

    /**
     * Test case for createNewRental method with an invalid userID.
     *
     * This test attempts to create a new rental using an invalid user ID. The userID is invalid if it is not a positive integer.
     * An IllegalArgumentException should be thrown with an appropriate error message.
     */
    @Test
    @Order(2)
    void testCreateNewRental_InvalidUserID() {
        System.out.println("\n2: Testing createNewRental method with invalid userID...");

        int invalidUserID = -1; // User IDs should be positive integers
        int validItemID = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RentalHandler.createNewRental(invalidUserID, validItemID);
        });

        String expectedMessage = "Error creating new rental: Invalid userID or itemID. userID: "
                + invalidUserID + ", itemID: " + validItemID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewRental method with an invalid itemID.
     *
     * This test attempts to create a new rental using an invalid item ID. The itemID is invalid if it is not a positive integer.
     * An IllegalArgumentException should be thrown with an appropriate error message.
     */
    @Test
    @Order(3)
    void testCreateNewRental_InvalidItemID() {
        System.out.println("\n3: Testing createNewRental method with invalid itemID...");

        int invalidItemID = 0; // Item IDs should be positive integers
        int validUserID = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RentalHandler.createNewRental(validUserID, invalidItemID);
        });

        String expectedMessage = "Error creating new rental: Invalid userID or itemID. userID: "
                + validUserID + ", itemID: " + invalidItemID;
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

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            RentalHandler.createNewRental(nonexistentUserID, validItemID);
        });

        String expectedMessage = "Failed to find user with ID: " + nonexistentUserID;
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

        Exception exception = assertThrows(ItemNotFoundException.class, () -> {
            RentalHandler.createNewRental(validUserID, nonexistentItemID);
        });

        String expectedMessage = "Failed to find item with ID: " + nonexistentItemID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

/**
     * This test method tests the getAllRentals method in RentalHandler.
     *
     * Test case: Retrieving all rentals.
     * First, a few new rentals are created to populate the database.
     * The getAllRentals method is then called to retrieve the list of rentals.
     * The size of this list should match the number of rentals created,
     * verifying that the method correctly retrieves all rentals from the database.
     *
     * If an SQLException is thrown at any point during the test, the test fails with an appropriate error message.
     *//*

    @Test
    @Order(3)
    void testGetAllRentals() {
        System.out.println("\n3: Testing getAllRentals method...");

        try {
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            User user1 = UserHandler.getUserByID(1);
            assertNotNull(user1);

            //Create a few new rentals
            Rental newRental1 = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            Rental newRental2 = RentalHandler.createNewRental(2, 2, LocalDateTime.now().minusDays(2));
            Rental newRental3 = RentalHandler.createNewRental(3, 3, LocalDateTime.now().minusDays(3));
            assertNotNull(newRental1);
            assertNotNull(newRental2);
            assertNotNull(newRental3);

            //Get the list of rentals
            List<Rental> rentals = RentalHandler.getAllRentals();

            //The list should contain the number of rentals we created
            assertEquals(3, rentals.size(), "The list of rentals should contain the number of rentals we created.");

            //Check that the rentals we created are in the list
            assertTrue(rentals.stream().anyMatch(rental -> rental.getRentalID() == newRental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(rental -> rental.getRentalID() == newRental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(rental -> rental.getRentalID() == newRental3.getRentalID()));

            //Check that username was set correctly
            assertEquals(user1.getUsername(), rentals.get(0).getUsername());
            //Check that the correct title has been retrieved
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Print rentals for fun
            RentalHandler.printRentalList(rentals);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test creates a new rental at the start, and then checks that the getRentalByID method returns a Rental
     * object with the same details as the created rental. It also checks that an IllegalArgumentException is thrown
     * when trying to retrieve a rental with an invalid ID.
     *//*

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
        assertEquals(rental.getUsername(), retrievedRental.getUsername()); //New test for username
        assertEquals(rental.getItemTitle(), retrievedRental.getItemTitle()); //New test for title

        //Test invalid getRentalByID
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalByID(-1));

        System.out.println("\nTEST FINISHED.");
    }


    */
/**
     * This test method first creates a few new rentals, two with the current date and time and one with a future date.
     * Then it tests the getRentalsByRentalDate method by asking for rentals from the current date, expecting to find
     * the two rentals it just created. It also checks for rentals from a date in the past, where it expects to find none.
     * Finally, it tests the method with invalid inputs, expecting it to throw an IllegalArgumentException.
     *//*

    @Test
    @Order(5)
    void testGetRentalsByRentalDate() {
        System.out.println("\n5: Testing getRentalsByRentalDate method...");

        LocalDateTime testDate1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime testDate2 = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime testDate3 = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime testDate4 = LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS);

        //Create new rentals to test with
        try {
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            User user1 = UserHandler.getUserByID(1);
            assertNotNull(user1);

            Rental rental1 = RentalHandler.createNewRental(1, 1, testDate1);
            Rental rental2 = RentalHandler.createNewRental(2, 2, testDate1);
            Rental rental3 = RentalHandler.createNewRental(3, 3, testDate3); //This rental has a different date

            //Test valid getRentalsByRentalDate
            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(rental1.getRentalDate());
            assertNotNull(rentals);
            assertEquals(2, rentals.size()); //There should be two rentals with the same date

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertFalse(rentals.contains(rental3));

            //Check that username was set correctly
            assertEquals(user1.getUsername(), rentals.get(0).getUsername());
            //Check that the correct title has been retrieved
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Test with a rentalDate that no rentals have
            rentals = RentalHandler.getRentalsByRentalDate(testDate4);
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByRentalDate
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByRentalDate(testDate2));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByRentalDate(null));

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test first creates three rentals on the same day but at different times. It then verifies that
     * getRentalsByRentalDay can correctly retrieve all three rentals. It also tests the method's behavior when passed
     * a day with no rentals and verifies that it correctly returns an empty list. Finally, it checks that the method
     * correctly throws an exception when passed invalid inputs.
     *//*

    @Test
    @Order(6)
    void testGetRentalsByRentalDay() {
        System.out.println("\n6: Testing getRentalsByRentalDay method...");

        LocalDate testDay1 = LocalDate.now().minusDays(1);
        LocalDate testDay2 = LocalDate.now().plusDays(1);
        LocalDate testDay3 = testDay1.minusDays(1);
        LocalDate testDay4 = testDay1.minusDays(2);

        //Create new rentals to test with
        try {
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            User user1 = UserHandler.getUserByID(1);
            assertNotNull(user1);

            //Three rentals on the same day, but different times
            Rental rental1 = RentalHandler.createNewRental(1, 1, testDay1.atStartOfDay().plusHours(2));
            Rental rental2 = RentalHandler.createNewRental(2, 2, testDay1.atStartOfDay().plusHours(4));
            Rental rental3 = RentalHandler.createNewRental(3, 3, testDay1.atStartOfDay().plusHours(6));

            //Test valid getRentalsByRentalDay
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(testDay1);
            assertNotNull(rentals);
            assertEquals(3, rentals.size()); //There should be three rentals on the same day

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental3.getRentalID()));

            //Check that username was set correctly
            assertEquals(user1.getUsername(), rentals.get(0).getUsername());
            //Check that the correct title has been retrieved
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Test with a rentalDay that no rentals have
            rentals = RentalHandler.getRentalsByRentalDay(testDay4);
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByRentalDay
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByRentalDay(testDay2));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByRentalDay(null));

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test first creates three rentals on different days. It then verifies that getRentalsByTimePeriod can
     * correctly retrieve all three rentals when they fall within the specified period. It also tests the method's
     * behavior when passed a period with no rentals and verifies that it correctly returns an empty list.
     * Finally, it checks that the method correctly throws an exception when passed invalid inputs.
     *//*

    @Test
    @Order(7)
    void testGetRentalsByTimePeriod() {
        System.out.println("\n7: Testing getRentalsByTimePeriod method...");

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate twoWeeksAgo = today.minusWeeks(2);
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        //Create new rentals to test with
        try {
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            User user1 = UserHandler.getUserByID(1);
            assertNotNull(user1);

            Rental rental1 = RentalHandler.createNewRental(1, 1, yesterday.atStartOfDay());
            Rental rental2 = RentalHandler.createNewRental(2, 2, today.atStartOfDay());
            Rental rental3 = RentalHandler.createNewRental(3, 3, twoDaysAgo.atStartOfDay());

            //Test valid getRentalsByTimePeriod
            List<Rental> rentals = RentalHandler.getRentalsByTimePeriod(twoDaysAgo, today);
            assertNotNull(rentals);
            assertEquals(3, rentals.size()); //There should be three rentals within this period

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental3.getRentalID()));

            //Check that username was set correctly
            assertEquals(user1.getUsername(), rentals.get(0).getUsername());
            //Check that the correct title has been retrieved
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Test with a period that no rentals have
            rentals = RentalHandler.getRentalsByTimePeriod(threeWeeksAgo, twoWeeksAgo);
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByTimePeriod
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByTimePeriod(null, today));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByTimePeriod(yesterday, null));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByTimePeriod(today, yesterday));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByTimePeriod(tomorrow, tomorrow.plusDays(1)));

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test first creates three rentals with the same user ID. It then verifies that getRentalsByUserID can
     * correctly retrieve all three rentals. It also tests the method's behavior when passed a user ID with no rentals
     * and verifies that it correctly returns an empty list.
     * Finally, it checks that the method correctly throws an exception when passed invalid inputs.
     *//*

    @Test
    @Order(8)
    void testGetRentalsByUserID() {
        System.out.println("\n8: Testing getRentalsByUserID method...");

        //Create new rentals to test with
        try {
            //Create three rentals with the same userID
            User user1 = UserHandler.getUserByID(1);
            assertNotNull(user1);
            Rental rental1 = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(3));
            Rental rental2 = RentalHandler.createNewRental(1, 2, LocalDateTime.now().minusDays(2));
            Rental rental3 = RentalHandler.createNewRental(1, 3, LocalDateTime.now().minusDays(1));

            //Test valid getRentalsByUserID
            List<Rental> rentals = RentalHandler.getRentalsByUserID(1);
            assertNotNull(rentals);
            assertEquals(3, rentals.size()); //There should be three rentals for userID 1

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental3.getRentalID()));

            //Check that username was set correctly
            assertEquals(user1.getUsername(), rentals.get(0).getUsername());

            //Test with a userID that has no rentals
            rentals = RentalHandler.getRentalsByUserID(2);
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByUserID
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByUserID(0));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByUserID(-1));

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test first creates three rentals with the same item ID. It then verifies that getRentalsByItemID can
     * correctly retrieve all three rentals. It also tests the method's behavior when passed a item ID with no rentals
     * and verifies that it correctly returns an empty list.
     * Finally, it checks that the method correctly throws an exception when passed invalid inputs.
     *//*

    @Test
    @Order(9)
    void testGetRentalsByItemID() {
        System.out.println("\n9: Testing getRentalsByItemID method...");

        //Create new rentals to test with
        try {
            //Create three rentals with the same itemID
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);
            Rental rental1 = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(3));
            Rental rental2 = RentalHandler.createNewRental(2, 1, LocalDateTime.now().minusDays(2));
            Rental rental3 = RentalHandler.createNewRental(3, 1, LocalDateTime.now().minusDays(1));

            //Test valid getRentalsByItemID
            List<Rental> rentals = RentalHandler.getRentalsByItemID(1);
            assertNotNull(rentals);
            assertEquals(3, rentals.size()); //There should be three rentals for itemID 1

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental3.getRentalID()));

            //Check that the correct title has been retrieved
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Test with a itemID that has no rentals
            rentals = RentalHandler.getRentalsByItemID(2);
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByItemID
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByItemID(0));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByItemID(-1));
        
        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test first creates three rentals with the same username. It then verifies that getRentalsByUsername can
     * correctly retrieve all three rentals. It also tests the method's behavior when passed a username with no rentals
     * and verifies that it correctly returns an empty list.
     * Finally, it checks that the method correctly throws an exception when passed invalid inputs.
     *//*

    @Test
    @Order(10)
    void testGetRentalsByUsername() {
        System.out.println("\n10: Testing getRentalsByUsername method...");

        //Create new rentals to test with
        try {
            //Create three rentals with the same username
            User user1 = UserHandler.getUserByID(1);
            assertNotNull(user1);
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);

            Rental rental1 = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(3));
            Rental rental2 = RentalHandler.createNewRental(1, 2, LocalDateTime.now().minusDays(2));
            Rental rental3 = RentalHandler.createNewRental(1, 3, LocalDateTime.now().minusDays(1));

            //Set username for each rental
            rental1.setUsername(user1.getUsername());
            rental2.setUsername(user1.getUsername());
            rental3.setUsername(user1.getUsername());

            //Test valid getRentalsByUsername
            List<Rental> rentals = RentalHandler.getRentalsByUsername(user1.getUsername());
            assertNotNull(rentals);
            assertEquals(3, rentals.size()); //There should be three rentals for this username

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental3.getRentalID()));

            //Check that username was set correctly
            assertEquals(user1.getUsername(), rentals.get(0).getUsername());
            //Check that the correct title has been retrieved
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Test with a username that has no rentals
            User user2 = UserHandler.getUserByID(2);
            assertNotNull(user2);
            rentals = RentalHandler.getRentalsByUsername(user2.getUsername());
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByUsername
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByUsername(null));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByUsername(""));

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test first creates three rentals with the same item title. It then verifies that getRentalsByItemTitle can
     * correctly retrieve all three rentals. It also tests the method's behavior when passed an item title with no rentals
     * and verifies that it correctly returns an empty list.
     * Finally, it checks that the method correctly throws an exception when passed invalid inputs.
     *//*

    @Test
    @Order(11)
    void testGetRentalsByItemTitle() {
        System.out.println("\n11: Testing getRentalsByItemTitle method...");

        //Create new rentals to test with
        try {
            //Create three rentals with the same item title
            Item item1 = ItemHandler.getItemByID(1);
            assertNotNull(item1);

            Rental rental1 = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(3));
            Rental rental2 = RentalHandler.createNewRental(2, 1, LocalDateTime.now().minusDays(2));
            Rental rental3 = RentalHandler.createNewRental(3, 1, LocalDateTime.now().minusDays(1));

            //Set item title for each rental
            rental1.setItemTitle(item1.getTitle());
            rental2.setItemTitle(item1.getTitle());
            rental3.setItemTitle(item1.getTitle());

            //Test valid getRentalsByItemTitle
            List<Rental> rentals = RentalHandler.getRentalsByItemTitle(item1.getTitle());
            assertNotNull(rentals);
            assertEquals(3, rentals.size()); //There should be three rentals for this item title

            //Check that the correct rentals were retrieved
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental1.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental2.getRentalID()));
            assertTrue(rentals.stream().anyMatch(r -> r.getRentalID() == rental3.getRentalID()));

            //Check that title was set correctly
            assertEquals(item1.getTitle(), rentals.get(0).getItemTitle());

            //Test with a title that has no rentals
            Item item2 = ItemHandler.getItemByID(2);
            assertNotNull(item2);
            rentals = RentalHandler.getRentalsByItemTitle(item2.getTitle());
            assertNotNull(rentals);
            assertTrue(rentals.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test invalid getRentalsByItemTitle
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByItemTitle(null));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.getRentalsByItemTitle(""));

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * Tests the updateRental method in the RentalHandler class. The method tests various scenarios including both valid and invalid cases.
     *
     * Test Scenarios:
     * 1. Updating a non-existent rental should return false.
     * 2. Updating a valid rental with updated userID, itemID, and rentalDate. The updated rental's properties should reflect the changes.
     * 3. Updating rentals with null values should throw an IllegalArgumentException.
     * 4. Updating a rental with changed userID but unchanged username. The updated rental's userID should reflect the change.
     * 5. Updating a rental with changed username but unchanged userID. The updated rental's username should reflect the change.
     * 6. Updating a rental with both userID and username changed to valid values. The updated rental's userID and username should reflect the changes.
     * 7. Updating a rental with changed itemID but unchanged title. The updated rental's itemID should reflect the change.
     * 8. Updating a rental with changed title but unchanged itemID. The updated rental's title should reflect the change.
     * 9. Updating a rental with both itemID and title changed to valid values. The updated rental's itemID and title should reflect the changes.
     * 10. Updating a rental with both userID and username changed but mismatched should throw an IllegalArgumentException.
     * 11. Updating a rental with both itemID and title changed but mismatched should throw an IllegalArgumentException.
     *
     * The test method also handles SQLExceptions that may be thrown during the process. The method prints out the test status and any exceptions thrown during the test.
     *//*

    @Test
    @Order(12)
    void testUpdateRental() {
        System.out.println("\n12: Testing updateRental method...");

        //Test case 1: Updating a non-existent rental should throw an exception
        System.out.println("\nTesting test case 1: non-existent rental...");
        try {
            Rental nonExistentRental = new Rental(1, 1, LocalDateTime.now());
            nonExistentRental.setRentalID(9999);  //Assuming this ID does not exist in the database

            //Username still needs to be correctly set
            User user = UserHandler.getUserByID(1);
            if (user == null) {
                throw new SQLException("User with ID 1 does not exist.");
            }
            nonExistentRental.setUsername(user.getUsername());

            //Title still needs to be correctly set
            Item item = ItemHandler.getItemByID(1);
            if (item == null) {
                throw new SQLException("Item with ID 1 does not exist.");
            }
            nonExistentRental.setItemTitle(item.getTitle());

            //Actual test
            assertFalse(RentalHandler.updateRental(nonExistentRental, nonExistentRental));
            System.out.println("Test case 1 finished.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test case 2: Updating a valid rental should return true
        System.out.println("\nTesting test case 2: valid rentals...");
        try {
            //Create a new rental first to ensure a valid rentalID
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            //Copy the rental to a newRental object
            Rental newRental = new Rental(oldRental);

            //Update the details of the newRental
            int updatedUserID = 2;
            int updatedItemID = 2;
            LocalDateTime updatedRentalDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            newRental.setUserID(updatedUserID);
            newRental.setItemID(updatedItemID);
            newRental.setRentalDate(updatedRentalDate);
            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            //Retrieve the updated rental to verify the details were correctly updated
            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedUserID, updatedRental.getUserID(), "The updated rental's userID should match the new userID.");
            assertEquals(updatedItemID, updatedRental.getItemID(), "The updated rental's itemID should match the new itemID.");
            assertEquals(updatedRentalDate, updatedRental.getRentalDate(), "The updated rental's rentalDate should match the new rentalDate.");
            System.out.println("Test case 2 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 3: invalid updateRental
        System.out.println("\nTesting test case 3: invalid rentals...");
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.updateRental(null, null));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.updateRental(RentalHandler.createNewRental(1, 1, LocalDateTime.now()), null));
        assertThrows(IllegalArgumentException.class, () -> RentalHandler.updateRental(null, RentalHandler.createNewRental(1, 1, LocalDateTime.now())));
        System.out.println("Test case 3 finished.");

        //Test case 4: userID is changed but not username
        System.out.println("\nTesting test case 4: userID is changed but not username...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            int updatedUserID = 2; //Assuming this userID exists and corresponds to the same username
            newRental.setUserID(updatedUserID);

            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedUserID, updatedRental.getUserID(), "The updated rental's userID should match the new userID.");
            System.out.println("Test case 4 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 5: username is changed but not userID
        System.out.println("\nTesting test case 5: username is changed but not userID...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            String updatedUsername = "user2"; //Assuming this username exists and corresponds to the same userID
            newRental.setUsername(updatedUsername);

            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedUsername, updatedRental.getUsername(), "The updated rental's username should match the new username.");
            System.out.println("Test case 5 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 6: both userID and username are changed and valid
        System.out.println("\nTesting test case 6: both userID and username are changed and valid...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            int updatedUserID = 2; //Assuming this userID exists
            String updatedUsername = "user2"; //Assuming this username exists and corresponds to the new userID
            newRental.setUserID(updatedUserID);
            newRental.setUsername(updatedUsername);

            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedUserID, updatedRental.getUserID(), "The updated rental's userID should match the new userID.");
            assertEquals(updatedUsername, updatedRental.getUsername(), "The updated rental's username should match the new username.");
            System.out.println("Test case 6 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 7: itemID is changed but not title
        System.out.println("\nTesting test case 7: itemID is changed but not title...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            int updatedItemID = 2; //Assuming this itemID exists and corresponds to the same title
            newRental.setItemID(updatedItemID);

            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedItemID, updatedRental.getItemID(), "The updated rental's itemID should match the new itemID.");
            System.out.println("Test case 7 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 8: title is changed but not itemID
        System.out.println("\nTesting test case 8: title is changed but not itemID...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            String updatedTitle = "item2"; //Assuming this title exists and corresponds to the same itemID
            newRental.setItemTitle(updatedTitle);

            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedTitle, updatedRental.getItemTitle(), "The updated rental's title should match the new title.");
            System.out.println("Test case 8 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 9: both itemID and title are changed and valid
        System.out.println("\nTesting test case 9: both itemID and title are changed and valid...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            int updatedItemID = 2; //Assuming this itemID exists
            String updatedTitle = "item2"; //Assuming this title exists and corresponds to the new itemID
            newRental.setItemID(updatedItemID);
            newRental.setItemTitle(updatedTitle);

            boolean updated = RentalHandler.updateRental(oldRental, newRental);
            assertTrue(updated, "Updating a valid rental should return true.");

            Rental updatedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNotNull(updatedRental);
            assertEquals(updatedItemID, updatedRental.getItemID(), "The updated rental's itemID should match the new itemID.");
            assertEquals(updatedTitle, updatedRental.getItemTitle(), "The updated rental's title should match the new title.");
            System.out.println("Test case 9 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 10: both userID and username are changed and mismatched
        System.out.println("\nTesting test case 10: both userID and username are changed and mismatched...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            int updatedUserID = 2; //Assuming this userID exists
            String updatedUsername = "user3"; //Assuming this username exists but does not match the updated userID
            newRental.setUserID(updatedUserID);
            newRental.setUsername(updatedUsername);

            assertThrows(IllegalArgumentException.class, () -> {
                RentalHandler.updateRental(oldRental, newRental);
            });

            System.out.println("Test case 10 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        //Test case 11: both itemID and title are changed and mismatched
        System.out.println("\nTesting test case 11: both itemID and title are changed and mismatched...");
        try {
            Rental oldRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(oldRental);

            Rental newRental = new Rental(oldRental);
            int updatedItemID = 2; //Assuming this itemID exists
            String updatedTitle = "item3"; //Assuming this title exists but does not match the updated itemID
            newRental.setItemID(updatedItemID);
            newRental.setItemTitle(updatedTitle);

            assertThrows(IllegalArgumentException.class, () -> {
                RentalHandler.updateRental(oldRental, newRental);
            });

            System.out.println("Test case 11 finished.");
        } catch (SQLException e) {
            fail("Updating a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    */
/**
     * This test method tests the deleteRental method in RentalHandler.
     *
     * Test case 1: Deleting a non-existent rental.
     * The method should return false since there's no rental with the provided ID in the database.
     *
     * Test case 2: Deleting a rental with a null object.
     * An IllegalArgumentException should be thrown in this case as the provided rental object is null.
     *
     * Test case 3: Deleting a valid rental.
     * First, a rental is created to ensure it exists in the database. The method should then successfully delete this rental and return true.
     * After deletion, attempting to retrieve the rental using its ID should return null, verifying that the rental was successfully deleted from the database.
     *
     * In all cases, if an SQLException is thrown, the test fails with an appropriate error message.
     *//*

    @Test
    @Order(13)
    void testDeleteRental() {
        System.out.println("\n13: Testing deleteRental method...");

        //Test: Deleting a non-existent rental should return false
        try {
            Rental nonExistentRental = new Rental(1, 1, LocalDateTime.now());
            nonExistentRental.setRentalID(9999);  //Assuming this ID does not exist in the database
            assertFalse(RentalHandler.deleteRental(nonExistentRental));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        //Test: Deleting a rental with null object should throw IllegalArgumentException
        try {
            Rental nullRental = null;
            assertThrows(IllegalArgumentException.class, () -> RentalHandler.deleteRental(nullRental));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception occurred during test: " + e.getMessage());
        }

        //Test: Deleting a valid rental should return true
        try {
            //Create a new rental first to ensure a valid rentalID
            Rental newRental = RentalHandler.createNewRental(1, 1, LocalDateTime.now().minusDays(1));
            assertNotNull(newRental);

            //Delete the newly created rental
            boolean deleted = RentalHandler.deleteRental(newRental);
            assertTrue(deleted, "Deleting a valid rental should return true.");

            //Try to retrieve the deleted rental to verify it was correctly deleted
            Rental deletedRental = RentalHandler.getRentalByID(newRental.getRentalID());
            assertNull(deletedRental, "The deleted rental should not be retrievable from the database.");
        } catch (SQLException e) {
            fail("Deleting a valid rental should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }*/
}
