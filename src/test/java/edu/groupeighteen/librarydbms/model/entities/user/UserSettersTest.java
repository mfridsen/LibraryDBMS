package edu.groupeighteen.librarydbms.model.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidRentalStatusChangeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UserSetters class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSettersTest
{
    //Previously established valid values.
    private static final int validUserID1 = 1;
    private static final int validUserID2 = 2;
    private static final String validUsername = "validusername";
    private static final String validUsername2 = "validusername2";
    private static final String shortUsername = "sh"; //3
    private static final String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1);
    private static final String validPassword = "validpassword";
    private static final String validPassword2 = "validpassword";
    private static final String shortPassword = "short"; //8
    private static final String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1);
    private static final User.UserType userType = User.UserType.PATRON;
    private static final User.UserType userType2 = User.UserType.RESEARCHER; //Verify allowedRentals changed
    private static final String validEmail = "validuser@example.com";
    private static final String validEmail2 = "validuser2@example.com";
    private static final String shortEmail = "shrt"; //6
    private static final String longEmail = "a".repeat(User.MAX_EMAIL_LENGTH + 1);
    private static final int allowedRentals = User.getDefaultAllowedRentals(User.UserType.PATRON);
    private static final int currentRentals = 0;
    private static final double lateFee = 0.0;
    private static final boolean allowedToRent = true;
    private static final boolean deleted = false;
    //Let's use the same User object for all tests here
    private static User user = null;

    /**
     * Creates a valid user object that we can perform tests on, ethical or not-so-ethical depending on currentMood.
     */
    @BeforeAll
    static void setUp()
    {
        System.out.println("Setting up User object for tests...");

        try
        {
            user = new User(validUserID1, validUsername, validPassword, validEmail, userType, allowedRentals,
                    currentRentals, lateFee, allowedToRent, deleted);
            assertNotNull(user);
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nSETUP FINISHED.");
    }

    /**
     * Resets the poor test subject.
     */
    static void reset()
    {
        try
        {
            user = new User(validUserID1, validUsername, validPassword, validEmail, userType, allowedRentals,
                    currentRentals, lateFee, allowedToRent, deleted);
            assertNotNull(user);
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }
    }

    /**
     * Test case for setUserID with valid userID.
     */
    @Test
    @Order(1)
    void testUserSetters_SetUserID_ValidID()
    {
        System.out.println("\n1: Testing setUserID with valid userID...");

        assertDoesNotThrow(() -> user.setUserID(validUserID2));
        assertEquals(validUserID2, user.getUserID());

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setUserID with invalid userID.
     */
    @Test
    @Order(2)
    void testUserSetters_SetUserID_InvalidID()
    {
        System.out.println("\n2: Testing setUserID with valid userID...");

        assertThrows(InvalidIDException.class, () -> user.setUserID(0));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setUsername with valid username.
     */
    @Test
    @Order(3)
    void testUserSetters_SetUsername_ValidUsername()
    {
        System.out.println("\n3: Testing setUsername with valid username...");

        assertDoesNotThrow(() -> user.setUsername(validUsername2));
        assertEquals(validUsername2, user.getUsername());

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setUsername with null username.
     */
    @Test
    @Order(4)
    void testUserSetters_SetUsername_NullUsername()
    {
        System.out.println("\n4: Testing setUsername with null username...");

        assertThrows(InvalidNameException.class, () -> user.setUsername(null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setUsername with empty username.
     */
    @Test
    @Order(5)
    void testUserSetters_SetUsername_EmptyUsername()
    {
        System.out.println("\n5: Testing setUsername with empty username...");

        assertThrows(InvalidNameException.class, () -> user.setUsername(""));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setUsername with short username.
     */
    @Test
    @Order(6)
    void testUserSetters_SetUsername_ShortUsername()
    {
        System.out.println("\n6: Testing setUsername with short username...");

        assertThrows(InvalidNameException.class, () -> user.setUsername(shortUsername));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setUsername with long username.
     */
    @Test
    @Order(7)
    void testUserSetters_SetUsername_LongUsername()
    {
        System.out.println("\n7: Testing setUsername with long username...");

        assertThrows(InvalidNameException.class, () -> user.setUsername(longUsername));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setPassword with null password.
     */
    @Test
    @Order(8)
    void testUserSetters_SetPassword_NullPassword()
    {
        System.out.println("\n8: Testing setPassword with null password...");

        assertThrows(InvalidPasswordException.class, () -> user.setPassword(null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setPassword with empty password.
     */
    @Test
    @Order(9)
    void testUserSetters_SetPassword_EmptyPassword()
    {
        System.out.println("\n9: Testing setPassword with empty password...");

        assertThrows(InvalidPasswordException.class, () -> user.setPassword(""));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setPassword with short password.
     */
    @Test
    @Order(10)
    void testUserSetters_SetPassword_ShortPassword()
    {
        System.out.println("\n10: Testing setPassword with short password...");

        assertThrows(InvalidPasswordException.class, () -> user.setPassword(shortPassword));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setPassword with long password.
     */
    @Test
    @Order(11)
    void testUserSetters_SetPassword_LongPassword()
    {
        System.out.println("\n11: Testing setPassword with long password...");

        assertThrows(InvalidPasswordException.class, () -> user.setPassword(longPassword));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting UserType with specific requirements.
     */
    @Test
    @Order(12)
    void testUserSetters_SetUserType_ValidType()
    {
        System.out.println("\n12: Testing setUserType with valid type, also verifying updates to user...");

        assertDoesNotThrow(() -> user.setUserType(User.UserType.RESEARCHER));
        assertEquals(User.getDefaultAllowedRentals(User.UserType.RESEARCHER), user.getAllowedRentals());
        reset(); //Reset the object to original state

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting UserType with null UserType.
     */
    @Test
    @Order(13)
    void testUserSetters_SetUserType_NullUserType()
    {
        System.out.println("\n13: Testing setUserType with null userType...");

        assertThrows(InvalidTypeException.class, () -> user.setUserType(null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting Email with valid Email2.
     */
    @Test
    @Order(14)
    void testUserSetters_SetEmail_ValidEmail()
    {
        System.out.println("\n14: Testing setEmail with valid email...");

        assertDoesNotThrow(() -> user.setEmail(validEmail2));
        assertEquals(validEmail2, user.getEmail());

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting Email with null Email.
     */
    @Test
    @Order(15)
    void testUserSetters_SetEmail_NullEmail()
    {
        System.out.println("\n15: Testing setEmail with null email...");

        assertThrows(InvalidEmailException.class, () -> user.setEmail(null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting Email with empty Email.
     */
    @Test
    @Order(16)
    void testUserSetters_SetEmail_EmptyEmail()
    {
        System.out.println("\n16: Testing setEmail with empty email...");

        assertThrows(InvalidEmailException.class, () -> user.setEmail(""));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting Email with short Email.
     */
    @Test
    @Order(17)
    void testUserSetters_SetEmail_ShortEmail()
    {
        System.out.println("\n17: Testing setEmail with short email...");

        assertThrows(InvalidEmailException.class, () -> user.setEmail(shortEmail));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting Email with long Email.
     */
    @Test
    @Order(18)
    void testUserSetters_SetEmail_LongEmail()
    {
        System.out.println("\n18: Testing setEmail with long email...");

        assertThrows(InvalidEmailException.class, () -> user.setEmail(longEmail));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting AllowedRentals with a negative number.
     */
    @Test
    @Order(19)
    void testUserSetters_SetAllowedRentals_NegativeAllowedRentals()
    {
        System.out.println("\n19: Testing setAllowedRentals with a negative number...");

        assertThrows(InvalidUserRentalsException.class, () -> user.setAllowedRentals(-1));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting CurrentRentals with a negative number.
     */
    @Test
    @Order(20)
    void testUserSetters_SetCurrentRentals_NegativeCurrentRentals()
    {
        System.out.println("\n20: Testing setCurrentRentals with a negative number...");

        assertThrows(InvalidUserRentalsException.class, () -> user.setCurrentRentals(-1));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting CurrentRentals more than AllowedRentals.
     */
    @Test
    @Order(21)
    void testUserSetters_SetCurrentRentals_MoreThanAllowedRentals()
    {
        System.out.println("\n21: Testing setCurrentRentals more than allowedRentals...");

        assertThrows(InvalidUserRentalsException.class, () -> user.setCurrentRentals(user.getAllowedRentals() + 1));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting LateFee with a negative number.
     */
    @Test
    @Order(22)
    void testUserSetters_SetLateFee_NegativeLateFee()
    {
        System.out.println("\n22: Testing setLateFee with a negative number...");

        assertThrows(InvalidLateFeeException.class, () -> user.setLateFee(-1));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting LateFee with 0.0 and AllowedRentals more than CurrentRentals and AllowedToRent is false.
     */
    @Test
    @Order(23)
    void testUserSetters_SetAllowedToRent_ZeroLateFee_AllowedRentalsMoreThanCurrentRentals_AllowedToRentFalse()
    {
        System.out.println("\n23: Testing setAllowedToRent when lateFee is 0.0 and allowedRentals is greater " +
                "than currentRentals, when allowedToRent is set to false...");

        assertDoesNotThrow(() -> user.setLateFee(0));
        assertDoesNotThrow(() -> user.setCurrentRentals(user.getAllowedRentals() - 1));
        assertThrows(InvalidRentalStatusChangeException.class, () -> user.setAllowedToRent(false));
        reset();

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting LateFee with more than 0.0 and AllowedToRent is true.
     */
    @Test
    @Order(24)
    void testUserSetters_SetAllowedToRent_PositiveLateFee_AllowedToRentTrue()
    {
        System.out.println("\n24: Testing setAllowedToRent with lateFee greater than 0.0 and allowedToRent is set to " +
                "true...");

        assertDoesNotThrow(() -> user.setLateFee(1));
        assertThrows(InvalidRentalStatusChangeException.class, () -> user.setAllowedToRent(true));
        reset();

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting CurrentRentals more than or equals to default AllowedRentals.
     */
    @Test
    @Order(25)
    void testUserSetters_SetAllowedToRent_MoreThanOrEqualsToDefaultAllowedRentals()
    {
        System.out.println("\n25: Testing setAllowedToRent when currentRentals greater than or equals to " +
                "allowedRentals, when setting allowedToRent to true...");

        assertDoesNotThrow(() -> user.setCurrentRentals(user.getAllowedRentals()));
        assertThrows(InvalidRentalStatusChangeException.class, () -> user.setAllowedToRent(true));
        reset();

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting Deleted to true and AllowedToRent to true.
     */
    @Test
    @Order(26)
    void testUserSetters_SetDeletedAndAllowedToRent_True()
    {
        System.out.println("\n26: Testing setting deleted to true and allowedToRent to true...");

        assertDoesNotThrow(() -> user.setDeleted(true));
        assertThrows(InvalidRentalStatusChangeException.class, () -> user.setAllowedToRent(true));
        reset();

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting valid allowedRentals.
     */
    @Test
    @Order(27)
    void testUserSetters_SetAllowedRentals_ValidAllowedRentals()
    {
        System.out.println("\n27: Testing setAllowedRentals with valid allowedRentals...");

        assertDoesNotThrow(() -> user.setAllowedRentals(7));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting valid currentRentals.
     */
    @Test
    @Order(28)
    void testUserSetters_SetCurrentRentals_ValidCurrentRentals()
    {
        System.out.println("\n28: Testing setCurrentRentals with valid currentRentals...");

        assertDoesNotThrow(() -> user.setCurrentRentals(2));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting valid lateFee.
     */
    @Test
    @Order(29)
    void testUserSetters_SetLateFee_ValidLateFee()
    {
        System.out.println("\n29: Testing setLateFee with valid lateFee...");

        assertDoesNotThrow(() -> user.setLateFee(2));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting valid password.
     */
    @Test
    @Order(30)
    void testUserSetters_SetPassword_ValidPassword()
    {
        System.out.println("\n30: Testing setPassword with valid password...");

        assertDoesNotThrow(() -> user.setPassword(validPassword2));

        System.out.println("\nTEST FINISHED.");
    }
}