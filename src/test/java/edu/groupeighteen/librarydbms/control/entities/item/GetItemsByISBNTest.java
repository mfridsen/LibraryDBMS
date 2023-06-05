package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetItemsByISBN class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetItemsByISBNTest
{
    protected static final String testDatabaseName = "test_database";
    //We need all this stuff, copied from BaseHandlerTest but adapted to be static
    protected static Connection connection = null;

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
    static void setUp()
    {
        System.out.println("\nSetting up test data...");

        try
        {
            setupConnectionAndTables();
            setupTestData();
            ItemHandler.setup(); //Otherwise barcodes are stored from previous tests
            // Create new Film and Literature objects
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        System.out.println("\nSETUP FINISHED.");
    }

    @AfterAll
    static void tearDown()
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

    @Test
    @Order(1)
    void testGetItemsByISBN_EmptyISBN()
    {
        System.out.println("\n1: Testing getItemsByISBN method with empty ISBN...");

        assertThrows(InvalidISBNException.class, () ->
        {
            ItemHandler.getItemsByISBN("");
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testGetItemsByISBN_NullISBN()
    {
        System.out.println("\n2: Testing getItemsByISBN method with null ISBN...");

        assertThrows(InvalidISBNException.class, () ->
        {
            ItemHandler.getItemsByISBN(null);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testGetItemsByISBN_TooLongISBN()
    {
        System.out.println("\n3: Testing getItemsByISBN method with too long ISBN...");

        String tooLongISBN = "1234567890123456789012345678901";  // more than Literature.LITERATURE_ISBN_LENGTH

        assertThrows(InvalidISBNException.class, () ->
        {
            ItemHandler.getItemsByISBN(tooLongISBN);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testGetItemsByISBN_ItemNotExist()
    {
        System.out.println("\n4: Testing getItemsByISBN method with ISBN of non-existent item...");

        try
        {
            String isbn = "999999999999";  // assume this ISBN does not exist in the database

            List<Item> items = ItemHandler.getItemsByISBN(isbn);

            assertEquals(0, items.size());
        }
        catch (InvalidISBNException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testGetItemsByISBN_ItemExist()
    {
        System.out.println("\n5: Testing getItemsByISBN method with existing item...");

        try
        {
            // assume that this ISBN exists in the database and corresponds to a single item
            String isbn = "9783161484100";

            List<Item> items = ItemHandler.getItemsByISBN(isbn);
            assertEquals(1, items.size());
        }
        catch (InvalidISBNException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    void testGetItemsByISBN_MultipleItemsExist()
    {
        System.out.println("\n6: Testing getItemsByISBN method with multiple items...");

        // assuming authorID = 1, classificationID = 1 are valid IDs in your database
        try
        {
            String isbn = "1234567890123";  // unique ISBN for the test
            ItemHandler.createNewLiterature("Title1", Item.ItemType.OTHER_BOOKS, 1, 1, "barcode1", isbn);
            ItemHandler.createNewLiterature("Title2", Item.ItemType.OTHER_BOOKS, 1, 1, "barcode2", isbn);

            List<Item> items = ItemHandler.getItemsByISBN(isbn);
            assertEquals(2, items.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}