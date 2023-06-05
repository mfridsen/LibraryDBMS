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
 * Tests for the Author Retrieval Constructor.
 * This class contains methods to test the functionality of the Retrieval Constructor
 * under different conditions such as valid data, invalid ID, empty first name, null first name,
 * and first name and last name exceeding the limit.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRetrievalTest
{
    /**
     * Tests the Author Retrieval Constructor with valid data.
     * This method creates an author with valid data and verifies that the fields are set correctly.
     */
    @Test
    @Order(1)
    void testAuthorRetrieval_ValidData()
    {
        System.out.println("\n1: Testing Author Retrieval Constructor with valid data...");

        int id = 1;
        String firstName = "John";
        String lastName = "Doe";
        String biography = "Author biography";
        boolean deleted = false;

        try
        {
            Author author = new Author(id, firstName, lastName, biography, deleted);
            assertFalse(author.isDeleted());
            assertEquals(id, author.getAuthorID(), "Author's ID should match the provided ID");
            assertEquals(firstName, author.getAuthorFirstname(), "Author's first name should match the provided name");
            assertEquals(lastName, author.getAuthorLastName(), "Author's last name should match the provided name");
            assertEquals(biography, author.getBiography(), "Author's biography should match the provided biography");
        }
        catch (ConstructionException e)
        {
            fail("Author retrieval failed with valid data. Exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Retrieval Constructor with an invalid ID.
     * This method attempts to create an author with an invalid ID
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(2)
    void testAuthorRetrieval_InvalidID()
    {
        System.out.println("\n2: Testing Author Retrieval Constructor with an invalid ID...");

        int id = 0;
        String firstName = "John";
        String lastName = "Doe";
        String biography = "Author biography";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () -> new Author(id, firstName, lastName, biography, deleted),
                "Expected ConstructionException for invalid ID");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Retrieval Constructor with an empty first name.
     * This method attempts to create an author with an empty first name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(3)
    void testAuthorRetrieval_EmptyFirstName()
    {
        System.out.println("\n3: Testing Author Retrieval Constructor with an empty first name...");

        int id = 1;
        String firstName = "";
        String lastName = "Doe";
        String biography = "Author biography";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () -> new Author(id, firstName, lastName, biography, deleted),
                "Expected ConstructionException for empty first name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Retrieval Constructor with a null first name.
     * This method attempts to create an author with a null first name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(4)
    void testAuthorRetrieval_NullFirstName()
    {
        System.out.println("\n4: Testing Author Retrieval Constructor with a null first name...");

        int id = 1;
        String firstName = null;
        String lastName = "Doe";
        String biography = "Author biography";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () -> new Author(id, firstName, lastName, biography, deleted),
                "Expected ConstructionException for null first name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Retrieval Constructor with a long first name.
     * This method attempts to create an author with a long first name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(5)
    void testAuthorRetrieval_LongFirstName()
    {
        System.out.println("\n5: Testing Author Retrieval Constructor with a first name exceeding the limit...");

        int id = 1;
        String firstName = "a".repeat(Author.AUTHOR_FIRST_NAME_LENGTH + 1);
        String lastName = "Doe";
        String biography = "Author biography";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () -> new Author(id, firstName, lastName, biography, deleted),
                "Expected ConstructionException for long first name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Retrieval Constructor with a long last name.
     * This method attempts to create an author with a long last name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(6)
    void testAuthorRetrieval_LongLastName()
    {
        System.out.println("\n6: Testing Author Retrieval Constructor with a last name exceeding the limit...");

        int id = 1;
        String firstName = "John";
        String lastName = "a".repeat(Author.AUTHOR_LAST_NAME_LENGTH + 1);
        String biography = "Author biography";
        boolean deleted = false;

        assertThrows(ConstructionException.class, () -> new Author(id, firstName, lastName, biography, deleted),
                "Expected ConstructionException for long last name");

        System.out.println("\nTEST FINISHED.");
    }
}