
package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit TestSuite for the RentalHandler class.
 */
@Suite
@SelectClasses({
        CreateNewRentalTest.class,
        GetRentalByIDTest.class,
        GetOverdueRentalsTest.class,
        DeleteAndRecoverRentalTest.class,
        UpdateRentalTest.class,
        //GetRentalsByRentalDayTest.class,
        //GetRentalsByRentalDateTest.class,
})
public class RentalHandlerTestSuite
{
    protected static Connection connection = null;
    protected static final String testDatabaseName = "test_database";

    @BeforeAll
    protected static void setup()
    {
        setupConnectionAndTables();
        setupTestData();
        ItemHandler.setup(); //Fills maps with items
        UserHandler.setup(); //Fills list with users
        DatabaseHandler.setVerbose(false); //Get that thing to shut up
    }


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
            System.err.println("RentalHandlerTestSuite failed while setting up connection and tables due to " +
                    e.getClass().getName() + " Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected static void setupTestData()
    {
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
        DatabaseHandler.setVerbose(true);
    }

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    protected static void tearDown()
    {
        DatabaseHandler.closeDatabaseConnection();
    }
}
