package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the login and validate methods.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginAndValidationTest extends BaseUserHandlerTest
{
    private static final String invalidPassword = "invalidPassword";
    //A nice batch of user objects to use over and over
    private static User admin;
    private static User staff;
    private static User patron;
    private static User student;
    private static User teacher;
    private static User researcher;
    private static User nonExistingUser; //CONSTRUCTOR NOT createNewUser
    private static User[] users;

    /**
     * Let's setup the needed users ahead of time.
     */
    @BeforeAll
    protected static void customSetup()
    {
        initializeUsers();
    }

    /**
     * Initializes the batch and prepares them for testing. Further preparation might be needed in specific tests.
     */
    protected static void initializeUsers()
    {
        System.out.println("\nInitializing users...");

        try
        {
            admin = UserHandler.createNewUser("admin", "adminPass",
                    "admin@mail.com", User.UserType.ADMIN);
            staff = UserHandler.createNewUser("staff", "staffPass",
                    "staff@mail.com", User.UserType.STAFF);
            patron = UserHandler.createNewUser("patron", "patronPass",
                    "patron@mail.com", User.UserType.PATRON);
            student = UserHandler.createNewUser("student", "studentPass",
                    "student@mail.com", User.UserType.STUDENT);
            teacher = UserHandler.createNewUser("teacher", "teacherPass",
                    "teacher@mail.com", User.UserType.TEACHER);
            researcher = UserHandler.createNewUser("researcher", "researcherPass",
                    "researcher@mail.com", User.UserType.RESEARCHER);

            users = new User[]{admin, staff, patron, student, teacher, researcher};

            nonExistingUser = new User("nonExistingUser", "nonExistingPassword",
                    "nonExisting@mail.com", User.UserType.TEACHER); //CONSTRUCTOR NOT createNewUser

            //Make sure our users are set correctly
            nonExistingUser.setUserID(9999);
        }
        catch (CreationException | ConstructionException | InvalidIDException e)
        {
            e.printStackTrace();
            fail("User initialization failed due to: " + e.getCause().getClass().getName()
                    + ". Message: " + e.getMessage());
        }

        System.out.println("\nUSERS INITIALIZED.");
    }

    /**
     * And now we can reset the table and the users back to original states after tests.
     */
    @Override
    @AfterEach
    protected void reset()
    {
        super.reset();
        resetUsersTable();
        initializeUsers();
    }

    /**
     * Tests the 'login' method with a valid username and matching password.
     */
    @Test
    @Order(1)
    void testLogin_ValidUsernameAndPassword()
    {
        System.out.println("\n1: Testing login method with valid username and matching password...\n");

        for (User user : users)
        {
            try
            {
                System.out.println("Attempting to login " + user.getUsername() + " with correct password.");

                assertTrue(UserHandler.login(user.getUsername(), user.getPassword()));

                System.out.println("Login success.");
            }
            catch (UserValidationException e)
            {
                e.printStackTrace();
                fail("User login test failed due to: " + e.getCause().getClass().getName()
                        + ". Message: " + e.getMessage());
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with a valid username and non-matching password.
     */
    @Test
    @Order(2)
    void testLogin_ValidUsernameInvalidPassword()
    {
        System.out.println("\n2: Testing login method with valid username and non-matching password...");

        for (User user : users)
        {
            try
            {
                System.out.println("Attempting to login " + user.getUsername() + " with invalid password.");

                assertFalse(UserHandler.login(user.getUsername(), invalidPassword));

                System.out.println("Login failed as expected.");
            }
            catch (UserValidationException e)
            {
                e.printStackTrace();
                fail("User login test failed due to: " + e.getCause().getClass().getName()
                        + ". Message: " + e.getMessage());
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with a valid username and null password.
     */
    @Test
    @Order(3)
    void testLogin_ValidUsernameNullPassword()
    {
        System.out.println("\n3: Testing login method with valid username and null password...");

        for (User user : users)
        {
            System.out.println("Attempting to login " + user.getUsername() + " with null password.");

            Exception e = assertThrows(UserValidationException.class, () -> UserHandler.login(user.getUsername(),
                    null));
            assertTrue(e.getCause() instanceof InvalidPasswordException);

            System.out.println("Login failed as expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with a valid username and empty password.
     */
    @Test
    @Order(4)
    void testLogin_ValidUsernameEmptyPassword()
    {
        System.out.println("\n4: Testing login method with valid username and empty password...");

        for (User user : users)
        {
            System.out.println("Attempting to login " + user.getUsername() + " with empty password.");

            Exception e = assertThrows(UserValidationException.class, () -> UserHandler.login(user.getUsername(),
                    ""));
            assertTrue(e.getCause() instanceof InvalidPasswordException);

            System.out.println("Login failed as expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with a non-existent user (User object exists but not in database) and a valid password.
     */
    @Test
    @Order(5)
    void testLogin_NonExistingUserValidPassword()
    {
        System.out.println("\n5: Testing login method with non-existent user (User object exists but not in database)" +
                " and a valid password...");

        System.out.println("Attempting to login " + nonExistingUser.getUsername() + " with valid password.");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.login(nonExistingUser.getUsername(),
                nonExistingUser.getPassword()));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

        System.out.println("Login failed as expected.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with a non-existent user (User object exists but not in database) and an invalid password.
     */
    @Test
    @Order(6)
    void testLogin_NonExistingUserInvalidPassword()
    {
        System.out.println("\n6: Testing login method with non-existent user (User object exists but not in database)" +
                " and an invalid password...");

        System.out.println("Attempting to login " + nonExistingUser.getUsername() + " with invalid password.");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.login(nonExistingUser.getUsername(),
                invalidPassword));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

        System.out.println("Login failed as expected.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with null username and a valid password.
     */
    @Test
    @Order(7)
    void testLogin_NullUsernameValidPassword()
    {
        System.out.println("\n7: Testing login method with null username and a valid password...");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.login(null,
                "password123"));
        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'login' method with empty username and a valid password.
     */
    @Test
    @Order(8)
    void testLogin_EmptyUserNameValidPassword()
    {
        System.out.println("\n8: Testing login method with empty username and a valid password...");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.login("",
                "password123"));
        assertTrue(e.getCause() instanceof InvalidNameException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'validate' method with a valid user and a matching password.
     */
    @Test
    @Order(9)
    void testValidate_ValidUserMatchingPassword()
    {
        System.out.println("\n9: Testing validate method with a valid user and a matching password...");

        for (User user : users)
        {
            try
            {
                System.out.println("Attempting to validate " + user.getUsername() + " with correct password.");

                assertTrue(UserHandler.validate(user, user.getPassword()));

                System.out.println("Validation success.");
            }
            catch (UserValidationException e)
            {
                e.printStackTrace();
                fail("User validation test failed due to: " + e.getCause().getClass().getName()
                        + ". Message: " + e.getMessage());
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'validate' method with a valid user and a non-matching password.
     */
    @Test
    @Order(10)
    void testValidate_ValidUserNonMatchingPassword()
    {
        System.out.println("\n10: Testing validate method with a valid user and a non-matching password...");

        for (User user : users)
        {
            try
            {
                System.out.println("Attempting to validate " + user.getUsername() + " with invalid password.");

                assertFalse(UserHandler.validate(user, invalidPassword));

                System.out.println("Validation failed as expected.");
            }
            catch (UserValidationException e)
            {
                e.printStackTrace();
                fail("User validation test failed due to: " + e.getCause().getClass().getName()
                        + ". Message: " + e.getMessage());
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'validate' method with a valid user and a null password.
     */
    @Test
    @Order(11)
    void testValidate_ValidUserNullPassword()
    {
        System.out.println("\n11: Testing validate method with a valid user and a null password...");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.validate(patron,
                null));
        assertTrue(e.getCause() instanceof InvalidPasswordException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'validate' method with a valid user and an empty password.
     */
    @Test
    @Order(12)
    void testValidate_ValidUserEmptyPassword()
    {
        System.out.println("\n12: Testing validate method with a valid user and an empty password...");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.validate(patron,
                ""));
        assertTrue(e.getCause() instanceof InvalidPasswordException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the 'validate' method with a null user object and a valid password.
     */
    @Test
    @Order(13)
    void testValidate_NullUserValidPassword()
    {
        System.out.println("\n13: Testing validate method with a null user object and a valid password...");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.validate(null,
                "password123"));
        assertTrue(e.getCause() instanceof NullEntityException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the `validate` method with a user that doesn't exist in database and a valid password.
     */
    @Test
    @Order(14)
    void testValidate_NonExistingUserValidPassword()
    {
        System.out.println("\n14: Testing login method with a non-existing username and a valid password...");

        Exception e = assertThrows(UserValidationException.class, () -> UserHandler.validate(nonExistingUser,
                nonExistingUser.getPassword()));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

        System.out.println("\nTEST FINISHED.");
    }
}