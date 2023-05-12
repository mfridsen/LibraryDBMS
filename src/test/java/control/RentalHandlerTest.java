package control;

import org.junit.jupiter.api.*;

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
 * Unit Test for the RentalHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalHandlerTest extends BaseHandlerTest {

    @BeforeEach
    @Override
    void setupAndReset() {
        super.setupAndReset();
        setupTestTablesAndData();
    }

    /**
     * Tests all constructors in RentalHandler.
     */
    @Test
    @Order(1)
    public void testCreateRentalHandler() {
        System.out.println("\n1: Testing creating RentalHandler...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("Test finished.");
    }

    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields.
     */
    @Test
    @Order(2)
    public void testGettersAndSetters() {
        System.out.println("\n2: Testing getters and setters...");
        System.out.println("No test implemented here yet!");
        //TODO Write more code here
        System.out.println("Test finished.");
    }
}