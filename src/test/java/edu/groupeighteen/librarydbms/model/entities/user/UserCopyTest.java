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
 * Unit Test for the UserCopy class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserCopyTest
{
    /**
     *
     */
    @Test
    @Order(1)
    void testUserCopy()
    {
        System.out.println("\n1: Testing User Copy Constructor...");

        try
        {
            //Define valid variables
            int userId = 1;
            String username = "validusername";
            String password = "validpassword";
            User.UserType userType = User.UserType.PATRON;
            String email = "validuser@example.com";
            int allowedRentals = User.getDefaultAllowedRentals(User.UserType.PATRON);
            int currentRentals = 0;
            double lateFee = 0.0;
            boolean allowedToRent = true;
            boolean deleted = false;

            //Create original
            User user = new User(userId, username, password, email, userType, allowedRentals, currentRentals, lateFee,
                    allowedToRent, deleted);

            //Copy
            User copy = new User(user);

            //Assert
            assertNotNull(copy);
            assertEquals(userId, copy.getUserID());
            assertEquals(username, copy.getUsername());
            assertEquals(password, copy.getPassword());
            assertEquals(userType, copy.getUserType());
            assertEquals(email, copy.getEmail());
            assertEquals(allowedRentals, copy.getAllowedRentals());
            assertEquals(currentRentals, copy.getCurrentRentals());
            assertEquals(lateFee, copy.getLateFee());
            assertEquals(allowedToRent, copy.isAllowedToRent());
            assertEquals(deleted, copy.isDeleted());
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}