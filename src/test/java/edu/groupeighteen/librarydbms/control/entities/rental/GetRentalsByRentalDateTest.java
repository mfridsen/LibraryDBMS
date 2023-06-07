package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetRentalsByRentalDate class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetRentalsByRentalDateTest extends BaseRentalHandlerTest
{
    /**
     * Tests the getRentalsByRentalDate method when supplied with a null rental date.
     * This test should fail with an InvalidDateException, as rental dates cannot be null.
     */
    @Test
    @Order(37)
    void testGetRentalsByRentalDate_InvalidDate()
    {
        System.out.println("\n37: Testing getRentalsByRentalDate method with invalid date...");

        LocalDateTime invalidDate = null;

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDate(invalidDate),
                "Expected InvalidDateException to be thrown, but it didn't");

        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDate(futureDate),
                "Expected InvalidDateException to be thrown, but it didn't");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the getRentalsByRentalDate method when supplied with a rental date that doesn't match any existing rentals.
     * This test should pass if the method correctly returns an empty list, indicating no rentals were found.
     */
    @Test
    @Order(38)
    void testGetRentalsByRentalDate_NoRentalsFound()
    {
        System.out.println("\n3: Testing getRentalsByRentalDate method with a date that doesn't match any rentals...");

        LocalDateTime dateWithNoRentals = LocalDateTime.now().minusDays(
                1); //Assuming no rentals are made 1 day in the past

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(dateWithNoRentals);

            assertNotNull(rentals, "The returned list should not be null");
            assertTrue(rentals.isEmpty(),
                    "The list should be empty as no rentals should be found for the provided date.");
        }
        catch (InvalidDateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the getRentalsByRentalDate method when supplied with a rental date matching one rental.
     * This test should pass if the method correctly returns a list containing the one matching rental.
     */
    @Test
    @Order(39)
    void testGetRentalsByRentalDate_OneRentalFound()
    {
        System.out.println("\n39: Testing getRentalsByRentalDate method with a date matching a single rental...");

        // Assuming a rental was made today
        LocalDateTime dateWithOneRental = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        try
        {
            RentalHandler.createNewRental(1, 1);

            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(dateWithOneRental);

            assertNotNull(rentals, "The returned list should not be null");
            assertEquals(1, rentals.size(), "The list should contain one rental.");
        }
        catch (InvalidIDException | EntityNotFoundException | RentalNotAllowedException | InvalidDateException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the getRentalsByRentalDate method when supplied with a rental date matching multiple rentals.
     * This test should pass if the method correctly returns a list containing all the matching rentals.
     */
    @Test
    @Order(40)
    void testGetRentalsByRentalDate_MultipleRentalsFound()
    {
        System.out.println("\n40: Testing getRentalsByRentalDate method with a date matching multiple rentals...");

        // Assuming multiple rentals were made today
        LocalDateTime dateWithMultipleRentals = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        try
        {
            // Create 5 rentals
            createAndSaveRentalsWithDifferentDates(5, 0);

            List<Rental> rentals = RentalHandler.getRentalsByRentalDate(dateWithMultipleRentals);

            assertNotNull(rentals, "The returned list should not be null");
            assertEquals(5, rentals.size(), "The list should contain five rentals.");

            // Verifying the contents of the returned list
            for (int i = 0; i < 5; i++)
            {
                Rental rental = rentals.get(i);
                assertNotNull(rental, "The rental at index " + i + " should not be null.");
                assertEquals(i + 1, rental.getRentalID(),
                        "The rental ID of the rental at index " + i + " should be " + (i + 1));
            }
        }
        catch (InvalidDateException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
        }

        System.out.println("\nTEST FINISHED.");
    }
}