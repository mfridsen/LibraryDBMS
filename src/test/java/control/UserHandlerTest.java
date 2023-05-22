package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
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

        try {
            String username = "ValidUser";
            String password = "validPassword123";
            User newUser = UserHandler.createNewUser(username, password);

            assertNotNull(newUser, "New user should be created");
            assertEquals(username, newUser.getUsername(), "Username should match input");
            assertEquals(password, newUser.getPassword(), "Password should match input");
            assertEquals(User.DEFAULT_ALLOWED_RENTALS, newUser.getAllowedRentals(), "Default allowed rentals should be set");
            assertEquals(0, newUser.getCurrentRentals(), "Current rentals should be zero");
            assertEquals(0.0, newUser.getLateFee(), "Late fee should be zero");
            assertFalse(newUser.isDeleted());
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            fail("Should not get exception for valid test.");
            e.printStackTrace();
        }

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(2)
    public void testCreateNewUser_DuplicateUsername() {
        System.out.println("\n2: Testing createNewUser with duplicate username...");

        try {
            //Insert first user
            String username = "ValidUser";
            String password = "validPassword123";
            UserHandler.createNewUser(username, password);

            //Check that attempting to create another with same name doesn't work
            assertThrows(InvalidUsernameException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for duplicate username");
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(3)
    public void testCreateNewUser_ShortUsername() {
        System.out.println("\n3: Testing createNewUser with short username...");

        String username = "ab";
        String password = "validPassword123";
        assertThrows(InvalidUsernameException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for short username");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(4)
    public void testCreateNewUser_LongUsername() {
        System.out.println("\n4: Testing createNewUser with long username...");

        String username = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String password = "validPassword123";
        assertThrows(InvalidUsernameException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for long username");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(5)
    public void testCreateNewUser_ShortPassword() {
        System.out.println("\n5: Testing createNewUser with short password...");

        String username = "ValidUser";
        String password = "short";
        assertThrows(InvalidPasswordException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for short password");

        System.out.println("TEST FINISHED.");
    }

    @Test
    @Order(6)
    public void testCreateNewUser_LongPassword() {
        System.out.println("\n6: Testing createNewUser with long password...");

        String username = "ValidUser";
        String password = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(InvalidPasswordException.class, () -> UserHandler.createNewUser(username, password), "Exception should be thrown for long password");

        System.out.println("TEST FINISHED.");
    }

    //SETUP-------------------------------------------------------------------------------------------------------------

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

        // Insert some users into the database without using createNewUser (which automatically increments storedUsernames)
        String query = "INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee, deleted) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String[] params1 = {"user1", "pass1", "5", "0", "0.0", "0"};
        String[] params2 = {"user2", "pass1", "5", "0", "0.0", "0"};
        String[] params3 = {"user3", "pass1", "5", "0", "0.0", "0"};
        DatabaseHandler.executePreparedQuery(query, params1);
        DatabaseHandler.executePreparedQuery(query, params2);
        DatabaseHandler.executePreparedQuery(query, params3);

        // Call the setup method
        UserHandler.setup();

        //Verify that there are the expected amount of users in stored usernames
        assertEquals(3, UserHandler.getStoredUsernames().size());

        System.out.println("\nTEST FINISHED.");
    }

    //GET BY ID---------------------------------------------------------------------------------------------------------

    @Test
    @Order(9)
    void testGetUserByID_ValidUserPresent() {
        System.out.println("\n9: Testing getUserByID method with a valid userID present in database...");

        try {
            String username = "username";
            String password = "password";
            // Insert a user into the database
            UserHandler.createNewUser(username, password);

            // Call the getUserByUsername method and get ID
            User user1 = UserHandler.getUserByUsername(username);
            assertNotNull(user1);
            int existingID = user1.getUserID();

            //Get user by ID
            User user2 = UserHandler.getUserByID(existingID);
            // Verify that a User is returned and not null
            assertNotNull(user2, "A User object should be returned when a valid userID is provided.");
            // Verify that the returned user has the expected fields set correctly
            assertEquals(existingID, user2.getUserID(), "The returned User object should have the userID provided in the getUserByID method.");
            assertEquals(username, user2.getUsername(), "Username should match input");
            assertEquals(password, user2.getPassword(), "Password should match input");
            assertEquals(User.DEFAULT_ALLOWED_RENTALS, user2.getAllowedRentals(), "Default allowed rentals should be set");
            assertEquals(0, user2.getCurrentRentals(), "Current rentals should be zero");
            assertEquals(0.0, user2.getLateFee(), "Late fee should be zero");
            assertFalse(user2.isDeleted());

        } catch (InvalidIDException | InvalidUsernameException | InvalidPasswordException e) {
            fail("Should not thrown an exception when user is retrieved with valid userID.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testGetUserByID_ValidUserNotPresent() {
        System.out.println("\n10: Testing getUserByID method with a valid userID not present in database...");

        try {
            int nonExistentID = 99999;
            // Insert a user into the database
            UserHandler.createNewUser("username", "password");

            //Verify it exists and doesn't have nonExistentID as ID
            User existingUser = UserHandler.getUserByUsername("username");
            assertNotNull(existingUser);
            assertNotEquals(nonExistentID, existingUser.getUserID());

            // Call the getUserByID method with a valid userID that is not present in the database
            assertNull(UserHandler.getUserByID(nonExistentID));
        } catch (InvalidIDException | InvalidUsernameException | InvalidPasswordException e) {
            fail("Should not thrown an exception when user is retrieved with valid userID.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(11)
    void testGetUserByID_InvalidUserID() {
        System.out.println("\n11: Testing getUserByID method with an invalid userID...");

        // Verify that an IllegalArgumentException is thrown when an invalid userID is provided
        assertThrows(InvalidIDException.class, () -> UserHandler.getUserByID(-1), "An IllegalArgumentException should be thrown when the userID is less than or equal to 0.");

        System.out.println("\nTEST FINISHED.");
    }

    //GET BY USERNAME---------------------------------------------------------------------------------------------------

    @Test
    @Order(12)
    void testGetUserByUsername_ValidUserPresent() {
        System.out.println("\n12: Testing getUserByUsername method with a valid username present in database...");

        try {
            String username = "username";
            String password = "password";
            // Insert a user into the database
            UserHandler.createNewUser(username, password);

            // Call the getUserByUsername method with a valid username
            User user = UserHandler.getUserByUsername(username);

            // Verify that a User is returned and not null and has expected values
            assertNotNull(user, "A User object should be returned when a valid username is provided.");
            assertEquals(username, user.getUsername(), "The returned User object should have the username provided in the getUserByUsername method.");
            assertEquals(password, user.getPassword(), "Password should match input");
            assertEquals(User.DEFAULT_ALLOWED_RENTALS, user.getAllowedRentals(), "Default allowed rentals should be set");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be zero");
            assertEquals(0.0, user.getLateFee(), "Late fee should be zero");
            assertFalse(user.isDeleted());

        } catch (InvalidUsernameException | InvalidPasswordException e) {
            fail("No exception should be thrown when retrieving user with a valid username.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testGetUserByUsername_ValidUserNotPresent() {
        System.out.println("\n13: Testing getUserByUsername method with a valid username not present in database...");

        try {
            // Call the getUserByUsername method with a valid username that is not present in the database
            assertNull(UserHandler.getUserByUsername("nonExistentUsername"));
        } catch (InvalidUsernameException e) {
            fail("No exception should be thrown when retrieving user with a valid username.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testGetUserByUsername_NullUsername() {
        System.out.println("\n14: Testing getUserByUsername method with a null username...");

        // Verify that an InvalidUsernameException is thrown when a null username is provided
        assertThrows(InvalidUsernameException.class, () -> UserHandler.getUserByUsername(null), "An InvalidUsernameException should be thrown when the username is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(15)
    void testGetUserByUsername_EmptyUsername() {
        System.out.println("\n15: Testing getUserByUsername method with an empty username...");

        // Verify that an InvalidUsernameException is thrown when an empty username is provided
        assertThrows(InvalidUsernameException.class, () -> UserHandler.getUserByUsername(""), "An InvalidUsernameException should be thrown when the username is empty.");

        System.out.println("\nTEST FINISHED.");
    }


    //TODO-test getUserByEmail x 3


    //TODO-test getUsersByFirstname x 4


    //TODO-test getUsersByLastname x 4

    //TODO 11

    //UPDATE------------------------------------------------------------------------------------------------------------

    @Test
    @Order(27)
    void testUpdateUser_NullNewUser() {
        System.out.println("\n27: Testing updateUser method with null newUser...");

        // Verify that an NullUserException is thrown when newUser is null
        assertThrows(NullUserException.class, () -> UserHandler.updateUser(null), "An IllegalArgumentException should be thrown when newUser is null.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(29)
    void testUpdateUser_SameUsername() {
        System.out.println("\n29: Testing updateUser method with same new and old usernames...");

        try {
            // Create a newUser object
            User newUser = UserHandler.createNewUser("user1", "password1");

            //Verify newUsers username exists in storedUsernames
            assertEquals(1, UserHandler.getStoredUsernames().size());
            assertEquals(newUser.getUsername(), UserHandler.getStoredUsernames().get(0));

            //Update newUser
            UserHandler.updateUser(newUser);

            //Verify only one username exists in storedUsernames, and it's the same
            assertEquals(1, UserHandler.getStoredUsernames().size());
            assertEquals(newUser.getUsername(), UserHandler.getStoredUsernames().get(0));
        } catch (NullUserException | InvalidUsernameException | InvalidPasswordException e) {
            fail("Should not get exception for valid test.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(30)
    void testUpdateUser_NewUsernameNotTaken() {
        System.out.println("\n30: Testing updateUser method with a new username not taken...");

        try {
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
            assertDoesNotThrow(() -> UserHandler.updateUser(newUser), "No exception should be thrown when the new username is not taken.");

            //Assert that still only two usernames exist in storedUsernames, and that they are the correct names
            assertEquals(2, UserHandler.getStoredUsernames().size());
            assertEquals(firstUsername, UserHandler.getStoredUsernames().get(0));
            assertEquals(thirdUsername, UserHandler.getStoredUsernames().get(1));
            assertEquals(newUser.getUsername(), thirdUsername);
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            fail("Should not get exception for valid test.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(31)
    void testUpdateUser_NewUsernameTaken() {
        System.out.println("\n31: Testing updateUser method with a new username already taken...");

        try {
            // Create our first user, 'taking' that username
            String firstUsername = "user1";
            UserHandler.createNewUser(firstUsername, "password1");

            // Create a newUser object with a second username
            String secondUsername = "user2";
            User newUser = UserHandler.createNewUser(secondUsername, "password1");

            // Set username of newUser to firstUsername (which is already taken)
            newUser.setUsername(firstUsername);

            // Assert that an UsernameTakenException is thrown when newUser's username is changed to a username that's already taken
            assertThrows(InvalidUsernameException.class, () -> UserHandler.updateUser(newUser), "An IllegalArgumentException should be thrown when the new username is already taken.");

            // Assert that still only two usernames exist in storedUsernames, and that they are the correct names
            assertEquals(2, UserHandler.getStoredUsernames().size());
            assertEquals(firstUsername, UserHandler.getStoredUsernames().get(0));
            assertEquals(secondUsername, UserHandler.getStoredUsernames().get(1));
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(32)
    void testUpdateUser_ChangeFields() {
        System.out.println("\n32: Testing updateUser method by changing fields...");

        try {
            // Create a new User
            User newUser = UserHandler.createNewUser("user1", "password1");

            // Change password
            newUser.setPassword("newPassword");
            UserHandler.updateUser(newUser);
            assertEquals("newPassword", newUser.getPassword(), "Password should be updated to 'newPassword'.");

            // Change currentRentals
            newUser.setCurrentRentals(3);
            UserHandler.updateUser(newUser);
            assertEquals(3, newUser.getCurrentRentals(), "Current rentals should be updated to 3.");

            // Change lateFee
            newUser.setLateFee(15.5);
            UserHandler.updateUser(newUser);
            assertEquals(15.5, newUser.getLateFee(), "Late fee should be updated to 15.5.");
        } catch (NullUserException | InvalidUsernameException | InvalidLateFeeException | InvalidRentalException | InvalidPasswordException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //DELETE------------------------------------------------------------------------------------------------------------


    //DELETE FROM TABLE-------------------------------------------------------------------------------------------------

    @Test
    @Order(33)
    void testDeleteUser_NullUser() {
        System.out.println("\n33: Testing deleteUser method with null user...");

        // Try to delete a null user
        assertThrows(NullUserException.class, () -> UserHandler.deleteUser(null), "An NullUserException should be thrown when the user is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(34)
    void testDeleteUser_NonExistingUser() {
        System.out.println("\n34: Testing deleteUser method with a non-existing user...");

        try {
            // Create a User object with an ID that does not exist in the database
            User nonExistingUser = new User("nonExistingUsername", "password");
            nonExistingUser.setUserID(1);

            //Assert User doesn't exist in database
            assertNull(UserHandler.getUserByID(1));

            // Call deleteUser and expect a UserNotFoundException to be thrown
            assertThrows(UserNotFoundException.class, () -> UserHandler.deleteUser(nonExistingUser), "deleteUser should throw UserNotFoundException when the user does not exist.");
        } catch (InvalidIDException | ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(35)
    void testDeleteUser_ValidUser() {
        System.out.println("\n35: Testing deleteUser method with valid user...");

        try {
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
            assertNull(UserHandler.getUserByUsername("user1"));
        } catch (NullUserException | InvalidUsernameException | InvalidPasswordException | UserNotFoundException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //LOGIN------------------------------------------------------------------------------------------------------------

    @Test
    @Order(36)
    void testLogin_EmptyUsername() {
        System.out.println("\n36: Testing login method with an empty username...");

        // Call login with empty username and expect false to be returned
        assertThrows(InvalidUsernameException.class, () -> UserHandler.login("", "password"), "Login should return false when username is empty.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(37)
    void testLogin_NullUsername() {
        System.out.println("\n37: Testing login method with null username...");

        // Call login with null username and expect false to be returned
        assertThrows(InvalidUsernameException.class, () -> UserHandler.login(null, "password"), "Login should return false when username is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(38)
    void testLogin_EmptyPassword() {
        System.out.println("\n38: Testing login method with an empty password...");

        // Call login with empty password and expect false to be returned
        assertThrows(InvalidPasswordException.class, () -> UserHandler.login("username", ""), "Login should return false when password is empty.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(39)
    void testLogin_NullPassword() {
        System.out.println("\n39: Testing login method with null password...");

        // Call login with null password and expect false to be returned
        assertThrows(InvalidPasswordException.class, () -> UserHandler.login("username", null), "Login should return false when password is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(40)
    void testLogin_NonexistentUsername() {
        System.out.println("\n40: Testing login method with nonexistent username...");

        // Call login with a nonexistent username and expect false to be returned
        assertThrows(UserNotFoundException.class, () -> UserHandler.login("nonexistentUser", "password"), "Login should return false when username does not exist.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(41)
    void testLogin_IncorrectPassword() {
        System.out.println("\n41: Testing login method with incorrect password...");

        try {
            // Create a new user
            UserHandler.createNewUser("user1", "password1");
            // Attempt to login with the correct username but incorrect password
            assertFalse(UserHandler.login("user1", "incorrectPassword"), "Login should return false when password is incorrect.");
        } catch (InvalidUsernameException | InvalidPasswordException | UserNotFoundException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(42)
    void testLogin_CorrectUsernamePassword() {
        System.out.println("\n42: Testing login method with correct username and password...");

        try {
            // Create a new user
            UserHandler.createNewUser("user2", "password2");

            // Attempt to login with the correct username and password
            assertTrue(UserHandler.login("user2", "password2"), "Login should return true when username and password are correct.");
        } catch (InvalidUsernameException | InvalidPasswordException | UserNotFoundException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //VALIDATE----------------------------------------------------------------------------------------------------------

    @Test
    @Order(43)
    void testValidateUser_NullUser() {
        System.out.println("\n43: Testing validateUser method with null user...");

        assertThrows(NullUserException.class, () -> UserHandler.validateUser(null, "validPassword"), "Validate should throw an exception for null users.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(44)
    void testValidateUser_NullPassword() {
        System.out.println("\n44: Testing validateUser method with null password...");

        try {
            // Create a new User
            User user = UserHandler.createNewUser("user1", "password1");

            // Call validateUser with null password
            assertThrows(InvalidPasswordException.class, () -> UserHandler.validateUser(user, null), "Validate should throw an exception for null passwords.");
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }


        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(45)
    void testValidateUser_CorrectPassword() {
        System.out.println("\n45: Testing validateUser method with correct password...");

        try {
            // Create a new user
            User user = new User("user1", "password1");

            // Call validateUser with correct password and expect true to be returned
            assertTrue(UserHandler.validateUser(user, "password1"), "ValidateUser should return true when the password is correct.");
        } catch (InvalidPasswordException | ConstructionException | NullUserException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(46)
    void testValidateUser_IncorrectPassword() {
        System.out.println("\n46: Testing validateUser method with incorrect password...");

        try {
            // Create a new user
            User user = new User("user1", "password1");

            // Call validateUser with incorrect password and expect false to be returned
            assertFalse(UserHandler.validateUser(user, "password2"), "ValidateUser should return false when the password is incorrect.");
        } catch (InvalidPasswordException | ConstructionException | NullUserException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}