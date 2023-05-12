package control;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the ItemHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemHandlerTest extends BaseHandlerTest {

    //TODO-future make all tests more verbose
    //TODO-future javadoc tests properly

    @BeforeEach
    @Override
    void setupAndReset() {
        super.setupAndReset();
        setupTestTablesAndData();
    }

    /**
     * Tests all constructors in ItemHandler.
     */
    @Test
    @Order(1)
    void testSaveItem() {
        System.out.println("\n1: Testing saveItem method...");
        // Test data
        String itemTitle = "Test Item";
        Item testItem = new Item(itemTitle);

        // Test: Saving a null item should return 0
        try {
            int itemId = ItemHandler.saveItem(null);
            assertEquals(0, itemId, "Saving a null item should return 0.");
        } catch (SQLException e) {
            fail("Saving a null item should not throw an exception.");
        }

        // Test: Saving an item with a null title should return 0
        try {
            testItem.setTitle(null);
            int itemId = ItemHandler.saveItem(testItem);
            assertEquals(0, itemId, "Saving an item with a null title should return 0.");
        } catch (SQLException e) {
            fail("Saving an item with a null title should not throw an exception.");
        }

        // Test: Saving an item with an empty title should return 0
        try {
            testItem.setTitle("");
            int itemId = ItemHandler.saveItem(testItem);
            assertEquals(0, itemId, "Saving an item with an empty title should return 0.");
        } catch (SQLException e) {
            fail("Saving an item with an empty title should not throw an exception.");
        }

        // Test: Saving a valid item should return a valid item ID
        try {
            testItem.setTitle(itemTitle);
            int itemId = ItemHandler.saveItem(testItem);
            assertTrue(itemId > 0, "Saving a valid item should return a valid item ID.");

            // Now, retrieve the saved item to verify that it was saved correctly.
            Item savedItem = ItemHandler.getItemByID(itemId);
            assertNotNull(savedItem, "The saved item should not be null.");
            assertEquals(itemTitle, savedItem.getTitle(), "The saved item's title should match the original title.");
        } catch (SQLException e) {
            fail("Saving a valid item should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("Test finished.");
    }

    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields.
     */
    @Test
    @Order(2)
    void testCreateNewItem() {
        System.out.println("\n2: Testing getters and setters...");
        System.out.println("No test implemented here yet!");
        //TODO Write more code here
        System.out.println("Test finished.");
    }

    @Test
    @Order(3)
    void testGetItemByID() {
        System.out.println("\n3: Testing getItemByID method...");
        System.out.println("Test finished.");
    }

}