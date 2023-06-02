package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UserHandlerSetup class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserHandlerSetupTest extends BaseUserHandlerTest
{
    @Test
    @Order(1)
    void testSetup_EmptyDatabase()
    {
        System.out.println("\n1: Testing setup method with an empty database...");

        //Call the setup method
        UserHandler.setup();

        //Verify that storedUsernames and registeredEmails are empty
        assertEquals(0, UserHandler.getStoredUsernames().size(),
                "storedUsernames list should be empty after setup with an empty database");
        assertEquals(0, UserHandler.getRegisteredEmails().size(),
                "registeredEmails list should be empty after setup with an empty database");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setup method with some users in the database.
     * This test verifies the behavior of the setup method when there are existing users in the database.
     */
    @Test
    @Order(2)
    void testSetup_WithSomeUsersInDatabase()
    {
        System.out.println("\n2: Testing setup method with some users in the database...");

        //Check that storedUsernames is empty
        assertEquals(0, UserHandler.getStoredUsernames().size());

        //Insert some users into the database without using createNewUser (which automatically increments storedUsernames)
        String query = "INSERT INTO users (username, password, userType, email, allowedRentals, currentRentals, lateFee, " +
                "allowedToRent, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String[] params1 = {"user1", "pass1", "PATRON", "email1@test.com", "5", "0", "0.0", "1", "0"};
        String[] params2 = {"user2", "pass2", "PATRON", "email2@test.com", "5", "0", "0.0", "1", "0"};
        String[] params3 = {"user3", "pass3", "PATRON", "email3@test.com", "5", "0", "0.0", "1", "0"};
        DatabaseHandler.executePreparedUpdate(query, params1);
        DatabaseHandler.executePreparedUpdate(query, params2);
        DatabaseHandler.executePreparedUpdate(query, params3);

        //Call the setup method
        UserHandler.setup();

        //Verify that there are the expected amount of users in stored usernames and emails in registeredEmails
        assertEquals(3, UserHandler.getStoredUsernames().size());
        assertEquals(3, UserHandler.getRegisteredEmails().size());

        //Reset table
        resetUsersTable();

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testSetup_WithTestData()
    {
        System.out.println("\n3: Testing setup method with actual test data in the database...");

        try
        {
            //Fill with test data and setup UserHandler
            setupTestData();
            setupUserHandler();

            //Retrieve number of rows in users table
            int numberOfUsers = 0;
            QueryResult queryResult = DatabaseHandler.executePreparedQuery("SELECT COUNT(*) FROM users;", null);
            ResultSet resultSet = queryResult.getResultSet();
            if (resultSet.next()) {
                numberOfUsers = resultSet.getInt(1);
                System.out.println("Number of rows in users: " + numberOfUsers);
            } else fail("There should be users in the table.");

            assertEquals(numberOfUsers, UserHandler.getStoredUsernames().size());
            assertEquals(numberOfUsers, UserHandler.getRegisteredEmails().size());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}