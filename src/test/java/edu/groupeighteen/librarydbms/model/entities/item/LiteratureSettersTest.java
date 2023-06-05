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
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/30/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Tests all setter methods in the Literature class and inherited setter methods from the Item class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiteratureSettersTest
{
    private Literature literature;

    /**
     * Creates a new valid Literature object before each test.
     */
    @BeforeEach
    void setUp()
    {
        try
        {
            literature = new Literature(
                    false, // deleted
                    1, // itemID
                    "Test Title", // title
                    Item.ItemType.REFERENCE_LITERATURE, // type
                    "1234567890123", // barcode
                    1, // authorID
                    1, // classificationID
                    "Test Author Firstname", // authorFirstname
                    "Test Author Lastname", //authorLastname
                    "Test Classification", // classificationName
                    10, // allowedRentalDays
                    true, // available
                    "978-3-16-1484" // ISBN
            );
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
            literature.setItemID(validID);
            assertEquals(validID, literature.getItemID(),
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
            literature.setItemID(zeroID);
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
            literature.setItemID(negativeID);
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
            literature.setTitle(validTitle);
            assertEquals(validTitle, literature.getTitle(),
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
            literature.setTitle(nullTitle);
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
            literature.setTitle(emptyTitle);
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
            literature.setTitle(tooLongTitle);
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
            Item.ItemType validType = Item.ItemType.REFERENCE_LITERATURE;
            literature.setType(validType);
            assertEquals(validType, literature.getType(),
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
            literature.setType(nullType);
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
            literature.setBarcode(validBarcode);
            assertEquals(validBarcode, literature.getBarcode(),
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
            literature.setBarcode(nullBarcode);
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
            literature.setBarcode(emptyBarcode);
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
            literature.setBarcode(tooLongBarcode);
            fail("Too long barcode should throw InvalidBarcodeException");

        }
        catch (InvalidBarcodeException e)
        {
            assertEquals("Item barcode length cannot be greater than " + Literature.ITEM_BARCODE_LENGTH
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
            literature.setAuthorID(validID);
            assertEquals(validID, literature.getAuthorID(),
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
            literature.setAuthorID(zeroID);
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
            literature.setAuthorID(negativeID);
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
            literature.setClassificationID(validID);
            assertEquals(validID, literature.getClassificationID(),
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
            literature.setClassificationID(zeroID);
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
            literature.setClassificationID(negativeID);
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

            literature.setAllowedRentalDays(zeroDays);
            assertEquals(zeroDays, literature.getAllowedRentalDays(),
                    "Allowed rental days must be the same as the one set");
            literature.setAllowedRentalDays(validDays);
            assertEquals(validDays, literature.getAllowedRentalDays(),
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
            literature.setAllowedRentalDays(negativeDays);
            fail("Negative days should throw InvalidDateException");

        }
        catch (InvalidDateException e)
        {
            assertEquals("Allowed rental days can't be negative. Received: " + negativeDays, e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setISBN method with a valid ISBN.
     */
    @Test
    @Order(22)
    void testSetISBN_ValidISBN()
    {
        System.out.println("\n22: Testing setISBN method with a valid ISBN...");

        try
        {
            String validISBN = "978-3-16-1484";
            literature.setISBN(validISBN);
            assertEquals(validISBN, literature.getISBN(), "ISBN must be the same as the one set");

        }
        catch (InvalidISBNException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setISBN method with a null ISBN.
     */
    @Test
    @Order(23)
    void testSetISBN_NullISBN()
    {
        System.out.println("\n23: Testing setISBN method with a null ISBN...");

        try
        {
            String nullISBN = null;
            literature.setISBN(nullISBN);
            fail("Null ISBN should throw InvalidISBNException");

        }
        catch (InvalidISBNException e)
        {
            assertEquals("ISBN must not be null or empty.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setISBN method with an empty ISBN.
     */
    @Test
    @Order(24)
    void testSetISBN_EmptyISBN()
    {
        System.out.println("\n24: Testing setISBN method with an empty ISBN...");

        try
        {
            String emptyISBN = "";
            literature.setISBN(emptyISBN);
            fail("Empty ISBN should throw InvalidISBNException");

        }
        catch (InvalidISBNException e)
        {
            assertEquals("ISBN must not be null or empty.", e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setISBN method with an ISBN that is too long.
     */
    @Test
    @Order(25)
    void testSetISBN_TooLongISBN()
    {
        System.out.println("\n25: Testing setISBN method with an ISBN that is too long...");

        String tooLongISBN = "1".repeat(Literature.LITERATURE_ISBN_LENGTH + 1);

        try
        {
            literature.setISBN(tooLongISBN);
            fail("Too long ISBN should throw InvalidISBNException");

        }
        catch (InvalidISBNException e)
        {
            assertEquals("ISBN cannot be longer than " + Literature.LITERATURE_ISBN_LENGTH +
                    " characters. Received: " + tooLongISBN.length(), e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

}