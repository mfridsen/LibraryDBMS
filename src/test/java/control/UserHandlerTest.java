package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.UserNotFoundException;
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
            UserHandler.reset();
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

        //Insert first user
        UserHandler.createNewUser(username, password);

        //Check that attempting to create another with same name doesn't work
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

    @Test
    @Order(7)
    void testSetup_EmptyDatabase() {
        System.out.println("\n7: Testing setup method with an empty database...");

        // Call the setup method
        UserHandler.setup();

        // Verify that the storedTitles and availableTitles maps are empty
        assertEquals(0, UserHandler.getStoredUsernames().size(), "storedUsernames list should be empty after setup with an empty database");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setup method with some items in the database.
     * This test verifies the behavior of the setup method when there are existing items in the database.
     */
    @Test
    @Order(8)
    void testSetup_WithSomeItemsInDatabase() {
        System.out.println("\n8: Testing setup method with some items in the database...");

        //Check that storedUsernames is empty
        assertEquals(0, UserHandler.getStoredUsernames().size());

        // Insert some items into the database, with one available single and two duplicates of which one is available
        String query = "INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee) " +
                "VALUES (?, ?, ?, ?, ?)";
        String[] params1 = {"user1", "pass1", "5", "0", "0.0"};
        String[] params2 = {"user2", "pass1", "5", "0", "0.0"};
        String[] params3 = {"user3", "pass1", "5", "0", "0.0"};
        DatabaseHandler.executePreparedQuery(query, params1);
        DatabaseHandler.executePreparedQuery(query, params2);
        DatabaseHandler.executePreparedQuery(query, params3);

        // Call the setup method
        UserHandler.setup();

        //Verify that there are the expected amount of users in stored usernames
        assertEquals(3, UserHandler.getStoredUsernames().size());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testGetUserByID_ValidUserPresent() {
        System.out.println("\n9: Testing getUserByID method with a valid userID present in database...");

        // Insert a user into the database
        String insertQuery = "INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee) VALUES (?, ?, ?, ?, ?)";
        String[] insertParams = {"user1", "password1", "5", "0", "0.0"};
        DatabaseHandler.executePreparedQuery(insertQuery, insertParams);

        // Call the getUserByID method with a valid userID (replace with the ID of the inserted user)
        User user = null; // Make sure this ID matches the one that was generated when you inserted the user
        try {
            user = UserHandler.getUserByID(1);
        } catch (UserNotFoundException e) {
            fail("Should not thrown an exception when user is retrieved with valid userID.");
            e.printStackTrace();
        }

        // Verify that a User is returned and not null
        assertNotNull(user, "A User object should be returned when a valid userID is provided.");

        // Verify that the returned user has the expected userID
        assertEquals(1, user.getUserID(), "The returned User object should have the userID provided in the getUserByID method.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(10)
    void testGetUserByID_ValidUserNotPresent() {
        System.out.println("\n10: Testing getUserByID method with a valid userID not present in database...");

        // Insert a user into the database
        String insertQuery = "INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee) VALUES (?, ?, ?, ?, ?)";
        String[] insertParams = {"user1", "pass1", "5", "0", "0.0"};
        DatabaseHandler.executePreparedQuery(insertQuery, insertParams);

        // Call the getUserByID method with a valid userID that is not present in the database
        assertThrows(UserNotFoundException.class, () ->  UserHandler.getUserByID(999999), "No User object should be returned when a valid userID not present in the database is provided.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(11)
    void testGetUserByID_InvalidUserID() {
        System.out.println("\n11: Testing getUserByID method with an invalid userID...");

        // Verify that an IllegalArgumentException is thrown when an invalid userID is provided
        assertThrows(IllegalArgumentException.class, () -> UserHandler.getUserByID(-1), "An IllegalArgumentException should be thrown when the userID is less than or equal to 0.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testGetUserByUsername_ValidUserPresent() {
        System.out.println("\n12: Testing getUserByUsername method with a valid username present in database...");

        // Insert a user into the database
        String insertQuery = "INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee) VALUES (?, ?, ?, ?, ?)";
        String[] insertParams = {"user1", "password1", "5", "0", "0.0"};
        DatabaseHandler.executePreparedQuery(insertQuery, insertParams);

        // Call the getUserByUsername method with a valid username
        User user = null;
        try {
            user = UserHandler.getUserByUsername("user1");
        } catch (UserNotFoundException e) {
            fail("No exception should be thrown when retrieving user with a valid username.");
            e.printStackTrace();
        }

        // Verify that a User is returned and not null
        assertNotNull(user, "A User object should be returned when a valid username is provided.");

        // Verify that the returned user has the expected username
        assertEquals("user1", user.getUsername(), "The returned User object should have the username provided in the getUserByUsername method.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testGetUserByUsername_ValidUserNotPresent() {
        System.out.println("\n13: Testing getUserByUsername method with a valid username not present in database...");

        // Call the getUserByUsername method with a valid username that is not present in the database
        assertThrows(UserNotFoundException.class, () -> UserHandler.getUserByUsername("nonExistentUser"), "No User object should be returned when a valid username not present in the database is provided.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testGetUserByUsername_NullUsername() {
        System.out.println("\n14: Testing getUserByUsername method with a null username...");

        // Verify that an IllegalArgumentException is thrown when a null username is provided
        assertThrows(IllegalArgumentException.class, () -> UserHandler.getUserByUsername(null), "An IllegalArgumentException should be thrown when the username is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(15)
    void testGetUserByUsername_EmptyUsername() {
        System.out.println("\n15: Testing getUserByUsername method with an empty username...");

        // Verify that an IllegalArgumentException is thrown when an empty username is provided
        assertThrows(IllegalArgumentException.class, () -> UserHandler.getUserByUsername(""), "An IllegalArgumentException should be thrown when the username is empty.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(16)
    void testUpdateUser_NullNewUser() {
        System.out.println("\n16: Testing updateUser method with null newUser...");

        // Verify that an IllegalArgumentException is thrown when newUser is null
        assertThrows(IllegalArgumentException.class, () -> UserHandler.updateUser(null, "oldUsername"), "An IllegalArgumentException should be thrown when newUser is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(17)
    void testUpdateUser_NullOrEmptyOldUsername() {
        System.out.println("\n17: Testing updateUser method with null or empty oldUsername...");

        // Create a newUser object
        User newUser = UserHandler.createNewUser("user1", "password1");

        // Verify that an IllegalArgumentException is thrown when oldUsername is null
        assertThrows(IllegalArgumentException.class, () -> UserHandler.updateUser(newUser, null), "An IllegalArgumentException should be thrown when oldUsername is null.");

        // Verify that an IllegalArgumentException is thrown when oldUsername is empty
        assertThrows(IllegalArgumentException.class, () -> UserHandler.updateUser(newUser, ""), "An IllegalArgumentException should be thrown when oldUsername is empty.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(18)
    void testUpdateUser_SameUsername() {
        System.out.println("\n18: Testing updateUser method with same new and old usernames...");

        // Create a newUser object
        User newUser = UserHandler.createNewUser("user1", "password1");

        //Verify newUsers username exists in storedUsernames
        assertEquals(1, UserHandler.getStoredUsernames().size());
        assertEquals(newUser.getUsername(), UserHandler.getStoredUsernames().get(0));

        //Update newUser
        UserHandler.updateUser(newUser, "user1");

        //Verify only one username exists in storedUsernames, and it's the same
        assertEquals(1, UserHandler.getStoredUsernames().size());
        assertEquals(newUser.getUsername(), UserHandler.getStoredUsernames().get(0));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(19)
    void testUpdateUser_NewUsernameNotTaken() {
        System.out.println("\n19: Testing updateUser method with a new username not taken...");

        //Create our first user, 'taking' that username
        String firstUsername = "user1";
        UserHandler.createNewUser(firstUsername, "password1");

        // Create a newUser object with a second username
        String secondUsername = "user2";
        User newUser = UserHandler.createNewUser(secondUsername, "password1");

        //Assert that two usernames exist in storedUsernames, and they are the correct names
        assertEquals(2, UserHandler.getStoredUsernames().size());
        assertEquals(firstUsername, UserHandler.getStoredUsernames().get(0));
        assertEquals(secondUsername, UserHandler.getStoredUsernames().get(1));

        //Set username of newUser to third username
        String thirdUsername = "user3";
        newUser.setUsername(thirdUsername);

        //Assert no exception is thrown when newUser's username is changed to a third username
        assertDoesNotThrow(() -> UserHandler.updateUser(newUser, secondUsername), "No exception should be thrown when the new username is not taken.");

        //Assert that still only two usernames exist in storedUsernames, and that they are the correct names
        assertEquals(2, UserHandler.getStoredUsernames().size());
        assertEquals(firstUsername, UserHandler.getStoredUsernames().get(0));
        assertEquals(thirdUsername, UserHandler.getStoredUsernames().get(1));
        assertEquals(newUser.getUsername(), thirdUsername);

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(20)
    void testUpdateUser_NewUsernameTaken() {
        System.out.println("\n20: Testing updateUser method with a new username already taken...");

        // Create our first user, 'taking' that username
        String firstUsername = "user1";
        UserHandler.createNewUser(firstUsername, "password1");

        // Create a newUser object with a second username
        String secondUsername = "user2";
        User newUser = UserHandler.createNewUser(secondUsername, "password1");

        // Set username of newUser to firstUsername (which is already taken)
        newUser.setUsername(firstUsername);

        // Assert that an IllegalArgumentException is thrown when newUser's username is changed to a username that's already taken
        assertThrows(IllegalArgumentException.class, () -> UserHandler.updateUser(newUser, secondUsername), "An IllegalArgumentException should be thrown when the new username is already taken.");

        // Assert that still only two usernames exist in storedUsernames, and that they are the correct names
        assertEquals(2, UserHandler.getStoredUsernames().size());
        assertEquals(firstUsername, UserHandler.getStoredUsernames().get(0));
        assertEquals(secondUsername, UserHandler.getStoredUsernames().get(1));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(21)
    void testUpdateUser_ChangeFields() {
        System.out.println("\n21: Testing updateUser method by changing fields...");

        // Create a new User
        User newUser = UserHandler.createNewUser("user1", "password1");

        // Change password
        newUser.setPassword("newPassword");
        UserHandler.updateUser(newUser, "user1");
        assertEquals("newPassword", newUser.getPassword(), "Password should be updated to 'newPassword'.");

        // Change currentRentals
        newUser.setCurrentRentals(3);
        UserHandler.updateUser(newUser, "user1");
        assertEquals(3, newUser.getCurrentRentals(), "Current rentals should be updated to 3.");

        // Change lateFee
        newUser.setLateFee(15.5);
        UserHandler.updateUser(newUser, "user1");
        assertEquals(15.5, newUser.getLateFee(), "Late fee should be updated to 15.5.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(22)
    void testDeleteUser_ValidUser() {
        System.out.println("\n22: Testing deleteUser method with valid user...");

        // Create a new User
        User newUser = UserHandler.createNewUser("user1", "password1");

        //Assert that a username exists in storedUsernames
        assertEquals(1, UserHandler.getStoredUsernames().size());
        assertEquals(newUser.getUsername(), UserHandler.getStoredUsernames().get(0));

        // Delete the user
        UserHandler.deleteUser(newUser);

        //Assert that no username exists in storedUsernames
        assertEquals(0, UserHandler.getStoredUsernames().size());

        // Verify that the user has been deleted from the database
        assertThrows(UserNotFoundException.class, () -> UserHandler.getUserByUsername("user1"), "User should have been deleted from the database.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(23)
    void testDeleteUser_NullUser() {
        System.out.println("\n23: Testing deleteUser method with null user...");

        // Try to delete a null user
        assertThrows(IllegalArgumentException.class, () -> UserHandler.deleteUser(null), "An IllegalArgumentException should be thrown when the user is null.");

        System.out.println("\nTEST FINISHED.");
    }

}