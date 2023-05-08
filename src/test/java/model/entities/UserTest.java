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
    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields.
     */
    @Test
    @Order(1)
    void testUserGettersAndSetters() {
        System.out.println("\nTesting User class...");
        // Arrange
        String expectedUsername = "testUsername";
        String expectedPassword = "testPassword";
        int expectedUserID = 1;

        // Act
        User user = new User(expectedUsername, expectedPassword);
        user.setUserID(expectedUserID);

        // Assert
        assertEquals(expectedUserID, user.getUserID(), "UserID does not match the expected value");
        assertEquals(expectedUsername, user.getUsername(), "Username does not match the expected value");
        assertEquals(expectedPassword, user.getPassword(), "Password does not match the expected value");

        System.out.println("Test finished!");
    }
}