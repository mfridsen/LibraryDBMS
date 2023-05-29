package model.entities.publisher;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidEmailException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
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
 * Tests for the setters of the Publisher class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherSettersTest
{
    /**
     * Test case for the setPublisherID method with valid data.
     * This test checks if the setPublisherID method correctly sets the publisherID.
     */
    @Test
    @Order(1)
    void testSetPublisherID_ValidData()
    {
        System.out.println("\n1: Testing setPublisherID method with valid data...");

        try
        {
            Publisher publisher = new Publisher("Test Publisher");
            int validId = 1;
            publisher.setPublisherID(validId);

            assertEquals(validId, publisher.getPublisherID(), "Publisher ID must be the same as the one set");
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setPublisherID method with invalid data (ID less than or equal to 0).
     * This test checks if the setPublisherID method throws an InvalidIDException for invalid data.
     */
    @Test
    @Order(2)
    void testSetPublisherID_InvalidData()
    {
        System.out.println("\n2: Testing setPublisherID method with invalid data...");

        try
        {
            Publisher publisher = new Publisher("Test Publisher");
            int invalidId = 0;

            assertThrows(InvalidIDException.class, () -> publisher.setPublisherID(invalidId),
                         "InvalidIDException should be thrown for ID less than or equal to 0");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setPublisherName method with valid data.
     * This test checks if the setPublisherName method correctly sets the publisherName.
     */
    @Test
    @Order(3)
    void testSetPublisherName_ValidData()
    {
        System.out.println("\n3: Testing setPublisherName method with valid data...");

        try {
            Publisher publisher = new Publisher("Test Publisher");
            String validName = "Valid Publisher";
            publisher.setPublisherName(validName);
            assertEquals(validName, publisher.getPublisherName(),
                         "Publisher name must be the same as the one set");
        } catch (InvalidNameException | ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setPublisherName method with invalid data - null or excessively long name.
     * This test checks if the setPublisherName method throws an InvalidNameException for invalid data.
     */
    @Test
    @Order(4)
    void testSetPublisherName_InvalidData()
    {
        System.out.println("\n4: Testing setPublisherName method with invalid data...");

        try
        {
            Publisher publisher = new Publisher("Test Publisher");
            String invalidName = null; // Try with excessively long name also

            assertThrows(InvalidNameException.class, () -> publisher.setPublisherName(invalidName),
                         "InvalidNameException should be thrown for null or too long name");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setEmail method with valid data.
     * This test checks if the setEmail method correctly sets the email.
     */
    @Test
    @Order(5)
    void testSetEmail_ValidData()
    {
        System.out.println("\n5: Testing setEmail method with valid data...");


        try {
            Publisher publisher = new Publisher("Test Publisher");
            String validEmail = "test@test.com";
            publisher.setEmail(validEmail);
            assertEquals(validEmail, publisher.getEmail(), "Publisher email must be the same as the one set");
        } catch (InvalidEmailException | ConstructionException e) {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the setEmail method with invalid data - null or excessively long email.
     * This test checks if the setEmail method throws an InvalidEmailException for invalid data.
     */
    @Test
    @Order(6)
    void testSetEmail_InvalidData()
    {
        System.out.println("\n6: Testing setEmail method with invalid data...");

        try
        {
            Publisher publisher = new Publisher("Test Publisher");
            String invalidEmail = "a".repeat(Publisher.PUBLISHER_EMAIL_LENGTH + 1);

            assertThrows(InvalidEmailException.class, () -> publisher.setEmail(invalidEmail),
                         "InvalidEmailException should be thrown for null or too long email");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}