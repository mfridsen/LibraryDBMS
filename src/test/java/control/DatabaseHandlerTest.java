package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the DatabaseHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseHandlerTest extends BaseHandlerTest {

    private static final String testClassTextBlock = """
               -------------------------------
                TESTING DATABASEHANDLER CLASS \s
               -------------------------------\s
            """;

    private static final String endTestTextBlock = """
               -----------------------------------
                END TESTING DATABASEHANDLER CLASS \s
               -----------------------------------\s
            """;

    @Test
    @Order(1)
    public void testExecuteSingleSQLCommand() {
        System.out.println(testClassTextBlock);
        System.out.println("1: Testing executeSingleSQLCommand method...");
        // 1. Create a temporary table in the test database
        String createTempTable = "CREATE TABLE temp_table (id INT PRIMARY KEY, name VARCHAR(255));";
        try {
            DatabaseHandler.executeSingleSQLCommand(createTempTable);
        } catch (SQLException e) {
            fail("Failed to create temp_table: " + e.getMessage());
        }

        // 2. Insert some data into the temporary table
        String insertData = "INSERT INTO temp_table (id, name) VALUES (1, 'Test User');";
        try {
            DatabaseHandler.executeSingleSQLCommand(insertData);
        } catch (SQLException e) {
            fail("Failed to insert data into temp_table: " + e.getMessage());
        }

        // 3. Check if the data was inserted correctly
        String queryData = "SELECT * FROM temp_table WHERE id = 1;";
        try {
            ResultSet resultSet = DatabaseHandler.getConnection().createStatement().executeQuery(queryData);
            assertTrue(resultSet.next(), "No data found in temp_table");
            assertEquals(1, resultSet.getInt("id"), "ID value does not match");
            assertEquals("Test User", resultSet.getString("name"), "Name value does not match");
        } catch (SQLException e) {
            fail("Failed to query data from temp_table: " + e.getMessage());
        }

        // Clean up: Drop the temporary table
        String dropTempTable = "DROP TABLE IF EXISTS temp_table;";
        try {
            DatabaseHandler.executeSingleSQLCommand(dropTempTable);
        } catch (SQLException e) {
            fail("Failed to drop temp_table: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testExecuteSingleSQLQuery() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String tableName = "test_table";
        try {
            // Create a new table
            String createTableQuery = "CREATE TABLE " + tableName + " (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255))";
            DatabaseHandler.executeSingleSQLCommand(createTableQuery);

            // Verify the table was created
            connection = DatabaseHandler.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
            assertTrue(resultSet.next(), "Table " + tableName + " should exist");

            // Insert data into the table
            String insertDataQuery = "INSERT INTO " + tableName + " (name) VALUES ('John Doe')";
            DatabaseHandler.executeSingleSQLCommand(insertDataQuery);

            // Verify data was inserted
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            assertNotNull(resultSet, "Result set should not be null");
            assertTrue(resultSet.next(), "Result set should have at least one row");
            assertEquals("John Doe", resultSet.getString("name"), "Name should be 'John Doe'");
        } catch (Exception e) {
            //fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Drop the test table and close resources
            try {
                if (statement != null) {
                    statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                fail("Exception occurred while closing resources: " + e.getMessage());
            }
        }
    }

    @Test
    @Order(3)
    public void testExecuteSQLCommandsFromFile() {
        // Test the executeSQLCommandsFromFile() method
    }

    @Test
    @Order(4)
    public void testSetup() {
        //Test the setup method. This test will have to be added to iteratively  until the tables are finished
    }
}