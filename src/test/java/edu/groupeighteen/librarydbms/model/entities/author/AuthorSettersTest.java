package edu.groupeighteen.librarydbms.model.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
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
 * Unit Test for the AuthorSetters class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorSettersTest
{

    /**
     * Tests the setAuthorID method with a valid ID.
     * This method sets an ID to an author and verifies that the ID field is set correctly.
     */
    @Test
    @Order(1)
    void testSetAuthorID_ValidID()
    {
        System.out.println("\n1: Testing setAuthorID method with a valid ID...");

        try
        {
            int id = 1;
            Author author = new Author("John", "Doe");
            author.setAuthorID(id);
            assertEquals(id, author.getAuthorID(), "Author's ID should match the set ID");
        }
        catch (InvalidIDException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    // Similar methods for setAuthorFirstname, setAuthorLastName, setBiography
    // ...

    /**
     * Tests the setAuthorID method with an invalid ID.
     * This method attempts to set an invalid ID to an author and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(2)
    void testSetAuthorID_InvalidID()
    {
        System.out.println("\n2: Testing setAuthorID method with an invalid ID...");

        try
        {
            int id = -1;
            Author author = new Author("John", "Doe");

            assertThrows(InvalidIDException.class, () -> author.setAuthorID(id),
                    "Expected InvalidIDException for invalid ID");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorFirstname method with a valid first name.
     * This method sets a first name to an author and verifies that the first name field is set correctly.
     */
    @Test
    @Order(3)
    void testSetAuthorFirstname_ValidName()
    {
        System.out.println("\n3: Testing setAuthorFirstname method with a valid name...");

        try
        {
            String firstname = "John";
            Author author = new Author(firstname, "Doe");
            author.setAuthorFirstname(firstname);
            assertEquals(firstname, author.getAuthorFirstname(), "Author's first name should match the set name");
        }
        catch (InvalidNameException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorFirstname method with a first name that exceeds the character limit.
     * This method attempts to set a long first name to an author and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(4)
    void testSetAuthorFirstname_NameTooLong()
    {
        System.out.println("\n4: Testing setAuthorFirstname method with a name that exceeds the character limit...");

        try
        {
            String firstname = "a".repeat(Author.AUTHOR_FIRST_NAME_LENGTH + 1);
            Author author = new Author("John", "Doe");

            assertThrows(InvalidNameException.class, () -> author.setAuthorFirstname(firstname),
                    "Expected InvalidNameException for name exceeding character limit");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorLastName method with a valid last name.
     * This method sets a last name to an author and verifies that the last name field is set correctly.
     */
    @Test
    @Order(5)
    void testSetAuthorLastName_ValidName()
    {
        System.out.println("\n5: Testing setAuthorLastName method with a valid name...");

        try
        {
            String lastname = "Doe";
            Author author = new Author("John", lastname);
            author.setAuthorLastName(lastname);
            assertEquals(lastname, author.getAuthorLastName(), "Author's last name should match the set name");
        }
        catch (InvalidNameException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setAuthorLastName method with a last name that exceeds the character limit.
     * This method attempts to set a long last name to an author and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(6)
    void testSetAuthorLastName_NameTooLong()
    {
        System.out.println("\n6: Testing setAuthorLastName method with a name that exceeds the character limit...");

        try
        {
            String lastname = "a".repeat(Author.AUTHOR_LAST_NAME_LENGTH + 1);
            Author author = new Author("John", "Doe");

            assertThrows(InvalidNameException.class, () -> author.setAuthorLastName(lastname),
                    "Expected InvalidNameException for name exceeding character limit");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setBiography method with valid biography text.
     * This method sets a biography to an author and verifies that the biography field is set correctly.
     */
    @Test
    @Order(7)
    void testSetBiography_ValidBio()
    {
        System.out.println("\n7: Testing setBiography method with valid biography text...");

        try
        {
            String biography = "This is a test biography.";
            Author author = new Author("John", "Doe");
            author.setBiography(biography);
            assertEquals(biography, author.getBiography(), "Author's biography should match the set biography");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}