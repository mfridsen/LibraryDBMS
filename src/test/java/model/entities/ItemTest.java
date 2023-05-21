package model.entities;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidItemIDException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the Item class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemTest {

    //TODO-PRIO UPDATE

    @Test
    @Order(1)
    void testItemCreationConstructor_ValidInput() {
        System.out.println("\n1: Testing Item creation constructor with valid input...");

        try {
            String title = "validTitle";
            Item testItem = new Item(title);

            assertEquals(0, testItem.getItemID());
            assertEquals(title, testItem.getTitle());
            assertEquals(Item.DEFAULT_ALLOWED_DAYS, testItem.getAllowedRentalDays());
            assertTrue(testItem.isAvailable());
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testItemCreationConstructor_InvalidInput() {
        System.out.println("\n2: Testing Item creation constructor with invalid input...");

        // Empty title
        assertThrows(InvalidTitleException.class, () -> new Item(""));

        // Null title
        assertThrows(InvalidTitleException.class, () -> new Item((String) null));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testItemRetrievalConstructor_ValidInput() {
        System.out.println("\n3: Testing Item retrieval constructor with valid input...");

        try {
            int itemID = 1;
            String title = "validTitle";
            int allowedRentalDays = Item.DEFAULT_ALLOWED_DAYS;

            Item testItem = new Item(itemID, title, allowedRentalDays, true);

            assertEquals(itemID, testItem.getItemID());
            assertEquals(title, testItem.getTitle());
            assertEquals(allowedRentalDays, testItem.getAllowedRentalDays());
            assertTrue(testItem.isAvailable());
        } catch (InvalidItemIDException | InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testItemRetrievalConstructor_InvalidInput() {
        System.out.println("\n4: Testing Item retrieval constructor with invalid input...");

        assertThrows(InvalidItemIDException.class, () -> new Item(0, "validTitle", 7, true));  // ItemID less than or equal to 0
        assertThrows(InvalidTitleException.class, () -> new Item(1, "", 7, true));  // Empty title
        assertThrows(InvalidTitleException.class, () -> new Item(1, null, 7, true));  // Null title

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testItemCopyConstructor_ValidInput() {
        System.out.println("\n5: Testing Item copy constructor with valid input...");

        try {
            Item originalItem = new Item("validTitle");
            Item copiedItem = new Item(originalItem);

            assertEquals(originalItem.getItemID(), copiedItem.getItemID());
            assertEquals(originalItem.getTitle(), copiedItem.getTitle());
            assertEquals(originalItem.getAllowedRentalDays(), copiedItem.getAllowedRentalDays());
            assertEquals(originalItem.isAvailable(), copiedItem.isAvailable());
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    void testSetItemID_ValidInput() {
        System.out.println("\n6: Testing setItemID with valid input...");

        try {
            Item testItem = new Item("validTitle");
            testItem.setItemID(2);
            assertEquals(2, testItem.getItemID());

            System.out.println("\nTEST FINISHED.");
        } catch (InvalidTitleException | InvalidItemIDException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    void testSetItemID_InvalidInput() {
        System.out.println("\n7: Testing setItemID with invalid input...");

        try {
            Item testItem = new Item("validTitle");
            assertThrows(InvalidItemIDException.class, () -> testItem.setItemID(0));  // ItemID less than or equal to 0
            assertThrows(InvalidItemIDException.class, () -> testItem.setItemID(-1));  // ItemID less than or equal to 0
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetTitle_ValidInput() {
        System.out.println("\n8: Testing setTitle with valid input...");

        try {
            Item testItem = new Item("oldTitle");
            testItem.setTitle("newTitle");
            assertEquals("newTitle", testItem.getTitle());
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetTitle_InvalidInput() {
        System.out.println("\n9: Testing setTitle with invalid input...");

        try {
            Item testItem = new Item("oldTitle");
            assertThrows(InvalidTitleException.class, () -> testItem.setTitle(""));  // Empty title
            assertThrows(InvalidTitleException.class, () -> testItem.setTitle(null));  // Null title
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testSetAllowedRentalDays_ValidInput() {
        System.out.println("\n10: Testing setAllowedRentalDays with valid input...");

        try {
            Item testItem = new Item("validTitle");
            testItem.setAllowedRentalDays(10);
            assertEquals(10, testItem.getAllowedRentalDays());
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }



    @Test
    @Order(11)
    void testSetAvailable_ValidInput() {
        System.out.println("\n11: Testing setAvailable with valid input...");

        try {
            // Since there's no rule regarding invalid input for setAllowedRentalDays, we skip an invalid input test for it.
            Item testItem = new Item("validTitle");
            testItem.setAvailable(false);
            assertFalse(testItem.isAvailable());
        } catch (InvalidTitleException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

}