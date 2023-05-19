package control;

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
            ItemHandler.setup();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            //storedTitles should contain 1 element
            assertEquals(1, ItemHandler.getStoredTitles().size());
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
            assertEquals(2, ItemHandler.getStoredTitles().get(validTitle).intValue(), "'validTitle' count does not match.");
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

        // Verify that the storedTitles map is empty
        assertEquals(0, ItemHandler.getStoredTitles().size(), "storedTitles map should be empty after setup with an empty database");

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

        // Insert some items into the database
        try {
            ItemHandler.createNewItem("Harry Potter");
            ItemHandler.createNewItem("The Lord of the Rings");
            ItemHandler.createNewItem("Harry Potter");
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
        Map<String, Integer> storedTitles = ItemHandler.getStoredTitles();

        assertEquals(2, storedTitles.size(), "Stored titles size does not match.");
        assertEquals(2, storedTitles.get("Harry Potter").intValue(), "'Harry Potter' count does not match.");
        assertEquals(1, storedTitles.get("The Lord of the Rings").intValue(), "'The Lord of the Rings' count does not match.");

        System.out.println("\nTEST FINISHED.");
    }


    /**
     * Test case for the getStoredTitles method.
     * This test verifies the correctness of the getStoredTitles method.
     */
    @Test
    @Order(7)
    void testGetStoredTitles() {
        System.out.println("\n7: Testing getStoredTitles method...");

        // Insert some items into the database
        try {
            ItemHandler.createNewItem("Harry Potter");
            ItemHandler.createNewItem("The Lord of the Rings");
            ItemHandler.createNewItem("Harry Potter");
        } catch (SQLException e) {
            System.out.println("Error while creating items: " + e.getMessage());
            fail("Error while creating items.");
        }

        // Retrieve the stored titles
        Map<String, Integer> storedTitles = ItemHandler.getStoredTitles();

        // Verify the size and contents of the stored titles
        assertEquals(2, storedTitles.size(), "Stored titles size does not match.");
        assertEquals(2, storedTitles.get("Harry Potter").intValue(), "'Harry Potter' count does not match.");
        assertEquals(1, storedTitles.get("The Lord of the Rings").intValue(), "'The Lord of the Rings' count does not match.");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(8)
    void testGetItemByID_InvalidID() {
        System.out.println("\n8: Testing getItemByID with an invalid itemID...");

        int invalidItemID = -1;
        assertThrows(IllegalArgumentException.class, () -> ItemHandler.getItemByID(invalidItemID), "Exception not thrown for invalid itemID");

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testGetItemByID_IDDoesNotExist() {
        System.out.println("\n9: Testing getItemByID with an itemID that does not exist...");

        int nonexistentItemID = 999; // assuming this ID does not exist in the database
        assertThrows(ItemNotFoundException.class, () -> ItemHandler.getItemByID(nonexistentItemID), "Exception not thrown for nonexistent itemID");

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(10)
    void testGetItemByID_ValidID() {
        System.out.println("\n10: Testing getItemByID with a valid itemID...");

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
    @Order(11)
    void testGetItemsByTitle_EmptyTitle() {
        System.out.println("\n11: Testing GetItemsByTitle with an empty title...");

        // Expect an IllegalArgumentException when passing an empty title
        assertThrows(IllegalArgumentException.class, () -> ItemHandler.getItemsByTitle(""));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testGetItemsByTitle_NullTitle() {
        System.out.println("\n12: Testing GetItemsByTitle with a null title...");

        // Expect an IllegalArgumentException when passing null as the title
        assertThrows(IllegalArgumentException.class, () -> ItemHandler.getItemsByTitle(null));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testGetItemsByTitle_TitleDoesNotExist() {
        System.out.println("\n13: Testing getItemsByTitle with a title that does not exist...");

        String nonexistentTitle = "Nonexistent Title";
        // Expect an ItemNotFoundException when passing a nonexistent title
        assertThrows(ItemNotFoundException.class, () -> ItemHandler.getItemsByTitle(nonexistentTitle));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testGetItemsByTitle_SingleItemWithGivenTitle() {
        System.out.println("\n14: Testing getItemsByTitle with a single item with the given title...");

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
    @Order(15)
    void testGetItemsByTitle_MultipleItemsWithGivenTitle() {
        System.out.println("\n15: Testing getItemsByTitle with multiple items with the given title...");

        try {
            String multipleItemsTitle = "Multiple Items Title";
            ItemHandler.createNewItem(multipleItemsTitle);
            ItemHandler.createNewItem(multipleItemsTitle);
            List<Item> items = ItemHandler.getItemsByTitle(multipleItemsTitle);
            // Ensure that there is more than one item in the returned list
            assertTrue(items.size() > 1);
            // Check the titles of all the items
            for (Item item : items) {
                assertEquals(multipleItemsTitle, item.getTitle());
            }
        } catch (SQLException | ItemNotFoundException e) {
            // No exceptions should be thrown
            fail("Error while getting items by title: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }







    //TODO-test getItemByISBN

    //TODO-test getItemByAuthor

    //TODO-test getItemByPublisher

    //TODO-test getItemByType

    //TODO-test updateItem



    //TODO-test deleteItem



    //TODO-test getAllowedRentalDaysByID



    //TODO-test getAvailableCopiesForItem


}