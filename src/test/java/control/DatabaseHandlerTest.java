package control;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit Test for the DatabaseHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseHandlerTest extends BaseHandlerTest {

    //TODO-future make all tests more verbose
    //TODO-future javadoc tests properly

    @Test
    @Order(1)
    void testExecuteCommand() {
        System.out.println("1: Testing executeSingleSQLCommand method...");
        // 1. Create a temporary table in the test database
        String createTempTable = "CREATE TABLE temp_table (id INT PRIMARY KEY, name VARCHAR(255));";
        try {
            DatabaseHandler.executeCommand(createTempTable);
        } catch (SQLException e) {
            fail("Failed to create temp_table: " + e.getMessage());
        }

        // 2. Insert some data into the temporary table
        String insertData = "INSERT INTO temp_table (id, name) VALUES (1, 'Test User');";
        try {
            DatabaseHandler.executeCommand(insertData);
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
            DatabaseHandler.executeCommand(dropTempTable);
        } catch (SQLException e) {
            fail("Failed to drop temp_table: " + e.getMessage());
        }
        System.out.println("Test finished.");
    }

    @Test
    @Order(2)
    void testExecuteQuery() {
        System.out.println("2: Testing executeQuery method...");
        String tableName = "test_table";
        try {
            // Create a new table
            String createTableQuery = "CREATE TABLE " + tableName + " (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255))";
            DatabaseHandler.executeCommand(createTableQuery);

            // Verify the table was created
            QueryResult tableVerificationResult = DatabaseHandler.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
            assertTrue(tableVerificationResult.getResultSet().next(), "Table " + tableName + " should exist");

            // Insert data into the table
            String insertDataQuery = "INSERT INTO " + tableName + " (name) VALUES ('John Doe')";
            DatabaseHandler.executeCommand(insertDataQuery);

            // Verify data was inserted
            QueryResult dataVerificationResult = DatabaseHandler.executeQuery("SELECT * FROM " + tableName);
            ResultSet resultSet = dataVerificationResult.getResultSet();
            assertNotNull(resultSet, "Result set should not be null");
            assertTrue(resultSet.next(), "Result set should have at least one row");
            assertEquals("John Doe", resultSet.getString("name"), "Name should be 'John Doe'");
            dataVerificationResult.close();
        } catch (Exception e) {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Drop the test table and close resources
            try {
                DatabaseHandler.executeCommand("DROP TABLE IF EXISTS " + tableName);
            } catch (SQLException e) {
                fail("Exception occurred while closing resources: " + e.getMessage());
            }
        }
        System.out.println("Test finished.");
    }

    @Test
    @Order(3)
    void testExecutePreparedQuery() {
        System.out.println("3: Testing executePreparedQuery method...");
        String tableName = "test_table";
        try {
            // Create a new table
            String createTableQuery = "CREATE TABLE " + tableName + " (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255))";
            DatabaseHandler.executeCommand(createTableQuery);

            // Insert data into the table using executePreparedQuery
            String insertDataQuery = "INSERT INTO " + tableName + " (name) VALUES (?)";
            String[] parameters = {"John Doe"};
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(insertDataQuery, parameters, Statement.RETURN_GENERATED_KEYS);

            // Verify data was inserted
            int generatedId = -1;
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
            assertTrue(generatedId != -1, "Generated ID should not be -1");

            // Clean up
            queryResult.close();
        }

        catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        finally {
            // Drop the test table and close resources
            try {
                DatabaseHandler.executeCommand("DROP TABLE IF EXISTS " + tableName);
            } catch (SQLException e) {
                fail("Exception occurred while closing resources: " + e.getMessage());
            }
        }
        System.out.println("Test finished.");
    }

    @Test
    @Order(4)
    void testExecuteSQLCommandsFromFile() {
        System.out.println("4: Testing executeSQLCommandsFromFile method...");
        // Set up the path to the test SQL file
        String testSQLFilePath = "src/test/resources/sql/test_sql_file.sql";

        // Create the test SQL file
        File testFile = createTestSQLFile(testSQLFilePath);

        // Call the method to execute the commands in the test SQL file
        assert testFile != null;
        DatabaseHandler.executeSQLCommandsFromFile(testFile.getPath());

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
            DatabaseHandler.executeCommand("DROP TABLE test_table");
            resultSet.close();
            statement.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to verify the result of executing SQL commands from file.");
        }

        testFile.delete();
        System.out.println("Test finished.");
    }

    /**
     * Creates an sql file for testing purposes.
     * @param filePath the path to the sql file
     */
    private File createTestSQLFile(String filePath) {
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
            return testSQLFile;
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test SQL file.");
            return null;
        }
    }

    @Test
    @Order(5)
    void testDatabaseExistsAndCreateDatabase() {
        System.out.println("5: Testing databaseExists and createDatabase methods...");
        try {
            DatabaseHandler.executeCommand("drop database if exists " + LibraryManager.databaseName);
            assertFalse(DatabaseHandler.databaseExists(LibraryManager.databaseName));
            DatabaseHandler.setVerbose(false);
            DatabaseHandler.createDatabase(LibraryManager.databaseName);
            DatabaseHandler.setVerbose(true);
            assertTrue(DatabaseHandler.databaseExists(LibraryManager.databaseName));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to execute SQL command.");
        }
        System.out.println("Test finished.");
    }

    @Test
    @Order(6)
    void testExecuteUpdate() {
        System.out.println("\n6: Testing executeUpdate...");
        setupTestTablesAndData();

        //Let's assume that there is a user with ID 1 in the database.
        int userIdToUpdate = 1;
        String newUsername = "updated_username";
        String newPassword = "updated_password";

        //Prepare the SQL command and the parameters.
        String sql = "UPDATE users SET username = ?, password = ? WHERE userID = ?";
        String[] params = {newUsername, newPassword, String.valueOf(userIdToUpdate)};

        //Execute the update.
        int rowsAffected = 0;
        try {
            rowsAffected = DatabaseHandler.executeUpdate(sql, params);
            //Verify that the update was successful.
            assertTrue(rowsAffected > 0);

            //Now, retrieve the updated user to verify that the username and password were updated.
            User updatedUser = UserHandler.getUserByID(userIdToUpdate);
            assertNotNull(updatedUser);
            assertEquals(newUsername, updatedUser.getUsername());
            assertEquals(newPassword, updatedUser.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to execute update: " + e.getMessage());
        }
        System.out.println("Test finished.");
    }
}