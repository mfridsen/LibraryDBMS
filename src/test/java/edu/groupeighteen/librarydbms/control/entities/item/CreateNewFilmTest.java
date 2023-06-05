package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Tests the createNewFilm method in ItemHandler.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewFilmTest extends BaseHandlerTest
{
    //Valid input  (remember to validate ALL fields and join tables)
    //Null title
    //Empty title
    //Too long title
    //Null type
    //Invalid authorID
    //Invalid classificationID
    //Non-existent author
    //Non-existent classification
    //Null barcode
    //Empty barcode
    //Too long barcode
    //Taken barcode
    //Negative age rating
    //Too high age rating

    @Override
    @BeforeEach
    protected void setupAndReset()
    {
        super.setupAndReset();
        ItemHandler.reset();
    }

    //TODO-PRIO validate join tables
    @Test
    @Order(1)
    void testCreateNewFilm_ValidInput()
    {
        System.out.println("\n1: Testing createNewFilm method with valid input...");

        try
        {
            String title = "Valid Title";
            int authorId = 1; // assuming this is a valid author ID
            int classificationId = 1; // assuming this is a valid classification ID
            String barcode = "validBarcode";
            int ageRating = 15;

            Film film = ItemHandler.createNewFilm(title, authorId, classificationId, barcode, ageRating);

            assertNotNull(film);
            assertEquals(title, film.getTitle());
            assertEquals(authorId, film.getAuthorID());
            assertEquals(classificationId, film.getClassificationID());
            assertEquals(barcode, film.getBarcode());
            assertEquals(ageRating, film.getAgeRating());

            Film retrievedFilm = (Film) ItemHandler.getItemByID(film.getItemID());
            System.out.println("Retrieved film ID: " + retrievedFilm.getItemID());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testCreateNewFilm_NullTitle()
    {
        System.out.println("\n2: Testing createNewFilm method with null title...");

        int authorId = 1;
        int classificationId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(null, authorId, classificationId, barcode, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testCreateNewFilm_EmptyTitle()
    {
        System.out.println("\n3: Testing createNewFilm method with empty title...");

        int authorId = 1;
        int classificationId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm("", authorId, classificationId, barcode, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testCreateNewFilm_TooLongTitle()
    {
        System.out.println("\n4: Testing createNewFilm method with too long title...");

        int authorId = 1;
        int classificationId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        String title = String.join("", Collections.nCopies(260, "a"));

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, classificationId, barcode, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testCreateNewFilm_InvalidAuthorID()
    {
        System.out.println("\n5: Testing createNewFilm method with invalid authorID...");

        String title = "Valid Title";
        int classificationId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        assertThrows(InvalidIDException.class, () ->
        {
            ItemHandler.createNewFilm(title, -1, classificationId, barcode, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(8)
    void testCreateNewFilm_InvalidClassificationID()
    {
        System.out.println("\n8: Testing createNewFilm method with invalid classificationID...");

        String title = "Valid Title";
        int authorId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        assertThrows(InvalidIDException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, -1, barcode, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testCreateNewFilm_NonExistentAuthor()
    {
        System.out.println("\n9: Testing createNewFilm method with non-existent author...");

        String title = "Valid Title";
        int classificationId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        assertThrows(EntityNotFoundException.class, () ->
        {
            ItemHandler.createNewFilm(title, 9999, classificationId, barcode,
                    ageRating); // assuming 9999 is non-existing author ID
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testCreateNewFilm_NonExistentClassification()
    {
        System.out.println("\n10: Testing createNewFilm method with non-existent classification...");

        String title = "Valid Title";
        int authorId = 1;
        String barcode = "validBarcode";
        int ageRating = 15;

        assertThrows(EntityNotFoundException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, 9999, barcode,
                    ageRating); // assuming 9999 is non-existing classification ID
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testCreateNewFilm_NullBarcode()
    {
        System.out.println("\n11: Testing createNewFilm method with null barcode...");

        String title = "Valid Title";
        int authorId = 1;
        int classificationId = 1;
        int ageRating = 15;

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, classificationId, null, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testCreateNewFilm_EmptyBarcode()
    {
        System.out.println("\n12: Testing createNewFilm method with empty barcode...");

        String title = "Valid Title";
        int authorId = 1;
        int classificationId = 1;
        int ageRating = 15;

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, classificationId, "", ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testCreateNewFilm_TooLongBarcode()
    {
        System.out.println("\n13: Testing createNewFilm method with too long barcode...");

        String title = "Valid Title";
        int authorId = 1;
        int classificationId = 1;
        int ageRating = 15;

        String barcode = String.join("", Collections.nCopies(260, "a"));

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, classificationId, barcode, ageRating);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testCreateNewFilm_TakenBarcode()
    {
        System.out.println("\n14: Testing createNewFilm method with taken barcode...");

        try
        {
            String title = "Valid Title";
            int authorId = 1;
            int classificationId = 1;
            int ageRating = 15;

            String barcode = "takenBarcode"; // assuming this barcode is already taken

            ItemHandler.createNewFilm(title, authorId, classificationId, barcode, ageRating);

            assertThrows(InvalidBarcodeException.class, () ->
            {
                ItemHandler.createNewFilm(title, authorId, classificationId, barcode, ageRating);
            });
        }
        catch (InvalidBarcodeException | InvalidIDException | EntityNotFoundException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid operations should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(15)
    void testCreateNewFilm_NegativeAgeRating()
    {
        System.out.println("\n15: Testing createNewFilm method with negative age rating...");

        String title = "Valid Title";
        int authorId = 1;
        int classificationId = 1;
        String barcode = "validBarcode";

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, classificationId, barcode, -1);
        });

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(16)
    void testCreateNewFilm_TooHighAgeRating()
    {
        System.out.println("\n16: Testing createNewFilm method with too high age rating...");

        String title = "Valid Title";
        int authorId = 1;
        int classificationId = 1;
        String barcode = "validBarcode";

        assertThrows(ConstructionException.class, () ->
        {
            ItemHandler.createNewFilm(title, authorId, classificationId, barcode,
                    101); // assuming the max rating is 100
        });

        System.out.println("\nTEST FINISHED.");
    }
}