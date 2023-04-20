package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.user.UserHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
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
                Testing UserHandler Class \s
               ---------------------------\s
            """;

    private static Connection connection = null;

    /**
     * Create the connection to the database.
     */
    @BeforeAll
    static void setup() {
        System.out.println("BeforeAll:");
        try { //TODO handle?
            connection = DatabaseConnection.connectToLocalSQLServer();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    static void tearDown() {
        DatabaseConnection.closeConnection();
    }

    /**
     * Tests all constructors in UserHandler.
     */
    @Test
    @Order(1)
    public void testRetrieveUsernamesFromTable() throws SQLException, ClassNotFoundException {
        System.out.println("\n" + testClassTextBlock);
        System.out.println("1: Testing to retrieve usernames from table...");
        DatabaseHandler.setup();
        UserHandler.retrieveUsernamesFromTable(connection);
        assertFalse(UserHandler.getUsernames().isEmpty());
        UserHandler.printUsernames();
    }

    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields. Technically we already tested all our getters but whatever.
     */
    @Test
    @Order(2)
    public void testGettersAndSetters() {
        System.out.println("\n2: Testing getters and setters...");
        System.out.println("No test implemented here yet!");
        //TODO Write more code here
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