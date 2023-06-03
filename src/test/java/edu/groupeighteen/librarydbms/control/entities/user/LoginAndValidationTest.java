package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the LoginAndValidation class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginAndValidationTest
{
    @BeforeAll
    static void setUp()
    {
    }

    @AfterAll
    static void tearDown()
    {
    }

    /**
     *
     */
    @Test
    @Order(1)
    void testLoginAndValidation()
    {
        System.out.println("\n1: Testing LoginAndValidation...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("\nTEST FINISHED.");
    }

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
            UserHandler.createNewUser("user1", "password1", "user1@mail.com", User.UserType.PATRON);
            //Attempt to login with the correct username but incorrect password
            assertFalse(UserHandler.login("user1", "incorrectPassword"),
                    "Login should return false when password is incorrect.");
        }
        catch (InvalidNameException | InvalidPasswordException | EntityNotFoundException | CreationException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
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
            UserHandler.createNewUser("user2", "password2", "user2@mail.com", User.UserType.PATRON);

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
            User user = UserHandler.createNewUser("user1", "password1", "user1@mail.com", User.UserType.PATRON);

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
            User user = new User("user1", "password1", "user1@mail.com", User.UserType.PATRON);

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
            User user = new User("user1", "password1", "user1@mail.com", User.UserType.PATRON);

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
}