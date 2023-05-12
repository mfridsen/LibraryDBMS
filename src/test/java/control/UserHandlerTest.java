package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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
        DatabaseHandler.setVerbose(false);
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/create_tables.sql");
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
        DatabaseHandler.setVerbose(true);
    }

    /**
     * Tests the setup method in UserHandler.
     */
    @Test
    @Order(1)
    void testSetup() throws SQLException{
        System.out.println("1: Testing to setup UserHandler...");
        UserHandler.setup(connection);
        assertFalse(UserHandler.getStoredUsernames().isEmpty());
        UserHandler.printUsernames();
    }

    @Test
    @Order(2)
    void testSaveUser() {
        // Create a User object
        User user = new User("test_username", "test_password");

        try {
            // Save the user to the database
            int generatedId = UserHandler.saveUser(user);

            // Verify the generated ID is valid
            assertTrue(generatedId > 0, "Generated ID should be greater than 0");

            // Retrieve the saved user from the database
            String sql = "SELECT * FROM users WHERE userID = ?";
            String[] params = {String.valueOf(generatedId)};
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);

            // Verify the retrieved user data
            ResultSet resultSet = queryResult.getResultSet();
            assertTrue(resultSet.next(), "Result set should have at least one row");
            assertEquals(user.getUsername(), resultSet.getString("username"), "Username should match");
            assertEquals(user.getPassword(), resultSet.getString("password"), "Password should match");

            DatabaseHandler.executeQuery("SELECT * FROM users");

            // Close resources
            queryResult.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    @Order(3)
    void testCreateNewUser() {
        System.out.println("\n2: Testing to create a new user...");
        String username = "example_username";
        String[] params = {username};
        User newUser = UserHandler.createNewUser(username, "example_password");
        assertNotNull(newUser);
        assertTrue(UserHandler.getStoredUsernames().contains(newUser.getUsername()));

        String query = "SELECT username FROM users WHERE username = ?";
        QueryResult result = DatabaseHandler.executePreparedQuery(query, params);
        try {
            if (result.getResultSet().next()) { // call next() to move cursor to the first row
                String storedUsername = result.getResultSet().getString("username");
                assertEquals(storedUsername, username);
            } else {
                fail("No user found with username: " + username);
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    @Order(4)
    void testLogin() {
        // Set up test user data
        String username = "test_user";
        String password = "test_password";

        // Create a new user
        User testUser = UserHandler.createNewUser(username, password);

        // Ensure the user was created
        assertNotNull(testUser);

        // Test login with correct credentials
        assertTrue(UserHandler.login(username, password), "Login with correct credentials should return true");

        // Test login with incorrect password
        assertFalse(UserHandler.login(username, "wrong_password"), "Login with incorrect password should return false");

        // Test login with non-existing user
        assertFalse(UserHandler.login("non_existing_user", password), "Login with non-existing user should return false");

        // Test login with null username
        assertFalse(UserHandler.login(null, "password"), "Login with null username should return false");

        // Test login with null password
        assertFalse(UserHandler.login("username", null), "Login with null password should return false");

        // Test login with empty username
        assertFalse(UserHandler.login("", "password"), "Login with empty username should return false");

        // Test login with empty password
        assertFalse(UserHandler.login("username", ""), "Login with empty password should return false");
    }

}