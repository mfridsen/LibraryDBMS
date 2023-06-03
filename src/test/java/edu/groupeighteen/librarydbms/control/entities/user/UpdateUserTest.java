package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
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
public class UpdateUserTest
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

    /**
     * Let's setup the three different needed users ahead of time.
     */
    @BeforeAll
    protected static void customSetup()
    {
        try
        {

        }
        catch (CreationException | ConstructionException | InvalidIDException e)
        {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown()
    {
    }

    /**
     * Test case for the updateUser method with a valid user where all fields are changed.
     */
    @Test
    @Order(1)
    void testUpdateUser_AllFieldsChanged()
    {
        System.out.println("\n1: Testing updateUser method with a valid user where all fields are changed...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where password is changed.
     */
    @Test
    @Order(2)
    void testUpdateUser_PasswordChanged()
    {
        System.out.println("\n2: Testing updateUser method with a valid user where the password is changed...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where userType is changed.
     */
    @Test
    @Order(3)
    void testUpdateUser_UserTypeChanged()
    {
        System.out.println("\n3: Testing updateUser method with a valid user where the userType is changed...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where allowedRentals is changed.
     */
    @Test
    @Order(4)
    void testUpdateUser_AllowedRentalsChanged()
    {
        System.out.println("\n4: Testing updateUser method with a valid user where the allowedRentals is changed...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where currentRentals is changed.
     */
    @Test
    @Order(5)
    void testUpdateUser_CurrentRentalsChanged()
    {
        System.out.println("\n5: Testing updateUser method with a valid user where the currentRentals is changed...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where the lateFee and allowedToRent are changed.
     */
    @Test
    @Order(6)
    void testUpdateUser_LateFee_AllowedToRent_Changed()
    {
        System.out.println("\n6: Testing updateUser method with a valid user where the lateFee and allowedToRent" +
                "are changed...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where the username is unchanged.
     */
    @Test
    @Order(7)
    void testUpdateUser_UsernameUnchanged()
    {
        System.out.println("\n7: Testing updateUser method with a valid user where the username is unchanged...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where the username is changed and not taken.
     */
    @Test
    @Order(8)
    void testUpdateUser_UsernameChangedNotTaken()
    {
        System.out.println("\n8: Testing updateUser method with a valid user where the username is changed and not taken...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where the email is unchanged.
     */
    @Test
    @Order(9)
    void testUpdateUser_EmailUnchanged()
    {
        System.out.println("\n9: Testing updateUser method with a valid user where the email is unchanged...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user where the email is changed and not taken.
     */
    @Test
    @Order(10)
    void testUpdateUser_EmailChangedNotTaken()
    {
        System.out.println("\n10: Testing updateUser method with a valid user where the email is changed and not taken...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user that has been soft deleted.
     */
    @Test
    @Order(11)
    void testUpdateUser_ValidUserSoftDeleted()
    {
        System.out.println("\n11: Testing updateUser method with a valid user that has been soft deleted...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid user that has been hard deleted.
     */
    @Test
    @Order(12)
    void testUpdateUser_ValidUserHardDeleted()
    {
        System.out.println("\n12: Testing updateUser method with a valid user that has been hard deleted...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a null user.
     */
    @Test
    @Order(13)
    void testUpdateUser_NullUser()
    {
        System.out.println("\n13: Testing updateUser method with a null user...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a valid non-existing user.
     */
    @Test
    @Order(14)
    void testUpdateUser_ValidNonExistingUser()
    {
        System.out.println("\n14: Testing updateUser method with a valid non-existing user...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a new username that is taken.
     */
    @Test
    @Order(15)
    void testUpdateUser_NewUsernameTaken()
    {
        System.out.println("\n15: Testing updateUser method with a new username that is taken...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the updateUser method with a new email that is taken.
     */
    @Test
    @Order(16)
    void testUpdateUser_NewEmailTaken()
    {
        System.out.println("\n16: Testing updateUser method with a new email that is taken...");

        // TODO: Implement test logic here

        System.out.println("\nTEST FINISHED.");
    }
}