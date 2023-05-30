package model.entities.item;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/30/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the Film class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmCopyTest
{
    /**
     *
     */
    @Test
    @Order(1)
    void testFilmCopy_ValidData()
    {
        System.out.println("\n1: Testing Film Copy Constructor with valid data...");

        try
        {
            Film film = new Film(new Film(false, 1, "Title",
                    Item.ItemType.FILM,"Barcode", 1, 1, "AuthorName",
                    "ClassificationName", 10, true, 18,
                    "USA", "Actor1, Actor2, Actor3"));

            // Verify that all fields are correctly initialized
            assertFalse(film.isDeleted(),
                    "Deleted flag should be false.");
            assertEquals(1, film.getItemID(),
                    "ItemID should be 1.");
            assertEquals("Title", film.getTitle(),
                    "Title should be 'Title'.");
            assertEquals(Item.ItemType.FILM, film.getType(),
                    "Type should be FILM.");
            assertEquals("Barcode", film.getBarcode(),
                    "Barcode should be 'Barcode'.");
            assertEquals(1, film.getAuthorID(),
                    "AuthorID should be 1.");
            assertEquals(1, film.getClassificationID(),
                    "ClassificationID should be 1.");
            assertEquals("AuthorName", film.getAuthorName(),
                    "AuthorName should be 'AuthorName'.");
            assertEquals("ClassificationName", film.getClassificationName(),
                    "ClassificationName should be 'ClassificationName'.");
            assertEquals(10, film.getAllowedRentalDays(),
                    "AllowedRentalDays should be 10.");
            assertTrue(film.isAvailable(),
                    "Available flag should be true.");
            assertEquals(18, film.getAgeRating(),
                    "Age rating should be set correctly in constructor");
            assertEquals("USA", film.getPublisherCountry());
            assertEquals("Actor1, Actor2, Actor3", film.getListOfActors());


        }
        catch (ConstructionException e)
        {
            fail("Valid parameters should not result in an exception.");
            e.printStackTrace();
        }
        
        System.out.println("\nTEST FINISHED.");
    }
}