package control.entities.author;

import static org.junit.jupiter.api.Assertions.*;

import control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.AuthorHandler;
import edu.groupeighteen.librarydbms.model.entities.Author;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetAuthorByID class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAuthorByIDTest extends BaseHandlerTest
{
    /**
     *
     */
    @Test
    @Order(1)
    void testGetAuthorByID()
    {
        System.out.println("\n1: Testing GetAuthorByID...");

        Author author = AuthorHandler.getAuthorByID(1);

        assertNotNull(author);
        assertEquals(1, author.getAuthorID());
        assertEquals("author1", author.getAuthorFirstname());
        assertEquals("lastname1", author.getAuthorLastName());
        assertEquals("is the first author", author.getBiography());
        assertFalse(author.isDeleted());

        System.out.println("\nTEST FINISHED.");
    }
}