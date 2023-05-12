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
    //TODO-future javadoc all tests properly

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
        System.out.println("\n1: Testing saveUser method...");

        // Test data
        String username = "Test User";
        String password = "Test Password";
        User testUser = new User(username, password);

        // Test: Saving a null user should return 0
        try {
            int userId = UserHandler.saveUser(null);
            assertEquals(0, userId, "Saving a null user should return 0.");
        } catch (SQLException e) {
            fail("Saving a null user should not throw an exception.");
        }

        // Test: Saving a user with a null username should return 0
        try {
            testUser.setUsername(null);
            int userId = UserHandler.saveUser(testUser);
            assertEquals(0, userId, "Saving a user with a null username should return 0.");
        } catch (SQLException e) {
            fail("Saving a user with a null username should not throw an exception.");
        }

        // Test: Saving a user with an empty username should return 0
        try {
            testUser.setUsername("");
            int userId = UserHandler.saveUser(testUser);
            assertEquals(0, userId, "Saving a user with an empty username should return 0.");
        } catch (SQLException e) {
            fail("Saving a user with an empty username should not throw an exception.");
        }

        // Test: Saving a user with a null password should return 0
        try {
            testUser.setUsername(username); // Reset the username
            testUser.setPassword(null);
            int userId = UserHandler.saveUser(testUser);
            assertEquals(0, userId, "Saving a user with a null password should return 0.");
        } catch (SQLException e) {
            fail("Saving a user with a null password should not throw an exception.");
        }

        // Test: Saving a user with an empty password should return 0
        try {
            testUser.setPassword("");
            int userId = UserHandler.saveUser(testUser);
            assertEquals(0, userId, "Saving a user with an empty password should return 0.");
        } catch (SQLException e) {
            fail("Saving a user with an empty password should not throw an exception.");
        }

        // Test: Saving a valid user should return a valid user ID
        try {
            testUser.setUsername(username); // Reset the username
            testUser.setPassword(password); // Reset the password
            int userId = UserHandler.saveUser(testUser);
            assertTrue(userId > 0, "Saving a valid user should return a valid user ID.");

            // Now, retrieve the saved user to verify that it was saved correctly.
            User savedUser = UserHandler.getUserByID(userId);
            assertNotNull(savedUser, "The saved user should not be null.");
            assertEquals(username, savedUser.getUsername(), "The saved user's username should match the original username.");
            assertEquals(password, savedUser.getPassword(), "The saved user's password should match the original password.");
        } catch (SQLException e) {
            fail("Saving a valid user should not throw an exception. Error: " + e.getMessage());
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
        try {
            User newUser = UserHandler.createNewUser(username, "example_password");
            assertNotNull(newUser);
            assertTrue(UserHandler.getStoredUsernames().contains(newUser.getUsername()));

            String query = "SELECT username FROM users WHERE username = ?";
            QueryResult result = DatabaseHandler.executePreparedQuery(query, params);
            if (result.getResultSet().next()) { // call next() to move cursor to the first row
                String storedUsername = result.getResultSet().getString("username");
                assertEquals(storedUsername, username);
            } else {
                fail("No user found with username: " + username);
            }
            result.close();

            //Verify that we don't create null users
            // Test create with null username
            assertNull(UserHandler.createNewUser(null, "password"), "Login with null username should return false");

            // Test create with null password
            assertNull(UserHandler.createNewUser("username", null), "Login with null password should return false");

            // Test create with empty username
            assertNull(UserHandler.createNewUser("", "password"), "Login with empty username should return false");

            // Test create with empty password
            assertNull(UserHandler.createNewUser("username", ""), "Login with empty password should return false");

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

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

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

        try {
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

            //Verify that invalid userIDs return null
            assertNull(UserHandler.getUserByID(0));
            assertNull(UserHandler.getUserByID(-1));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
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
        try {
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

            //Verify that empty usernames return false
            assertNull(UserHandler.getUserByUsername(null));
            assertNull(UserHandler.getUserByUsername(""));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
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
        try {
            // Create a new user and save it in the database
            User user = UserHandler.createNewUser("original_username", "original_password");
            assertNotNull(user);

            // Update the details of the user object
            user.setUsername("updated_username");
            user.setPassword("updated_password");
            // Call the updateUser method
            boolean isUpdated = UserHandler.updateUser(user);
            assertTrue(isUpdated, "User should be successfully updated.");

            // Retrieve the updated user from the database
            User retrievedUser = UserHandler.getUserByID(user.getUserID());

            // Check if the details match the updated user object
            assertNotNull(retrievedUser, "Retrieved user should not be null.");
            assertEquals(user.getUsername(), retrievedUser.getUsername(), "Usernames should match.");
            assertEquals(user.getPassword(), retrievedUser.getPassword(), "Passwords should match.");

            //Check that null users aren't updated
            assertFalse(UserHandler.updateUser(null));

            //Check that users with invalid userID can't be deleted
            user = new User("asd", "asd");
            user.setUserID(0);
            assertFalse(UserHandler.updateUser(user));
            user.setUserID(-1);
            assertFalse(UserHandler.updateUser(user));
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
        try {
            User userToDelete = UserHandler.createNewUser("test_username", "test_password");
            assertNotNull(userToDelete);
            assertTrue(UserHandler.getStoredUsernames().contains(userToDelete.getUsername()));
            assertTrue(UserHandler.deleteUser(userToDelete));
            assertFalse(UserHandler.getStoredUsernames().contains(userToDelete.getUsername()));
            assertNull(UserHandler.getUserByUsername(userToDelete.getUsername()));
            assertFalse(UserHandler.deleteUser(null));
            userToDelete = new User("asd", "asd");
            userToDelete.setUserID(0);
            assertFalse(UserHandler.deleteUser(userToDelete));
            userToDelete.setUserID(-1);
            assertFalse(UserHandler.deleteUser(userToDelete));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
        System.out.println("Test finished.");
    }
}