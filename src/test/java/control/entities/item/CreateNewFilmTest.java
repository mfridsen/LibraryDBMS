package control.entities.item;

import static org.junit.jupiter.api.Assertions.*;

import control.BaseHandlerTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Tests the createNewFilm method in ItemHandler.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewFilmTest extends BaseHandlerTest
{
    /**
     * Test case for createNewFilm with valid input.
     *
     * <p>This test ensures that createNewFilm method behaves correctly
     * when provided with valid input parameters. It verifies that a Film object
     * is constructed without throwing any exceptions and that the Film properties
     * are correctly set.</p>
     */
    @Test
    @Order(1)
    void testCreateNewFilm_ValidInput()
    {
        System.out.println("\n1: Testing createNewFilm with valid input...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with null title.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidTitleException
     * when provided with a null title. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(2)
    void testCreateNewFilm_NullTitle()
    {
        System.out.println("\n2: Testing createNewFilm with null title...");

        // Test case: Null title
        // Set the title as null
        // Ensure an InvalidTitleException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with empty title.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidTitleException
     * when provided with an empty title. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(3)
    void testCreateNewFilm_EmptyTitle()
    {
        System.out.println("\n3: Testing createNewFilm with empty title...");

        // Test case: Empty title
        // Set the title as an empty string
        // Ensure an InvalidTitleException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with too long title.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidTitleException
     * when provided with a title exceeding the maximum length. It ensures that an exception
     * is correctly thrown during the construction of the Item object.</p>
     */
    @Test
    @Order(4)
    void testCreateNewFilm_TooLongTitle()
    {
        System.out.println("\n4: Testing createNewFilm with too long title...");

        // Test case: Too long title
        // Set the title with a length exceeding FILM_TITLE_MAX_LENGTH
        // Ensure an InvalidTitleException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with null barcode.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidBarcodeException
     * when provided with a null barcode. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(5)
    void testCreateNewFilm_NullBarcode()
    {
        System.out.println("\n5: Testing createNewFilm with null barcode...");

        // Test case: Null barcode
        // Set the barcode as null
        // Ensure an InvalidBarcodeException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with empty barcode.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidBarcodeException
     * when provided with an empty barcode. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(6)
    void testCreateNewFilm_EmptyBarcode()
    {
        System.out.println("\n6: Testing createNewFilm with empty barcode...");

        // Test case: Empty barcode
        // Set the barcode as an empty string
        // Ensure an InvalidBarcodeException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with too long barcode.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidBarcodeException
     * when provided with a barcode exceeding the maximum length. It ensures that an exception
     * is correctly thrown during the construction of the Item object.</p>
     */
    @Test
    @Order(7)
    void testCreateNewFilm_TooLongBarcode()
    {
        System.out.println("\n7: Testing createNewFilm with too long barcode...");

        // Test case: Too long barcode
        // Set the barcode with a length exceeding FILM_BARCODE_LENGTH
        // Ensure an InvalidBarcodeException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with negative authorID.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidIDException
     * when provided with a negative authorID. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(8)
    void testCreateNewFilm_NegativeAuthorID()
    {
        System.out.println("\n8: Testing createNewFilm with negative authorID...");

        // Test case: Negative authorID
        // Set the authorID as a negative value
        // Ensure an InvalidIDException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for createNewFilm with negative classificationID.
     *
     * <p>This test verifies that createNewFilm method throws an InvalidIDException
     * when provided with a negative classificationID. It ensures that an exception is correctly thrown
     * during the construction of the Item object.</p>
     */
    @Test
    @Order(9)
    void testCreateNewFilm_NegativeClassificationID()
    {
        System.out.println("\n9: Testing createNewFilm with negative classificationID...");

        // Test case: Negative classificationID
        // Set the classificationID as a negative value
        // Ensure an InvalidIDException is thrown during the construction of the Film

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testCreateNewFilm_NegativeAgeRating() {
        System.out.println("\n10: Testing createNewFilm with negative age rating...");

        // Test case: Negative age rating
        // Set the age rating as a negative value
        // Ensure an InvalidAgeRatingException is thrown during the execution of createNewFilm()

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testCreateNewFilm_ExceedsMaxAgeRating() {
        System.out.println("\n11: Testing createNewFilm with age rating exceeding the maximum...");

        // Test case: Age rating exceeds the maximum
        // Set the age rating greater than FILM_MAX_AGE_RATING
        // Ensure an InvalidAgeRatingException is thrown during the execution of createNewFilm()

        System.out.println("\nTEST FINISHED.");
    }
}