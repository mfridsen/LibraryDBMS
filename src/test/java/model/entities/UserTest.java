package model.entities;

import edu.groupeighteen.librarydbms.model.entities.User;
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

        // Arrange
        String username = "validUsername";
        String password = "validPassword1";

        // Act
        User testUser = new User(username, password);

        // Assert
        assertEquals(username, testUser.getUsername());
        assertEquals(password, testUser.getPassword());
        assertEquals(User.DEFAULT_ALLOWED_RENTALS, testUser.getAllowedRentals());
        assertEquals(0, testUser.getCurrentRentals());
        assertEquals(0.0, testUser.getLateFee());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testUserCreationConstructor_InvalidInput() {
        System.out.println("\n2: Testing User creation constructor with invalid input...");

        // Arrange
        String username = "";
        String password = "shrt";

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new User(username, password));
        assertThrows(IllegalArgumentException.class, () -> new User(username, password));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testUserRetrievalConstructor_ValidInput() {
        System.out.println("\n3: Testing User retrieval constructor with valid input...");

        // Arrange
        int userID = 1;
        String username = "validUsername";
        String password = "validPassword1";
        int allowedRentals = 5;
        int currentRentals = 2;
        double lateFee = 10.0;

        // Act
        User testUser = new User(userID, username, password, allowedRentals, currentRentals, lateFee);

        // Assert
        assertEquals(userID, testUser.getUserID());
        assertEquals(username, testUser.getUsername());
        assertEquals(password, testUser.getPassword());
        assertEquals(allowedRentals, testUser.getAllowedRentals());
        assertEquals(currentRentals, testUser.getCurrentRentals());
        assertEquals(lateFee, testUser.getLateFee());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testUserRetrievalConstructor_InvalidInput() {
        System.out.println("\n4: Testing User retrieval constructor with invalid input...");

        // Arrange
        int userID = 0;
        String username = "";
        String password = "shrt";
        int allowedRentals = 5;
        int currentRentals = 2;
        double lateFee = 10.0;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new User(userID, username, password, allowedRentals, currentRentals, lateFee));
        assertThrows(IllegalArgumentException.class, () -> new User(userID, username, password, allowedRentals, currentRentals, lateFee));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testUserCopyConstructor_ValidInput() {
        System.out.println("\n5: Testing User copy constructor with valid input...");

        // Arrange
        User originalUser = new User(1, "validUsername", "validPassword1", 5, 2, 10.0);

        // Act
        User copiedUser = new User(originalUser);

        // Assert
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

        // Arrange
        User testUser = new User("username", "password123");
        int userID = 1;

        // Act
        testUser.setUserID(userID);

        // Assert
        assertEquals(userID, testUser.getUserID());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testSetUserID_InvalidInput() {
        System.out.println("\n7: Testing set UserID with invalid input...");

        // Arrange
        User testUser = new User("username", "password123");
        int userID = 0;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> testUser.setUserID(userID));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetUsername_ValidInput() {
        System.out.println("\n8: Testing set and get Username with valid input...");

        // Arrange
        User testUser = new User("username", "password123");
        String username = "newUsername";

        // Act
        testUser.setUsername(username);

        // Assert
        assertEquals(username, testUser.getUsername());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetUsername_InvalidInput() {
        System.out.println("\n9: Testing set Username with invalid input...");

        // Arrange
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

        // Arrange
        User testUser = new User("username", "password123");
        String password = "newPassword123";

        // Act
        testUser.setPassword(password);

        // Assert
        assertEquals(password, testUser.getPassword());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testSetPassword_InvalidInput() {
        System.out.println("\n11: Testing set Password with invalid input...");

        // Arrange
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

        // Arrange
        User testUser = new User("username", "password123");
        int currentRentals = 2;

        // Act
        testUser.setCurrentRentals(currentRentals);

        // Assert
        assertEquals(currentRentals, testUser.getCurrentRentals());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testSetCurrentRentals_InvalidInput() {
        System.out.println("\n13: Testing setCurrentRentals with invalid input...");

        // Arrange
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

        // Arrange
        User testUser = new User("username", "password123");
        double lateFee = -1.0;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> testUser.setLateFee(lateFee));

        System.out.println("\nTEST FINISHED.");
    }



}