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
    void testConstructor() {
        System.out.println("\n1: Testing User Constructor...");

        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "password");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "password");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new User("username", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new User("username", "");
        });

        User user = new User("username", "password");
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());

        System.out.println("Test finished.");
    }

    @Test
    @Order(2)
    void testSetUserID() {
        System.out.println("\n2: Testing setUserID method...");

        User user = new User("username", "password");
        assertThrows(IllegalArgumentException.class, () -> {
            user.setUserID(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            user.setUserID(-1);
        });

        user.setUserID(1);
        assertEquals(1, user.getUserID());

        System.out.println("Test finished.");
    }

    @Test
    @Order(3)
    void testSetUsername() {
        System.out.println("\n3: Testing setUsername method...");

        User user = new User("username", "password");
        assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("");
        });

        user.setUsername("new_username");
        assertEquals("new_username", user.getUsername());

        System.out.println("Test finished.");
    }

    @Test
    @Order(4)
    void testSetPassword() {
        System.out.println("\n4: Testing setPassword method...");

        User user = new User("username", "password");
        assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword("");
        });

        user.setPassword("new_password");
        assertEquals("new_password", user.getPassword());

        System.out.println("Test finished.");
    }
}