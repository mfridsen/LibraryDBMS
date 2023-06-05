package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the deleteFilm and undoDeleteFilm methods.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteAndRecoverItemTest
{
    protected static final String testDatabaseName = "test_database";
    protected static Connection connection = null;
    // Declare these at the class level so all tests can access them
    static Film film;
    static Literature literature;

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
            literature = ItemHandler.createNewLiterature("Test Literature", Item.ItemType.OTHER_BOOKS,
                    1, 2, "5678", "978-3-16-1484");
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
     * Test case for deleting an item.
     */
    @Test
    @Order(1)
    void testDeleteItem_ValidItem()
    {
        System.out.println("\n1: Testing deleteItem method with valid item...");

        try
        {
            // Call the deleteItem method on the Film object
            ItemHandler.deleteItem(film);
            ItemHandler.deleteItem(literature);

            // Retrieve the film from the database
            Film retrievedFilm = (Film) ItemHandler.getItemByID(film.getItemID());
            Literature retrievedLiterature = (Literature) ItemHandler.getItemByID(literature.getItemID());

            assertNotNull(retrievedFilm);
            assertNotNull(retrievedLiterature);

            // Verify that the deleted field is set to true
            assertTrue(retrievedFilm.isDeleted(), "Film item should be marked as deleted after calling deleteItem.");
            assertTrue(retrievedLiterature.isDeleted(),
                    "Literature item should not be marked as deleted after calling " +
                            "undoDeleteItem.");
        }
        catch (InvalidIDException | RetrievalException | DeletionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for undoing the deletion of an item.
     */
    @Test
    @Order(2)
    void testUndoDeleteItem_ValidItem()
    {
        System.out.println("\n2: Testing undoDeleteItem method with valid item...");

        try
        {
            // Call the undoDeleteItem method on the Literature object
            ItemHandler.recoverItem(film);
            ItemHandler.recoverItem(literature);

            // Retrieve the literature from the database
            Film retrievedFilm = (Film) ItemHandler.getItemByID(film.getItemID());
            Literature retrievedLiterature = (Literature) ItemHandler.getItemByID(literature.getItemID());

            assertNotNull(retrievedFilm);
            assertNotNull(retrievedLiterature);

            // Verify that the deleted field is set to false
            assertFalse(retrievedFilm.isDeleted(), "Film item should be marked as deleted after calling deleteItem.");
            assertFalse(retrievedLiterature.isDeleted(),
                    "Literature item should not be marked as deleted after calling undoDeleteItem.");
        }
        catch (InvalidIDException | RetrievalException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //TODO-future Delete Null item
    //TODO-future Undo Delete Null item
}