package edu.groupeighteen.librarydbms.model.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidAgeRatingException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
public class FilmCreationTest
{
    /**
     * This method tests if a Film object is correctly created with valid data.
     */
    @Test
    @Order(1)
    void testFilmCreation_ValidData()
    {
        System.out.println("\n1: Testing Film Creation Constructor with valid data...");

        try
        {
            //Create the new Film object
            Film film = new Film("Title", 1, 1, "1234567890", 18);

            //Verify that the Film Film object has the correct properties
            assertEquals("Title", film.getTitle(),
                    "Title should be set correctly in constructor");
            assertEquals(Item.ItemType.FILM, film.getType(),
                    "ItemType should be set correctly in constructor");
            assertEquals("1234567890", film.getBarcode(),
                    "ISBN should be set correctly in constructor");
            assertEquals(18, film.getAgeRating(),
                    "Age rating should be set correctly in constructor");
            assertNull(film.getCountryOfProduction(),
                    "Publisher country should be set correctly in constructor.");
            assertNull(film.getListOfActors(),
                    "List of actors should be set correctly in constructor.");
            assertTrue(film.isAvailable(),
                    "New Film items should be available by default");
            assertFalse(film.isDeleted(),
                    "New Film items should not be deleted by default");
            assertEquals(0, film.getItemID(),
                    "New Film items should have an itemID of 0 by default");
            assertEquals(1, film.getAuthorID(),
                    "AuthorID should be set correctly in constructor");
            assertEquals(1, film.getClassificationID(),
                    "ClassificationID should be set correctly in constructor");
            assertNull(film.getAuthorFirstname(),
                    "New Film items should have null authorName by default");
            assertNull(film.getClassificationName(),
                    "New Film items should have null classificationName by default");
            assertEquals(Item.getDefaultAllowedRentalDays(Item.ItemType.FILM), film.getAllowedRentalDays(),
                    "AllowedRentalDays should be set correctly in constructor");
        }
        catch (ConstructionException e)
        {
            fail("Valid data should not throw ConstructionException.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Film object throws a ConstructionException when null title is provided.
     */
    @Test
    @Order(2)
    void testFilmCreation_NullTitle()
    {
        System.out.println("\n2: Testing Film constructor with null title...");

        try
        {
            //Create a new Film object with null title
            Film film = new Film(null, 1, 1, "1234567890", 18);
            fail("Construction with null title should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "Cause should be InvalidTitleException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Film object throws a ConstructionException when an empty title is provided.
     */
    @Test
    @Order(3)
    void testFilmCreation_EmptyTitle()
    {
        System.out.println("\n3: Testing Film constructor with empty title...");

        try
        {
            //Create a new Film object with empty title
            Film film = new Film("", 1, 1, "1234567890", 18);
            fail("Construction with empty title should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "Cause should be InvalidTitleException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Film object throws a ConstructionException when a too long title is provided.
     */
    @Test
    @Order(4)
    void testFilmCreation_LongTitle()
    {
        System.out.println("\n4: Testing Film constructor with long title...");

        //Generate a long title
        String longTitle = "a".repeat(Item.ITEM_TITLE_MAX_LENGTH + 1);

        try
        {
            //Create a new Film object with long title
            Film film = new Film(longTitle, 1, 1, "1234567890", 18);
            fail("Construction with long title should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "Cause should be InvalidTitleException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Film object throws a ConstructionException when an invalid authorID is provided.
     */
    @Test
    @Order(5)
    void testFilmCreation_InvalidAuthorID()
    {
        System.out.println("\n5: Testing Film constructor with invalid authorID...");

        try
        {
            //Create a new Film object with invalid authorID
            Film film = new Film("Title", -1, 1, "1234567890", 18);
            fail("Construction with invalid authorID should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidIDException, "Cause should be InvalidAuthorIDException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Film object throws a ConstructionException when an invalid classificationID
     * is provided.
     */
    @Test
    @Order(6)
    void testFilmCreation_InvalidClassificationID()
    {
        System.out.println("\n6: Testing Film constructor with invalid classificationID...");

        try
        {
            //Create a new Film object with invalid classificationID
            Film film = new Film("Title", 1, -1, "1234567890", 18);
            fail("Construction with invalid classificationID should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidIDException,
                    "Cause should be InvalidClassificationIDException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Film constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with a null barcode.
     */
    @Test
    @Order(7)
    void testFilmConstruction_NullBarcode()
    {
        System.out.println("\n7: Testing Film constructor with null barcode...");

        try
        {
            Film film = new Film("Title", 1, 1, null, 18);
            fail("ConstructionException expected due to null barcode.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidBarcodeException, "InvalidBarcodeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Film constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with an empty barcode string.
     */
    @Test
    @Order(8)
    void testFilmConstruction_EmptyBarcode()
    {
        System.out.println("\n8 Testing Film constructor with empty barcode...");

        try
        {
            Film film = new Film("Title", 1, 1, "", 18);
            fail("ConstructionException expected due to empty barcode.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidBarcodeException, "InvalidBarcodeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Film constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with a title string that exceeds Item.ITEM_BARCODE_LENGTH.
     */
    @Test
    @Order(9)
    void testFilmConstruction_TooLongBarcode()
    {
        System.out.println(
                "\n9: Testing Film constructor with title string longer than Item.ITEM_BARCODE_LENGTH...");

        try
        {
            String tooLongBarcode = String.format("%0" + (Item.ITEM_BARCODE_LENGTH + 1) + "d", 0);
            Film film = new Film("Title", 1, 1, tooLongBarcode, 18);
            fail("ConstructionException expected due to too long barcode.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidBarcodeException, "InvalidBarcodeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Film constructor throws a ConstructionException caused by InvalidAgeRatingException when
     * provided with a negative age rating.
     */
    @Test
    @Order(10)
    void testFilmConstruction_NegativeAgeRating()
    {
        System.out.println("\n10: Testing Film constructor with age rating below 0...");

        try
        {
            Film film = new Film("Title", 1, 1, "1234567890", -1);
            fail("ConstructionException expected due to too long low age rating.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidAgeRatingException,
                    "InvalidAgeRatingException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Film constructor throws a ConstructionException caused by InvalidAgeRatingException when
     * provided with a too high age rating.
     */
    @Test
    @Order(11)
    void testFilmConstruction_TooHighAgeRating()
    {
        System.out.println("\n11: Testing Film constructor with age rating below 0...");

        try
        {
            Film film = new Film("Title", 1, 1,
                    "1234567890", Film.FILM_MAX_AGE_RATING + 1);
            fail("ConstructionException expected due to too long high age rating.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidAgeRatingException,
                    "InvalidAgeRatingException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}