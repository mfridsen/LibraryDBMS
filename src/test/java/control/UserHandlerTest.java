package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.user.UserHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/19/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the UserHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserHandlerTest {
    private static final String testClassTextBlock = """
               ---------------------------
                TESTING USERHANDLER CLASS \s
               ---------------------------\s
            """;

    private static final String endTestTextBlock = """
               -------------------------
                END TESTING USERHANDLER \s
               -------------------------\s
            """;

    private static final String demoDatabaseName = "demo_database";

    private static Connection connection = null;

    /**
     * Create the connection to the database and set DatabaseHandlers connection.
     */
    @BeforeAll
    static void setup() {
        System.out.println(testClassTextBlock);
        try { //TODO handle?
            connection = DatabaseConnection.connectToLocalSQLServer();
            DatabaseHandler.setConnection(connection); //Need to set connection in DBHandler or pass to it
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the database before each test. Drops and creates a demo database, uses demo database and fills it with
     * test data.
     */
    @BeforeEach
    void reset() {
        try {
            //Connection seems to drop when we get here a second time
            if (connection.isClosed()) {
                System.out.println("Connection is closed before drop database command.");
                connection = DatabaseConnection.connectToLocalSQLServer();
                DatabaseHandler.setConnection(connection);
            }

            DatabaseHandler.executeSingleSQLCommand("drop database if exists " + demoDatabaseName);
            DatabaseHandler.executeSingleSQLCommand("create database " + demoDatabaseName);
            DatabaseHandler.executeSingleSQLCommand("use " + demoDatabaseName);
            DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/create_tables.sql");
            DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/data/test_data.sql");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    static void tearDown() {
        System.out.println(endTestTextBlock);
        DatabaseConnection.closeConnection();
    }

    /**
     * Tests retrieving the usernames from the demo database.
     */
    @Test
    @Order(1)
    public void testRetrieveUsernamesFromTable() throws SQLException, ClassNotFoundException {
        System.out.println("1: Testing to retrieve usernames from table...");
        UserHandler.retrieveUsernamesFromTable(connection);
        assertFalse(UserHandler.getUsernames().isEmpty());
        UserHandler.printUsernames();
    }

    /**
     *
     */
    @Test
    @Order(2)
    public void testCreateNewUser() {
        System.out.println("\n2: Testing to create a new user...");
        User newUser = UserHandler.createNewUser("example_username", "example_password");
        if (newUser == null) {
            // Display an error message to the user or take another action
        } else {
            // Continue with the newUser object
        }
    }

    /**
     * Dummy test method for looping through valid and invalid test data.
     */
    @Test
    @Order(3)
    public void testIsValidTestString() {
        System.out.println("THIS IS A DUMMY TEST METHOD TEMPLATE THAT LOOPS THROUGH VALID AND INVALID TEST DATA!");

    }
}