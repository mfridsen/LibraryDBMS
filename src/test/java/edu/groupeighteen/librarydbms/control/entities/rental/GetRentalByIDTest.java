package edu.groupeighteen.librarydbms.control.entities.rental;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.temporal.ChronoUnit;

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
    @Order(14)
    void testGetRentalByID_InvalidRentalID()
    {
        System.out.println("\n14: Testing getRentalByID method with an invalid rentalID...");

        try
        {
            // Create 5 rentals, should get IDs 1-5
            for (int i = 1; i <= 5; i++)
                RentalHandler.createNewRental(i, i);

            //These should result in exceptions
            assertThrows(InvalidIDException.class, () -> RentalHandler.getRentalByID(0));
            assertThrows(InvalidIDException.class, () -> RentalHandler.getRentalByID(-1));

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
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
    @Order(15)
    void testGetRentalByID_NonExistentRentalID()
    {
        System.out.println("\n15: Testing getRentalByID method with non-existent rentalID...");

        try
        {
            // Create 5 rentals, should get IDs 1-5
            for (int i = 1; i <= 5; i++)
                RentalHandler.createNewRental(i, i);

            // These should return null as no rental with these IDs exist
            for (int i = 6; i <= 10; i++)
                assertNull(RentalHandler.getRentalByID(i), "Expected null for non-existent rental ID " + i);

        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
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
    @Order(16)
    void testGetRentalByID_ValidRentalID()
    {
        System.out.println("\n16: Testing getRentalByID method with valid rentalID...");

        try
        {
            for (int i = 0; i < 5; i++)
            {
                // Create rental
                RentalHandler.createNewRental(i + 1, i + 1);
                Rental rental = RentalHandler.getRentalByID(i + 1);

                // Verify non-nullness
                assertNotNull(rental, "Expected Rental object for rental ID " + i + 1);

                // Verify fields
                assertEquals(i + 1, rental.getRentalID());
                assertEquals(i + 1, rental.getUserID());
                assertEquals(i + 1, rental.getItemID());
                assertNotNull(rental.getRentalDate());
                assertEquals("user" + (i + 1), rental.getUsername());
                assertEquals("item" + (i + 1), rental.getItemTitle());
                assertEquals(rental.getRentalDate().plusDays(14).truncatedTo(ChronoUnit.SECONDS).withHour(20)
                        .withMinute(0).withSecond(0), rental.getRentalDueDate());
                assertNull(rental.getRentalReturnDate());
                assertEquals(0.0, rental.getLateFee(), 0.001);
            }
        }
        catch (EntityNotFoundException | RentalNotAllowedException
                | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}