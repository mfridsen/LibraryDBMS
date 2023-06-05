package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetUserByUsername class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserByUsernameTest extends BaseUserHandlerTest
{
    @Test
    @Order(1)
    void testGetUserByUsername_ValidUserPresent()
    {
        System.out.println("\n1: Testing getUserByUsername method with a valid username present in database...");

        try
        {
            String username = "username";
            String password = "password";
            //Insert a user into the database
            UserHandler.createNewUser(username, password, "user1@mail.com", User.UserType.PATRON);

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
    @Order(2)
    void testGetUserByUsername_ValidUserNotPresent()
    {
        System.out.println("\n2: Testing getUserByUsername method with a valid username not present in database...");

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
    @Order(3)
    void testGetUserByUsername_NullUsername()
    {
        System.out.println("\n3: Testing getUserByUsername method with a null username...");

        //Verify that an InvalidNameException is thrown when a null username is provided
        assertThrows(InvalidNameException.class, () -> UserHandler.getUserByUsername(null),
                "An InvalidNameException should be thrown when the username is null.");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testGetUserByUsername_EmptyUsername()
    {
        System.out.println("\n4: Testing getUserByUsername method with an empty username...");

        //Verify that an InvalidNameException is thrown when an empty username is provided
        assertThrows(InvalidNameException.class, () -> UserHandler.getUserByUsername(""),
                "An InvalidNameException should be thrown when the username is empty.");

        System.out.println("\nTEST FINISHED.");
    }
}