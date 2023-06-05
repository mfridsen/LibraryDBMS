package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test class for ItemHandler.createNewLiterature method.
 * The purpose of these tests is to verify that the createNewLiterature method behaves as expected under
 * different circumstances.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewLiteratureTest extends BaseHandlerTest
{
    //Valid input  (remember to validate ALL fields)
    //Valid input different types (remember to validate ALL fields)
    //Null title
    //Empty title
    //Too long title
    //Null type
    //Invalid authorID
    //Invalid classificationID
    //Non-existent author
    //Non-existent classification
    //Null barcode
    //Empty barcode
    //Too long barcode
    //Taken barcode
    //Null ISBN
    //Empty ISBN
    //Too long ISBN

    @Override
    @BeforeEach
    protected void setupAndReset()
    {
        super.setupAndReset();
        ItemHandler.reset();
    }

    //TODO-PRIO validate join tables

    /**
     * Test case for creating new literature with valid input.
     */
    @Test
    @Order(1)
    void testCreateNewLiterature_ValidInput()
    {
        System.out.println("\n1: Testing createNewLiterature method with valid input...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            Literature newLiterature = ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode,
                    isbn);

            assertNotNull(newLiterature);
            assertEquals(title, newLiterature.getTitle());
            assertEquals(type, newLiterature.getType());
            assertEquals(authorId, newLiterature.getAuthorID());
            assertEquals(classificationId, newLiterature.getClassificationID());
            assertEquals(barcode, newLiterature.getBarcode());
            assertEquals(isbn, newLiterature.getISBN());
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with different valid types.
     */
    @Test
    @Order(2)
    void testCreateNewLiterature_ValidInputDifferentTypes()
    {
        System.out.println("\n2: Testing createNewLiterature method with different valid types...");

        for (Item.ItemType type : Item.ItemType.values())
        {
            try
            {
                String title = "Valid Title " + type.name();
                int authorId = 1; // assuming this is a valid author ID
                int classificationId = 1; // assuming this is a valid classification ID
                String barcode = "validBarcode" + type.name();
                String isbn = "9783161484100"; // valid ISBN-13

                Literature newLiterature = ItemHandler.createNewLiterature(title, type, authorId, classificationId,
                        barcode, isbn);

                assertNotNull(newLiterature);
                assertEquals(title, newLiterature.getTitle());
                assertEquals(type, newLiterature.getType());
                assertEquals(authorId, newLiterature.getAuthorID());
                assertEquals(classificationId, newLiterature.getClassificationID());
                assertEquals(barcode, newLiterature.getBarcode());
                assertEquals(isbn, newLiterature.getISBN());
            }
            catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException e)
            {
                fail("Valid operations should not throw exceptions.");
                e.printStackTrace();
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null title.
     */
    @Test
    @Order(3)
    void testCreateNewLiterature_NullTitle()
    {
        System.out.println("\n3: Testing createNewLiterature method with null title...");

        String title = null;
        Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
        int authorId = 1; // assuming this is a valid author ID
        int classificationId = 1; // assuming this is a valid classification ID
        String barcode = "validBarcode";
        String isbn = "9783161484100"; // valid ISBN-13

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
        });

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with empty title.
     */
    @Test
    @Order(4)
    void testCreateNewLiterature_EmptyTitle()
    {
        System.out.println("\n4: Testing createNewLiterature method with empty title...");

        String title = "";
        Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
        int authorId = 1; // assuming this is a valid author ID
        int classificationId = 1; // assuming this is a valid classification ID
        String barcode = "validBarcode";
        String isbn = "9783161484100"; // valid ISBN-13

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
        });

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with too long title.
     */
    @Test
    @Order(5)
    void testCreateNewLiterature_TooLongTitle()
    {
        System.out.println("\n5: Testing createNewLiterature method with too long title...");

        String title = String.join("", Collections.nCopies(256, "a")); // 256 characters long title
        Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
        int authorId = 1; // assuming this is a valid author ID
        int classificationId = 1; // assuming this is a valid classification ID
        String barcode = "validBarcode";
        String isbn = "9783161484100"; // valid ISBN-13

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
        });

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null type.
     */
    @Test
    @Order(6)
    void testCreateNewLiterature_NullType()
    {
        System.out.println("\n6: Testing createNewLiterature method with null type...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = null;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with invalid authorID.
     */
    @Test
    @Order(7)
    void testCreateNewLiterature_InvalidAuthorID()
    {
        System.out.println("\n7: Testing createNewLiterature method with invalid authorID...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = -1; // invalid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(InvalidIDException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with invalid classificationID.
     */
    @Test
    @Order(8)
    void testCreateNewLiterature_InvalidClassificationID()
    {
        System.out.println("\n8: Testing createNewLiterature method with invalid classificationID...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = -1; // invalid classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(InvalidIDException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with non-existent author.
     */
    @Test
    @Order(9)
    void testCreateNewLiterature_NonExistentAuthor()
    {
        System.out.println("\n9: Testing createNewLiterature method with non-existent author...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 9999; // non-existent author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(EntityNotFoundException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with non-existent classification.
     */
    @Test
    @Order(10)
    void testCreateNewLiterature_NonExistentClassification()
    {
        System.out.println("\n10: Testing createNewLiterature method with non-existent classification...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 9999; // non-existent classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(EntityNotFoundException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null barcode.
     */
    @Test
    @Order(11)
    void testCreateNewLiterature_NullBarcode()
    {
        System.out.println("\n11: Testing createNewLiterature method with null barcode...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = null;
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with empty barcode.
     */
    @Test
    @Order(12)
    void testCreateNewLiterature_EmptyBarcode()
    {
        System.out.println("\n12: Testing createNewLiterature method with empty barcode...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "";
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with too long barcode.
     */
    @Test
    @Order(13)
    void testCreateNewLiterature_TooLongBarcode()
    {
        System.out.println("\n13: Testing createNewLiterature method with too long barcode...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "a".repeat(Item.ITEM_BARCODE_LENGTH + 1);
            String isbn = "9783161484100"; // valid ISBN-13

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with taken barcode.
     */
    @Test
    @Order(14)
    void testCreateNewLiterature_TakenBarcode()
    {
        System.out.println("\n14: Testing createNewLiterature method with taken barcode...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "takenBarcode";
            String isbn = "9783161484100"; // valid ISBN-13

            ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);

            assertThrows(InvalidBarcodeException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null ISBN.
     */
    @Test
    @Order(15)
    void testCreateNewLiterature_NullISBN()
    {
        System.out.println("\n15: Testing createNewLiterature method with null ISBN...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = null;

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with empty ISBN.
     */
    @Test
    @Order(16)
    void testCreateNewLiterature_EmptyISBN()
    {
        System.out.println("\n16: Testing createNewLiterature method with empty ISBN...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = "";

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with too long ISBN.
     */
    @Test
    @Order(17)
    void testCreateNewLiterature_TooLongISBN()
    {
        System.out.println("\n17: Testing createNewLiterature method with too long ISBN...");

        try
        {
            String title = "Valid Title";
            Item.ItemType type = Item.ItemType.REFERENCE_LITERATURE;
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            String isbn = "9783161484100ExtraDigits"; // too long ISBN

            assertThrows(ConstructionException.class, () ->
            {
                ItemHandler.createNewLiterature(title, type, authorId, classificationId, barcode, isbn);
            });
        }
        catch (Exception e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}