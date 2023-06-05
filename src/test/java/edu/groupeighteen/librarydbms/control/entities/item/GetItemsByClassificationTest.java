package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.Classification;
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
 * Unit Test for the GetItemsByClassification class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetItemsByClassificationTest
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
     * Test for getItemsByClassification method with an empty classificationName.
     * It's expected to throw InvalidNameException.
     */
    @Test
    @Order(1)
    void testGetItemsByClassification_EmptyClassificationName()
    {
        System.out.println("\n1: Testing getItemsByClassification method with an empty classificationName...");

        assertThrows(InvalidNameException.class, () -> ItemHandler.getItemsByClassification(""));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByClassification method with null classificationName.
     * It's expected to throw InvalidNameException.
     */
    @Test
    @Order(2)
    void testGetItemsByClassification_NullClassificationName()
    {
        System.out.println("\n2: Testing getItemsByClassification method with null classificationName...");

        assertThrows(InvalidNameException.class, () -> ItemHandler.getItemsByClassification(null));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByClassification method with classificationName that's too long.
     * It's expected to throw InvalidNameException.
     */
    @Test
    @Order(3)
    void testGetItemsByClassification_ClassificationNameTooLong()
    {
        System.out.println("\n3: Testing getItemsByClassification method with classificationName that's too long...");

        String longName = "a".repeat(Classification.CLASSIFICATION_NAME_LENGTH + 1);
        assertThrows(InvalidNameException.class, () -> ItemHandler.getItemsByClassification(longName));

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test for getItemsByClassification method with a classificationName that does not exist.
     * It's expected to return an empty list.
     */
    @Test
    @Order(4)
    void testGetItemsByClassification_ClassificationNameDoesNotExist()
    {
        System.out.println(
                "\n4: Testing getItemsByClassification method with a classificationName that does not exist...");

        try
        {
            List<Item> items = ItemHandler.getItemsByClassification("NonexistentClassification");
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
     * Test for getItemsByClassification method with classificationName that has no items.
     * A new classification with no associated items is created for this test.
     * It's expected to return an empty list.
     */
    @Test
    @Order(5)
    void testGetItemsByClassification_NoItemsInClassification()
    {
        System.out.println("\n5: Testing getItemsByClassification method with classificationName that has no items...");

        try
        {
            DatabaseHandler.executeCommand(
                    "INSERT INTO classifications (classificationName, deleted) VALUES ('EmptyClassification', 0)");
            List<Item> items = ItemHandler.getItemsByClassification("EmptyClassification");
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
     * Test for getItemsByClassification method with classificationName that has a single item.
     * It's expected to return a list with one item.
     */
    @Test
    @Order(6)
    void testGetItemsByClassification_SingleItemExists()
    {
        System.out.println(
                "\n6: Testing getItemsByClassification method with classificationName that has a single item...");

        try
        {
            List<Item> items = ItemHandler.getItemsByClassification("Physics");
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
     * Test for getItemsByClassification method with classificationName that has multiple items.
     * It's expected to return a list with multiple items.
     */
    @Test
    @Order(7)
    void testGetItemsByClassification_MultipleItemsExist()
    {
        System.out.println(
                "\n7: Testing getItemsByClassification method with classificationName that has multiple items...");

        try
        {
            List<Item> items = ItemHandler.getItemsByClassification("Fantasy");
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