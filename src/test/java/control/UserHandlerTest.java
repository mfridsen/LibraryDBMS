package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        setupTestTablesAndData();
    }

    @Override
    void setupTestTablesAndData() {
        super.setupTestTablesAndData();
        try {
            //Needed or else anything that checks the storedUsernames will fail
            UserHandler.syncUsernames();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO-future make all tests more verbose

    /**
     * Tests the setup method in UserHandler.
     */
    @Test
    @Order(1)
    void testSetup() {
        System.out.println("\n1: Testing to setup UserHandler...");
        try {
            UserHandler.setup();
            assertFalse(UserHandler.getStoredUsernames().isEmpty());
            UserHandler.printUsernames();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        System.out.println("Test finished.");
    }

    @Test
    @Order(2)
    void testSaveUser() {
        System.out.println("\n2: Testing to save a user to database...");
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
        System.out.println("Test finished.");
    }

    /**
     *
     */
    @Test
    @Order(3)
    void testCreateNewUser() {
        System.out.println("\n3: Testing to create a new user...");
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
        System.out.println("Test finished.");
    }

    /**
     * This test case first creates a new user, then checks that the login method returns true when correct
     * credentials are provided, false when the password is incorrect, and false when the user doesn't exist.
     */
    @Test
    @Order(4)
    void testLogin() {
        System.out.println("\n4: Testing the login method...");
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

        System.out.println("Test finished.");
    }

    /**
     * This test creates a new user, then retrieves that user using getUserByID and checks that the returned user has
     * the same ID, username, and password as the original user.
     *
     * If the test fails at any point, it will throw an assertion error, which will cause the test to fail and give
     * you a message indicating at which point the test failed.
     */
    @Test
    @Order(5)
    void testGetUserByID() {
        System.out.println("\n5: Testing to get a user by userID...");

        // Create a new user to test with
        User testUser = UserHandler.createNewUser("test_username", "test_password");
        assertNotNull(testUser, "Test user should not be null");

        // Fetch the user from the database using the method to test
        User fetchedUser = UserHandler.getUserByID(testUser.getUserID());
        assertNotNull(fetchedUser, "Fetched user should not be null");

        // Check that the fetched user is the same as the test user
        assertEquals(testUser.getUserID(), fetchedUser.getUserID(), "User IDs should match");
        assertEquals(testUser.getUsername(), fetchedUser.getUsername(), "Usernames should match");
        assertEquals(testUser.getPassword(), fetchedUser.getPassword(), "Passwords should match");
        System.out.println("Test finished.");
    }

    /**
     * In this test, we first create a new user. Then we retrieve the user by username and verify that the
     * retrieved user matches the created user in username, password, and user ID. After the test, we delete the
     * created user from the database to clean up. If there's any exception during the test, we fail the test and
     * print the exception.
     */
    @Test
    @Order(6)
    void testGetUserByUsername() {
        System.out.println("\n6: Testing to get a user by username...");
        // Create a new user for testing
        String username = "test_username";
        String password = "test_password";
        User newUser = UserHandler.createNewUser(username, password);
        assertNotNull(newUser, "New user should not be null");

        // Test getUserByUsername
        User retrievedUser = UserHandler.getUserByUsername(username);
        assertNotNull(retrievedUser, "Retrieved user should not be null");
        assertEquals(newUser.getUsername(), retrievedUser.getUsername(), "Usernames should match");
        assertEquals(newUser.getPassword(), retrievedUser.getPassword(), "Passwords should match");
        assertEquals(newUser.getUserID(), retrievedUser.getUserID(), "User IDs should match");

        // Clean up
        String sql = "DELETE FROM users WHERE userID = ?";
        String[] params = {String.valueOf(newUser.getUserID())};
        DatabaseHandler.executePreparedQuery(sql, params, Statement.NO_GENERATED_KEYS);
        System.out.println("Test finished.");
    }

    /**
     * Tests the updateUser method in UserHandler class.
     * <p>
     * This test does the following:
     * 1. Creates a new user and saves it to the database.
     * 2. Updates the details of the user object.
     * 3. Calls the updateUser method to update the user's details in the database.
     * 4. Retrieves the updated user from the database and checks if the details match the updated user object.
     * <p>
     * The test will pass if the updateUser method successfully updates the user's details in the database,
     * and the retrieved user's details match the updated user object.
     */
    @Test
    @Order(7)
    void testUpdateUser() {
        System.out.println("\n7: Testing to update a user...");
        // Create a new user and save it in the database
        User user = UserHandler.createNewUser("original_username", "original_password");
        assertNotNull(user);

        // Update the details of the user object
        user.setUsername("updated_username");
        user.setPassword("updated_password");

        try {
            // Call the updateUser method
            boolean isUpdated = UserHandler.updateUser(user);
            assertTrue(isUpdated, "User should be successfully updated.");

            // Retrieve the updated user from the database
            User retrievedUser = UserHandler.getUserByID(user.getUserID());

            // Check if the details match the updated user object
            assertNotNull(retrievedUser, "Retrieved user should not be null.");
            assertEquals(user.getUsername(), retrievedUser.getUsername(), "Usernames should match.");
            assertEquals(user.getPassword(), retrievedUser.getPassword(), "Passwords should match.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        System.out.println("Test finished.");
    }

    /**
     * Tests the deleteUser method in UserHandler class.
     * <p>
     * This test does the following:
     * 1. Creates a new user and saves it to the database.
     * 2. Calls the deleteUser method to delete the user from the database.
     * 3. Checks if the user's username is removed from the storedUsernames list.
     * 4. Retrieves the user by username from the database and checks if it's null.
     * <p>
     * The test will pass if the deleteUser method successfully deletes the user from the database and the storedUsernames list,
     * and the retrieved user is null.
     */
    @Test
    @Order(8)
    void testDeleteUser() {
        System.out.println("\n8: Testing to get a user by ID...");
        User userToDelete = UserHandler.createNewUser("test_username", "test_password");
        assertNotNull(userToDelete);
        assertTrue(UserHandler.getStoredUsernames().contains(userToDelete.getUsername()));
        try {
            assertTrue(UserHandler.deleteUser(userToDelete));
            assertFalse(UserHandler.getStoredUsernames().contains(userToDelete.getUsername()));
            assertNull(UserHandler.getUserByUsername(userToDelete.getUsername()));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        System.out.println("Test finished.");
    }
}