package edu.groupeighteen.librarydbms.model.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
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
 * @date 5/30/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * This class tests the creation of Literature object with valid data.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiteratureCreationTest
{

    /**
     * This method tests if a Literature object is correctly created with valid data.
     */
    @Test
    @Order(1)
    void testLiteratureCreation_ValidData()
    {
        System.out.println("\n1: Testing Literature constructor with valid data...");

        try
        {
            //Create a new Literature object
            Literature literature = new Literature("Title", Item.ItemType.OTHER_BOOKS, 1, 1,
                    "1234567890", "0123456789");

            //Verify that the created Literature object has the correct properties
            assertEquals("Title", literature.getTitle(),
                    "Title should be set correctly in constructor");
            assertEquals(Item.ItemType.OTHER_BOOKS, literature.getType(),
                    "ItemType should be set correctly in constructor");
            assertEquals("1234567890", literature.getBarcode(),
                    "Barcode should be set correctly in constructor");
            assertEquals("0123456789", literature.getISBN(),
                    "ISBN should be set correctly in constructor");
            assertTrue(literature.isAvailable(),
                    "New Literature items should be available by default");
            assertFalse(literature.isDeleted(),
                    "New Literature items should not be deleted by default");
            assertEquals(0, literature.getItemID(),
                    "New Literature items should have an itemID of 0 by default");
            assertEquals(1, literature.getAuthorID(),
                    "AuthorID should be set correctly in constructor");
            assertEquals(1, literature.getClassificationID(),
                    "ClassificationID should be set correctly in constructor");
            assertNull(literature.getAuthorFirstname(),
                    "New Literature items should have null authorName by default");
            assertNull(literature.getClassificationName(),
                    "New Literature items should have null classificationName by default");
            assertEquals(Item.getDefaultAllowedRentalDays(Item.ItemType.OTHER_BOOKS), literature.getAllowedRentalDays(),
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
     * This method tests if a Literature object throws a ConstructionException when null title is provided.
     */
    @Test
    @Order(2)
    void testLiteratureCreation_NullTitle()
    {
        System.out.println("\n2: Testing Literature constructor with null title...");

        try
        {
            //Create a new Literature object with null title
            Literature literature = new Literature(null, Item.ItemType.OTHER_BOOKS, 1, 1,
                    "1234567890", "0123456789");
            fail("Construction with null title should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "Cause should be InvalidTitleException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when an empty title is provided.
     */
    @Test
    @Order(3)
    void testLiteratureCreation_EmptyTitle()
    {
        System.out.println("\n3: Testing Literature constructor with empty title...");

        try
        {
            //Create a new Literature object with empty title
            Literature literature = new Literature("", Item.ItemType.OTHER_BOOKS, 1, 1,
                    "1234567890", "0123456789");
            fail("Construction with empty title should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "Cause should be InvalidTitleException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when a too long title is provided.
     */
    @Test
    @Order(4)
    void testLiteratureCreation_LongTitle()
    {
        System.out.println("\n4: Testing Literature constructor with long title...");

        //Generate a long title
        StringBuilder longTitle = new StringBuilder();
        longTitle.append("a".repeat(Math.max(0, Item.ITEM_TITLE_MAX_LENGTH + 1)));

        try
        {
            //Create a new Literature object with long title
            Literature literature = new Literature(longTitle.toString(), Item.ItemType.OTHER_BOOKS, 1,
                    1, "1234567890", "0123456789");
            fail("Construction with long title should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "Cause should be InvalidTitleException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when a null ISBN is provided.
     */
    @Test
    @Order(5)
    void testLiteratureCreation_NullISBN()
    {
        System.out.println("\n5: Testing Literature constructor with null ISBN...");

        try
        {
            //Create a new Literature object with null ISBN
            Literature literature = new Literature("The Title", Item.ItemType.OTHER_BOOKS, 1, 1,
                    "1234567890", null);
            fail("Construction with null ISBN should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidISBNException, "Cause should be InvalidISBNException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when an empty ISBN is provided.
     */
    @Test
    @Order(6)
    void testLiteratureCreation_EmptyISBN()
    {
        System.out.println("\n6: Testing Literature constructor with empty ISBN...");

        try
        {
            //Create a new Literature object with empty ISBN
            Literature literature = new Literature("The Title", Item.ItemType.OTHER_BOOKS, 1, 1,
                    "1234567890", "");
            fail("Construction with empty ISBN should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidISBNException, "Cause should be InvalidISBNException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when ISBN is too long.
     */
    @Test
    @Order(7)
    void testLiteratureCreation_LongISBN()
    {
        System.out.println("\n7: Testing Literature constructor with long ISBN...");

        try
        {
            //Create a new Literature object with long ISBN
            Literature literature = new Literature("The Title", Item.ItemType.OTHER_BOOKS, 1, 1,
                    "1234567890", "0123456789012345678901234567890");
            fail("Construction with long ISBN should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidISBNException, "Cause should be InvalidISBNException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when an invalid authorID is provided.
     */
    @Test
    @Order(8)
    void testLiteratureCreation_InvalidAuthorID()
    {
        System.out.println("\n8: Testing Literature constructor with invalid authorID...");

        try
        {
            //Create a new Literature object with invalid authorID
            Literature literature = new Literature("The Title", Item.ItemType.OTHER_BOOKS, -1,
                    1, "1234567890", "0123456789");
            fail("Construction with invalid authorID should throw ConstructionException");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidIDException, "Cause should be InvalidAuthorIDException");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This method tests if a Literature object throws a ConstructionException when an invalid classificationID
     * is provided.
     */
    @Test
    @Order(9)
    void testLiteratureCreation_InvalidClassificationID()
    {
        System.out.println("\n9: Testing Literature constructor with invalid classificationID...");

        try
        {
            //Create a new Literature object with invalid classificationID
            Literature literature = new Literature("The Title", Item.ItemType.OTHER_BOOKS, 1,
                    -1, "1234567890", "0123456789");
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
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with a null barcode.
     */
    @Test
    @Order(9)
    void testLiteratureConstruction_NullBarcode()
    {
        System.out.println("\n9: Testing Literature constructor with null barcode...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    null, 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to null barcode.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidBarcodeException, "InvalidBarcodeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with an empty barcode string.
     */
    @Test
    @Order(10)
    void testLiteratureConstruction_EmptyBarcode()
    {
        System.out.println("\n10: Testing Literature constructor with empty barcode...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to empty barcode.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidBarcodeException, "InvalidBarcodeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with a title string that exceeds Item.ITEM_BARCODE_LENGTH.
     */
    @Test
    @Order(11)
    void testLiteratureConstruction_TooLongBarcode()
    {
        System.out.println(
                "\n11: Testing Literature constructor with title string longer than Item.ITEM_BARCODE_LENGTH...");

        try
        {
            String tooLongBarcode = String.format("%0" + (Item.ITEM_BARCODE_LENGTH + 1) + "d", 0);
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    tooLongBarcode, 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to too long title.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidBarcodeException, "InvalidBarcodeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}