package edu.groupeighteen.librarydbms.model.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

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
public class FilmRetrievalTest
{

    /**
     * Tests the Film retrieval constructor with valid data.
     */
    @Test
    @Order(1)
    void testFilmConstruction_ValidData()
    {
        try
        {
            Film film = new Film(false, 1, "Film Title", Item.ItemType.FILM,
                    "1234567890123", 1, 1, "Director FName",
                    "Director LName", "Genre Name", 7, true, 15,
                    "USA", "Actor1, Actor2, Actor3");
            assertFalse(film.isDeleted());
            assertEquals(1, film.getItemID());
            assertEquals("Film Title", film.getTitle());
            assertEquals(Item.ItemType.FILM, film.getType());
            assertEquals("1234567890123", film.getBarcode());
            assertEquals(1, film.getAuthorID());
            assertEquals(1, film.getClassificationID());
            assertEquals("Director FName", film.getAuthorFirstname());
            assertEquals("Director LName", film.getAuthorLastname());
            assertEquals("Genre Name", film.getClassificationName());
            assertEquals(7, film.getAllowedRentalDays());
            assertTrue(film.isAvailable());
            assertEquals(15, film.getAgeRating());
            assertEquals("USA", film.getCountryOfProduction());
            assertEquals("Actor1, Actor2, Actor3", film.getListOfActors());
        }
        catch (ConstructionException e)
        {
            fail("Construction failed with valid data. Exception: " + e.getMessage());
        }
    }

    /**
     * Tests the Film retrieval constructor with null title.
     */
    @Test
    @Order(2)
    void testFilmConstruction_NullTitle()
    {
        assertThrows(ConstructionException.class, () ->
        {
            new Film(false, 1, null, Item.ItemType.FILM, "1234567890123", 1,
                    1, "Director FName",
                    "Director LName", "Genre Name", 7, true,
                    15, "USA", "Actor1, Actor2, Actor3");
        });
    }

    /**
     * Tests the Film retrieval constructor with empty title.
     */
    @Test
    @Order(3)
    void testFilmConstruction_EmptyTitle()
    {
        assertThrows(ConstructionException.class, () ->
        {
            new Film(false, 1, "", Item.ItemType.FILM, "1234567890123", 1,
                    1, "Director FName",
                    "Director LName", "Genre Name", 7, true,
                    15, "USA", "Actor1, Actor2, Actor3");
        });
    }

    /**
     * Tests the Film retrieval constructor with title that is too long.
     */
    @Test
    @Order(4)
    void testFilmConstruction_TooLongTitle()
    {
        String tooLongTitle = String.join("", Collections.nCopies(Item.ITEM_TITLE_MAX_LENGTH + 1, "a"));

        assertThrows(ConstructionException.class, () ->
        {
            new Film(false, 1, tooLongTitle, Item.ItemType.FILM, "1234567890123", 1,
                    1, "Director FName",
                    "Director LName", "Genre Name", 7, true,
                    15, "USA", "Actor1, Actor2, Actor3");
        });
    }

    /**
     * Tests the Film retrieval constructor with null barcode.
     */
    @Test
    @Order(5)
    void testFilmConstruction_NullBarcode()
    {
        assertThrows(ConstructionException.class, () ->
        {
            new Film(false, 1, "Film Title", Item.ItemType.FILM, null, 1,
                    1, "Director FName",
                    "Director LName", "Genre Name", 7, true,
                    15, "USA", "Actor1, Actor2, Actor3");
        });
    }

    /**
     * Tests the Film retrieval constructor with empty barcode.
     */
    @Test
    @Order(6)
    void testFilmConstruction_EmptyBarcode()
    {
        assertThrows(ConstructionException.class, () ->
        {
            new Film(false, 1, "Film Title", Item.ItemType.FILM, "", 1,
                    1, "Director FName",
                    "Director LName", "Genre Name", 7, true,
                    15, "USA", "Actor1, Actor2, Actor3");
        });
    }

    /**
     * Tests the Film retrieval constructor with barcode that is too long.
     */
    @Test
    @Order(7)
    void testFilmConstruction_TooLongBarcode()
    {
        String tooLongBarcode = String.join("", Collections.nCopies(Item.ITEM_BARCODE_LENGTH + 1, "1"));

        assertThrows(ConstructionException.class, () ->
        {
            new Film(false, 1, "Film Title", Item.ItemType.FILM, tooLongBarcode, 1,
                    1, "Director FName",
                    "Director LName", "Genre Name", 7, true,
                    15, "USA", "Actor1, Actor2, Actor3");
        });
    }

    /**
     * Tests the Film retrieval constructor with invalid item ID.
     */
    @Test
    @Order(8)
    void testFilmConstruction_InvalidItemID()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, -1, "Film Title", Item.ItemType.FILM, "123456789", 1,
                        1, "Director FName",
                        "Director LName", "Classification Name",
                        10, true, 15,
                        "USA", "Actor1, Actor2"));
    }

    /**
     * Tests the Film retrieval constructor with invalid author ID.
     */
    @Test
    @Order(9)
    void testFilmConstruction_InvalidAuthorID()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, 1, "Film Title", Item.ItemType.FILM, "123456789", -1,
                        1, "Director FName",
                        "Director LName", "Classification Name",
                        10, true, 15,
                        "USA", "Actor1, Actor2"));
    }

    /**
     * Tests the Film retrieval constructor with invalid classification ID.
     */
    @Test
    @Order(10)
    void testFilmConstruction_InvalidClassificationID()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, 1, "Film Title", Item.ItemType.FILM, "123456789", 1,
                        -1, "Director FName",
                        "Director LName", "Classification Name",
                        10, true, 15,
                        "USA", "Actor1, Actor2"));
    }

    /**
     * Tests the Film retrieval constructor with negative rental days.
     */
    @Test
    @Order(11)
    void testFilmConstruction_NegativeRentalDays()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, 1, "Film Title", Item.ItemType.FILM, "123456789", 1,
                        1, "Director FName",
                        "Director LName", "Classification Name",
                        -1, true, 15,
                        "USA", "Actor1, Actor2"));
    }

    /**
     * Tests the Film retrieval constructor with age rating that's less than 0.
     */
    @Test
    @Order(12)
    void testFilmConstruction_InvalidAgeRating_LessThanZero()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, 1, "Film Title", Item.ItemType.FILM, "123456789", 1,
                        1, "Director FName",
                        "Director LName", "Classification Name",
                        10, true, -1,
                        "USA", "Actor1, Actor2"));
    }

    /**
     * Tests the Film retrieval constructor with age rating that's more than the maximum allowed age rating.
     */
    @Test
    @Order(13)
    void testFilmConstruction_InvalidAgeRating_MoreThanMax()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, 1, "Film Title", Item.ItemType.FILM, "123456789", 1,
                        1, "Director FName",
                        "Director LName", "Classification Name",
                        10, true, 19,
                        "USA", "Actor1, Actor2"));
    }

    /**
     * Tests the Film retrieval constructor with country name that's too long.
     */
    @Test
    @Order(14)
    void testFilmConstruction_CountryNameTooLong()
    {
        assertThrows(ConstructionException.class, () ->
                new Film(false, 1, "Film Title", Item.ItemType.FILM, "123456789", 1,
                        1, "Director FName",
                        "Director LName", "Classification Name",
                        10, true, 15,
                        "A".repeat(101), "Actor1, Actor2"));
    }
}