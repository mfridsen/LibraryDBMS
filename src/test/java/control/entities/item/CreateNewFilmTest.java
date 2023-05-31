package control.entities.item;

import static org.junit.jupiter.api.Assertions.*;

import control.BaseHandlerTest;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

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
    //Negative age rating
    //Too high age rating

    @Override
    @BeforeEach
    protected void setupAndReset()
    {
        try
        {
            setupConnectionAndTables();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Test case for creating new film with valid input.
     */
    @Test
    @Order(1)
    void testCreateNewFilm_ValidInput() {
        System.out.println("\n1: Testing createNewFilm method with valid input...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with different valid types.
     */
    @Test
    @Order(2)
    void testCreateNewFilm_ValidInputDifferentTypes() {
        System.out.println("\n2: Testing createNewFilm method with different valid types...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with null title.
     */
    @Test
    @Order(3)
    void testCreateNewFilm_NullTitle() {
        System.out.println("\n3: Testing createNewFilm method with null title...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with empty title.
     */
    @Test
    @Order(4)
    void testCreateNewFilm_EmptyTitle() {
        System.out.println("\n4: Testing createNewFilm method with empty title...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with too long title.
     */
    @Test
    @Order(5)
    void testCreateNewFilm_TooLongTitle() {
        System.out.println("\n5: Testing createNewFilm method with too long title...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with null type.
     */
    @Test
    @Order(6)
    void testCreateNewFilm_NullType() {
        System.out.println("\n6: Testing createNewFilm method with null type...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with invalid authorID.
     */
    @Test
    @Order(7)
    void testCreateNewFilm_InvalidAuthorID() {
        System.out.println("\n7: Testing createNewFilm method with invalid authorID...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with invalid classificationID.
     */
    @Test
    @Order(8)
    void testCreateNewFilm_InvalidClassificationID() {
        System.out.println("\n8: Testing createNewFilm method with invalid classificationID...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with non-existent author.
     */
    @Test
    @Order(9)
    void testCreateNewFilm_NonExistentAuthor() {
        System.out.println("\n9: Testing createNewFilm method with non-existent author...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with non-existent classification.
     */
    @Test
    @Order(10)
    void testCreateNewFilm_NonExistentClassification() {
        System.out.println("\n10: Testing createNewFilm method with non-existent classification...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with null barcode.
     */
    @Test
    @Order(11)
    void testCreateNewFilm_NullBarcode() {
        System.out.println("\n11: Testing createNewFilm method with null barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with empty barcode.
     */
    @Test
    @Order(12)
    void testCreateNewFilm_EmptyBarcode() {
        System.out.println("\n12: Testing createNewFilm method with empty barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with too long barcode.
     */
    @Test
    @Order(13)
    void testCreateNewFilm_TooLongBarcode() {
        System.out.println("\n13: Testing createNewFilm method with too long barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with taken barcode.
     */
    @Test
    @Order(14)
    void testCreateNewFilm_TakenBarcode() {
        System.out.println("\n14: Testing createNewFilm method with taken barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with negative age rating.
     */
    @Test
    @Order(15)
    void testCreateNewFilm_NegativeAgeRating() {
        System.out.println("\n15: Testing createNewFilm method with negative age rating...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new film with too high age rating.
     */
    @Test
    @Order(16)
    void testCreateNewFilm_TooHighAgeRating() {
        System.out.println("\n16: Testing createNewFilm method with too high age rating...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }
}