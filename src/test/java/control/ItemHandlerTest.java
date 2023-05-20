package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
        try {
            setupConnectionAndTables();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void resetItemHandler() {
        ItemHandler.reset(); //Need to clear everything between tests
    }

    /**
     * Test case for the createNewItem method with an empty title.
     * This test verifies the behavior of the createNewItem method when an empty title is provided.
     */
    @Test
    @Order(1)
    void testCreateNewItem_EmptyTitle() {
        System.out.println("\n1: Testing createNewItem with an empty title...");

        // Expect an IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ItemHandler.createNewItem("");
        });

        // Check the exception message
        String expectedMessage = "Empty title.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        // Verify that no new titles have been added to the map
        assertEquals(0, ItemHandler.getStoredTitles().size());
        assertEquals(0, ItemHandler.getAvailableTitles().size());

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a null title.
     * This test verifies the behavior of the createNewItem method when a null title is provided.
     */
    @Test
    @Order(2)
    void testCreateNewItem_NullTitle() {
        System.out.println("\n2: Testing createNewItem with a null title...");

        // Expect an IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ItemHandler.createNewItem(null);
        });

        // Check the exception message
        String expectedMessage = "Empty title.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        // Verify that no new titles have been added to the map
        assertEquals(0, ItemHandler.getStoredTitles().size());
        assertEquals(0, ItemHandler.getAvailableTitles().size());

        System.out.println("\nTEST FINISHED.");
    }


    /**
     * Test case for the createNewItem method with a valid title.
     * This test verifies the behavior of the createNewItem method when a valid title is provided.
     */
    //TODO-prio update test when Item is finished
    @Test
    @Order(3)
    void testCreateNewItem_ValidTitle() {
        System.out.println("\n3: Testing createNewItem with a valid title...");

        try {
            //Create a valid Item
            String validTitle = "Valid Title";
            Item validItem = ItemHandler.createNewItem(validTitle);

            //Item should not be null
            assertNotNull(validItem);

            //Item ID should be greater than 0
            assertTrue(validItem.getItemID() > 0);

            //Item allowedRentalDays should be greater than 0
            assertTrue(validItem.getAllowedRentalDays() > 0);

            //Item should have correct title
            assertEquals(validTitle, validItem.getTitle());

            //storedTitles and availableTitles should contain 1 element
            assertEquals(1, ItemHandler.getStoredTitles().size());
            assertEquals(1, ItemHandler.getAvailableTitles().size());
        } catch (SQLException sqle) { //No exceptions should be thrown
            fail("Error while creating valid item: " + sqle.getMessage());
            sqle.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with an existing title.
     * This test verifies the behavior of the createNewItem method when an existing title is provided.
     */
    //TODO-prio update test when Item is finished
    @Test
    @Order(4)
    void testCreateNewItem_ExistingTitle() {
        System.out.println("\n4: Testing createNewItem with an existing title...");

        try {
            //Create valid Items
            String validTitle = "ValidTitle";
            Item validItem = ItemHandler.createNewItem(validTitle);
            Item validItem2 = ItemHandler.createNewItem(validTitle);

            //Items should not be null
            assertNotNull(validItem);
            assertNotNull(validItem2);

            //Item IDs should be greater than 0
            assertTrue(validItem.getItemID() > 0);
            assertTrue(validItem2.getItemID() > 0);

            //Items allowedRentalDays should be greater than 0
            assertTrue(validItem.getAllowedRentalDays() > 0);
            assertTrue(validItem2.getAllowedRentalDays() > 0);

            //Items should have correct title
            assertEquals(validTitle, validItem.getTitle());
            assertEquals(validTitle, validItem2.getTitle());

            //storedTitles should contain 1 title with 2 counts
            assertEquals(1, ItemHandler.getStoredTitles().size());
            assertEquals(2, ItemHandler.getStoredTitles().get(validTitle).intValue(), "'ValidTitle' count does not match.");

            //availableTitles should contain 1 title with 2 counts
            assertEquals(1, ItemHandler.getAvailableTitles().size());
            assertEquals(2, ItemHandler.getAvailableTitles().get(validTitle).intValue(), "'ValidTitle' count does not match.");
        } catch (SQLException sqle) { //No exceptions should be thrown
            fail("Error while creating valid item: " + sqle.getMessage());
            sqle.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the {@link ItemHandler#setup()} method of the ItemHandler class when the database is empty.
     * The test validates the correct initialization of the {@code storedTitles} map.
     * It should be empty after calling {@link ItemHandler#setup()} as there are no items in the database.
     */
    @Test
    @Order(5)
    void testSetup_EmptyDatabase() {
        System.out.println("\n5: Testing setup method with an empty database...");

        // Call the setup method
        try {
            ItemHandler.setup();
        } catch (SQLException e) {
            fail("ItemHandler.setup() method threw an SQLException: " + e.getMessage());
        }

        // Verify that the storedTitles and availableTitles maps are empty
        assertEquals(0, ItemHandler.getStoredTitles().size(), "storedTitles map should be empty after setup with an empty database");
        assertEquals(0, ItemHandler.getAvailableTitles().size(), "storedTitles map should be empty after setup with an empty database");

        System.out.println("\nTEST FINISHED.");
    }


    /**
     * Test case for the setup method with some items in the database.
     * This test verifies the behavior of the setup method when there are existing items in the database.
     */
    @Test
    @Order(6)
    void testSetup_WithSomeItemsInDatabase() {
        System.out.println("\n6: Testing setup method with some items in the database...");

        // Insert some items into the database, with one available single and two duplicates of which one is available
        try {
            String query = "INSERT INTO items (title, allowedRentalDays, available) VALUES (?, ?, ?)";
            String[] params1 = {"Harry Potter", "14", "1"};
            String[] params2 = {"The Lord of the Rings", "14", "1"};
            String[] params3 = {"Harry Potter", "14", "0"};
            DatabaseHandler.executePreparedQuery(query, params1);
            DatabaseHandler.executePreparedQuery(query, params2);
            DatabaseHandler.executePreparedQuery(query, params3);
        } catch (SQLException e) {
            System.out.println("Error while creating items: " + e.getMessage());
            fail("Error while creating items.");
        }

        // Call the setup method
        try {
            ItemHandler.setup();
        } catch (SQLException e) {
            System.out.println("Error while setting up ItemHandler: " + e.getMessage());
            fail("Error while setting up ItemHandler.");
        }

        // Check that the correct titles have been stored
        assertEquals(2, ItemHandler.getStoredTitles().size(), "Stored titles size does not match.");
        assertEquals(2, ItemHandler.getStoredTitles().get("Harry Potter").intValue(), "'Harry Potter' count does not match.");
        assertEquals(1, ItemHandler.getStoredTitles().get("The Lord of the Rings").intValue(), "'The Lord of the Rings' count does not match.");
        //Check that available titles are counted correctly
        assertEquals(2, ItemHandler.getAvailableTitles().size(), "Stored titles size does not match.");
        assertEquals(1, ItemHandler.getAvailableTitles().get("Harry Potter").intValue(), "'Harry Potter' count does not match.");
        assertEquals(1, ItemHandler.getAvailableTitles().get("The Lord of the Rings").intValue(), "'The Lord of the Rings' count does not match.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(7)
    void testGetItemByID_InvalidID() {
        System.out.println("\n7: Testing getItemByID with an invalid itemID...");

        int invalidItemID = -1;
        assertThrows(IllegalArgumentException.class, () -> ItemHandler.getItemByID(invalidItemID), "Exception not thrown for invalid itemID");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testGetItemByID_IDDoesNotExist() {
        System.out.println("\n8: Testing getItemByID with an itemID that does not exist...");

        int nonexistentItemID = 999; // assuming this ID does not exist in the database
        assertThrows(ItemNotFoundException.class, () -> ItemHandler.getItemByID(nonexistentItemID), "Exception not thrown for nonexistent itemID");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(9)
    void testGetItemByID_ValidID() {
        System.out.println("\n9: Testing getItemByID with a valid itemID...");

        try {
            // First, create a new item
            String title = "Valid Item";
            Item createdItem = ItemHandler.createNewItem(title);

            // Then try to retrieve it using its ID
            Item retrievedItem = ItemHandler.getItemByID(createdItem.getItemID());

            // The retrieved item should not be null
            assertNotNull(retrievedItem, "Item retrieval by ID returned null.");

            // The retrieved item should have the same ID as the created item
            assertEquals(createdItem.getItemID(), retrievedItem.getItemID(), "Retrieved item ID does not match created item ID.");

            // The retrieved item should have the same title as the created item
            assertEquals(createdItem.getTitle(), retrievedItem.getTitle(), "Retrieved item title does not match created item title.");

            // The retrieved item should have the same allowed rental days as the created item
            assertEquals(createdItem.getAllowedRentalDays(), retrievedItem.getAllowedRentalDays(), "Retrieved item allowed rental days does not match created item allowed rental days.");
        } catch (SQLException sqle) { // No exceptions should be thrown
            fail("Unexpected error occurred: " + sqle.getMessage());
            sqle.printStackTrace();
        } catch (ItemNotFoundException infe) {
            fail("Valid, existing itemID should not throw a ItemNotFoundException: " + infe.getMessage());
            infe.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(10)
    void testGetItemsByTitle_EmptyTitle() {
        System.out.println("\n10: Testing GetItemsByTitle with an empty title...");

        // Expect an IllegalArgumentException when passing an empty title
        assertThrows(IllegalArgumentException.class, () -> ItemHandler.getItemsByTitle(""));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testGetItemsByTitle_NullTitle() {
        System.out.println("\n11: Testing GetItemsByTitle with a null title...");

        // Expect an IllegalArgumentException when passing null as the title
        assertThrows(IllegalArgumentException.class, () -> ItemHandler.getItemsByTitle(null));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testGetItemsByTitle_TitleDoesNotExist() {
        System.out.println("\n12: Testing getItemsByTitle with a title that does not exist...");

        String nonexistentTitle = "Nonexistent Title";
        // Expect an ItemNotFoundException when passing a nonexistent title
        assertThrows(ItemNotFoundException.class, () -> ItemHandler.getItemsByTitle(nonexistentTitle));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testGetItemsByTitle_SingleItemWithGivenTitle() {
        System.out.println("\n13: Testing getItemsByTitle with a single item with the given title...");

        try {
            //Create a single item
            String singleItemTitle = "Single Item Title";
            ItemHandler.createNewItem(singleItemTitle);
            List<Item> items = ItemHandler.getItemsByTitle(singleItemTitle);
            // Ensure that there is exactly one item in the returned list
            assertEquals(1, items.size());
            // Check the title of the item
            assertEquals(singleItemTitle, items.get(0).getTitle());
        } catch (SQLException | ItemNotFoundException e) {
            // No exceptions should be thrown
            fail("Error while getting item by title: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testGetItemsByTitle_MultipleItemsWithGivenTitle() {
        System.out.println("\n14: Testing getItemsByTitle with multiple items with the given title...");

        try {
            String multipleItemsTitle = "Multiple Items Title";
            String query = "INSERT INTO items (title, allowedRentalDays, available) VALUES (?, ?, ?)";
            String[] params1 = {multipleItemsTitle, "14", "1"}; //One available
            String[] params2 = {multipleItemsTitle, "7", "0"}; //One not
            DatabaseHandler.executePreparedQuery(query, params1);
            DatabaseHandler.executePreparedQuery(query, params2);

            List<Item> items = ItemHandler.getItemsByTitle(multipleItemsTitle);

            // Ensure that there is more than one item in the returned list
            assertTrue(items.size() > 1);

            // Check the titles of all the items
            for (Item item : items) {
                assertEquals(multipleItemsTitle, item.getTitle());
            }

            //Check allowedRentalDays
            assertEquals(14, items.get(0).getAllowedRentalDays());
            assertEquals(7, items.get(1).getAllowedRentalDays());

            //Check the availability of the items
            assertTrue(items.get(0).isAvailable());
            assertFalse(items.get(1).isAvailable());

        } catch (SQLException | ItemNotFoundException e) {
            // No exceptions should be thrown
            fail("Error while getting items by title: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }







    //TODO-test getItemByISBN

    //TODO-test getItemsByGenre

    //TODO-test getItemByAuthor

    //TODO-test getItemByPublisher

    //TODO-test getItemByType

    //TODO-test at least 27 more test cases in total here


    @Test
    @Order(41)
    void testUpdateItem_NullItem() {
        System.out.println("\n41: Testing updateItem with null item...");

        try {
            // Try to update with null item
            ItemHandler.updateItem(null);
            fail("An IllegalArgumentException was expected.");
        } catch (IllegalArgumentException iae) {
            assertEquals("Invalid item: item is null.", iae.getMessage());
        } catch (SQLException | ItemNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(42)
    void testUpdateItem_NonexistentItemID() {
        System.out.println("\n42: Testing updateItem with a nonexistent itemID...");

        try {
            // Create an item with nonexistent ID
            Item nonexistentItem = new Item("Nonexistent Item");
            nonexistentItem.setItemID(99999);

            // Try to update this nonexistent item
            ItemHandler.updateItem(nonexistentItem);
            fail("An ItemNotFoundException was expected.");
        } catch (ItemNotFoundException inf) {
            assertEquals("Item with ID 99999 does not exist.", inf.getMessage());
        } catch (SQLException | IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(44)
    void testUpdateItem_ValidItem() {
        System.out.println("\n44: Testing updateItem with a valid item...");

        try {
            // Create a new item
            String originalTitle = "Original Title";
            Item newItem = ItemHandler.createNewItem(originalTitle);

            // Update the title of the item
            String updatedTitle = "Updated Title";
            newItem.setTitle(updatedTitle);

            // Update the item and check if the update was successful
            assertTrue(ItemHandler.updateItem(newItem));

            // Retrieve the updated item
            Item updatedItem = ItemHandler.getItemByID(newItem.getItemID());

            // The updated item should not be null and should have the updated title
            assertNotNull(updatedItem);
            assertEquals(updatedTitle, updatedItem.getTitle());

            // Verify that the original title is no longer in the maps
            assertEquals(0, ItemHandler.getStoredTitles().getOrDefault(originalTitle, 0).intValue());
            assertEquals(0, ItemHandler.getAvailableTitles().getOrDefault(originalTitle, 0).intValue());

            // Verify that the updated title is in the maps with a count of 1
            assertEquals(1, ItemHandler.getStoredTitles().get(updatedTitle).intValue());
            assertEquals(1, ItemHandler.getAvailableTitles().get(updatedTitle).intValue());

        } catch (SQLException | ItemNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }




    @Test
    @Order(44)
    void testDeleteItem_NullItem() {
        System.out.println("\n44: Testing deleteItem with null item...");

        try {
            // Try to delete null item
            ItemHandler.deleteItem(null);
            fail("An IllegalArgumentException was expected.");
        } catch (IllegalArgumentException iae) {
            assertEquals("Invalid item: item is null.", iae.getMessage());
        } catch (SQLException | ItemNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(45)
    void testDeleteItem_NonexistentItem() {
        System.out.println("\n45: Testing deleteItem with a nonexistent item...");

        try {
            // Create an item with nonexistent ID
            Item nonexistentItem = new Item("Nonexistent Item");
            nonexistentItem.setItemID(99999);

            // Try to delete this nonexistent item
            ItemHandler.deleteItem(nonexistentItem);
            fail("An ItemNotFoundException was expected.");
        } catch (ItemNotFoundException inf) {
            assertEquals("Item with ID 99999 does not exist.", inf.getMessage());
        } catch (SQLException | IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(46)
    void testDeleteItem_ValidItem() {
        System.out.println("\n46: Testing deleteItem with a valid item...");

        try {
            // Create a new item
            Item validItem = ItemHandler.createNewItem("Valid Item");

            // Check if the stored and available titles are correctly updated
            assertEquals(1, ItemHandler.getStoredTitles().get(validItem.getTitle()).intValue());
            assertEquals(1, ItemHandler.getAvailableTitles().get(validItem.getTitle()).intValue());

            // Delete the item
            boolean isDeleted = ItemHandler.deleteItem(validItem);

            // Check if the item was deleted
            assertTrue(isDeleted);

            // Try to retrieve the deleted item
            try {
                ItemHandler.getItemByID(validItem.getItemID());
                fail("An ItemNotFoundException was expected.");
            } catch (ItemNotFoundException inf) {
                assertEquals("Item not found. Item ID: " + validItem.getItemID(), inf.getMessage());
            }

            // Check if the stored and available titles are correctly updated
            if (ItemHandler.getStoredTitles().get(validItem.getTitle()) != null) {
                assertEquals(0, ItemHandler.getStoredTitles().get(validItem.getTitle()).intValue());
            } else {
                assertNull(ItemHandler.getStoredTitles().get(validItem.getTitle()));
            }

            if (ItemHandler.getAvailableTitles().get(validItem.getTitle()) != null) {
                assertEquals(0, ItemHandler.getAvailableTitles().get(validItem.getTitle()).intValue());
            } else {
                assertNull(ItemHandler.getAvailableTitles().get(validItem.getTitle()));
            }

        } catch (SQLException | ItemNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(47)
    void testGetAllowedRentalDaysByID_ValidItem() {
        System.out.println("\n47: Testing getAllowedRentalDaysByID with a valid item...");

        try {
            // Create a new item
            String title = "Valid Item";
            Item validItem = ItemHandler.createNewItem(title);

            // Retrieve the allowed rental days for the created item
            int allowedRentalDays = ItemHandler.getAllowedRentalDaysByID(validItem.getItemID());

            // The allowed rental days should be equal to the allowed rental days of the created item
            assertEquals(validItem.getAllowedRentalDays(), allowedRentalDays);
        } catch (SQLException | ItemNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(48)
    void testGetAllowedRentalDaysByID_InvalidItem() {
        System.out.println("\n48: Testing getAllowedRentalDaysByID with an invalid item...");

        // Try to retrieve the allowed rental days for an item with an invalid ID
        int invalidID = 1;
        Exception exception = assertThrows(ItemNotFoundException.class, () -> {
            ItemHandler.getAllowedRentalDaysByID(invalidID);
        });

        // Check the exception message
        String expectedMessage = "Item not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(49)
    void testGetAvailableCopiesForItem_ValidItem() {
        System.out.println("\n49: Testing getAvailableCopiesForItem with a valid item...");

        try {
            // Create a new item
            String title = "Valid Item";
            Item validItem = ItemHandler.createNewItem(title);

            // Retrieve the number of available copies for the created item
            int availableCopies = ItemHandler.getAvailableCopiesForItem(validItem);

            // The number of available copies should be 1
            assertEquals(1, availableCopies);
        } catch (SQLException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ItemNotFoundException e) {
            fail("Item should exist in availableTitles.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(50)
    void testGetAvailableCopiesForItem_InvalidItem() {
        System.out.println("\n50: Testing getAvailableCopiesForItem with an invalid item...");

        // Create an item with a title that does not exist in the map
        Item invalidItem = new Item("Invalid Title");

        // Expect an ItemNotFoundException
        Exception exception = assertThrows(ItemNotFoundException.class, () -> {
            ItemHandler.getAvailableCopiesForItem(invalidItem);
        });

        // Check the exception message
        String expectedMessage = "Item not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("\nTEST FINISHED.");
    }

}