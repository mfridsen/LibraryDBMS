package model.entities.publisher;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
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
 * Tests for the Publisher's copy constructor.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherCopyTest
{
    /**
     * Test case for Publisher copy constructor with valid data.
     * This test checks if a new Publisher object correctly copies the data of an existing Publisher object.
     */
    @Test
    @Order(1)
    void testPublisherCopy_ValidData()
    {
        System.out.println("\n1: Testing Publisher copy constructor with valid data...");

        int validId = 1;
        String validName = "Test Publisher";
        String validEmail = "test@test.com";
        boolean deleted = false;

        try
        {
            Publisher originalPublisher = new Publisher(validId, validName, validEmail, deleted);
            Publisher copiedPublisher = new Publisher(originalPublisher);

            // Check if copiedPublisher correctly copied the data from originalPublisher
            assertEquals(validId, copiedPublisher.getPublisherID(),
                         "Publisher ID must be the same in the copied publisher");
            assertEquals(validName, copiedPublisher.getPublisherName(),
                         "Publisher name must be the same in the copied publisher");
            assertEquals(validEmail, copiedPublisher.getEmail(),
                         "Publisher email must be the same in the copied publisher");
            assertFalse(copiedPublisher.isDeleted());
        }
        catch (ConstructionException e)
        {
            System.out.println("Exception caught during test execution: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }
}