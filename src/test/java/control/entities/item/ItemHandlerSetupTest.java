package control.entities.item;

import static org.junit.jupiter.api.Assertions.*;

import control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the ItemHandlerSetup class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemHandlerSetupTest extends BaseHandlerTest
{
    @BeforeEach
    @Override
    protected void setupAndReset()
    {
        try
        {
            setupConnectionAndTables();
            ItemHandler.reset();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Test case for the setup method with an empty database.
     * This test checks if the setup method correctly initializes the ItemHandler's state.
     */
    @Test
    @Order(1)
    void testSetup_EmptyDatabase()
    {
        System.out.println("\n1: Testing setup method with an empty database...");

        // Call the setup method
        ItemHandler.setup();

        // Verify that the storedTitles, availableTitles maps, and registeredBarcodes list are empty
        assertEquals(0, ItemHandler.getStoredTitles().size(),
                "storedTitles map should be empty after setup with an empty database");
        assertEquals(0, ItemHandler.getAvailableTitles().size(),
                "availableTitles map should be empty after setup with an empty database");
        assertEquals(0, ItemHandler.getRegisteredBarcodes().size(),
                "registeredBarcodes list should be empty after setup with an empty database");

        System.out.println("\nTEST FINISHED.");
    }
}