package edu.groupeighteen.librarydbms.model.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UserCreation class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserCreationTest
{
    //Valid Admin + verify allowedRentals + verify ALL fields are set correctly
    //Valid Staff + verify allowedRentals + verify ALL fields are set correctly
    //Valid Patron + verify allowedRentals + verify ALL fields are set correctly
    //Valid Student + verify allowedRentals + verify ALL fields are set correctly
    //Valid Teacher + verify allowedRentals + verify ALL fields are set correctly
    //Valid Researcher + verify allowedRentals + verify ALL fields are set correctly

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

    @Test
    @Order(1)
    void testUser_ValidAdmin()
    {
        System.out.println("\n1: Testing User constructor with valid ADMIN...");

        try
        {
            String username = "Admin";
            String password = "AdminPassword";
            User.UserType userType = User.UserType.ADMIN;
            String email = "admin@example.com";

            User user = new User(username, password, email, userType);
            assertNotNull(user);

            //Verify ALL fields
            assertEquals(0, user.getUserID());
            assertEquals(username, user.getUsername(), "Username should be the same as the one set");
            assertEquals(password, user.getPassword(), "Password should be the same as the one set");
            assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
            assertEquals(email, user.getEmail(), "Email should be the same as the one set");
            assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                    "Allowed rentals should be default for userType");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
            assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
            assertFalse(user.isDeleted(), "User should not be marked as deleted");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testUser_ValidStaff()
    {
        System.out.println("\n2: Testing User constructor with valid STAFF...");

        try
        {
            String username = "Staff";
            String password = "StaffPassword";
            User.UserType userType = User.UserType.STAFF;
            String email = "staff@example.com";

            User user = new User(username, password, email, userType);
            assertNotNull(user);

            //Verify ALL fields
            assertEquals(0, user.getUserID());
            assertEquals(username, user.getUsername(), "Username should be the same as the one set");
            assertEquals(password, user.getPassword(), "Password should be the same as the one set");
            assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
            assertEquals(email, user.getEmail(), "Email should be the same as the one set");
            assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                    "Allowed rentals should be default for userType");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
            assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
            assertFalse(user.isDeleted(), "User should not be marked as deleted");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testUser_ValidPatron()
    {
        System.out.println("\n3: Testing User constructor with valid PATRON...");

        try
        {
            String username = "Patron";
            String password = "PatronPassword";
            User.UserType userType = User.UserType.PATRON;
            String email = "patron@example.com";

            User user = new User(username, password, email, userType);
            assertNotNull(user);

            //Verify ALL fields
            assertEquals(0, user.getUserID());
            assertEquals(username, user.getUsername(), "Username should be the same as the one set");
            assertEquals(password, user.getPassword(), "Password should be the same as the one set");
            assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
            assertEquals(email, user.getEmail(), "Email should be the same as the one set");
            assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                    "Allowed rentals should be default for userType");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
            assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
            assertFalse(user.isDeleted(), "User should not be marked as deleted");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testUser_ValidStudent()
    {
        System.out.println("\n4: Testing User constructor with valid STUDENT...");

        try
        {
            String username = "Student";
            String password = "StudentPassword";
            User.UserType userType = User.UserType.STUDENT;
            String email = "student@example.com";

            User user = new User(username, password, email, userType);
            assertNotNull(user);

            //Verify ALL fields
            assertEquals(0, user.getUserID());
            assertEquals(username, user.getUsername(), "Username should be the same as the one set");
            assertEquals(password, user.getPassword(), "Password should be the same as the one set");
            assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
            assertEquals(email, user.getEmail(), "Email should be the same as the one set");
            assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                    "Allowed rentals should be default for userType");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
            assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
            assertFalse(user.isDeleted(), "User should not be marked as deleted");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testUser_ValidTeacher()
    {
        System.out.println("\n5: Testing User constructor with valid TEACHER...");

        try
        {
            String username = "Teacher";
            String password = "TeacherPassword";
            User.UserType userType = User.UserType.TEACHER;
            String email = "teacher@example.com";

            User user = new User(username, password, email, userType);
            assertNotNull(user);

            //Verify ALL fields
            assertEquals(0, user.getUserID());
            assertEquals(username, user.getUsername(), "Username should be the same as the one set");
            assertEquals(password, user.getPassword(), "Password should be the same as the one set");
            assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
            assertEquals(email, user.getEmail(), "Email should be the same as the one set");
            assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                    "Allowed rentals should be default for userType");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
            assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
            assertFalse(user.isDeleted(), "User should not be marked as deleted");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    void testUser_ValidResearcher()
    {
        System.out.println("\n6: Testing User constructor with valid RESEARCHER...");

        try
        {
            String username = "Researcher";
            String password = "ResearcherPassword";
            User.UserType userType = User.UserType.RESEARCHER;
            String email = "researcher@example.com";

            User user = new User(username, password, email, userType);
            assertNotNull(user);

            //Verify ALL fields
            assertEquals(0, user.getUserID());
            assertEquals(username, user.getUsername(), "Username should be the same as the one set");
            assertEquals(password, user.getPassword(), "Password should be the same as the one set");
            assertEquals(userType, user.getUserType(), "UserType should be the same as the one set");
            assertEquals(email, user.getEmail(), "Email should be the same as the one set");
            assertEquals(User.getDefaultAllowedRentals(userType), user.getAllowedRentals(),
                    "Allowed rentals should be default for userType");
            assertEquals(0, user.getCurrentRentals(), "Current rentals should be 0 by default");
            assertEquals(0, user.getLateFee(), "Late fee should be 0 by default");
            assertFalse(user.isDeleted(), "User should not be marked as deleted");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testUser_NullUsername()
    {
        System.out.println("\n7: Testing User constructor with null username...");

        assertThrows(ConstructionException.class, () ->
                new User(null, "password", "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testUser_EmptyUsername()
    {
        System.out.println("\n8: Testing User constructor with empty username...");

        assertThrows(ConstructionException.class, () ->
                new User("", "password", "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testUser_ShortUsername()
    {
        System.out.println("\n9: Testing User constructor with short username...");

        String shortUsername = "ab"; //less than MIN_USERNAME_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(shortUsername, "password", "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testUser_LongUsername()
    {
        System.out.println("\n10: Testing User constructor with long username...");

        String longUsername = String.join("",
                Collections.nCopies(User.MAX_USERNAME_LENGTH + 1, "a")); //More than MAX_USERNAME_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User(longUsername, "password", "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testUser_NullPassword()
    {
        System.out.println("\n11: Testing User constructor with null password...");

        assertThrows(ConstructionException.class, () ->
                new User("username", null, "patron@example.com", User.UserType.PATRON));


        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testUser_EmptyPassword()
    {
        System.out.println("\n12: Testing User constructor with empty password...");

        assertThrows(ConstructionException.class, () ->
                new User("username", "", "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testUser_ShortPassword()
    {
        System.out.println("\n13: Testing User constructor with short password...");

        String shortPassword = "abc1234"; //less than MIN_PASSWORD_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User("username", shortPassword, "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testUser_LongPassword()
    {
        System.out.println("\n14: Testing User constructor with long password...");

        String longPassword = String.join("",
                Collections.nCopies(User.MAX_PASSWORD_LENGTH + 1, "a")); //More than MAX_PASSWORD_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User("username", longPassword, "patron@example.com", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(15)
    void testUser_NullUserType()
    {
        System.out.println("\n15: Testing User constructor with null userType...");

        assertThrows(ConstructionException.class, () ->
                new User("username", "password", "patron@example.com", null));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(16)
    void testUser_NullEmail()
    {
        System.out.println("\n16: Testing User constructor with null email...");

        assertThrows(ConstructionException.class, () ->
                new User("username", "password", null, User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(17)
    void testUser_EmptyEmail()
    {
        System.out.println("\n17: Testing User constructor with empty email...");

        assertThrows(ConstructionException.class, () ->
                new User("username", "password", "", User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(18)
    void testUser_ShortEmail()
    {
        System.out.println("\n18: Testing User constructor with short email...");

        String shortEmail = "a@b.c"; //less than MIN_EMAIL_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User("username", "password", shortEmail, User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(19)
    void testUser_LongEmail()
    {
        System.out.println("\n19: Testing User constructor with long email...");

        String longEmail = "a".repeat(User.MAX_EMAIL_LENGTH + 1); //More than MAX_EMAIL_LENGTH

        assertThrows(ConstructionException.class, () ->
                new User("username", "password", longEmail, User.UserType.PATRON));

        System.out.println("\nTEST FINISHED.");
    }
}