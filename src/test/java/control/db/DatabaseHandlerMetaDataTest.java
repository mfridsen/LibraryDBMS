package control.db;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/29/2023
 * @contact matfir-1@student.ltu.se
 *
 * Tests the Meta Data retrieval methods in DatabaseHandler.
 *
 * NOTE: These tests will fail if rules of any related tables are changed.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseHandlerMetaDataTest
{
    @BeforeAll
    static void setUp()
    {
        DatabaseHandler.setup(false);
    }

    @AfterAll
    static void tearDown()
    {
        DatabaseHandler.closeDatabaseConnection();
    }

    /**
     * Tests retrieving Author meta data.
     */
    @Test
    @Order(1)
    void testGetAuthorMetaData()
    {
        System.out.println("\n1: Testing to retrieve Author meta data...");

        int[] metaData = DatabaseHandler.getAuthorMetaData();
        assertEquals(100, metaData[0]);
        assertEquals(100, metaData[1]);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests retrieving Classification meta data.
     */
    @Test
    @Order(2)
    void testGetClassificationMetaData()
    {
        System.out.println("\n2: Testing to retrieve Classification meta data...");

        int[] metaData = DatabaseHandler.getClassificationMetaData();
        assertEquals(255, metaData[0]);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests retrieving Item meta data.
     */
    @Test
    @Order(3)
    void testGetItemMetaData()
    {
        System.out.println("\n3: Testing to retrieve Item meta data...");

        int[] metaData = DatabaseHandler.getItemMetaData();
        assertEquals(255, metaData[0]);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests retrieving User meta data.
     */
    @Test
    @Order(4)
    void testGetUserMetaData()
    {
        System.out.println("\n4: Testing to retrieve User meta data...");

        int[] metaData = DatabaseHandler.getUserMetaData();
        assertEquals(20, metaData[0]);
        assertEquals(50, metaData[1]);

        System.out.println("\nTEST FINISHED.");
    }
}