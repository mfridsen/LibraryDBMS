package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test class for the ItemHandler.getItemByID method.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetItemByIDTest extends BaseHandlerTest
{
    /**
     * Test case for getItemByID method with valid itemID.
     * This test verifies that the getItemByID method behaves correctly when provided with a valid itemID.
     * It ensures that the method returns the expected Item object with the given itemID.
     */
    @Test
    @Order(1)
    void testGetItemByID_ValidID()
    {
        System.out.println("\n1: Testing getItemByID method with valid itemID...");

        try
        {
            int itemID = 1; // Assuming valid itemID
            Item item = ItemHandler.getItemByID(itemID);

            assertNotNull(item);
            assertEquals(itemID, item.getItemID());
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for getItemByID method with invalid itemID (0 or less).
     * This test verifies that the getItemByID method throws an InvalidIDException when provided with an invalid itemID (0 or less).
     */
    @Test
    @Order(2)
    void testGetItemByID_InvalidID()
    {
        System.out.println("\n2: Testing getItemByID method with invalid itemID...");

        try
        {
            int itemID = 0; // Invalid itemID
            assertThrows(InvalidIDException.class, () -> ItemHandler.getItemByID(itemID));
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for getItemByID method with valid itemID but no existing item.
     * This test verifies that the getItemByID method returns null when provided with a valid itemID but no existing item.
     */
    @Test
    @Order(3)
    void testGetItemByID_NoExistingItem()
    {
        System.out.println("\n3: Testing getItemByID method with valid itemID but no existing item...");

        try
        {
            int itemID = 100; // Assuming valid itemID, but no existing item
            Item item = ItemHandler.getItemByID(itemID);

            assertNull(item);
        }
        catch (RetrievalException | InvalidIDException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testGetItemByID_LiteratureItem()
    {
        System.out.println("\n4: Testing getItemByID method with literature item...");

        try
        {
            // Create a new Literature item
            Literature newLit = ItemHandler.createNewLiterature("Title4", Item.ItemType.OTHER_BOOKS, 4, 4, "barcode4",
                    "ISBN4");
            int itemID = newLit.getItemID();

            Literature retrievedLit = (Literature) ItemHandler.getItemByID(itemID);
            assertNotNull(retrievedLit);

        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException
               | ConstructionException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testUpdateItem_FilmItem()
    {
        System.out.println("\n5: Testing getItemByID method with film item...");

        try
        {
            // Create a new Film item
            Film newFilm = ItemHandler.createNewFilm("Title3", 3, 3, "barcode3", 15);
            int itemID = newFilm.getItemID();

            Film retrievedFilm = (Film) ItemHandler.getItemByID(itemID);
            assertNotNull(retrievedFilm);
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException
               | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}