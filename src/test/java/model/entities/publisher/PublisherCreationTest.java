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
 * This class tests the creation of Publisher objects.
 * It includes testing with both valid and invalid data.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherCreationTest
{
    /**
     * Test case for Publisher creation constructor with valid data.
     * The test assumes that the DatabaseHandler.getPublisherMetaData() method returns [255, 255].
     * This test checks if the Publisher object is correctly created and its fields are set properly.
     */
    @Test
    @Order(1)
    void testPublisherCreation_ValidData()
    {
        System.out.println("\n1: Testing Publisher creation constructor with valid data...");

        String validName = "Test Publisher";
        try
        {
            Publisher publisher = new Publisher(validName);
            assertFalse(publisher.isDeleted());
            assertEquals(validName, publisher.getPublisherName(), "Publisher name should match the valid data input");
            assertNull(publisher.getEmail(), "Publisher email should be null for creation constructor");
        }
        catch (ConstructionException e)
        {
            fail("ConstructionException should not have been thrown for valid data.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher creation constructor with invalid data - specifically empty publisher name.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with an empty name.
     */
    @Test
    @Order(2)
    void testPublisherCreation_InvalidData_EmptyName()
    {
        System.out.println("\n2: Testing Publisher creation constructor with empty name...");

        String invalidName = "";
        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(invalidName);
        }, "ConstructionException should be thrown for empty publisher name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher creation constructor with invalid data - specifically null publisher name.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with a null name.
     */
    @Test
    @Order(3)
    void testPublisherCreation_InvalidData_NullName()
    {
        System.out.println("\n3: Testing Publisher creation constructor with null name...");

        String invalidName = null;
        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(invalidName);
        }, "ConstructionException should be thrown for null publisher name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for Publisher creation constructor with invalid data - specifically publisher name exceeding the maximum length.
     * This test checks if the ConstructionException is thrown when trying to create a Publisher object with a name longer than PUBLISHER_NAME_LENGTH.
     */
    @Test
    @Order(4)
    void testPublisherCreation_InvalidData_LongName()
    {
        System.out.println("\n4: Testing Publisher creation constructor with name exceeding maximum length...");

        String invalidName = "a".repeat(Publisher.PUBLISHER_NAME_LENGTH + 1);

        assertThrows(ConstructionException.class, () ->
        {
            new Publisher(invalidName);
        }, "ConstructionException should be thrown for publisher name exceeding maximum length");

        System.out.println("\nTEST FINISHED.");
    }
}

