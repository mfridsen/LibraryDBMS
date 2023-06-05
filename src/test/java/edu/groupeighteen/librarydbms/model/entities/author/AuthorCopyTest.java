package edu.groupeighteen.librarydbms.model.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/28/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Tests for the Author Copy Constructor.
 * This class contains methods to test the functionality of the Copy Constructor
 * under different conditions such as copying from a valid author object and copying from a null author object.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorCopyTest
{
    /**
     * Tests the Author Copy Constructor with a valid Author object.
     * This method creates a copy of an author and verifies that the fields in the copy match those in the original.
     */
    @Test
    @Order(1)
    void testAuthorCopy_ValidData()
    {
        System.out.println("\n1: Testing Author Copy Constructor with valid Author object...");

        int id = 1;
        String firstName = "John";
        String lastName = "Doe";
        String biography = "Author biography";
        boolean deleted = false;

        try
        {
            Author originalAuthor = new Author(id, firstName, lastName, biography, deleted);
            Author copiedAuthor = new Author(originalAuthor);

            assertFalse(copiedAuthor.isDeleted());
            assertEquals(originalAuthor.getAuthorID(), copiedAuthor.getAuthorID(),
                    "Copied author's ID should match the original author's ID");
            assertEquals(originalAuthor.getAuthorFirstname(), copiedAuthor.getAuthorFirstname(),
                    "Copied author's first name should match the original author's first name");
            assertEquals(originalAuthor.getAuthorLastName(), copiedAuthor.getAuthorLastName(),
                    "Copied author's last name should match the original author's last name");
            assertEquals(originalAuthor.getBiography(), copiedAuthor.getBiography(),
                    "Copied author's biography should match the original author's biography");

        }
        catch (ConstructionException e)
        {
            fail("Author copy failed with valid data. Exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Copy Constructor with a null Author object.
     * This method attempts to create a copy from a null author and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(2)
    void testAuthorCopy_NullAuthor()
    {
        System.out.println("\n2: Testing Author Copy Constructor with null Author object...");

        Author originalAuthor = null;

        assertThrows(NullPointerException.class, () -> new Author(originalAuthor),
                "Expected NullPointerException for null author");

        System.out.println("\nTEST FINISHED.");
    }
}