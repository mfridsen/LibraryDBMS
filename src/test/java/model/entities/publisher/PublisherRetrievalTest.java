package model.entities.publisher;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/29/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * This class tests the retrieval constructor of the Publisher class.
 * It includes testing with both valid and invalid data.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherRetrievalTest
{
    /**
     * Test case for Publisher retrieval constructor with valid data.
     * This test checks if the Publisher object is correctly retrieved and its fields are set properly.
     */
    @Test
    @Order(1)
    void testPublisherRetrieval_ValidData()
    {
        System.out.println("\n1: Testing Publisher retrieval constructor with valid data...");

        int validId = 1;
        String validName = "Test Publisher";
        String validEmail = "test@test.com";
        boolean deleted = false;

        try
        {
            Publisher publisher = new Publisher(validId, validName, validEmail, deleted);
            assertEquals(deleted, publisher.isDeleted());
            assertEquals(validId, publisher.getPublisherID(), "Publisher ID should match the valid data input");
            assertEquals(validName, publisher.getPublisherName(), "Publisher name should match the valid data input");
            assertEquals(validEmail, publisher.getEmail(), "Publisher email should match the valid data input");
        }
        catch (ConstructionException e)
        {
            fail("ConstructionException should not have been thrown for valid data.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher retrieval constructor with invalid data - specifically negative publisher id.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with a negative id.
     */
    @Test
    @Order(2)
    void testPublisherRetrieval_InvalidData_NegativeId()
    {
        System.out.println("\n2: Testing Publisher retrieval constructor with negative id...");

        int invalidId = -1;
        String validName = "Test Publisher";
        String validEmail = "test@test.com";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(invalidId, validName, validEmail, deleted);
        }, "ConstructionException should be thrown for negative publisher id");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher retrieval constructor with invalid data - specifically null publisher name.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with a null name.
     */
    @Test
    @Order(3)
    void testPublisherRetrieval_InvalidData_NullName()
    {
        System.out.println("\n3: Testing Publisher retrieval constructor with null name...");

        int validId = 1;
        String invalidName = null;
        String validEmail = "test@test.com";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(validId, invalidName, validEmail, deleted);
        }, "ConstructionException should be thrown for null publisher name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher retrieval constructor with invalid data - specifically empty publisher name.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with an empty name.
     */
    @Test
    @Order(4)
    void testPublisherRetrieval_InvalidData_EmptyName()
    {
        System.out.println("\n4: Testing Publisher retrieval constructor with empty name...");

        int validId = 1;
        String invalidName = "";
        String validEmail = "test@test.com";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(validId, invalidName, validEmail, deleted);
        }, "ConstructionException should be thrown for empty publisher name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher retrieval constructor with invalid data - specifically publisher name exceeding the maximum length.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with a name longer than PUBLISHER_NAME_LENGTH.
     */
    @Test
    @Order(5)
    void testPublisherRetrieval_InvalidData_LongName()
    {
        System.out.println("\n5: Testing Publisher retrieval constructor with name exceeding maximum length...");

        int validId = 1;
        String invalidName = "a".repeat(Publisher.PUBLISHER_NAME_LENGTH + 1);
        String validEmail = "test@test.com";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(validId, invalidName, validEmail, deleted);
        }, "ConstructionException should be thrown for publisher name exceeding maximum length");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher retrieval constructor with invalid data - specifically publisher email exceeding the maximum length.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with an email longer than PUBLISHER_EMAIL_LENGTH.
     */
    @Test
    @Order(6)
    void testPublisherRetrieval_InvalidData_LongEmail()
    {
        System.out.println("\n6: Testing Publisher retrieval constructor with email exceeding maximum length...");

        int validId = 1;
        String validName = "Test Publisher";
        String invalidEmail = "a".repeat(Publisher.PUBLISHER_EMAIL_LENGTH + 1);
        boolean deleted = false;

        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(validId, validName, invalidEmail, deleted);
        }, "ConstructionException should be thrown for publisher email exceeding maximum length");

        System.out.println("\nTEST FINISHED.");
    }
}