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
 * Test class for ItemHandler.createNewLiterature method.
 * The purpose of these tests is to verify that the createNewLiterature method behaves as expected under
 * different circumstances.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewLiteratureTest extends BaseHandlerTest
{
    //Valid input
    //Valid input different types
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
     * Test case for creating new literature with valid input.
     */
    @Test
    @Order(1)
    void testCreateNewLiterature_ValidInput() {
        System.out.println("\n1: Testing createNewLiterature method with valid input...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with different valid types.
     */
    @Test
    @Order(2)
    void testCreateNewLiterature_ValidInputDifferentTypes() {
        System.out.println("\n2: Testing createNewLiterature method with different valid types...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null title.
     */
    @Test
    @Order(3)
    void testCreateNewLiterature_NullTitle() {
        System.out.println("\n3: Testing createNewLiterature method with null title...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with empty title.
     */
    @Test
    @Order(4)
    void testCreateNewLiterature_EmptyTitle() {
        System.out.println("\n4: Testing createNewLiterature method with empty title...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with too long title.
     */
    @Test
    @Order(5)
    void testCreateNewLiterature_TooLongTitle() {
        System.out.println("\n5: Testing createNewLiterature method with too long title...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null type.
     */
    @Test
    @Order(6)
    void testCreateNewLiterature_NullType() {
        System.out.println("\n6: Testing createNewLiterature method with null type...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with invalid authorID.
     */
    @Test
    @Order(7)
    void testCreateNewLiterature_InvalidAuthorID() {
        System.out.println("\n7: Testing createNewLiterature method with invalid authorID...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with invalid classificationID.
     */
    @Test
    @Order(8)
    void testCreateNewLiterature_InvalidClassificationID() {
        System.out.println("\n8: Testing createNewLiterature method with invalid classificationID...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with non-existent author.
     */
    @Test
    @Order(9)
    void testCreateNewLiterature_NonExistentAuthor() {
        System.out.println("\n9: Testing createNewLiterature method with non-existent author...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with non-existent classification.
     */
    @Test
    @Order(10)
    void testCreateNewLiterature_NonExistentClassification() {
        System.out.println("\n10: Testing createNewLiterature method with non-existent classification...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null barcode.
     */
    @Test
    @Order(11)
    void testCreateNewLiterature_NullBarcode() {
        System.out.println("\n11: Testing createNewLiterature method with null barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with empty barcode.
     */
    @Test
    @Order(12)
    void testCreateNewLiterature_EmptyBarcode() {
        System.out.println("\n12: Testing createNewLiterature method with empty barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with too long barcode.
     */
    @Test
    @Order(13)
    void testCreateNewLiterature_TooLongBarcode() {
        System.out.println("\n13: Testing createNewLiterature method with too long barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with taken barcode.
     */
    @Test
    @Order(14)
    void testCreateNewLiterature_TakenBarcode() {
        System.out.println("\n14: Testing createNewLiterature method with taken barcode...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with null ISBN.
     */
    @Test
    @Order(15)
    void testCreateNewLiterature_NullISBN() {
        System.out.println("\n15: Testing createNewLiterature method with null ISBN...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with empty ISBN.
     */
    @Test
    @Order(16)
    void testCreateNewLiterature_EmptyISBN() {
        System.out.println("\n16: Testing createNewLiterature method with empty ISBN...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for creating new literature with too long ISBN.
     */
    @Test
    @Order(17)
    void testCreateNewLiterature_TooLongISBN() {
        System.out.println("\n17: Testing createNewLiterature method with too long ISBN...");
        // TODO: Write test
        System.out.println("\nTEST FINISHED.");
    }

}