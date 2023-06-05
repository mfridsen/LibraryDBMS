package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities.user
 * @contact matfir-1@student.ltu.se
 * @date 6/2/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public abstract class BaseUserHandlerTest
{
    protected static final String testDatabaseName = "test_database";
    protected static Connection connection;

    /**
     * This method sets up the test environment before all test cases are executed.
     * It sets up a connection to the database and creates the necessary tables for testing.
     */
    @BeforeAll
    protected static void setUp()
    {
        //The connection and tables need to be set up before tests can commence.
        setupConnectionAndTables();
        //Test data and userhandler setups are optional
        customSetup();
    }

    /**
     * To be overridden when needed.
     */
    protected static void customSetup()
    {

    }

    /**
     * This method sets up a connection to the database and creates necessary tables for testing.
     */
    protected static void setupConnectionAndTables()
    {
        try
        {
            connection = DatabaseConnection.setup();
            DatabaseHandler.setConnection(connection);
            DatabaseHandler.setVerbose(true); //For testing we want DBHandler to be Verboten
            DatabaseHandler.executeCommand("drop database if exists " + testDatabaseName);
            DatabaseHandler.executeCommand("create database " + testDatabaseName);
            DatabaseHandler.executeCommand("use " + testDatabaseName);
            DatabaseHandler.setVerbose(false);
            DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/create_tables.sql");
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
            Assertions.fail("Failed due to exception in setupConnectionAndTables: ", e);
        }
    }

    /**
     * This method sets up test data for use in the test cases.
     */
    protected static void setupTestData()
    {
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/user_test_data.sql");
        DatabaseHandler.setVerbose(true);
    }

    /**
     * This method sets up UserHandler for use in the test cases.
     */
    protected static void setupUserHandler()
    {
        UserHandler.setup();
    }

    /**
     * This method is called after all test cases are executed. It drops the test database and closes the connection.
     */
    @AfterAll
    static void tearDown()
    {
        System.out.println("\nCleaning up test data...");

        try
        {
            //Drop the test database
            DatabaseHandler.executeCommand("DROP DATABASE IF EXISTS " + testDatabaseName);

            //Close the database connection
            if (connection != null && !connection.isClosed())
            {
                DatabaseHandler.closeDatabaseConnection();
            }

            //Reset UserHandler
            resetUserHandler();
        }
        catch (SQLException e)
        {
            System.err.println("An error occurred during cleanup: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nCLEANUP FINISHED.");
    }

    protected static void resetUserHandler()
    {
        UserHandler.reset();
    }

    protected static void resetUsersTable()
    {
        //When a new record is inserted into the table, MySQL automatically increments the userID value,
        //regardless of whether old records are deleted. So, if the first user is deleted, the next user that is
        //created will have userID equal to 2, not 1.
        DatabaseHandler.executeCommand("DELETE FROM users;");
        DatabaseHandler.executeCommand("ALTER TABLE users AUTO_INCREMENT = 1;");
    }

    protected static int getNumberOfUsers()
    {
        int numberOfUsers = 0;

        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery("SELECT COUNT(*) FROM users;", null))
        {
            ResultSet resultSet = queryResult.getResultSet();
            if (resultSet.next())
            {
                numberOfUsers = resultSet.getInt(1);
                System.out.println("Number of rows in users: " + numberOfUsers);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return numberOfUsers;
    }

    /**
     * This method is called after each test case. It resets the UserHandler to its default state.
     */
    @AfterEach
    protected void reset()
    {
        resetUserHandler();
    }
}