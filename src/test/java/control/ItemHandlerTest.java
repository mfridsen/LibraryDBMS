package control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNotFoundException;
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
        try {
            ItemHandler.setup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the {@link ItemHandler#setup()} method of the ItemHandler class when the database is empty.
     * The test validates the correct initialization of the {@code storedTitles} map.
     * It should be empty after calling {@link ItemHandler#setup()} as there are no items in the database.
     */

    @Test
    @Order(1)
    void testSetup_EmptyDatabase() {
        System.out.println("\n1: Testing setup method with an empty database...");

        // Clear all items in the database
        try {
            DatabaseHandler.executePreparedUpdate("DELETE FROM items", null);
        } catch (SQLException e) {
            fail("Failed to clean up the items in the database: " + e.getMessage());
        }

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


    @Test
    @Order(2)
    void testSetup_WithSomeItemsInDatabase() {
        System.out.println("\n2: Testing setup method with some items in the database...");

        // Your test code here...

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testGetStoredTitles() {
        System.out.println("\n3: Testing getStoredTitles method...");

        // Your test code here...

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testSetStoredTitles() {
        System.out.println("\n4: Testing setStoredTitles method...");

        // Your test code here...

        System.out.println("\nTEST FINISHED.");
    }


}