package control;

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
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 *
 * This class contains the methods and fields that are shared among all HandlerTest classes, in order to better adhere
 * to the DRY principle.
 */
public abstract class BaseHandlerTest {

    protected Connection connection = null;
    protected static final String testDatabaseName = "test_database";

    /**
     * Create the connection to the database, set DatabaseHandlers connection, and reset the database before each test.
     */
    @BeforeEach
    void setupAndReset() {
        try {
            connection = DatabaseConnection.connectToLocalSQLServer();
            DatabaseHandler.setConnection(connection);
            DatabaseHandler.setVerbose(true); //For testing we want DBHandler to be Verboten
            DatabaseHandler.executeSingleSQLCommand("drop database if exists " + testDatabaseName);
            DatabaseHandler.executeSingleSQLCommand("create database " + testDatabaseName);
            DatabaseHandler.executeSingleSQLCommand("use " + testDatabaseName);
            //DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/create_tables.sql");
            //DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/data/test_data.sql");
        }

        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    static void tearDown() {
        DatabaseHandler.closeDatabaseConnection();
    }
}