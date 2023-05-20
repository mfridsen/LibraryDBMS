package control;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
            UserHandler.syncUsernames();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //TODO-future make all tests more verbose
    //TODO-future javadoc all tests properly


    @Test
     void testCreateNewUser_ValidInput() {
        String username = "ValidUser";
        String password = "validPassword123";
        User newUser = UserHandler.createNewUser(username, password);
        assertNotNull(newUser, "New user should be created");
        assertEquals(username, newUser.getUsername(), "Username should match input");
        assertEquals(password, newUser.getPassword(), "Password should match input");
        assertEquals(User.DEFAULT_ALLOWED_RENTALS, newUser.getAllowedRentals(), "Default allowed rentals should be set");
        assertEquals(0, newUser.getCurrentRentals(), "Current rentals should be zero");
        assertEquals(0.0, newUser.getLateFee(), "Late fee should be zero");
    }

    @Test
     void testCreateNewUser_DuplicateUsername() {
        String username = "ValidUser";
        String password = "validPassword123";
        UserHandler.createNewUser(username, password);
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for duplicate username");
    }

    @Test
     void testCreateNewUser_ShortUsername() {
        String username = "ab";
        String password = "validPassword123";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for short username");
    }

    @Test
     void testCreateNewUser_LongUsername() {
        String username = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String password = "validPassword123";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for long username");
    }

    @Test
     void testCreateNewUser_ShortPassword() {
        String username = "ValidUser";
        String password = "short";
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for short password");
    }

    @Test
     void testCreateNewUser_LongPassword() {
        String username = "ValidUser";
        String password = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for long password");
    }
}