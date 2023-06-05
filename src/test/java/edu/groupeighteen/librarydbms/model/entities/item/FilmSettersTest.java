package edu.groupeighteen.librarydbms.model.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidItemTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import org.junit.jupiter.api.*;

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
public class FilmSettersTest
{
    private Film film;

    /**
     * Creates a new valid Film object before each test.
     */
    @BeforeEach
    void setUp()
    {
        try
        {
            film = new Film(false, 1, "Film", Item.ItemType.FILM,
                    "1", 1, 1, "D", "F",
                    "G", 1, true, 1,
                    "U", "Actor1");
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Tests the setItemID method with a valid ID.
     */
    @Test
    @Order(1)
    void testSetItemID_ValidID()
    {
        System.out.println("\n1: Testing setItemID method with a valid ID...");

        try
        {
            int validID = 2;
            film.setItemID(validID);
            assertEquals(validID, film.getItemID(),
                    "Item ID must be the same as the one set");

        }
        catch (InvalidIDException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setItemID method with a zero ID.
     */
    @Test
    @Order(2)
    void testSetItemID_ZeroID()
    {
        System.out.println("\n2: Testing setItemID method with a zero ID...");

        int zeroID = 0;

        try
        {
            film.setItemID(zeroID);
            fail("Zero ID should throw InvalidIDException");

        }
        catch (InvalidIDException e)
        {
            assertEquals("Item ID must be greater than 0. Received: " + zeroID, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setItemID method with a negative ID.
     */
    @Test
    @Order(3)
    void testSetItemID_NegativeID()
    {
        System.out.println("\n3: Testing setItemID method with a negative ID...");

        int negativeID = -1;

        try
        {
            film.setItemID(negativeID);
            fail("Negative ID should throw InvalidIDException");

        }
        catch (InvalidIDException e)
        {
            assertEquals("Item ID must be greater than 0. Received: " + negativeID, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setTitle method with a valid title.
     */
    @Test
    @Order(4)
    void testSetTitle_ValidTitle()
    {
        System.out.println("\n4: Testing setTitle method with a valid title...");

        try
        {
            String validTitle = "The Great Gatsby";
            film.setTitle(validTitle);
            assertEquals(validTitle, film.getTitle(),
                    "Title must be the same as the one set");

        }
        catch (InvalidTitleException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setTitle method with a null title.
     */
    @Test
    @Order(5)
    void testSetTitle_NullTitle()
    {
        System.out.println("\n5: Testing setTitle method with a null title...");

        try
        {
            String nullTitle = null;
            film.setTitle(nullTitle);
            fail("Null title should throw InvalidTitleException");

        }
        catch (InvalidTitleException e)
        {
            assertEquals("Title cannot be null or empty.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setTitle method with an empty title.
     */
    @Test
    @Order(6)
    void testSetTitle_EmptyTitle()
    {
        System.out.println("\n6: Testing setTitle method with an empty title...");

        try
        {
            String emptyTitle = "";
            film.setTitle(emptyTitle);
            fail("Empty title should throw InvalidTitleException");

        }
        catch (InvalidTitleException e)
        {
            assertEquals("Title cannot be null or empty.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setTitle method with a title that is too long.
     */
    @Test
    @Order(7)
    void testSetTitle_TooLongTitle()
    {
        System.out.println("\n7: Testing setTitle method with a too long title...");

        String tooLongTitle = "A".repeat(Item.ITEM_TITLE_MAX_LENGTH + 1);

        try
        {
            film.setTitle(tooLongTitle);
            fail("Too long title should throw InvalidTitleException");

        }
        catch (InvalidTitleException e)
        {
            assertEquals("Title cannot be longer than " + Item.ITEM_TITLE_MAX_LENGTH +
                    " characters. Received: " + tooLongTitle.length(), e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setType method with a valid ItemType.
     */
    @Test
    @Order(8)
    void testSetType_ValidType()
    {
        System.out.println("\n8: Testing setType method with a valid ItemType...");

        try
        {
            Item.ItemType validType = Item.ItemType.FILM;
            film.setType(validType);
            assertEquals(validType, film.getType(),
                    "ItemType must be the same as the one set");

        }
        catch (InvalidItemTypeException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setType method with a null ItemType.
     */
    @Test
    @Order(9)
    void testSetType_NullType()
    {
        System.out.println("\n9: Testing setType method with a null ItemType...");

        try
        {
            Item.ItemType nullType = null;
            film.setType(nullType);
            fail("Null ItemType should throw InvalidItemTypeException");

        }
        catch (InvalidItemTypeException e)
        {
            assertEquals("Item type cannot be null.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setBarcode method with a valid barcode.
     */
    @Test
    @Order(10)
    void testSetBarcode_ValidBarcode()
    {
        System.out.println("\n10: Testing setBarcode method with a valid barcode...");

        try
        {
            String validBarcode = "12345678901234567890"; // 20 digits
            film.setBarcode(validBarcode);
            assertEquals(validBarcode, film.getBarcode(),
                    "Barcode must be the same as the one set");

        }
        catch (InvalidBarcodeException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setBarcode method with a null barcode.
     */
    @Test
    @Order(11)
    void testSetBarcode_NullBarcode()
    {
        System.out.println("\n11: Testing setBarcode method with a null barcode...");

        try
        {
            String nullBarcode = null;
            film.setBarcode(nullBarcode);
            fail("Null barcode should throw InvalidBarcodeException");

        }
        catch (InvalidBarcodeException e)
        {
            assertEquals("Item barcode cannot be null or empty.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setBarcode method with an empty barcode.
     */
    @Test
    @Order(12)
    void testSetBarcode_EmptyBarcode()
    {
        System.out.println("\n12: Testing setBarcode method with an empty barcode...");

        try
        {
            String emptyBarcode = "";
            film.setBarcode(emptyBarcode);
            fail("Empty barcode should throw InvalidBarcodeException");

        }
        catch (InvalidBarcodeException e)
        {
            assertEquals("Item barcode cannot be null or empty.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setBarcode method with a barcode that is too long.
     */
    @Test
    @Order(13)
    void testSetBarcode_TooLongBarcode()
    {
        System.out.println("\n13: Testing setBarcode method with a too long barcode...");

        String tooLongBarcode = "a".repeat(Item.ITEM_BARCODE_LENGTH + 1);

        try
        {
            film.setBarcode(tooLongBarcode);
            fail("Too long barcode should throw InvalidBarcodeException");

        }
        catch (InvalidBarcodeException e)
        {
            assertEquals("Item barcode length cannot be greater than " + Film.ITEM_BARCODE_LENGTH
                    + " characters. Received: " + tooLongBarcode.length(), e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorID method with a valid ID.
     */
    @Test
    @Order(14)
    void testSetAuthorID_ValidID()
    {
        System.out.println("\n14: Testing setAuthorID method with a valid ID...");

        try
        {
            int validID = 10;
            film.setAuthorID(validID);
            assertEquals(validID, film.getAuthorID(),
                    "Author ID must be the same as the one set");

        }
        catch (InvalidIDException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorID method with a zero ID.
     */
    @Test
    @Order(15)
    void testSetAuthorID_ZeroID()
    {
        System.out.println("\n15: Testing setAuthorID method with a zero ID...");

        int zeroID = 0;

        try
        {
            film.setAuthorID(zeroID);
            fail("Zero ID should throw InvalidIDException");

        }
        catch (InvalidIDException e)
        {
            assertEquals("Author ID must be greater than 0. Received: " + zeroID, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorID method with a negative ID.
     */
    @Test
    @Order(16)
    void testSetAuthorID_NegativeID()
    {
        System.out.println("\n16: Testing setAuthorID method with a negative ID...");

        int negativeID = -1;

        try
        {
            film.setAuthorID(negativeID);
            fail("Negative ID should throw InvalidIDException");

        }
        catch (InvalidIDException e)
        {
            assertEquals("Author ID must be greater than 0. Received: " + negativeID, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setClassificationID method with a valid ID.
     */
    @Test
    @Order(17)
    void testSetClassificationID_ValidID()
    {
        System.out.println("\n17: Testing setClassificationID method with a valid ID...");

        try
        {
            int validID = 10;
            film.setClassificationID(validID);
            assertEquals(validID, film.getClassificationID(),
                    "Classification ID must be the same as the one set");

        }
        catch (InvalidIDException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setClassificationID method with a zero ID.
     */
    @Test
    @Order(18)
    void testSetClassificationID_ZeroID()
    {
        System.out.println("\n18: Testing setClassificationID method with a zero ID...");

        int zeroID = 0;

        try
        {
            film.setClassificationID(zeroID);
            fail("Zero ID should throw InvalidIDException");

        }
        catch (InvalidIDException e)
        {
            assertEquals("Classification ID must be greater than 0. Received: " + zeroID, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setClassificationID method with a negative ID.
     */
    @Test
    @Order(19)
    void testSetClassificationID_NegativeID()
    {
        System.out.println("\n19: Testing setClassificationID method with a negative ID...");

        int negativeID = -1;

        try
        {
            film.setClassificationID(negativeID);
            fail("Negative ID should throw InvalidIDException");

        }
        catch (InvalidIDException e)
        {
            assertEquals("Classification ID must be greater than 0. Received: " + negativeID, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAllowedRentalDays method with a valid number of days.
     */
    @Test
    @Order(20)
    void testSetAllowedRentalDays_ValidDays()
    {
        System.out.println("\n20: Testing setAllowedRentalDays method with a valid number of days...");

        try
        {
            int zeroDays = 0;
            int validDays = 7;

            film.setAllowedRentalDays(zeroDays);
            assertEquals(zeroDays, film.getAllowedRentalDays(),
                    "Allowed rental days must be the same as the one set");
            film.setAllowedRentalDays(validDays);
            assertEquals(validDays, film.getAllowedRentalDays(),
                    "Allowed rental days must be the same as the one set");

        }
        catch (InvalidDateException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAllowedRentalDays method with a negative number of days.
     */
    @Test
    @Order(21)
    void testSetAllowedRentalDays_NegativeDays()
    {
        System.out.println("\n21: Testing setAllowedRentalDays method with a negative number of days...");

        int negativeDays = -1;

        try
        {
            film.setAllowedRentalDays(negativeDays);
            fail("Negative days should throw InvalidDateException");

        }
        catch (InvalidDateException e)
        {
            assertEquals("Allowed rental days can't be negative. Received: " + negativeDays, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for setting a valid age rating.
     */
    @Test
    @Order(22)
    void testSetAgeRating_ValidAgeRating()
    {
        try
        {
            film.setAgeRating(12);
        }
        catch (InvalidAgeRatingException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(12, film.getAgeRating());
    }

    /**
     * Test case for setting an age rating lower than zero.
     */
    @Test
    @Order(23)
    void testSetAgeRating_AgeRatingLowerThanZero()
    {
        InvalidAgeRatingException exception = assertThrows(InvalidAgeRatingException.class,
                () -> film.setAgeRating(-1));
        assertEquals("Cannot set an age rating lower than 0.", exception.getMessage());
    }

    /**
     * Test case for setting an age rating higher than the maximum allowed value.
     */
    @Test
    @Order(24)
    void testSetAgeRating_AgeRatingHigherThanMax()
    {
        InvalidAgeRatingException exception = assertThrows(InvalidAgeRatingException.class,
                () -> film.setAgeRating(19));
        assertEquals("Cannot set an age rating higher than " + Film.FILM_MAX_AGE_RATING + ".", exception.getMessage());
    }

    /**
     * Test case for setting a valid publisher country.
     */
    @Test
    @Order(25)
    void testSetPublisherCountry_ValidPublisherCountry()
    {
        try
        {
            film.setCountryOfProduction("Canada");
        }
        catch (InvalidNameException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals("Canada", film.getCountryOfProduction());
    }

    /**
     * Test case for setting a publisher country that exceeds the maximum allowed length.
     */
    @Test
    @Order(26)
    void testSetPublisherCountry_PublisherCountryTooLong()
    {
        String tooLongPublisherCountry = String.join("", Collections.nCopies(Film.FILM_COUNTRY_LENGTH + 1, "a"));
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> film.setCountryOfProduction(tooLongPublisherCountry));
        assertEquals("Film country name cannot be greater than " + Film.FILM_COUNTRY_LENGTH + ".",
                exception.getMessage());
    }

    /**
     * Test case for setting a valid list of actors.
     */

    @Test
    @Order(27)
    void testSetListOfActors_ValidListOfActors()
    {
        String listOfActors = "Actor1, Actor2, Actor3";
        film.setListOfActors(listOfActors);
        assertEquals(listOfActors, film.getListOfActors());
    }
}