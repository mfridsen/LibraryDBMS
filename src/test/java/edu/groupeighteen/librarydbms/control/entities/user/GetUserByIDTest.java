package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetUserByID class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserByIDTest extends BaseUserHandlerTest
{
    //Variables to be used in tests
    private static final String validUsername = "validusername";
    private static final String validUsername2 = "validusername2";
    private static final String validUsername3 = "validusername3";

    private static final String validPassword = "validpassword";
    private static final String validPassword2 = "validpassword2";
    private static final String validPassword3 = "validpassword3";

    private static final String validEmail = "validuser@example.com";
    private static final String validEmail2 = "validuser2@example.com";
    private static final String validEmail3 = "validuser3@example.com";

    private static final User.UserType userType = User.UserType.ADMIN;
    private static final int invalidID = -1;
    private static int existingUserID;
    private static int nonExistingUserID;
    private static int deletedUserID;

    /**
     * Let's setup the three different needed users ahead of time.
     */
    @BeforeAll
    protected static void customSetup()
    {
        try
        {
            User existingUser = UserHandler.createNewUser(validUsername, validPassword, validEmail, userType);
            existingUserID = existingUser.getUserID();

            User nonExistingUser = new User(validUsername2, validPassword2, validEmail2, userType);

            //userID is set to 0 by default by constructor, which is invalid
            nonExistingUser.setUserID(9999);
            nonExistingUserID = nonExistingUser.getUserID();

            User deletedUser = UserHandler.createNewUser(validUsername3, validPassword3, validEmail3, userType);
            deletedUserID = deletedUser.getUserID();

            //Soft delete deletedUser
            DatabaseHandler.executeCommand("UPDATE users SET deleted = 1 WHERE username = 'validusername3';");
        }
        catch (CreationException | ConstructionException | InvalidIDException e)
        {
            e.printStackTrace();
            Assertions.fail("Failed due to exception in GetUserByIDTest.customSetup: ", e);
        }
    }

    /**
     * Tests the `getUserByID` method with a valid ID for an existing user.
     */
    @Test
    @Order(1)
    void testGetUserByID_ValidExistingUser()
    {
        System.out.println("\n1: Testing getUserByID method with a valid ID for an existing user...");

        try
        {
            assertNotNull(UserHandler.getUserByID(existingUserID));
        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the `getUserByID` method with a valid ID for a non-existing user.
     */
    @Test
    @Order(2)
    void testGetUserByID_ValidNonExistingUser()
    {
        System.out.println("\n2: Testing getUserByID method with a valid ID for a non-existing user...");

        try
        {
            assertNull(UserHandler.getUserByID(nonExistingUserID));
        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the `getUserByID` method with a valid ID for a deleted user (getDeleted = false).
     */
    @Test
    @Order(3)
    void testGetUserByID_ValidDeletedUser_GetDeletedFalse()
    {
        System.out.println(
                "\n3: Testing getUserByID method with a valid ID for a deleted user (getDeleted = false)...");

        try
        {
            assertNull(UserHandler.getUserByID(deletedUserID));
        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the `getUserByID` method with a valid ID for a deleted user (getDeleted = true).
     */
    @Test
    @Order(4)
    void testGetUserByID_ValidDeletedUser_GetDeletedTrue()
    {
        System.out.println("\n4: Testing getUserByID method with a valid ID for a deleted user (getDeleted = true)...");

        try
        {
            assertNotNull(UserHandler.getUserByID(deletedUserID, true));
        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the `getUserByID` method with an invalid user ID.
     */
    @Test
    @Order(5)
    void testGetUserByID_InvalidUserID()
    {
        System.out.println("\n5: Testing getUserByID method with an invalid user ID...");

        assertThrows(InvalidIDException.class, () -> UserHandler.getUserByID(invalidID));

        System.out.println("\nTEST FINISHED.");
    }
}