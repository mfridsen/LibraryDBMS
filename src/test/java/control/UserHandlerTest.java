package control;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/19/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit Test for the UserHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserHandlerTest extends BaseHandlerTest {

    @BeforeEach
    @Override
    void setupAndReset() {
        try {
            setupConnectionAndTables();
            UserHandler.getStoredUsernames().clear();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //TODO-future make all tests more verbose
    //TODO-future javadoc all tests properly


    @Test
    @Order(1)
    public void testCreateNewUser_ValidInput() {
        System.out.println("\n1: Testing createNewUser with valid input...");

        String username = "ValidUser";
        String password = "validPassword123";
        User newUser = UserHandler.createNewUser(username, password);

        assertNotNull(newUser, "New user should be created");
        assertEquals(username, newUser.getUsername(), "Username should match input");
        assertEquals(password, newUser.getPassword(), "Password should match input");
        assertEquals(User.DEFAULT_ALLOWED_RENTALS, newUser.getAllowedRentals(), "Default allowed rentals should be set");
        assertEquals(0, newUser.getCurrentRentals(), "Current rentals should be zero");
        assertEquals(0.0, newUser.getLateFee(), "Late fee should be zero");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(2)
    public void testCreateNewUser_DuplicateUsername() {
        System.out.println("\n2: Testing createNewUser with duplicate username...");

        String username = "ValidUser";
        String password = "validPassword123";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for duplicate username");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(3)
    public void testCreateNewUser_ShortUsername() {
        System.out.println("\n3: Testing createNewUser with short username...");

        String username = "ab";
        String password = "validPassword123";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for short username");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(4)
    public void testCreateNewUser_LongUsername() {
        System.out.println("\n4: Testing createNewUser with long username...");

        String username = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String password = "validPassword123";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for long username");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(5)
    public void testCreateNewUser_ShortPassword() {
        System.out.println("\n5: Testing createNewUser with short password...");

        String username = "ValidUser";
        String password = "short";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for short password");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(6)
    public void testCreateNewUser_LongPassword() {
        System.out.println("\n6: Testing createNewUser with long password...");

        String username = "ValidUser";
        String password = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for long password");

        System.out.println("TEST FINISHED.");
    }

}