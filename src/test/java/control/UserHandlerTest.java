package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/19/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the UserHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserHandlerTest extends BaseHandlerTest {

    @BeforeEach
    @Override
    void setupAndReset() {
        super.setupAndReset();
        DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/create_tables.sql");
        DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/data/test_data.sql");
    }

    /**
     * Tests the setup method in UserHandler.
     */
    @Test
    @Order(1)
    void testSetup() throws SQLException{
        System.out.println("1: Testing to setup UserHandler...");
        UserHandler.setup(connection);
        assertFalse(UserHandler.getUsernames().isEmpty());
        UserHandler.printUsernames();
    }

    /**
     *
     */
    @Test
    @Order(2)
    void testCreateNewUser() {
        System.out.println("\n2: Testing to create a new user...");
        User newUser = UserHandler.createNewUser("example_username", "example_password");
        if (newUser == null) {
            // Display an error message to the user or take another action
        } else {
            // Continue with the newUser object
        }
    }

    /**
     *
     */
    @Test
    @Order(3)
    void testLogin() {

    }
}