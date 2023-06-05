package edu.groupeighteen.librarydbms.model.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidItemTypeException;
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
 * This test class contains test cases for the Literature constructor that is used when retrieving an existing
 * Literature object from the database. Each test case tests a specific aspect of the constructor's functionality
 * to ensure that it correctly initializes a Literature object given the provided parameters, or throws the
 * appropriate exceptions when given invalid parameters.
 * <p>
 * The tests are ordered, and higher numbered tests assume that the functionality tested in all previous tests is
 * working correctly.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiteratureRetrievalTest
{
    /**
     * Tests whether the Literature constructor correctly initializes a Literature object when provided with valid
     * parameters, and the deleted flag is false.
     */
    @Test
    @Order(1)
    void testLiteratureConstruction_ValidData_DeletedFalse()
    {
        System.out.println("\n1: Testing Literature constructor with valid data, deleted flag is false...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");

            // Verify that all fields are correctly initialized
            assertFalse(literature.isDeleted(),
                    "Deleted flag should be false.");
            assertEquals(1, literature.getItemID(),
                    "ItemID should be 1.");
            assertEquals("Title", literature.getTitle(),
                    "Title should be 'Title'.");
            assertEquals(Item.ItemType.OTHER_BOOKS, literature.getType(),
                    "Type should be LITERATURE.");
            assertEquals("Barcode", literature.getBarcode(),
                    "Barcode should be 'Barcode'.");
            assertEquals(1, literature.getAuthorID(),
                    "AuthorID should be 1.");
            assertEquals(1, literature.getClassificationID(),
                    "ClassificationID should be 1.");
            assertEquals("AuthorFName", literature.getAuthorFirstname(),
                    "AuthorName should be 'AuthorFName'.");
            assertEquals("AuthorLName",
                    literature.getAuthorLastname());
            assertEquals("ClassificationName", literature.getClassificationName(),
                    "ClassificationName should be 'ClassificationName'.");
            assertEquals(10, literature.getAllowedRentalDays(),
                    "AllowedRentalDays should be 10.");
            assertTrue(literature.isAvailable(),
                    "Available flag should be true.");
            assertEquals("1234567890123", literature.getISBN(),
                    "ISBN should be '1234567890123'.");
        }
        catch (ConstructionException e)
        {
            fail("Valid parameters should not result in an exception.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests whether the Literature constructor correctly initializes a Literature object when provided with valid
     * parameters, and the deleted flag is true.
     */
    @Test
    @Order(2)
    void testLiteratureConstruction_ValidData_DeletedTrue()
    {
        System.out.println("\n2: Testing Literature constructor with valid data, deleted flag is true...");

        try
        {
            Literature literature = new Literature(true, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");

            // Verify that all fields are correctly initialized
            assertTrue(literature.isDeleted(),
                    "Deleted flag should be true.");
            assertEquals(1, literature.getItemID(),
                    "ItemID should be 1.");
            assertEquals("Title", literature.getTitle(),
                    "Title should be 'Title'.");
            assertEquals(Item.ItemType.OTHER_BOOKS, literature.getType(),
                    "Type should be LITERATURE.");
            assertEquals("Barcode", literature.getBarcode(),
                    "Barcode should be 'Barcode'.");
            assertEquals(1, literature.getAuthorID(),
                    "AuthorID should be 1.");
            assertEquals(1, literature.getClassificationID(),
                    "ClassificationID should be 1.");
            assertEquals("AuthorFName", literature.getAuthorFirstname(),
                    "AuthorName should be 'AuthorFName'.");
            assertEquals("AuthorLName",
                    literature.getAuthorLastname());
            assertEquals("ClassificationName", literature.getClassificationName(),
                    "ClassificationName should be 'ClassificationName'.");
            assertEquals(10, literature.getAllowedRentalDays(),
                    "AllowedRentalDays should be 10.");
            assertTrue(literature.isAvailable(),
                    "Available flag should be true.");
            assertEquals("1234567890123", literature.getISBN(),
                    "ISBN should be '1234567890123'.");
        }
        catch (ConstructionException e)
        {
            fail("Valid parameters should not result in an exception.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the Literature constructor with invalid itemID.
     * This test checks if the Literature constructor throws the correct exception when an invalid itemID is given.
     */
    @Test
    @Order(3)
    void testLiteratureConstruction_InvalidItemID()
    {
        System.out.println("\n3: Testing Literature constructor with invalid itemID...");

        assertThrows(ConstructionException.class, () ->
        {
            new Literature(true, -1, "Test Title", Item.ItemType.OTHER_BOOKS, "1234567890",
                    1, 1, "AuthorFName", "AuthorLName", "Classification Name",
                    7, true, "1234567890");
        }, "ConstructionException was expected due to invalid itemID.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidTitleException when
     * provided with a null title.
     */
    @Test
    @Order(4)
    void testLiteratureConstruction_NullTitle()
    {
        System.out.println("\n4: Testing Literature constructor with null title...");

        try
        {
            Literature literature = new Literature(false, 1, null, Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to null title.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "InvalidTitleException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidTitleException when
     * provided with an empty title string.
     */
    @Test
    @Order(5)
    void testLiteratureConstruction_EmptyTitle()
    {
        System.out.println("\n5: Testing Literature constructor with empty title...");

        try
        {
            Literature literature = new Literature(false, 1, "", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to empty title.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "InvalidTitleException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidTitleException when
     * provided with a title string that exceeds Item.MAX_TITLE_LENGTH.
     */
    @Test
    @Order(6)
    void testLiteratureConstruction_TooLongTitle()
    {
        System.out.println("\n6: Testing Literature constructor with title string longer than Item.MAX_TITLE_LENGTH.." +
                ".");

        try
        {
            String tooLongTitle = String.format("%0" + (Item.ITEM_TITLE_MAX_LENGTH + 1) + "d", 0);
            Literature literature = new Literature(false, 1, tooLongTitle, Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to too long title.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidTitleException, "InvalidTitleException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidItemTypeException when
     * provided with a null ItemType.
     */
    @Test
    @Order(7)
    void testLiteratureConstruction_NullItemType()
    {
        System.out.println("\n7: Testing Literature constructor with null ItemType...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", null,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to null ItemType.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidItemTypeException, "InvalidItemTypeException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidBarcodeException when
     * provided with a null barcode.
     */
    @Test
    @Order(8)
    void testLiteratureConstruction_NullBarcode()
    {
        System.out.println("\n8: Testing Literature constructor with null barcode...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    null, 1, 1, "AuthorFName", "AuthorLName", "ClassificationName",
                    10, true, "1234567890123");
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
    @Order(9)
    void testLiteratureConstruction_EmptyBarcode()
    {
        System.out.println("\n9: Testing Literature constructor with empty barcode...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "", 1, 1, "AuthorFName", "AuthorLName", "ClassificationName",
                    10, true, "1234567890123");
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
    @Order(10)
    void testLiteratureConstruction_TooLongBarcode()
    {
        System.out.println(
                "\n10: Testing Literature constructor with title string longer than Item.ITEM_BARCODE_LENGTH...");

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

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidIDException when
     * provided with a negative authorID.
     */
    @Test
    @Order(11)
    void testLiteratureConstruction_NegativeAuthorID()
    {
        System.out.println("\n11: Testing Literature constructor with negative authorID...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", -1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to negative authorID.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidIDException, "InvalidIDException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidIDException when
     * provided with a negative classificationID.
     */
    @Test
    @Order(12)
    void testLiteratureConstruction_NegativeClassificationID()
    {
        System.out.println("\n12: Testing Literature constructor with negative classificationID...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, -1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123");
            fail("ConstructionException expected due to negative classificationID.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidIDException, "InvalidIDException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidDateException
     * when provided with a negative allowedRentalDays.
     */
    @Test
    @Order(13)
    void testLiteratureConstruction_NegativeAllowedRentalDays()
    {
        System.out.println("\n13: Testing Literature constructor with negative allowedRentalDays...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", -1, true, "1234567890123");
            fail("ConstructionException expected due to negative allowedRentalDays.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidDateException, "InvalidDateException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidISBNException when
     * provided with a null ISBN.
     */
    @Test
    @Order(14)
    void testLiteratureConstruction_NullISBN()
    {
        System.out.println("\n14: Testing Literature constructor with null ISBN...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, null);
            fail("ConstructionException expected due to null ISBN.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidISBNException, "InvalidISBNException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidISBNException when
     * provided with an empty ISBN string.
     */
    @Test
    @Order(15)
    void testLiteratureConstruction_EmptyISBN()
    {
        System.out.println("\n15: Testing Literature constructor with empty ISBN...");

        try
        {
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, "");
            fail("ConstructionException expected due to empty ISBN.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidISBNException, "InvalidISBNException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests if the Literature constructor throws a ConstructionException caused by InvalidISBNException when
     * provided with an ISBN string that exceeds LITERATURE_ISBN_LENGTH.
     */
    @Test
    @Order(16)
    void testLiteratureConstruction_TooLongISBN()
    {
        System.out.println("\n16: Testing Literature constructor with ISBN string longer than LITERATURE_ISBN_LENGTH." +
                "..");

        try
        {
            String tooLongISBN = String.format("%0" + (Literature.LITERATURE_ISBN_LENGTH + 1) + "d", 0);
            Literature literature = new Literature(false, 1, "Title", Item.ItemType.OTHER_BOOKS,
                    "Barcode", 1, 1, "AuthorFName", "AuthorLName",
                    "ClassificationName", 10, true, tooLongISBN);
            fail("ConstructionException expected due to too long ISBN.");
        }
        catch (ConstructionException e)
        {
            assertTrue(e.getCause() instanceof InvalidISBNException, "InvalidISBNException expected.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}