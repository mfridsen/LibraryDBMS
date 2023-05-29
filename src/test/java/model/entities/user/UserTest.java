package model.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidRentalStatusChangeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUsernameException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package model.user
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit test for the User class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    static {
        System.out.println("Verifying metadata...");
        System.out.println("User.DEFAULT_ALLOWED_RENTALS = " + User.DEFAULT_ALLOWED_RENTALS);
        System.out.println("User.MIN_USERNAME_LENGTH = " + User.MIN_USERNAME_LENGTH);
        System.out.println("User.MAX_USERNAME_LENGTH = " + User.MAX_USERNAME_LENGTH);
        System.out.println("User.MIN_PASSWORD_LENGTH = " + User.MIN_PASSWORD_LENGTH);
        System.out.println("User.MAX_PASSWORD_LENGTH = " + User.MAX_PASSWORD_LENGTH);
    }

    @Test
    @Order(1)
    void testUserCreationConstructor_ValidInput() {
        System.out.println("\n1: Testing User creation constructor with valid input...");

        try {
            String username = "validUsername";
            String password = "validPassword1";
            User testUser = new User(username, password);

            //TODO-prio add more checks when more fields are added
            assertEquals(username, testUser.getUsername());
            assertEquals(password, testUser.getPassword());
            assertEquals(User.DEFAULT_ALLOWED_RENTALS, testUser.getAllowedRentals());
            assertEquals(0, testUser.getCurrentRentals());
            assertEquals(0.0, testUser.getLateFee());
            assertTrue(testUser.isAllowedToRent());
            assertFalse(testUser.isDeleted());
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testUserCreationConstructor_InvalidInput() {
        System.out.println("\n2: Testing User creation constructor with invalid input...");

        String validUsername = "username";
        String validPassword = "password";

        //Null username and password
        assertThrows(ConstructionException.class, () -> new User(null, validPassword));
        assertThrows(ConstructionException.class, () -> new User(validUsername, null));

        //Empty username and password
        assertThrows(ConstructionException.class, () -> new User("", validPassword));
        assertThrows(ConstructionException.class, () -> new User(validUsername, ""));

        //Short username and password
        assertThrows(ConstructionException.class, () -> new User("us", validPassword));
        assertThrows(ConstructionException.class, () -> new User(validUsername, "shrt"));

        //Too long username and password
        String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(ConstructionException.class, () -> new User(longUsername, validPassword));
        assertThrows(ConstructionException.class, () -> new User(validUsername, longPassword));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testUserRetrievalConstructor_ValidInput() {
        System.out.println("\n3: Testing User retrieval constructor with valid input...");

        try {
            int userID = 1;
            String username = "validUsername";
            String password = "validPassword1";
            int allowedRentals = 5;
            int currentRentals = 2;
            double lateFee = 0.0;

            User testUser = new User(userID, username, password, allowedRentals, currentRentals,
                    lateFee, true, false);

            //TODO-prio add more checks when more fields are added
            assertEquals(userID, testUser.getUserID());
            assertEquals(username, testUser.getUsername());
            assertEquals(password, testUser.getPassword());
            assertEquals(allowedRentals, testUser.getAllowedRentals());
            assertEquals(currentRentals, testUser.getCurrentRentals());
            assertEquals(lateFee, testUser.getLateFee());
            assertTrue(testUser.isAllowedToRent());
            assertFalse(testUser.isDeleted());
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.getCause().printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testUserRetrievalConstructor_InvalidInput() {
        System.out.println("\n4: Testing User retrieval constructor with invalid input...");

        int validUserID = 1;
        String validUsername = "username";
        String validPassword = "password";
        int allowedRentals = 5;
        int currentRentals = 2;
        double lateFee = 10.0;

        //Null username and password
        assertThrows(ConstructionException.class, () -> new User(validUserID, null, validPassword, allowedRentals, currentRentals, lateFee, true, false));
        assertThrows(ConstructionException.class, () -> new User(validUserID, validUsername, null, allowedRentals, currentRentals, lateFee, true, false));

        //Empty username and password
        assertThrows(ConstructionException.class, () -> new User(validUserID, "", validPassword, allowedRentals, currentRentals, lateFee, true,false));
        assertThrows(ConstructionException.class, () -> new User(validUserID, validUsername, "", allowedRentals, currentRentals, lateFee, true, false));

        //Short username and password
        assertThrows(ConstructionException.class, () -> new User(validUserID, "us", validPassword, allowedRentals, currentRentals, lateFee, true, false));
        assertThrows(ConstructionException.class, () -> new User(validUserID, validUsername, "shrt", allowedRentals, currentRentals, lateFee, true, false));

        //Too long username and password
        String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(ConstructionException.class, () -> new User(validUserID, longUsername, validPassword, allowedRentals, currentRentals, lateFee, true, false));
        assertThrows(ConstructionException.class, () -> new User(validUserID, validUsername, longPassword, allowedRentals, currentRentals, lateFee, true, false));

        //Invalid userID
        assertThrows(ConstructionException.class, () -> new User(0, validUsername, validPassword, allowedRentals, currentRentals, lateFee, true, false));

        //Invalid currentRentals
        assertThrows(ConstructionException.class, () -> new User(validUserID, validUsername, validPassword, allowedRentals, allowedRentals + 1, lateFee, true, false));

        //Invalid lateFee
        assertThrows(ConstructionException.class, () -> new User(validUserID, validUsername, validPassword, allowedRentals, currentRentals, -1.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testUserCopyConstructor_ValidInput() {
        System.out.println("\n5: Testing User copy constructor with valid input...");

        try {
            User originalUser = new User(1, "validUsername", "validPassword1", 5, 2, 10.0, false, false);
            User copiedUser = new User(originalUser);

            assertEquals(originalUser.getUserID(), copiedUser.getUserID());
            assertEquals(originalUser.getUsername(), copiedUser.getUsername());
            assertEquals(originalUser.getPassword(), copiedUser.getPassword());
            assertEquals(originalUser.getAllowedRentals(), copiedUser.getAllowedRentals());
            assertEquals(originalUser.getCurrentRentals(), copiedUser.getCurrentRentals());
            assertEquals(originalUser.getLateFee(), copiedUser.getLateFee());
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.getCause().printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(6)
    void testSetUserID_ValidInput() {
        System.out.println("\n6: Testing set and get UserID with valid input...");

        try {
            User testUser = new User("username", "password123");
            int userID = 1;
            testUser.setUserID(userID);
            assertEquals(userID, testUser.getUserID());
        } catch (InvalidIDException | ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testSetUserID_InvalidInput() {
        System.out.println("\n7: Testing set UserID with invalid input...");

        try {
            User testUser = new User("username", "password123");
            int userID = 0;
            assertThrows(InvalidIDException.class, () -> testUser.setUserID(userID));
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetUsername_ValidInput() {
        System.out.println("\n8: Testing set and get Username with valid input...");

        try {
            User testUser = new User("username", "password123");
            String username = "newUsername";
            testUser.setUsername(username);
            assertEquals(username, testUser.getUsername());
        } catch (InvalidUsernameException | ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetUsername_InvalidInput() {
        System.out.println("\n9: Testing set Username with invalid input...");

        try {
            //Create test user
            User testUser = new User("username", "password123");

            // Test for null input
            String nullUsername = null;
            assertThrows(InvalidUsernameException.class, () -> testUser.setUsername(nullUsername));

            // Test for empty string
            String emptyUsername = "";
            assertThrows(InvalidUsernameException.class, () -> testUser.setUsername(emptyUsername));

            // Test for username length less than minimum length
            String shortUsername = "ab";
            assertThrows(InvalidUsernameException.class, () -> testUser.setUsername(shortUsername));

            // Test for username length greater than maximum length
            String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
            assertThrows(InvalidUsernameException.class, () -> testUser.setUsername(longUsername));
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testSetPassword_ValidInput() {
        System.out.println("\n10: Testing set and get Password with valid input...");

        try {
            User testUser = new User("username", "password123");
            String password = "newPassword123";
            testUser.setPassword(password);
            assertEquals(password, testUser.getPassword());
        } catch (InvalidPasswordException | ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testSetPassword_InvalidInput() {
        System.out.println("\n11: Testing set Password with invalid input...");

        try {
            //Create test user
            User testUser = new User("username", "password123");

            // Test for null input
            String nullPassword = null;
            assertThrows(InvalidPasswordException.class, () -> testUser.setPassword(nullPassword));

            // Test for empty string
            String emptyPassword = "";
            assertThrows(InvalidPasswordException.class, () -> testUser.setPassword(emptyPassword));

            // Test for password length less than minimum length
            String shortPassword = "abc1234";
            assertThrows(InvalidPasswordException.class, () -> testUser.setPassword(shortPassword));

            // Test for password length greater than maximum length
            String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
            assertThrows(InvalidPasswordException.class, () -> testUser.setPassword(longPassword));
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testSetCurrentRentals_ValidInput() {
        System.out.println("\n12: Testing set and get CurrentRentals with valid input...");

        try {
            User testUser = new User("username", "password123");
            int currentRentals = 2;
            testUser.setCurrentRentals(currentRentals);
            assertEquals(currentRentals, testUser.getCurrentRentals());
        } catch (ConstructionException | RentalNotAllowedException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testSetCurrentRentals_InvalidInput() {
        System.out.println("\n13: Testing setCurrentRentals with invalid input...");

        try {
            //Create test user
            User testUser = new User("username", "password123");

            // Test for currentRentals greater than allowedRentals
            int invalidRentals = testUser.getAllowedRentals() + 1;
            assertThrows(RentalNotAllowedException.class, () -> testUser.setCurrentRentals(invalidRentals));
            assertThrows(RentalNotAllowedException.class, () -> testUser.setCurrentRentals(-1));
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testSetLateFee_InvalidInput() {
        System.out.println("\n14: Testing set LateFee with invalid input...");

        try {
            User testUser = new User("username", "password123");
            double lateFee = -1.0;
            assertThrows(InvalidLateFeeException.class, () -> testUser.setLateFee(lateFee));
        } catch (ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(15)
    void testSetCurrentRentals_withinAllowedRentals() {
        System.out.println("\n15: Testing setCurrentRentals method with current rentals within allowed rentals...");

        try {
            // Instantiate a User
            User user = new User("username", "password");
            // Set current rentals within the limit
            user.setCurrentRentals(4);
            // Check if allowedToRent is true
            assertTrue(user.isAllowedToRent(), "User should be allowed to rent when current rentals are " +
                    "less than allowed");
        } catch (ConstructionException | RentalNotAllowedException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(16)
    void testSetCurrentRentals_equalToAllowedRentals() {
        System.out.println("\n16: Testing setCurrentRentals method with current rentals equal to allowed rentals...");

        try {
            // Instantiate a User
            User user = new User("username", "password");
            // Set current rentals equal to allowed rentals
            user.setCurrentRentals(5);
            // Check if allowedToRent is false
            assertFalse(user.isAllowedToRent(), "User should not be allowed to rent when current rentals " +
                    "equal to allowed rentals");
        } catch (ConstructionException | RentalNotAllowedException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(17)
    void testSetLateFee_positiveLateFee() {
        System.out.println("\n17: Testing setLateFee method with positive late fee...");

        try {
            // Instantiate a User
            User user = new User("username", "password");
            // Set late fee to a positive value
            user.setLateFee(1);
            // Check if allowedToRent is false
            assertFalse(user.isAllowedToRent(), "User should not be allowed to rent when there is a late fee");
        } catch (ConstructionException | InvalidLateFeeException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(18)
    void testSetLateFee_zeroLateFee() {
        System.out.println("\n18: Testing setLateFee method with zero late fee...");

        try {
            // Instantiate a User
            User user = new User("username", "password");
            //Set late fee to 1
            user.setLateFee(1);
            //Verify user is not allowed to rent
            assertFalse(user.isAllowedToRent());
            // Set late fee to 0
            user.setLateFee(0);
            // Check if allowedToRent is true
            assertTrue(user.isAllowedToRent(), "User should be allowed to rent when late fee is zero");
        } catch (ConstructionException | InvalidLateFeeException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(19)
    void testSetAllowedToRent_invalidChange() {
        System.out.println("\n19: Testing setAllowedToRent method with an invalid rental status change...");

        try {
            // Instantiate a User
            User user = new User("username", "password");
            user.setLateFee(0);
            user.setCurrentRentals(1);
            // Attempt to disallow rentals
            assertThrows(InvalidRentalStatusChangeException.class, () -> user.setAllowedToRent(false));
        } catch (ConstructionException | InvalidLateFeeException | RentalNotAllowedException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


}