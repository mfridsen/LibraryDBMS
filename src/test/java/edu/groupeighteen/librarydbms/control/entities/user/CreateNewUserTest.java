package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import org.junit.jupiter.api.*;

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
    //Test cases:

    //Valid ADMIN VERIFY ALL FIELDS
    //Valid STAFF VERIFY ALL FIELDS
    //Valid PATRON VERIFY ALL FIELDS
    //Valid STUDENT VERIFY ALL FIELDS
    //Valid TEACHER VERIFY ALL FIELDS
    //Valid RESEARCHER VERIFY ALL FIELDS

    //Null username
    //Empty username
    //Short username 3
    //Long username 20

    //Null password
    //Empty password
    //Short password 8
    //Long password 50

    //Null email
    //Empty email
    //Short email 6
    //Long email 255

    //Null userType

    @Test
    @Order(1)
    public void testCreateNewUser_ValidInput()
    {
        System.out.println("\n1: Testing createNewUser with valid input...");

        try
        {
            String username = "ValidUser";
            String password = "validPassword123";
            User newUser = UserHandler.createNewUser(username, password, null, null);

            assertNotNull(newUser, "New user should be created");
            assertEquals(username, newUser.getUsername(), "Username should match input");
            assertEquals(password, newUser.getPassword(), "Password should match input");
            //assertEquals(User.DEFAULT_ALLOWED_RENTALS, newUser.getAllowedRentals(),
            //      "Default allowed rentals should be set");
            assertEquals(0, newUser.getCurrentRentals(), "Current rentals should be zero");
            assertEquals(0.0, newUser.getLateFee(), "Late fee should be zero");
            assertFalse(newUser.isDeleted());
        }
        catch (CreationException e)
        {
            fail("Should not get exception for valid test.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    public void testCreateNewUser_DuplicateUsername()
    {
        System.out.println("\n2: Testing createNewUser with duplicate username...");

        try
        {
            //Insert first user
            String username = "ValidUser";
            String password = "validPassword123";
            UserHandler.createNewUser(username, password, null, null);

            //Check that attempting to create another with same name doesn't work
            assertThrows(InvalidNameException.class, () -> UserHandler.createNewUser(username, password, null, null),
                    "Exception should be thrown for duplicate username");
        }
        catch (CreationException e)
        {
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    public void testCreateNewUser_ShortUsername()
    {
        System.out.println("\n3: Testing createNewUser with short username...");

        String username = "ab";
        String password = "validPassword123";
        assertThrows(ConstructionException.class, () -> UserHandler.createNewUser(username, password, null, null),
                "Exception should be thrown for short username");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    public void testCreateNewUser_LongUsername()
    {
        System.out.println("\n4: Testing createNewUser with long username...");

        String username = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
        String password = "validPassword123";
        assertThrows(ConstructionException.class, () -> UserHandler.createNewUser(username, password, null, null),
                "Exception should be thrown for long username");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    public void testCreateNewUser_ShortPassword()
    {
        System.out.println("\n5: Testing createNewUser with short password...");

        String username = "ValidUser";
        String password = "short";
        assertThrows(InvalidPasswordException.class, () -> UserHandler.createNewUser(username, password, null, null),
                "Exception should be thrown for short password");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    public void testCreateNewUser_LongPassword()
    {
        System.out.println("\n6: Testing createNewUser with long password...");

        String username = "ValidUser";
        String password = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
        assertThrows(InvalidPasswordException.class, () -> UserHandler.createNewUser(username, password, null, null),
                "Exception should be thrown for long password");

        System.out.println("\nTEST FINISHED.");
    }
}