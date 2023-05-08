package model.entities;

import edu.groupeighteen.librarydbms.model.entities.Item;
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
    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields.
     */
    @Test
    @Order(1)
    void testItemGettersAndSetters() {
        System.out.println("\nTesting Item class...");
        // Arrange
        String expectedTitle = "Test Item Title";
        String secondExpectedTitle = "Test Item Title 2";
        int expectedItemID = 1;

        // Act
        Item item = new Item(expectedTitle);
        item.setItemID(expectedItemID);

        // Assert
        assertEquals(expectedItemID, item.getItemID(), "ItemID does not match the expected value");
        assertEquals(expectedTitle, item.getTitle(), "Title does not match the expected value");

        item.setTitle(secondExpectedTitle);
        assertEquals(secondExpectedTitle, item.getTitle(), "Title does not match the expected value");

        System.out.println("Test finished!");
    }
}