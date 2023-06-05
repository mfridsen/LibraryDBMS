package edu.groupeighteen.librarydbms.model.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
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
 * Unit Test for the UserRetrieval class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRetrievalTest
{
    //Valid retrieval
    //Invalid ID

    //Null username
    //Empty username
    //Short username
    //Long username

    //Null password
    //Empty password
    //Short password
    //Long password

    //Null userType

    //Null email
    //Empty email
    //Short email
    //Long email

    //Invalid allowedRentals
    // < 0 currentRentals
    // currentRentals > allowedRentals

    // < 0 lateFee

    // lateFee == 0.0 && allowedRentals > currentRentals AND allowedToRent = false
    // lateFee > 0.0 AND allowedToRent = true
    // currentRentals >= defaultAllowedRentals
    // deleted == true and allowedToRent == true

    //Valid retrieval
    @Test
    @Order(1)
    void testUser_ValidRetrieval()
    {
        System.out.println("\n1: Testing User constructor with valid inputs...");

        try
        {
            int userId = 1;
            String username = "validusername";
            String password = "validpassword";
            String email = "validuser@example.com";
            User.UserType userType = User.UserType.PATRON;
            int allowedRentals = User.getDefaultAllowedRentals(User.UserType.PATRON);
            int currentRentals = 0;
            double lateFee = 0.0;
            boolean allowedToRent = true;
            boolean deleted = false;

            User user = new User(userId, username, password, email, userType, allowedRentals, currentRentals, lateFee,
                    allowedToRent, deleted);

            assertNotNull(user);
            assertEquals(userId, user.getUserID());
            assertEquals(username, user.getUsername());
            assertEquals(password, user.getPassword());
            assertEquals(email, user.getEmail());
            assertEquals(userType, user.getUserType());
            assertEquals(allowedRentals, user.getAllowedRentals());
            assertEquals(currentRentals, user.getCurrentRentals());
            assertEquals(lateFee, user.getLateFee());
            assertEquals(allowedToRent, user.isAllowedToRent());
            assertEquals(deleted, user.isDeleted());
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    // Invalid ID
    @Test
    @Order(2)
    void testUser_InvalidID()
    {
        System.out.println("\n2: Testing User constructor with invalid ID...");

        assertThrows(ConstructionException.class, () ->
                new User(-1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Null username
    @Test
    @Order(3)
    void testUser_NullUsername()
    {
        System.out.println("\n3: Testing User constructor with null username...");

        assertThrows(ConstructionException.class, () ->
                new User(1, null, "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Empty username
    @Test
    @Order(4)
    void testUser_EmptyUsername()
    {
        System.out.println("\n4: Testing User constructor with empty username...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Short username
    @Test
    @Order(5)
    void testUser_ShortUsername()
    {
        System.out.println("\n5: Testing User constructor with short username...");

        String shortUsername = "ab"; // less than MIN_USERNAME_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(1, shortUsername, "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Long username
    @Test
    @Order(6)
    void testUser_LongUsername()
    {
        System.out.println("\n6: Testing User constructor with long username...");

        String longUsername = "a".repeat(User.MAX_USERNAME_LENGTH + 1); // More than MAX_USERNAME_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(1, longUsername, "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Null password
    @Test
    @Order(7)
    void testUser_NullPassword()
    {
        System.out.println("\n7: Testing User constructor with null password...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", null, "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Empty password
    @Test
    @Order(8)
    void testUser_EmptyPassword()
    {
        System.out.println("\n8: Testing User constructor with empty password...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Short password
    @Test
    @Order(9)
    void testUser_ShortPassword()
    {
        System.out.println("\n9: Testing User constructor with short password...");

        String shortPassword = "abc1234"; // less than MIN_PASSWORD_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", shortPassword, "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Long password
    @Test
    @Order(10)
    void testUser_LongPassword()
    {
        System.out.println("\n10: Testing User constructor with long password...");

        String longPassword = "a".repeat(User.MAX_PASSWORD_LENGTH + 1); // More than MAX_PASSWORD_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", longPassword, "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Null userType
    @Test
    @Order(11)
    void testUser_NullUserType()
    {
        System.out.println("\n11: Testing User constructor with null userType...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", null,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Null email
    @Test
    @Order(12)
    void testUser_NullEmail()
    {
        System.out.println("\n12: Testing User constructor with null email...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", null, User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Empty email
    @Test
    @Order(13)
    void testUser_EmptyEmail()
    {
        System.out.println("\n13: Testing User constructor with empty email...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Short email
    @Test
    @Order(14)
    void testUser_ShortEmail()
    {
        System.out.println("\n14: Testing User constructor with short email...");

        String shortEmail = "a@b.c"; // less than MIN_EMAIL_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", shortEmail, User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Long email
    @Test
    @Order(15)
    void testUser_LongEmail()
    {
        System.out.println("\n15: Testing User constructor with long email...");

        String longEmail = "a@b." + "a".repeat(User.MAX_EMAIL_LENGTH + 1); // More than MAX_EMAIL_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", longEmail, User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // Invalid allowedRentals
    @Test
    @Order(16)
    void testUser_InvalidAllowedRentals()
    {
        System.out.println("\n16: Testing User constructor with invalid allowedRentals...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        -1, 0, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // < 0 currentRentals
    @Test
    @Order(17)
    void testUser_NegativeCurrentRentals()
    {
        System.out.println("\n17: Testing User constructor with negative currentRentals...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), -1, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // currentRentals > allowedRentals
    @Test
    @Order(18)
    void testUser_CurrentRentalsExceedingAllowedRentals()
    {
        System.out.println("\n18: Testing User constructor with currentRentals exceeding allowedRentals...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        5, 6, 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // < 0 lateFee
    @Test
    @Order(19)
    void testUser_NegativeLateFee()
    {
        System.out.println("\n19: Testing User constructor with negative lateFee...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, -1.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // lateFee == 0.0 && allowedRentals > currentRentals AND allowedToRent = false
    @Test
    @Order(20)
    void testUser_LateFeeZeroNotAllowedToRent()
    {
        System.out.println(
                "\n20: Testing User constructor with lateFee == 0.0 && allowedRentals > currentRentals AND allowedToRent = false...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        5, 4, 0.0, false, false));

        System.out.println("\nTEST FINISHED.");
    }

    // lateFee > 0.0 AND allowedToRent = true
    @Test
    @Order(21)
    void testUser_LateFeePositiveAllowedToRent()
    {
        System.out.println("\n21: Testing User constructor with lateFee > 0.0 AND allowedToRent = true...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 1.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // currentRentals >= defaultAllowedRentals
    @Test
    @Order(22)
    void testUser_CurrentRentalsEqualToDefaultAllowedRentals()
    {
        System.out.println("\n22: Testing User constructor with currentRentals >= defaultAllowedRentals...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON),
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0.0, true, false));

        System.out.println("\nTEST FINISHED.");
    }

    // deleted == true and allowedToRent == true
    @Test
    @Order(23)
    void testUser_DeletedAllowedToRent()
    {
        System.out.println("\n23: Testing User constructor with deleted == true and allowedToRent == true...");

        assertThrows(ConstructionException.class, () ->
                new User(1, "validusername", "validpassword", "validuser@example.com", User.UserType.PATRON,
                        User.getDefaultAllowedRentals(User.UserType.PATRON), 0, 0.0, true, true));

        System.out.println("\nTEST FINISHED.");
    }

}