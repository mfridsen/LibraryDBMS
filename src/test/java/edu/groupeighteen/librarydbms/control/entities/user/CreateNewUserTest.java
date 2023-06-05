package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidEmailException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the CreateNewUser class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewUserTest extends BaseUserHandlerTest
{
    //Variables to be used in tests
    private static final String validUsername = "validusername";
    private static final String validUsername2 = "validusername2";

    private static final String validPassword = "validpassword";

    private static final String validEmail = "validuser@example.com";
    private static final String validEmail2 = "validuser2@example.com";

    private static final String shortUsername = "sh"; //3
    private static final String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);

    private static final String shortPassword = "short"; //8
    private static final String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);

    private static final String shortEmail = "shrt"; //6
    private static final String longEmail = "a".repeat(User.MAX_EMAIL_LENGTH + 1);

    private static final User.UserType userType = User.UserType.ADMIN;

    /**
     * Clear lists and table.
     */
    @Override
    @AfterEach
    protected void reset()
    {
        super.reset(); //Clears lists
        resetUsersTable();
    }

    /**
     * Test case for the createNewItem method with valid Users of all UserTypes.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(1)
    void testCreateNewItem_ValidUsers_AllUserTypes()
    {
        System.out.println("\n1: Testing createNewItem method with valid Users of all UserTypes...");

        for (User.UserType userType : User.UserType.values())
        {
            try
            {
                System.out.println("\nTesting valid user of userType " + userType.toString());

                //Create the user with given type
                User user = UserHandler.createNewUser(validUsername, validPassword, validEmail, userType);
                assertNotNull(user);

                //Assert all fields
                assertEquals(1, user.getUserID());
                assertEquals(validUsername, user.getUsername(), "Username should be the same as the one set");
                assertEquals(validPassword, user.getPassword(), "Password should be the same as the one set");
                assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
                assertEquals(validEmail, user.getEmail(), "Email should be the same as the one set");
                assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                        "Allowed rentals should be default for userType");
                assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
                assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
                assertFalse(user.isDeleted(), "User should not be marked as deleted");

                //Assert lists and table
                assertEquals(1, getNumberOfUsers());
                assertEquals(1, UserHandler.getStoredUsernames().size());
                assertEquals(1, UserHandler.getRegisteredEmails().size());

                //Reset table and lists after each test
                reset();
                assertEquals(0, getNumberOfUsers());
                assertEquals(0, UserHandler.getStoredUsernames().size());
                assertEquals(0, UserHandler.getRegisteredEmails().size());
            }
            catch (CreationException e)
            {
                e.printStackTrace();
                fail("Valid operations should not throw exceptions.");
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a duplicate username.
     */
    @Test
    @Order(2)
    void testCreateNewItem_DuplicateUsername()
    {
        System.out.println("\n2: Testing createNewItem method with a duplicate username...");

        assertDoesNotThrow(() -> UserHandler.createNewUser(validUsername, validPassword, validEmail, userType));

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                validPassword, validEmail2, userType));

        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a duplicate email.
     */
    @Test
    @Order(3)
    void testCreateNewItem_DuplicateEmail()
    {
        System.out.println("\n3: Testing createNewItem method with a duplicate email...");

        assertDoesNotThrow(() -> UserHandler.createNewUser(validUsername, validPassword, validEmail, userType));

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername2,
                validPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidEmailException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with null username.
     */
    @Test
    @Order(4)
    void testCreateNewItem_NullUsername()
    {
        System.out.println("\n4: Testing createNewItem method with null username...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(null,
                validPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with an empty username.
     */
    @Test
    @Order(5)
    void testCreateNewItem_EmptyUsername()
    {
        System.out.println("\n5: Testing createNewItem method with an empty username...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser("",
                validPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a short username of 3 characters.
     */
    @Test
    @Order(6)
    void testCreateNewItem_ShortUsername()
    {
        System.out.println("\n6: Testing createNewItem method with a short username of 3 characters...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(shortUsername,
                validPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a long username of 20 characters.
     */
    @Test
    @Order(7)
    void testCreateNewItem_LongUsername()
    {
        System.out.println("\n7: Testing createNewItem method with a long username of 20 characters...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(longUsername,
                validPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with null password.
     */
    @Test
    @Order(8)
    void testCreateNewItem_NullPassword()
    {
        System.out.println("\n8: Testing createNewItem method with null password...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                null, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidPasswordException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with an empty password.
     */
    @Test
    @Order(9)
    void testCreateNewItem_EmptyPassword()
    {
        System.out.println("\n9: Testing createNewItem method with an empty password...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                "", validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidPasswordException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a short password of 8 characters.
     */
    @Test
    @Order(10)
    void testCreateNewItem_ShortPassword()
    {
        System.out.println("\n10: Testing createNewItem method with a short password of 8 characters...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                shortPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidPasswordException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a long password of 50 characters.
     */
    @Test
    @Order(11)
    void testCreateNewItem_LongPassword()
    {
        System.out.println("\n11: Testing createNewItem method with a long password of 50 characters...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                longPassword, validEmail, userType));

        assertTrue(e.getCause() instanceof InvalidPasswordException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with null email.
     */
    @Test
    @Order(12)
    void testCreateNewItem_NullEmail()
    {
        System.out.println("\n12: Testing createNewItem method with null email...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                validPassword, null, userType));

        assertTrue(e.getCause() instanceof InvalidEmailException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with an empty email.
     */
    @Test
    @Order(13)
    void testCreateNewItem_EmptyEmail()
    {
        System.out.println("\n13: Testing createNewItem method with an empty email...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                validPassword, "", userType));

        assertTrue(e.getCause() instanceof InvalidEmailException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a short email of 6 characters.
     */
    @Test
    @Order(14)
    void testCreateNewItem_ShortEmail()
    {
        System.out.println("\n14: Testing createNewItem method with a short email of 6 characters...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                validPassword, shortEmail, userType));

        assertTrue(e.getCause() instanceof InvalidEmailException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with a long email of 255 characters.
     */
    @Test
    @Order(15)
    void testCreateNewItem_LongEmail()
    {
        System.out.println("\n15: Testing createNewItem method with a long email of 255 characters...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                validPassword, longEmail, userType));

        assertTrue(e.getCause() instanceof InvalidEmailException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Testing createNewItem method with null userType.
     */
    @Test
    @Order(16)
    void testCreateNewItem_NullUserType()
    {
        System.out.println("\n16: Testing createNewItem method with null userType...");

        Exception e = assertThrows(CreationException.class, () -> UserHandler.createNewUser(validUsername,
                validPassword, validEmail, null));

        assertTrue(e.getCause() instanceof InvalidTypeException);

        System.out.println("\nTEST FINISHED.");
    }
}