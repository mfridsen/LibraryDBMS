package edu.groupeighteen.librarydbms.control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package control
 * @contact matfir-1@student.ltu.se
 * @date 5/5/2023
 * <p>
 * This class contains the methods and fields that are shared among all HandlerTest classes, in order to better adhere
 * to the DRY principle.
 */
public abstract class BaseHandlerTest
{

    protected static final String testDatabaseName = "test_database";
    protected Connection connection = null;

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    static protected void tearDown()
    {
        DatabaseHandler.closeDatabaseConnection();
    }

    /**
     * Create the connection to the database, set DatabaseHandlers connection, and reset the database before each test.
     */
    @BeforeEach
    protected void setupAndReset()
    {
        System.out.println("\nSetting up and resetting database...");
        try
        {
            setupConnectionAndTables();
            setupTestData();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        System.out.println("Setup finished.");
    }

    protected void setupConnectionAndTables()
    throws SQLException, ClassNotFoundException
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

    protected void setupTestData()
    {
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
        DatabaseHandler.setVerbose(true);
    }
}