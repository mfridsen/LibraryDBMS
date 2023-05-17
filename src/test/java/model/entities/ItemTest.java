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

    @Test
    @Order(1)
    void testConstructor() {
        System.out.println("\n1: Testing Item Constructor...");

        //Test valid constructor
        Item validItem = new Item("Harry Potter");
        assertEquals("Harry Potter", validItem.getTitle());

        //Test invalid constructor
        assertThrows(IllegalArgumentException.class, () -> new Item((String) null));
        assertThrows(IllegalArgumentException.class, () -> new Item(""));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testSetItemID() {
        System.out.println("\n2: Testing setItemID method...");

        Item validItem = new Item("Harry Potter");

        //Test valid setItemID
        validItem.setItemID(1);
        assertEquals(1, validItem.getItemID());

        //Test invalid setItemID
        assertThrows(IllegalArgumentException.class, () -> validItem.setItemID(0));
        assertThrows(IllegalArgumentException.class, () -> validItem.setItemID(-1));

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testSetTitle() {
        System.out.println("\n3: Testing setTitle method...");

        Item validItem = new Item("Harry Potter");

        //Test valid setTitle
        validItem.setTitle("Harry Potter 2");
        assertEquals("Harry Potter 2", validItem.getTitle());

        //Test invalid setTitle
        assertThrows(IllegalArgumentException.class, () -> validItem.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> validItem.setTitle(""));

        System.out.println("\nTEST FINISHED.");
    }
}