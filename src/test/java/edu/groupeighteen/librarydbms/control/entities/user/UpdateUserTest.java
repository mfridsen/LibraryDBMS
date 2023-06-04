package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserValidationException;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UpdateUser class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateUserTest extends BaseUserHandlerTest
{
    //Variables to be used in tests
    private static final String validUsername = "validUsername";
    private static final String changedUsername = "changedUsername";
    private static final String takenUsername = "takenUsername";

    private static final String validPassword = "validPassword";
    private static final String changedPassword = "changedPassword";

    private static final String validEmail = "validEmail@example.com";
    private static final String changedEmail = "changedEmail@example.com";
    private static final String takenEmail = "takenEmail@example.com";

    private static final User.UserType userType = User.UserType.PATRON;
    private static final User.UserType changedUserType = User.UserType.STUDENT;

    private static final int changedAllowedRentals = 7;
    private static final int changedCurrentRentals = 2;

    private static User baseUser;

    /**
     * Let's setup the needed users ahead of time.
     */
    @BeforeAll
    protected static void customSetup()
    {
        try
        {
            baseUser = UserHandler.createNewUser(validUsername, validPassword, validEmail, userType);
        }
        catch (CreationException e)
        {
            e.printStackTrace();
        }
    }

    @AfterEach
    @Override
    protected void reset()
    {
        super.reset(); //Resets UserHandler lists
        resetUsersTable();

        try //Reset user
        {
            baseUser = UserHandler.createNewUser(validUsername, validPassword, validEmail, userType);
        }
        catch (CreationException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Test to update a User's username to a not taken one.
     */
    @Test
    @Order(1)
    void testUpdateUser_UsernameChangedNotTaken()
    {
        System.out.println("\n1: Testing updateUser method with new valid and unique username...");

        try
        {
            baseUser.setUsername(changedUsername);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(changedUsername, updatedUser.getUsername());
        }
        catch (InvalidNameException | NullEntityException | UpdateException | InvalidIDException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's password.
     */
    @Test
    @Order(2)
    void testUpdateUser_PasswordChanged()
    {
        System.out.println("\n2: Testing updateUser method with new password...");

        try
        {
            baseUser.setPassword(changedPassword);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(changedPassword, updatedUser.getPassword());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidPasswordException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's email to a not taken one.
     */
    @Test
    @Order(3)
    void testUpdateUser_EmailChangedNotTaken()
    {
        System.out.println("\n3: Testing updateUser method with new valid and unique email...");

        try
        {
            baseUser.setEmail(changedEmail);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(changedEmail, updatedUser.getEmail());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidEmailException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's userType.
     */
    @Test
    @Order(4)
    void testUpdateUser_UserTypeChanged()
    {
        System.out.println("\n4: Testing updateUser method with new user type...");

        try
        {
            baseUser.setUserType(changedUserType);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(changedUserType, updatedUser.getUserType());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidTypeException | InvalidUserRentalsException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's allowedRentals.
     */
    @Test
    @Order(5)
    void testUpdateUser_AllowedRentalsChanged()
    {
        System.out.println("\n5: Testing updateUser method with updated allowed rentals...");

        try
        {
            baseUser.setAllowedRentals(changedAllowedRentals);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(changedAllowedRentals, updatedUser.getAllowedRentals());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidUserRentalsException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's currentRentals.
     */
    @Test
    @Order(6)
    void testUpdateUser_CurrentRentalsChanged()
    {
        System.out.println("\n6: Testing updateUser method with updated current rentals...");

        try
        {
            baseUser.setCurrentRentals(changedCurrentRentals);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(changedCurrentRentals, updatedUser.getCurrentRentals());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidUserRentalsException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's lateFee and allowedToRent.
     */
    @Test
    @Order(7)
    void testUpdateUser_LateFeeAndAllowedToRentChanged()
    {
        System.out.println("\n7: Testing updateUser method with updated late fee and allowed to rent...");

        try
        {
            baseUser.setLateFee(1);
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());
            assertNotNull(updatedUser);
            assertEquals(1, updatedUser.getLateFee());
            assertFalse(updatedUser.isAllowedToRent());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidLateFeeException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update all fields of a User.
     */
    @Test
    @Order(8)
    void testUpdateUser_AllFieldsChanged()
    {
        System.out.println("\n8: Testing updateUser method with all fields updated...");

        // (Allowed To Rent Changed    true -> false)

        try
        {
            // (Username Changed, Not Taken "validUsername" -> "changedUsername")
            baseUser.setUsername(changedUsername);
            // (Password Changed           "validPassword" -> "changedPassword")
            baseUser.setPassword(changedPassword);
            // (Email Changed Not Taken    "validEmail@example.com" -> "changedEmail@example.com")
            baseUser.setEmail(changedEmail);
            // (User Type Changed          PATRON -> STUDENT)
            baseUser.setUserType(changedUserType);
            // (Allowed Rentals Changed    3 -> 7)
            baseUser.setAllowedRentals(changedAllowedRentals);
            // (Current Rentals Changed    0 -> 7)
            baseUser.setCurrentRentals(7);
            // Late fee 0 -> 1
            baseUser.setLateFee(1);

            //Update
            UserHandler.updateUser(baseUser);
            User updatedUser = UserHandler.getUserByID(baseUser.getUserID());

            //Assert
            assertNotNull(updatedUser);
            assertEquals(changedUsername, updatedUser.getUsername());
            assertEquals(changedPassword, updatedUser.getPassword());
            assertEquals(changedEmail, updatedUser.getEmail());
            assertEquals(changedUserType, updatedUser.getUserType());
            assertEquals(changedAllowedRentals, updatedUser.getAllowedRentals());
            assertEquals(7, updatedUser.getCurrentRentals());
            assertEquals(1, updatedUser.getLateFee());
            assertFalse(updatedUser.isAllowedToRent());
        }
        catch (NullEntityException | UpdateException | InvalidIDException | InvalidLateFeeException |
                InvalidNameException | InvalidPasswordException | InvalidEmailException | InvalidTypeException |
                InvalidUserRentalsException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a null User.
     */
    @Test
    @Order(9)
    void testUpdateUser_NullUser()
    {
        System.out.println("\n9: Testing updateUser method with null user...");

        assertThrows(NullEntityException.class, () -> UserHandler.updateUser(null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User that is soft deleted.
     */
    @Test
    @Order(10)
    void testUpdateUser_ValidUserSoftDeleted()
    {
        System.out.println("\n10: Testing updateUser method with valid user that has been soft deleted...");

        Exception e = assertThrows(NullEntityException.class, () -> UserHandler.updateUser(null));
        assertTrue(e.getCause() instanceof NullEntityException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User that was soft deleted and then recovered.
     */
    @Test
    @Order(11)
    void testUpdateUser_ValidUserRecovered()
    {
        System.out.println("\n11: Testing updateUser method with valid user that was soft deleted and then recovered...");

        // TODO: Implement test logic.

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User that is hard deleted.
     */
    @Test
    @Order(12)
    void testUpdateUser_ValidUserHardDeleted()
    {
        System.out.println("\n12: Testing updateUser method with valid user that has been hard deleted...");

        // TODO: Implement test logic.

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a valid User that does not exist in the database.
     */
    @Test
    @Order(13)
    void testUpdateUser_ValidUserDoesNotExistInDatabase()
    {
        System.out.println("\n13: Testing updateUser method with valid user that does not exist in database...");

        // TODO: Implement test logic.

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's username to a taken one.
     */
    @Test
    @Order(14)
    void testUpdateUser_NewUsernameTaken()
    {
        System.out.println("\n14: Testing updateUser method with new username that is already taken...");

        // TODO: Implement test logic.

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test to update a User's email to a taken one.
     */
    @Test
    @Order(15)
    void testUpdateUser_NewEmailTaken()
    {
        System.out.println("\n15: Testing updateUser method with new email that is already taken...");

        // TODO: Implement test logic.

        System.out.println("\nTEST FINISHED.");
    }
}