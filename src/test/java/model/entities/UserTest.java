package model.entities;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
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
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the User class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {


    @Test
    @Order(1)
    void testUserCreationConstructor_ValidInput() {
        System.out.println("\n1: Testing User creation constructor with valid input...");

        try {
            String username = "validUsername";
            String password = "validPassword1";

            User testUser = new User(username, password);

            assertEquals(username, testUser.getUsername());
            assertEquals(password, testUser.getPassword());
            assertEquals(User.DEFAULT_ALLOWED_RENTALS, testUser.getAllowedRentals());
            assertEquals(0, testUser.getCurrentRentals());
            assertEquals(0.0, testUser.getLateFee());
        } catch (InvalidUsernameException | InvalidPasswordException e) {
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
        assertThrows(InvalidUsernameException.class, () -> new User(null, validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, null));

        //Empty username and password
        assertThrows(InvalidUsernameException.class, () -> new User("", validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, ""));

        //Short username and password
        assertThrows(InvalidUsernameException.class, () -> new User("us", validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, "shrt"));

        //Too long username and password
        String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(InvalidUsernameException.class, () -> new User(longUsername, validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, longPassword));

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
            double lateFee = 10.0;

            User testUser = new User(userID, username, password, allowedRentals, currentRentals, lateFee);

            assertEquals(userID, testUser.getUserID());
            assertEquals(username, testUser.getUsername());
            assertEquals(password, testUser.getPassword());
            assertEquals(allowedRentals, testUser.getAllowedRentals());
            assertEquals(currentRentals, testUser.getCurrentRentals());
            assertEquals(lateFee, testUser.getLateFee());
        } catch (InvalidUserIDException | InvalidRentalException | InvalidLateFeeException | InvalidUsernameException | InvalidPasswordException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testUserRetrievalConstructor_InvalidInput() {
        System.out.println("\n4: Testing User retrieval constructor with invalid input...");

        int userID = 0;
        int allowedRentals = 5;
        int currentRentals = 2;
        double lateFee = 10.0;

        String validUsername = "username";
        String validPassword = "password";

        //Null username and password
        assertThrows(InvalidUsernameException.class, () -> new User(null, validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, null));

        //Empty username and password
        assertThrows(InvalidUsernameException.class, () -> new User("", validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, ""));

        //Short username and password
        assertThrows(InvalidUsernameException.class, () -> new User("us", validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, "shrt"));

        //Too long username and password
        String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(InvalidUsernameException.class, () -> new User(longUsername, validPassword));
        assertThrows(InvalidPasswordException.class, () -> new User(validUsername, longPassword));

        assertThrows(IllegalArgumentException.class, () -> new User(userID, username, password, allowedRentals, currentRentals, lateFee));
        assertThrows(IllegalArgumentException.class, () -> new User(userID, username, password, allowedRentals, currentRentals, lateFee));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testUserCopyConstructor_ValidInput() {
        System.out.println("\n5: Testing User copy constructor with valid input...");

        
        User originalUser = new User(1, "validUsername", "validPassword1", 5, 2, 10.0);

        
        User copiedUser = new User(originalUser);

        
        assertEquals(originalUser.getUserID(), copiedUser.getUserID());
        assertEquals(originalUser.getUsername(), copiedUser.getUsername());
        assertEquals(originalUser.getPassword(), copiedUser.getPassword());
        assertEquals(originalUser.getAllowedRentals(), copiedUser.getAllowedRentals());
        assertEquals(originalUser.getCurrentRentals(), copiedUser.getCurrentRentals());
        assertEquals(originalUser.getLateFee(), copiedUser.getLateFee());

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(6)
    void testSetUserID_ValidInput() {
        System.out.println("\n6: Testing set and get UserID with valid input...");

        
        User testUser = new User("username", "password123");
        int userID = 1;

        
        testUser.setUserID(userID);

        
        assertEquals(userID, testUser.getUserID());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testSetUserID_InvalidInput() {
        System.out.println("\n7: Testing set UserID with invalid input...");

        
        User testUser = new User("username", "password123");
        int userID = 0;

        
        assertThrows(IllegalArgumentException.class, () -> testUser.setUserID(userID));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetUsername_ValidInput() {
        System.out.println("\n8: Testing set and get Username with valid input...");

        
        User testUser = new User("username", "password123");
        String username = "newUsername";

        
        testUser.setUsername(username);

        
        assertEquals(username, testUser.getUsername());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetUsername_InvalidInput() {
        System.out.println("\n9: Testing set Username with invalid input...");

        
        User testUser = new User("username", "password123");

        // Test for null input
        String nullUsername = null;
        assertThrows(IllegalArgumentException.class, () -> testUser.setUsername(nullUsername));

        // Test for empty string
        String emptyUsername = "";
        assertThrows(IllegalArgumentException.class, () -> testUser.setUsername(emptyUsername));

        // Test for username length less than minimum length
        String shortUsername = "ab";
        assertThrows(IllegalArgumentException.class, () -> testUser.setUsername(shortUsername));

        // Test for username length greater than maximum length
        String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> testUser.setUsername(longUsername));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testSetPassword_ValidInput() {
        System.out.println("\n10: Testing set and get Password with valid input...");

        
        User testUser = new User("username", "password123");
        String password = "newPassword123";

        
        testUser.setPassword(password);

        
        assertEquals(password, testUser.getPassword());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testSetPassword_InvalidInput() {
        System.out.println("\n11: Testing set Password with invalid input...");

        
        User testUser = new User("username", "password123");

        // Test for null input
        String nullPassword = null;
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(nullPassword));

        // Test for empty string
        String emptyPassword = "";
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(emptyPassword));

        // Test for password length less than minimum length
        String shortPassword = "abc1234";
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(shortPassword));

        // Test for password length greater than maximum length
        String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> testUser.setPassword(longPassword));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testSetCurrentRentals_ValidInput() {
        System.out.println("\n12: Testing set and get CurrentRentals with valid input...");

        
        User testUser = new User("username", "password123");
        int currentRentals = 2;

        
        testUser.setCurrentRentals(currentRentals);

        
        assertEquals(currentRentals, testUser.getCurrentRentals());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testSetCurrentRentals_InvalidInput() {
        System.out.println("\n13: Testing setCurrentRentals with invalid input...");

        
        User testUser = new User("username", "password123");

        // Test for currentRentals greater than allowedRentals
        int invalidRentals = testUser.getAllowedRentals() + 1;
        assertThrows(IllegalArgumentException.class, () -> testUser.setCurrentRentals(invalidRentals));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testSetLateFee_InvalidInput() {
        System.out.println("\n14: Testing set LateFee with invalid input...");

        
        User testUser = new User("username", "password123");
        double lateFee = -1.0;

        
        assertThrows(IllegalArgumentException.class, () -> testUser.setLateFee(lateFee));

        System.out.println("\nTEST FINISHED.");
    }



}