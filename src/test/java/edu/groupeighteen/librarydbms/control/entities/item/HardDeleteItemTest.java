package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the hardDeleteFilm method.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HardDeleteItemTest
{
    protected static final String testDatabaseName = "test_database";
    //We need all this stuff, copied from BaseHandlerTest but adapted to be static
    protected static Connection connection = null;
    // Declare these at the class level so all tests can access them
    static Film film;
    static Literature literature;

    //BeforeEach
    static void setupConnectionAndTables()
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

    static void setupTestData()
    {
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
        DatabaseHandler.setVerbose(true);
    }

    @BeforeAll
    static void setup()
    {
        System.out.println("\nSetting up test data...");

        try
        {
            setupConnectionAndTables();
            setupTestData();
            ItemHandler.setup(); //Otherwise barcodes are stored from previous tests
            // Create new Film and Literature objects
            film = ItemHandler.createNewFilm("Test Film", 1, 2, "1234", 15);
            assertNotNull(film);
            literature = ItemHandler.createNewLiterature("Test Literature", Item.ItemType.OTHER_BOOKS,
                    1, 2, "5678", "978-3-16-1484");
            assertNotNull(literature);
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException |
               SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        System.out.println("\nSETUP FINISHED.");
    }

    @AfterAll
    static void cleanup()
    {
        System.out.println("\nCleaning up test data...");

        try
        {
            // Drop the test database
            DatabaseHandler.executeCommand("DROP DATABASE IF EXISTS " + testDatabaseName);

            // Close the database connection
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            System.err.println("An error occurred during cleanup: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nCLEANUP FINISHED.");
    }

    /**
     * Test case where the item to delete is null.
     */
    @Test
    @Order(1)
    void testHardDeleteItem_NullItem()
    {
        System.out.println("\n1: Testing hardDeleteItem method with null item...");

        // Attempt to delete null item
        assertThrows(NullEntityException.class, () ->
        {
            ItemHandler.hardDeleteItem(null);
        }, "Deleting null item should throw NullEntityException");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for deleting an item which doesn't exist in the database.
     */
    @Test
    @Order(2)
    void testHardDeleteItem_NonExistingItem()
    {
        System.out.println("\n2: Testing hardDeleteItem method with non-existing item...");

        // Attempt to delete non-existing item
        assertThrows(EntityNotFoundException.class, () ->
        {
            ItemHandler.hardDeleteItem(new Literature(
                    false,
                    999,
                    "non-existing",
                    Item.ItemType.OTHER_BOOKS,
                    "999",
                    1,
                    1,
                    "Kalle",
                    "Anka",
                    "Skräck",
                    5,
                    false,
                    "non-existing"));
        }, "Deleting non-existing item should throw EntityNotFoundException");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for deleting an existing literature.
     */
    @Test
    @Order(3)
    void testHardDeleteItem_ExistingLiterature()
    {
        System.out.println("\n3: Testing hardDeleteItem method with existing Literature...");

        try
        {
            // Delete the literature
            assertDoesNotThrow(() -> ItemHandler.hardDeleteItem(literature),
                    "Deleting existing literature should not throw exception");

            // Try to get the deleted literature, should return null
            assertNull(ItemHandler.getItemByID(literature.getItemID()));

            // Check if literature was deleted from the 'literature' table
            String sql = "SELECT * FROM literature WHERE literatureID = ?";
            String[] params = {String.valueOf(literature.getItemID())};
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);
            ResultSet resultSet = queryResult.getResultSet();
            assertFalse(resultSet.next(), "Deleted literature should not exist in 'literature' table");
        }
        catch (InvalidIDException | SQLException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for deleting an existing film.
     */
    @Test
    @Order(4)
    void testHardDeleteItem_ExistingFilm()
    {
        System.out.println("\n4: Testing hardDeleteItem method with existing Film...");

        try
        {
            // Delete the film
            assertDoesNotThrow(() -> ItemHandler.hardDeleteItem(film),
                    "Deleting existing film should not throw exception");

            // Try to get the deleted film, should return null
            assertNull(ItemHandler.getItemByID(film.getItemID()));

            // Check if film was deleted from the 'films' table
            String sql = "SELECT * FROM films WHERE filmID = ?";
            String[] params = {String.valueOf(film.getItemID())};
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);
            ResultSet resultSet = queryResult.getResultSet();
            assertFalse(resultSet.next(), "Deleted film should not exist in 'films' table");
        }
        catch (InvalidIDException | SQLException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}