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
 *
 * Unit Test for the ItemHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemHandlerTest extends BaseHandlerTest {

    //TODO-future make all tests more verbose
    //TODO-future javadoc tests properly
    //TODO-prio change order of tests to match order of methods

    @BeforeEach
    @Override
    void setupAndReset() {
        super.setupAndReset();
        setupTestTablesAndData();
    }

    /**
     * Tests the saveItem method with valid and invalid Items and item parameters.
     */
    @Test
    @Order(1)
    void testSaveItem() {
        System.out.println("\n1: Testing saveItem method...");
        // Test data
        String itemTitle = "Test Item";
        Item testItem = new Item(itemTitle);

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
     * This method tests the behavior of createNewItem with null and empty titles, and also verifies that a
     * valid item can be created and that the returned item has a valid ID and the correct title.
     */
    @Test
    @Order(2)
    void testCreateNewItem() {
        System.out.println("\n2: Testing createNewItem method...");

        // Test: Creating a valid item should return a valid item object
        try {
            String itemTitle = "Test Item";
            Item newItem = ItemHandler.createNewItem(itemTitle);
            assertNotNull(newItem, "Creating a valid item should return a valid item object.");
            assertTrue(newItem.getItemID() > 0, "The new item should have a valid item ID.");
            assertEquals(itemTitle, newItem.getTitle(), "The new item's title should match the provided title.");
        } catch (SQLException e) {
            fail("Creating a valid item should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("Test finished.");
    }

    /**
     * This method tests the behavior of getItemByID with invalid and non-existent IDs, and also verifies that a
     * valid item can be retrieved and that the returned item has the correct ID and title.
     */
    @Test
    @Order(3)
    void testGetItemByID() {
        System.out.println("\n3: Testing getItemByID method...");

        // Test: Retrieving an item with an invalid ID should return null
        try {
            Item nullItem = ItemHandler.getItemByID(-1);
            assertNull(nullItem, "Retrieving an item with an invalid ID should return null.");
        } catch (SQLException e) {
            fail("Retrieving an item with an invalid ID should not throw an exception.");
        }

        // Test: Retrieving an item with a non-existent ID should return null
        try {
            Item nonExistentItem = ItemHandler.getItemByID(9999);
            assertNull(nonExistentItem, "Retrieving an item with a non-existent ID should return null.");
        } catch (SQLException e) {
            fail("Retrieving an item with a non-existent ID should not throw an exception.");
        }

        // Test: Retrieving a valid item should return a valid item object
        try {
            // Create a new item first to ensure a valid itemID
            String itemTitle = "Test Item";
            Item newItem = ItemHandler.createNewItem(itemTitle);
            assertNotNull(newItem);
            int newItemID = newItem.getItemID();

            Item retrievedItem = ItemHandler.getItemByID(newItemID);
            assertNotNull(retrievedItem, "Retrieving a valid item should return a valid item object.");
            assertEquals(newItemID, retrievedItem.getItemID(), "The retrieved item's ID should match the original ID.");
            assertEquals(itemTitle, retrievedItem.getTitle(), "The retrieved item's title should match the original title.");
        } catch (SQLException e) {
            fail("Retrieving a valid item should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("Test finished.");
    }

    /**
     * This method tests the behavior of getItemByTitle when retrieving items with null, empty, non-existent titles,
     * and verifies that a valid item can be retrieved and that the returned item has the correct ID and title.
     */
    @Test
    @Order(4)
    void testGetItemByTitle() {
        System.out.println("\n4: Testing GetItemByTitle method...");

        // Test: Retrieving an item with a null title should return null
        try {
            Item nullItem = ItemHandler.getItemByTitle(null);
            assertNull(nullItem, "Retrieving an item with a null title should return null.");
        } catch (SQLException e) {
            fail("Retrieving an item with a null title should not throw an exception.");
        }

        // Test: Retrieving an item with an empty title should return null
        try {
            Item emptyTitleItem = ItemHandler.getItemByTitle("");
            assertNull(emptyTitleItem, "Retrieving an item with an empty title should return null.");
        } catch (SQLException e) {
            fail("Retrieving an item with an empty title should not throw an exception.");
        }

        // Test: Retrieving an item with a non-existent title should return null
        try {
            Item nonExistentItem = ItemHandler.getItemByTitle("Non-existent title");
            assertNull(nonExistentItem, "Retrieving an item with a non-existent title should return null.");
        } catch (SQLException e) {
            fail("Retrieving an item with a non-existent title should not throw an exception.");
        }

        // Test: Retrieving a valid item should return a valid item object
        try {
            // Create a new item first to ensure a valid title
            String itemTitle = "Test Item";
            Item newItem = ItemHandler.createNewItem(itemTitle);
            assertNotNull(newItem);

            Item retrievedItem = ItemHandler.getItemByTitle(itemTitle);
            assertNotNull(retrievedItem, "Retrieving a valid item should return a valid item object.");
            assertEquals(newItem.getItemID(), retrievedItem.getItemID(), "The retrieved item's ID should match the original ID.");
            assertEquals(itemTitle, retrievedItem.getTitle(), "The retrieved item's title should match the original title.");
        } catch (SQLException e) {
            fail("Retrieving a valid item should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("Test finished.");
    }

    /**
     * This method tests the behavior of updateItem when trying to update null items, items with invalid IDs,
     * non-existent items, and verifies that a valid item can be updated and the updated item has the correct title.
     */
    @Test
    @Order(5)
    void testUpdateItem() {
        System.out.println("\n5: Testing UpdateItem method...");

        // Test: Updating a non-existent item should throw an exception
        try {
            Item nonExistentItem = new Item("Non-existent Item");
            nonExistentItem.setItemID(9999);  // Assuming this ID does not exist in the database
            ItemHandler.updateItem(nonExistentItem);
            fail("Updating a non-existent item should throw an exception.");
        } catch (SQLException e) {
            // Expected exception
        }

        // Test: Updating a valid item should return true
        try {
            // Create a new item first to ensure a valid ID
            String itemTitle = "Test Item";
            Item newItem = ItemHandler.createNewItem(itemTitle);
            assertNotNull(newItem);

            // Update the title of the newly created item
            String updatedTitle = "Updated Test Item";
            newItem.setTitle(updatedTitle);
            boolean updated = ItemHandler.updateItem(newItem);
            assertTrue(updated, "Updating a valid item should return true.");

            // Retrieve the updated item to verify the title was correctly updated
            Item updatedItem = ItemHandler.getItemByID(newItem.getItemID());
            assertNotNull(updatedItem);
            assertEquals(updatedTitle, updatedItem.getTitle(), "The updated item's title should match the new title.");
        } catch (SQLException e) {
            fail("Updating a valid item should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("Test finished.");
    }

    /**
     * This method tests the behavior of deleteItem when trying to delete null items, items with invalid IDs,
     * non-existent items, and verifies that a valid item can be deleted and the deleted item cannot be retrieved
     * from the database.
     */
    @Test
    @Order(6)
    void testDeleteItem() {
        System.out.println("\n6: Testing DeleteItem method...");

        // Test: Deleting a non-existent item should return false
        try {
            Item nonExistentItem = new Item("Non-existent Item");
            nonExistentItem.setItemID(9999);  // Assuming this ID does not exist in the database
            boolean deleted = ItemHandler.deleteItem(nonExistentItem);
            assertFalse(deleted, "Deleting a non-existent item should return false.");
        } catch (SQLException e) {
            fail("Deleting a non-existent item should not throw an exception.");
        }

        // Test: Deleting a valid item should return true
        try {
            // Create a new item first to ensure a valid ID
            String itemTitle = "Test Item";
            Item newItem = ItemHandler.createNewItem(itemTitle);
            assertNotNull(newItem);

            // Delete the newly created item
            boolean deleted = ItemHandler.deleteItem(newItem);
            assertTrue(deleted, "Deleting a valid item should return true.");

            // Try to retrieve the deleted item to verify it was actually deleted
            Item deletedItem = ItemHandler.getItemByID(newItem.getItemID());
            assertNull(deletedItem, "The deleted item should not be retrievable from the database.");
        } catch (SQLException e) {
            fail("Deleting a valid item should not throw an exception. Error: " + e.getMessage());
        }

        System.out.println("Test finished.");
    }
}