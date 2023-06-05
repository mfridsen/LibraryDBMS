package edu.groupeighteen.librarydbms.model.entities.item;

import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
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
 * Simple test for the Copy Constructor in the Literature class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiteratureCopyTest
{
    /**
     * Since no validation is performed by the Copy Constructor we only need to perform this one test.
     */
    @Test
    @Order(1)
    void testLiteratureCopy_ValidData()
    {
        System.out.println("\n1: Testing Literature copy constructor with valid data...");

        try
        {
            Literature literature = new Literature(new Literature(false, 1, "Title",
                    Item.ItemType.OTHER_BOOKS, "Barcode", 1, 1, "AuthorFName",
                    "AuthorLName",
                    "ClassificationName", 10, true, "1234567890123"));

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
}