package edu.groupeighteen.librarydbms.model.db;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package model
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the DatabaseConnectionTest class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseConnectionTest
{
    /**
     * Create the connection to the database.
     */
    @BeforeAll
    static void setup()
    {
        try
        {
            System.out.println("BeforeAll:");
            //TODO handle?
            DatabaseConnection.setVerbose(true);
            DatabaseConnection.setup();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    static void tearDown()
    {
        DatabaseConnection.closeConnection();
    }

    /**
     * Tests the getConnection method of the DatabaseConnection class by asserting that the
     * connection object is not null.
     */
    @Test
    @Order(1)
    public void testConnection()
    {
        System.out.println("\n1: Testing connection...");
        System.out.println("Creating connection to database...");
        Connection connection = DatabaseConnection.getConnection();
        System.out.println("Asserting connection is not null, should be true: " + (connection != null));
        Assertions.assertNotNull(connection);
        DatabaseConnection.closeConnection();
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the connectToDatabaseServer method of the DatabaseConnection class with an incorrect URL.
     * Expects a SQLException and asserts that the error message contains "Communications link failure".
     */
    @Test
    @Order(2)
    public void testConnectionWithIncorrectURL()
    {
        System.out.println("\n2: Attempting to connect to an invalid URL...");
        String url = "jdbc:mysql://localhost:3307"; // incorrect port number
        String user = "root";
        String password = "password";

        SQLException exception = assertThrows(SQLException.class, () ->
        {
            DatabaseConnection.connectToDatabaseServer(url, user, password);
        });

        String expectedMessage = "Communications link failure";
        String actualMessage = exception.getMessage();

        System.out.println("Asserting that error message contains '" + expectedMessage +
                "', should be true: " + actualMessage.contains(expectedMessage));
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the connectToDatabaseServer method of the DatabaseConnection class with an incorrect username.
     * Expects a SQLException and asserts that the error message contains "Access denied for user".
     */
    @Test
    @Order(3)
    public void testConnectionWithIncorrectUsername()
    {
        System.out.println("\n3: Attempting to connect with an invalid username...");
        String url = "jdbc:mysql://localhost:3306";
        String user = "badusername";
        String password = "password";

        SQLException exception = assertThrows(SQLException.class, () ->
        {
            DatabaseConnection.connectToDatabaseServer(url, user, password);
        });

        String expectedMessage = "Access denied for user";
        String actualMessage = exception.getMessage();

        System.out.println("Asserting that error message contains '" + expectedMessage +
                "', should be true: " + actualMessage.contains(expectedMessage));
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the connectToDatabaseServer method of the DatabaseConnection class with an incorrect username.
     * Expects a SQLException and asserts that the error message contains "Access denied for user".
     */
    @Test
    @Order(4)
    public void testConnectionWithIncorrectPassword()
    {
        System.out.println("\n4: Attempting to connect with an invalid password...");
        String url = "jdbc:mysql://localhost:3306";
        String user = "root";
        String password = "badpassword";

        SQLException exception = assertThrows(SQLException.class, () ->
        {
            DatabaseConnection.connectToDatabaseServer(url, user, password);
        });

        String expectedMessage = "Access denied for user";
        String actualMessage = exception.getMessage();

        System.out.println("Asserting that error message contains '" + expectedMessage +
                "', should be true: " + actualMessage.contains(expectedMessage));
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("\nTEST FINISHED.");
    }
}