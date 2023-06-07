package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetRentalByID class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetRentalByIDTest extends BaseRentalHandlerTest
{
    @BeforeEach
    void setupRentals()
    {
        System.out.println("\nSetting up test rentals...");

        try
        {
            for (int i = 0; i < validUserIDs.length; i++)
            {
                Rental rental = RentalHandler.createNewRental(validUserIDs[i], i + 10);
                assertNotNull(rental);
                System.out.println("Rental ID: " + rental.getRentalID() +
                        ", User ID: " + rental.getUserID() +
                        ", Item ID: " + rental.getItemID());
            }
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
        {
            e.printStackTrace();
            fail("Exception occurred during setupRentals: " + e.getMessage());
        }

        System.out.println("\nTest rentals setup finished.");
    }

    /**
     * This is a test for the method 'getRentalByID' in the class 'RentalHandler'.
     * <p>
     * The purpose of this test is to validate that the method correctly throws an 'InvalidIDException' when given
     * invalid rental IDs.
     * <p>
     * Initially, the test creates 5 rentals with rental IDs ranging from 1 to 5. Following this, it attempts to fetch
     * rentals with IDs 0 and -1, which should trigger the 'InvalidIDException' as these IDs are not valid.
     */
    @Test
    @Order(1)
    void testGetRentalByID_InvalidRentalID()
    {
        System.out.println("\n1: Testing getRentalByID method with an invalid rentalID...");

        //These should result in exceptions
        assertThrows(InvalidIDException.class, () -> RentalHandler.getRentalByID(0));
        assertThrows(InvalidIDException.class, () -> RentalHandler.getRentalByID(-1));

        System.out.println("\nTest finished.");
    }

    /**
     * This is a test for the method 'getRentalByID' in the class 'RentalHandler'.
     * <p>
     * The purpose of this test is to confirm that the method correctly returns null when trying to fetch a rental
     * with a non-existent rental ID.
     * <p>
     * Initially, the test creates 5 rentals with rental IDs ranging from 1 to 5. Following this, it attempts to
     * fetch rentals with IDs 6 to 10, which should return null as no rentals with these IDs exist.
     */
    @Test
    @Order(2)
    void testGetRentalByID_NonExistentRentalID()
    {
        System.out.println("\n2: Testing getRentalByID method with non-existent rentalID...");

        try
        {
            // These should return null as no rental with these IDs exist
            for (int i = validUserIDs.length + 1; i <= validUserIDs.length + 10; i++)
                assertNull(RentalHandler.getRentalByID(i), "Expected null for non-existent rental ID " + i);

        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTest finished.");
    }

    /**
     * This is a test for the method 'getRentalByID' in the class 'RentalHandler'.
     * <p>
     * The purpose of this test is to confirm that the method correctly retrieves rentals with valid rental IDs and all
     * the fields of the retrieved rentals are as expected.
     * <p>
     * The test creates 5 rentals with rental IDs ranging from 1 to 5 and then attempts to fetch each of them.
     * <p>
     * For each fetched rental, the test asserts that the object is not null and all its fields
     * (rentalID, userID, itemID, rentalDate, username, itemTitle, rentalDueDate, rentalReturnDate, and lateFee)
     * match the expected values.
     */
    @Test
    @Order(3)
    void testGetRentalByID_ValidRentalID()
    {
        System.out.println("\n3: Testing getRentalByID method with valid rentalID...");

        for (int i = 0; i < 5; i++)
        {
            try
            {
                // Create rental
                Rental rental = RentalHandler.getRentalByID(i + 1);

                // Verify non-nullness
                assertNotNull(rental, "Expected Rental object for rental ID " + i + 1);

                //Retrieve allowedRentalDays
                Item.ItemType type = Item.ItemType.valueOf(rental.getItemType());
                int allowedRentalDays = Item.getDefaultAllowedRentalDays(type);

                //Create expected dueDate
                LocalDateTime expectedDueDate = rental.getRentalDate().plusDays(allowedRentalDays).
                        withHour(20).withMinute(0).withSecond(0).
                        truncatedTo(ChronoUnit.SECONDS);

                //Retrieve item title
                Item item = ItemHandler.getItemByID(rental.getItemID());
                assertNotNull(item);
                String expectedTitle = item.getTitle();

                // Verify fields
                assertEquals(i + 1, rental.getRentalID());
                assertEquals(validUserIDs[i], rental.getUserID());
                assertEquals(i + 10, rental.getItemID());
                assertNotNull(rental.getRentalDate());
                assertEquals("user" + validUserIDs[i], rental.getUsername());


                assertEquals(expectedTitle, rental.getItemTitle());
                assertEquals(expectedDueDate, rental.getRentalDueDate());
                assertNull(rental.getRentalReturnDate());

                assertEquals(0.0, rental.getLateFee(), 0.001);
            }
            catch (InvalidIDException | RetrievalException e)
            {
                e.printStackTrace();
                fail("Exception occurred during test: " + e.getMessage());
            }
        }

        System.out.println("\nTest finished.");
    }
}