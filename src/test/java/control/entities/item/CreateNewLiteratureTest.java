package control.entities.item;

import static org.junit.jupiter.api.Assertions.*;

import control.BaseHandlerTest;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Tests the createNewLiterature method in ItemHandler.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewLiteratureTest extends BaseHandlerTest
{
    /**
     * Test case for createNewLiterature with valid input.
     *
     * <p>This test ensures that createNewLiterature method behaves correctly
     * when provided with valid input parameters. It verifies that a Literature object
     * is constructed without throwing any exceptions and that the Literature properties
     * are correctly set.</p>
     */
    @Test
    @Order(1)
    void testCreateNewLiterature_ValidInput()
    {
        System.out.println("\n1: Testing createNewLiterature with valid input...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with null title.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidTitleException
     * when provided with a null title. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(2)
    void testCreateNewLiterature_NullTitle()
    {
        System.out.println("\n2: Testing createNewLiterature with null title...");

        // Test case: Null title
        // Set the title as null
        // Ensure an InvalidTitleException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with empty title.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidTitleException
     * when provided with an empty title. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(3)
    void testCreateNewLiterature_EmptyTitle()
    {
        System.out.println("\n3: Testing createNewLiterature with empty title...");

        // Test case: Empty title
        // Set the title as an empty string
        // Ensure an InvalidTitleException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with too long title.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidTitleException
     * when provided with a title exceeding the maximum length. It ensures that an exception
     * is correctly thrown during the construction of the Item object.</p>
     */
    @Test
    @Order(4)
    void testCreateNewLiterature_TooLongTitle()
    {
        System.out.println("\n4: Testing createNewLiterature with too long title...");

        // Test case: Too long title
        // Set the title with a length exceeding LITERATURE_TITLE_MAX_LENGTH
        // Ensure an InvalidTitleException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with null barcode.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidBarcodeException
     * when provided with a null barcode. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(5)
    void testCreateNewLiterature_NullBarcode()
    {
        System.out.println("\n5: Testing createNewLiterature with null barcode...");

        // Test case: Null barcode
        // Set the barcode as null
        // Ensure an InvalidBarcodeException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with empty barcode.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidBarcodeException
     * when provided with an empty barcode. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(6)
    void testCreateNewLiterature_EmptyBarcode()
    {
        System.out.println("\n6: Testing createNewLiterature with empty barcode...");

        // Test case: Empty barcode
        // Set the barcode as an empty string
        // Ensure an InvalidBarcodeException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with too long barcode.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidBarcodeException
     * when provided with a barcode exceeding the maximum length. It ensures that an exception
     * is correctly thrown during the construction of the Item object.</p>
     */
    @Test
    @Order(7)
    void testCreateNewLiterature_TooLongBarcode()
    {
        System.out.println("\n7: Testing createNewLiterature with too long barcode...");

        // Test case: Too long barcode
        // Set the barcode with a length exceeding ITEM_BARCODE_LENGTH
        // Ensure an InvalidBarcodeException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with negative authorID.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidIDException
     * when provided with a negative authorID. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(8)
    void testCreateNewLiterature_NegativeAuthorID()
    {
        System.out.println("\n8: Testing createNewLiterature with negative authorID...");

        // Test case: Negative authorID
        // Set the authorID as a negative value
        // Ensure an InvalidIDException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with negative classificationID.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidIDException
     * when provided with a negative classificationID. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(9)
    void testCreateNewLiterature_NegativeClassificationID()
    {
        System.out.println("\n9: Testing createNewLiterature with negative classificationID...");

        // Test case: Negative classificationID
        // Set the classificationID as a negative value
        // Ensure an InvalidIDException is thrown during the construction of the Literature

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with null ISBN.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidISBNException
     * when provided with a null ISBN. It ensures that an exception is correctly thrown
     * during the execution of the createNewLiterature method.</p>
     */
    @Test
    @Order(10)
    void testCreateNewLiterature_NullISBN()
    {
        System.out.println("\n10: Testing createNewLiterature with null ISBN...");

        // Test case: Null ISBN
        // Set the ISBN as null
        // Ensure an InvalidISBNException is thrown during the execution of createNewLiterature()

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with empty ISBN.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidISBNException
     * when provided with an empty ISBN. It ensures that an exception is correctly thrown
     * during the execution of the createNewLiterature method.</p>
     */
    @Test
    @Order(11)
    void testCreateNewLiterature_EmptyISBN()
    {
        System.out.println("\n11: Testing createNewLiterature with empty ISBN...");

        // Test case: Empty ISBN
        // Set the ISBN as an empty string
        // Ensure an InvalidISBNException is thrown during the execution of createNewLiterature()

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewLiterature with too long ISBN.
     *
     * <p>This test verifies that createNewLiterature method throws an InvalidISBNException
     * when provided with an ISBN exceeding the maximum length. It ensures that an exception
     * is correctly thrown during the execution of the createNewLiterature method.</p>
     */
    @Test
    @Order(12)
    void testCreateNewLiterature_TooLongISBN()
    {
        System.out.println("\n12: Testing createNewLiterature with too long ISBN...");

        // Test case: Too long ISBN
        // Set the ISBN with a length exceeding LITERATURE_ISBN_LENGTH
        // Ensure an InvalidISBNException is thrown during the execution of createNewLiterature()

        System.out.println("\nTEST FINISHED.");
    }
}