package edu.groupeighteen.librarydbms.model.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.*;

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
    //Let's use the same User object for all tests here
    private static User user = null;

    //Previously established valid values.
    private static int validUserID1 = 1;
    private static int validUserID2 = 2;

    private static String validUsername = "validusername";
    private static String validUsername2 = "validusername2";

    private static String validPassword = "validpassword";
    private static String validPassword2 = "validpassword";

    private static User.UserType userType = User.UserType.PATRON;
    private static User.UserType userType2 = User.UserType.RESEARCHER; //Verify allowedRentals changed

    private static String validEmail = "validuser@example.com";
    private static String validEmail2 = "validuser2@example.com";

    private static int allowedRentals = User.getDefaultAllowedRentals(User.UserType.PATRON);
    private static int currentRentals = 0;
    private static double lateFee = 0.0;
    private static boolean allowedToRent = true;
    private static boolean deleted = false;

    //List of test cases

    //validUserID2
    //userID 0

    //validUsername
    //Null username
    //Empty username
    //Short username
    //Long username

    //validPassword2
    //Null password
    //Empty password
    //Short password
    //Long password

    //userType2, change currentRentals to 5, verify allowedToRent false, update,
    //          verify allowedRentals == 20 and allowedToRent true
    //Null userType

    //validEmail2
    //Null email
    //Empty email
    //Short email
    //Long email

    // < 0 allowedRentals (user is changed to RESEARCHER so allowedRentals by default == 20)

    // < 0 currentRentals
    // currentRentals > allowedRentals (user is changed to RESEARCHER so allowedRentals by default == 20)

    // < 0 lateFee

    // lateFee == 0.0 && allowedRentals > currentRentals AND allowedToRent = false
    // lateFee > 0.0 AND allowedToRent = true
    // currentRentals >= defaultAllowedRentals
    // deleted == true and allowedToRent == true



    @BeforeAll
    static void setUp()
    {
        try
        {
            user = new User(validUserID1, validUsername, validPassword, userType, validEmail, allowedRentals,
                    currentRentals, lateFee, allowedToRent, deleted);
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }
    }

    /**
     *
     */
    @Test
    @Order(1)
    void testUserSetters()
    {
        System.out.println("\n1: Testing UserSetters...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("\nTEST FINISHED.");
    }
}