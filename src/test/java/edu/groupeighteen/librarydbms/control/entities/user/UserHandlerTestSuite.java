package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/19/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UserHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Suite
@SelectClasses({
        UserHandlerSetupTest.class,
        CreateNewUserTest.class,
        GetUserByIDTest.class,
        DeleteAndRecoverUserTest.class,
        LoginAndValidationTest.class,
        UpdateUserTest.class,
        GetUserByUsernameTest.class
})
public class UserHandlerTestSuite extends BaseHandlerTest
{


    //SOFT DELETE ------------------------------------------------------------------------------------------------------


    //UNDO DELETE ------------------------------------------------------------------------------------------------------


    //HARD DELETE ------------------------------------------------------------------------------------------------------

    @Test
    @Order(33)
    void testHardDeleteUser_NullUser()
    {
        System.out.println("\n33: Testing deleteUser method with null user...");

        //Try to delete a null user
        assertThrows(NullEntityException.class, () -> UserHandler.hardDeleteUser(null),
                "An NullEntityException should be thrown when the user is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(34)
    void testHardDeleteUser_NonExistingUser()
    {
        System.out.println("\n34: Testing deleteUser method with a non-existing user...");

        try
        {
            //Create a User object with an ID that does not exist in the database
            User nonExistingUser = new User("nonExistingUsername", "password", null, null);
            nonExistingUser.setUserID(1);

            //Assert User doesn't exist in database
            assertNull(UserHandler.getUserByID(1));

            //Call deleteUser and expect a EntityNotFoundException to be thrown
            assertThrows(EntityNotFoundException.class, () -> UserHandler.hardDeleteUser(nonExistingUser),
                    "deleteUser should throw EntityNotFoundException when the user does not exist.");
        }
        catch (InvalidIDException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(35)
    void testHardDeleteUser_ValidUser()
    {
        System.out.println("\n35: Testing deleteUser method with valid user...");

        try
        {
            //Create a new User
            User newUser = UserHandler.createNewUser("user1", "password1", null, null);

            //Assert that a username exists in storedUsernames
            assertEquals(1, UserHandler.getStoredUsernames().size());
            assertEquals(newUser.getUsername(), UserHandler.getStoredUsernames().get(0));

            //Delete the user
            UserHandler.hardDeleteUser(newUser);
            //Assert that no username exists in storedUsernames
            assertEquals(0, UserHandler.getStoredUsernames().size());

            //Verify that the user has been deleted from the database
            assertNull(UserHandler.getUserByUsername("user1"));
        }
        catch (InvalidNameException | CreationException | DeletionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //LOGIN------------------------------------------------------------------------------------------------------------

    @Test
    @Order(36)
    void testLogin_EmptyUsername()
    {
        System.out.println("\n36: Testing login method with an empty username...");

        //Call login with empty username and expect false to be returned
        assertThrows(InvalidNameException.class, () -> UserHandler.login("", "password"),
                "Login should return false when username is empty.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(37)
    void testLogin_NullUsername()
    {
        System.out.println("\n37: Testing login method with null username...");

        //Call login with null username and expect false to be returned
        assertThrows(InvalidNameException.class, () -> UserHandler.login(null, "password"),
                "Login should return false when username is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(38)
    void testLogin_EmptyPassword()
    {
        System.out.println("\n38: Testing login method with an empty password...");

        //Call login with empty password and expect false to be returned
        assertThrows(InvalidPasswordException.class, () -> UserHandler.login("username", ""),
                "Login should return false when password is empty.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(39)
    void testLogin_NullPassword()
    {
        System.out.println("\n39: Testing login method with null password...");

        //Call login with null password and expect false to be returned
        assertThrows(InvalidPasswordException.class, () -> UserHandler.login("username", null),
                "Login should return false when password is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(40)
    void testLogin_NonexistentUsername()
    {
        System.out.println("\n40: Testing login method with nonexistent username...");

        //Call login with a nonexistent username and expect false to be returned
        assertThrows(EntityNotFoundException.class, () -> UserHandler.login("nonexistentUser", "password"),
                "Login should return false when username does not exist.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(41)
    void testLogin_IncorrectPassword()
    {
        System.out.println("\n41: Testing login method with incorrect password...");

        try
        {
            //Create a new user
            UserHandler.createNewUser("user1", "password1", null, null);
            //Attempt to login with the correct username but incorrect password
            assertFalse(UserHandler.login("user1", "incorrectPassword"),
                    "Login should return false when password is incorrect.");
        }
        catch (InvalidNameException | InvalidPasswordException | EntityNotFoundException | CreationException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(42)
    void testLogin_CorrectUsernamePassword()
    {
        System.out.println("\n42: Testing login method with correct username and password...");

        try
        {
            //Create a new user
            UserHandler.createNewUser("user2", "password2", null, null);

            //Attempt to login with the correct username and password
            assertTrue(UserHandler.login("user2", "password2"),
                    "Login should return true when username and password are correct.");
        }
        catch (InvalidNameException | InvalidPasswordException | EntityNotFoundException | CreationException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //VALIDATE----------------------------------------------------------------------------------------------------------

    @Test
    @Order(43)
    void testValidateUser_NullUser()
    {
        System.out.println("\n43: Testing validateUser method with null user...");

        assertThrows(NullEntityException.class, () -> UserHandler.validate(null, "validPassword"),
                "Validate should throw an exception for null users.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(44)
    void testValidateUser_NullPassword()
    {
        System.out.println("\n44: Testing validateUser method with null password...");

        try
        {
            //Create a new User
            User user = UserHandler.createNewUser("user1", "password1", null, null);

            //Call validateUser with null password
            assertThrows(InvalidPasswordException.class, () -> UserHandler.validate(user, null),
                    "Validate should throw an exception for null passwords.");
        }
        catch (CreationException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }


        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(45)
    void testValidateUser_CorrectPassword()
    {
        System.out.println("\n45: Testing validateUser method with correct password...");

        try
        {
            //Create a new user
            User user = new User("user1", "password1", null, null);

            //Call validateUser with correct password and expect true to be returned
            assertTrue(UserHandler.validate(user, "password1"),
                    "ValidateUser should return true when the password is correct.");
        }
        catch (InvalidPasswordException | ConstructionException | UserValidationException | NullEntityException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(46)
    void testValidateUser_IncorrectPassword()
    {
        System.out.println("\n46: Testing validateUser method with incorrect password...");

        try
        {
            //Create a new user
            User user = new User("user1", "password1", null, null);

            //Call validateUser with incorrect password and expect false to be returned
            assertFalse(UserHandler.validate(user, "password2"),
                    "ValidateUser should return false when the password is incorrect.");
        }
        catch (InvalidPasswordException | ConstructionException | NullEntityException | UserValidationException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //GET BY USERNAME---------------------------------------------------------------------------------------------------

    @Test
    @Order(12)
    void testGetUserByUsername_ValidUserPresent()
    {
        System.out.println("\n12: Testing getUserByUsername method with a valid username present in database...");

        try
        {
            String username = "username";
            String password = "password";
            //Insert a user into the database
            UserHandler.createNewUser(username, password, null, null);

            //Call the getUserByUsername method with a valid username
            User user = UserHandler.getUserByUsername(username);

            //Verify that a User is returned and not null and has expected values
            assertNotNull(user, "A User object should be returned when a valid username is provided.");
            assertEquals(username, user.getUsername(),
                    "The returned User object should have the username provided in the getUserByUsername method.");
            assertEquals(password, user.getPassword(), "Password should match input");
            //assertEquals(User.DEFAULT_ALLOWED_RENTALS, user.getAllowedRentals(),
              //      "Default allowed rentals should be set");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be zero");
            assertEquals(0.0, user.getLateFee(), "Late fee should be zero");
            assertFalse(user.isDeleted());

        }
        catch (InvalidNameException | CreationException e)
        {
            fail("No exception should be thrown when retrieving user with a valid username.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testGetUserByUsername_ValidUserNotPresent()
    {
        System.out.println("\n13: Testing getUserByUsername method with a valid username not present in database...");

        try
        {
            //Call the getUserByUsername method with a valid username that is not present in the database
            assertNull(UserHandler.getUserByUsername("nonExistentUsername"));
        }
        catch (InvalidNameException e)
        {
            fail("No exception should be thrown when retrieving user with a valid username.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testGetUserByUsername_NullUsername()
    {
        System.out.println("\n14: Testing getUserByUsername method with a null username...");

        //Verify that an InvalidNameException is thrown when a null username is provided
        assertThrows(InvalidNameException.class, () -> UserHandler.getUserByUsername(null),
                "An InvalidNameException should be thrown when the username is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(15)
    void testGetUserByUsername_EmptyUsername()
    {
        System.out.println("\n15: Testing getUserByUsername method with an empty username...");

        //Verify that an InvalidNameException is thrown when an empty username is provided
        assertThrows(InvalidNameException.class, () -> UserHandler.getUserByUsername(""),
                "An InvalidNameException should be thrown when the username is empty.");

        System.out.println("\nTEST FINISHED.");
    }


    //TODO-test getUserByEmail x 3


    //TODO-test getUsersByFirstname x 4


    //TODO-test getUsersByLastname x 4

    //TODO 11
}