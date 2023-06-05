package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/1/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UpdateItem class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateItemTest extends BaseHandlerTest
{

    /**
     * Test case for updating an item with valid input.
     */
    @Test
    @Order(1)
    void testUpdateItem_ValidInput()
    {
        System.out.println("\n1: Testing updateItem method with valid input...");

        try
        {
            // Create a new Literature item
            Literature newLit = ItemHandler.createNewLiterature("Title1", Item.ItemType.OTHER_BOOKS, 1, 1, "barcode1",
                    "ISBN1");
            int itemID = newLit.getItemID();

            // Change some fields
            newLit.setTitle("UpdatedTitle1");
            newLit.setISBN("UpdatedISBN1");

            // Update the item
            ItemHandler.updateItem(newLit);

            // Retrieve the updated item
            Literature updatedLit = (Literature) ItemHandler.getItemByID(itemID);
            assertNotNull(updatedLit);

            // Verify that the fields have the expected changed values
            assertEquals("UpdatedTitle1", updatedLit.getTitle());
            assertEquals("UpdatedISBN1", updatedLit.getISBN());
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException |
               InvalidTitleException | InvalidISBNException | NullEntityException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updating a non-existent item.
     */
    @Test
    @Order(2)
    void testUpdateItem_NonExistingItem()
    {
        System.out.println("\n2: Testing updateItem method with non-existing item...");

        try
        {
            // Create a new Literature item
            Literature newLit = ItemHandler.createNewLiterature("Title2", Item.ItemType.OTHER_BOOKS, 2, 2, "barcode2",
                    "ISBN2");

            // Change itemID to a non-existent ID
            newLit.setItemID(Integer.MAX_VALUE);

            // Attempt to update the non-existent item
            assertThrows(EntityNotFoundException.class, () -> ItemHandler.updateItem(newLit));
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updating a film item and checking changes in the 'items' and 'films' tables.
     */
    @Test
    @Order(3)
    void testUpdateItem_FilmItem()
    {
        System.out.println("\n3: Testing updateItem method with film item...");

        try
        {
            // Create a new Film item
            Film newFilm = ItemHandler.createNewFilm("Title3", 3, 3, "barcode37", 15);
            int itemID = newFilm.getItemID();

            // Change some fields
            newFilm.setTitle("UpdatedTitle3");
            newFilm.setAgeRating(18);

            // Update the item
            ItemHandler.updateItem(newFilm);

            // Retrieve the updated item
            Film updatedFilm = (Film) ItemHandler.getItemByID(itemID);
            assertNotNull(updatedFilm);

            // Verify that the fields have the expected changed values
            assertEquals("UpdatedTitle3", updatedFilm.getTitle());
            assertEquals(18, updatedFilm.getAgeRating());
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException |
               InvalidTitleException | InvalidAgeRatingException | NullEntityException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updating a literature item and checking changes in the 'items' and 'literature' tables.
     */
    @Test
    @Order(4)
    void testUpdateItem_LiteratureItem()
    {
        System.out.println("\n4: Testing updateItem method with literature item...");

        try
        {
            // Create a new Literature item
            Literature newLit = ItemHandler.createNewLiterature("Title4", Item.ItemType.OTHER_BOOKS, 4, 4, "barcode47",
                    "ISBN4");
            int itemID = newLit.getItemID();

            // Change some fields
            newLit.setTitle("UpdatedTitle4");
            newLit.setISBN("UpdatedISBN4");

            // Update the item
            ItemHandler.updateItem(newLit);

            // Retrieve the updated item
            Literature updatedLit = (Literature) ItemHandler.getItemByID(itemID);
            assertNotNull(updatedLit);

            // Verify that the fields have the expected changed values
            assertEquals("UpdatedTitle4", updatedLit.getTitle());
            assertEquals("UpdatedISBN4", updatedLit.getISBN());
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException |
               InvalidTitleException | InvalidISBNException | NullEntityException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

}