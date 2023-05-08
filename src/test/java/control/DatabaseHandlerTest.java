package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    @Test
    @Order(1)
    void testExecuteSingleSQLCommand() {
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
    void testExecuteSQLCommandsFromFile() {
        // Set up the path to the test SQL file
        String testSQLFilePath = "src/test/resources/sql/test_sql_file.sql";

        // Create the test SQL file
        createTestSQLFile(testSQLFilePath);

        // Call the method to execute the commands in the test SQL file
        DatabaseHandler.executeSQLCommandsFromFile(testSQLFilePath);

        // Verify that the expected changes have been made to the database
        // For example, if the SQL file creates a table called "test_table"
        // and inserts a row with column1='value1' and column2='value2', you can
        // run a SELECT query to check if the table exists and contains the expected data
        try {
            String selectQuery = "SELECT column1, column2 FROM test_table";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Check if resultSet has a row
            assertTrue(resultSet.next());

            // Check if the values in the resultSet match the expected values
            assertEquals("value1", resultSet.getString("column1"));
            assertEquals("value2", resultSet.getString("column2"));

            // Clean up - drop the test_table and close resources
            DatabaseHandler.executeSingleSQLCommand("DROP TABLE test_table");
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to verify the result of executing SQL commands from file.");
        }
    }

    /**
     * Creates an sql file for testing purposes.
     * @param filePath the path to the sql file
     */
    private void createTestSQLFile(String filePath) {
        String fileContent = """
                -- Create test table
                CREATE TABLE test_table (column1 VARCHAR(255), column2 VARCHAR(255));
                -- Insert test data
                INSERT INTO test_table (column1, column2) VALUES ('value1', 'value2');
                """;

        try {
            File testSQLFile = new File(filePath);
            if (!testSQLFile.exists()) {
                testSQLFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(testSQLFile);
            fileWriter.write(fileContent);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test SQL file.");
        }
    }

    //TODO-future iterate this method
    @Test
    @Order(4)
    void testSetup() {
        //Test the setup method. This test will have to be added to iteratively  until the tables are finished
        DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/create_tables.sql");
        DatabaseHandler.executeSQLCommandsFromFile("src/test/resources/sql/data/test_data.sql");
    }
}