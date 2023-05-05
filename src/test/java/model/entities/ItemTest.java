package model.entities;

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

    private static final String testClassTextBlock = """
               --------------------
                TESTING ITEM CLASS \s
               --------------------\s
            """;

    private static final String endTestTextBlock = """
               ------------------------
                END TESTING ITEM CLASS \s
               ------------------------\s
            """;

    /**
     * Tests all constructors in Item.
     */
    @Test
    @Order(1)
    public void testCreateItem() {
        System.out.println("\n" + testClassTextBlock);
        System.out.println("1: Testing creating Item...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
    }

    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields. Technically we already tested all our getters but whatever.
     */
    @Test
    @Order(2)
    public void testGettersAndSetters() {
        System.out.println("\n2: Testing getters and setters...");
        System.out.println("No test implemented here yet!");
        //TODO Write more code here
    }
}