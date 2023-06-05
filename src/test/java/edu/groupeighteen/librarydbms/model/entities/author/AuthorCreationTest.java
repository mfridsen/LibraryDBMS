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
 * Tests for the Author Creation Constructor.
 * This class contains methods to test the functionality of the Creation Constructor
 * under different conditions such as valid data, empty first name, null first name,
 * and first name and last name exceeding the limit.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorCreationTest
{
    /**
     * Tests the Author Creation Constructor with valid first and last names.
     * This method creates an author with valid data and verifies that the fields are set correctly.
     */
    @Test
    @Order(1)
    void testAuthorCreation_ValidData()
    {
        System.out.println("\n1: Testing Author Creation Constructor with valid first and last names...");

        String firstName = "John";
        String lastName = "Doe";

        try
        {
            Author author = new Author(firstName, lastName);
            assertFalse(author.isDeleted());
            assertEquals(0, author.getAuthorID());
            assertEquals(firstName, author.getAuthorFirstname(), "Author's first name should match the provided name");
            assertEquals(lastName, author.getAuthorLastName(), "Author's last name should match the provided name");
            assertNull(author.getBiography(), "Author's biography should be null for a newly created author");
        }
        catch (ConstructionException e)
        {
            fail("Author creation failed with valid data. Exception: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Creation Constructor with an empty first name.
     * This method attempts to create an author with an empty first name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(2)
    void testAuthorCreation_EmptyFirstName()
    {
        System.out.println("\n2: Testing Author Creation Constructor with an empty first name...");

        String firstName = "";
        String lastName = "Doe";

        assertThrows(ConstructionException.class, () -> new Author(firstName, lastName),
                "Expected ConstructionException for empty first name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Creation Constructor with a null first name.
     * This method attempts to create an author with a null first name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(3)
    void testAuthorCreation_NullFirstName()
    {
        System.out.println("\n3: Testing Author Creation Constructor with a null first name...");

        String firstName = null;
        String lastName = "Doe";

        assertThrows(ConstructionException.class, () -> new Author(firstName, lastName),
                "Expected ConstructionException for null first name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Creation Constructor with a long first name.
     * This method attempts to create an author with a long first name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(4)
    void testAuthorCreation_LongFirstName()
    {
        System.out.println("\n4: Testing Author Creation Constructor with a first name exceeding the limit...");

        String firstName = "a".repeat(Author.AUTHOR_FIRST_NAME_LENGTH + 1);
        String lastName = "Doe";

        assertThrows(ConstructionException.class, () -> new Author(firstName, lastName),
                "Expected ConstructionException for long first name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Author Creation Constructor with a long last name.
     * This method attempts to create an author with a long last name
     * and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(5)
    void testAuthorCreation_LongLastName()
    {
        System.out.println("\n5: Testing Author Creation Constructor with a last name exceeding the limit...");

        String firstName = "John";
        String lastName = "a".repeat(Author.AUTHOR_LAST_NAME_LENGTH + 1);

        assertThrows(ConstructionException.class, () -> new Author(firstName, lastName),
                "Expected ConstructionException for long last name");

        System.out.println("\nTEST FINISHED.");
    }
}