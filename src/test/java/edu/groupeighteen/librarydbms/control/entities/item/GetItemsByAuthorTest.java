package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
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
 * Unit Test for the GetItemsByAuthor class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetItemsByAuthorTest
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

    /**
     * Test for getItemsByAuthor method with an empty authorFirstname and a valid authorLastname.
     * It's expected to return a list with items.
     */
    @Test
    @Order(1)
    void testGetItemsByAuthor_EmptyFirstname_ValidLastname()
    {
        System.out.println(
                "\n1: Testing getItemsByAuthor method with an empty authorFirstname and a valid authorLastname...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor("", "Abercrombie");
            assertFalse(items.isEmpty());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with a null authorFirstname and a valid authorLastname.
     * It's expected to return a list with items.
     */
    @Test
    @Order(2)
    void testGetItemsByAuthor_NullFirstname_ValidLastname()
    {
        System.out.println(
                "\n2: Testing getItemsByAuthor method with a null authorFirstname and a valid authorLastname...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor(null, "Abercrombie");
            assertFalse(items.isEmpty());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with an empty authorLastname and a valid authorFirstname.
     * It's expected to return a list with items.
     */
    @Test
    @Order(3)
    void testGetItemsByAuthor_EmptyLastname_ValidFirstname()
    {
        System.out.println(
                "\n3: Testing getItemsByAuthor method with an empty authorLastname and a valid authorFirstname...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor("Joe", "");
            assertFalse(items.isEmpty());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with a null authorLastname and a valid authorFirstname.
     * It's expected to return a list with items.
     */
    @Test
    @Order(4)
    void testGetItemsByAuthor_NullLastname_ValidFirstname()
    {
        System.out.println(
                "\n4: Testing getItemsByAuthor method with a null authorLastname and a valid authorFirstname...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor("Joe", null);
            assertFalse(items.isEmpty());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with an empty authorFirstname and authorLastname.
     * It's expected to throw InvalidNameException.
     */
    @Test
    @Order(5)
    void testGetItemsByAuthor_EmptyNames()
    {
        System.out.println("\n5: Testing getItemsByAuthor method with an empty authorFirstname and authorLastname...");

        assertThrows(InvalidNameException.class, () -> ItemHandler.getItemsByAuthor("", ""));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with a null authorFirstname and authorLastname.
     * It's expected to throw InvalidNameException.
     */
    @Test
    @Order(6)
    void testGetItemsByAuthor_NullNames()
    {
        System.out.println("\n6: Testing getItemsByAuthor method with a null authorFirstname and authorLastname...");

        assertThrows(InvalidNameException.class, () -> ItemHandler.getItemsByAuthor(null, null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with an authorFirstname and authorLastname that does not exist.
     * It's expected to return an empty list.
     */
    @Test
    @Order(7)
    void testGetItemsByAuthor_NoSuchAuthor()
    {
        System.out.println(
                "\n7: Testing getItemsByAuthor method with an authorFirstname and authorLastname that does not exist...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor("NoSuch", "Author");
            assertTrue(items.isEmpty());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with an author that exists but has no items.
     * It's expected to return an empty list.
     */
    @Test
    @Order(8)
    void testGetItemsByAuthor_AuthorExists_NoTitles()
    {
        System.out.println("\n8: Testing getItemsByAuthor method with an author that exists but has no titles...");

        try
        {
            // Insert an author with no items.
            String authorFirstname = "AuthorWithoutTitles";
            String authorLastname = "AuthorWithoutTitles";
            String sql = "INSERT INTO authors (authorFirstname, authorLastname, biography, deleted) VALUES (?, ?, ?, ?)";
            String biography = "This is a test author with no items.";
            int deleted = 0;
            DatabaseHandler.executePreparedUpdate(sql,
                    new String[]{authorFirstname, authorLastname, biography, String.valueOf(deleted)});

            // Test the getItemsByAuthor method.
            List<Item> items = ItemHandler.getItemsByAuthor(authorFirstname, authorLastname);
            assertTrue(items.isEmpty());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with an author that exists and has an item.
     * It's expected to return a list with one item.
     */
    @Test
    @Order(9)
    void testGetItemsByAuthor_AuthorExists_HasTitle()
    {
        System.out.println("\n9: Testing getItemsByAuthor method with an author that exists and has a title...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor("author1", null);
            assertEquals(1, items.size());
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByAuthor method with an author that has multiple items.
     * It's expected to return a list with multiple items.
     */
    @Test
    @Order(10)
    void testGetItemsByAuthor_MultipleItemsExist()
    {
        System.out.println("\n10: Testing getItemsByAuthor method with an author that has multiple items...");

        try
        {
            List<Item> items = ItemHandler.getItemsByAuthor("Joe", "Abercrombie");
            assertTrue(items.size() > 1);
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}