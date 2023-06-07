package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetAllRentals class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAllRentalsTest extends BaseRentalHandlerTest
{
    /**
     * Test case for the getAllRentals method with an empty database.
     * <p>
     * This test verifies that the getAllRentals method correctly returns an empty list when there are no rentals
     * in the database.
     * <p>
     * It clears the rentals table in the database, calls the getAllRentals method, and compares the expected empty
     * list with the actual result.
     * <p>
     * If the actual result matches the expected empty list, the test passes.
     */
    @Test
    @Order(11)
    void testGetAllRentals_EmptyRentalsTable()
    {
        System.out.println("\n11: Testing getAllRentals method with an empty database...");

        // Clear the rentals table in the database
        DatabaseHandler.executeCommand("DELETE FROM rentals");

        List<Rental> expectedRentals = Collections.emptyList();
        List<Rental> actualRentals;
        actualRentals = RentalHandler.getAllRentals();
        assertEquals(expectedRentals, actualRentals);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test method validates the functionality of the getAllRentals method in the RentalHandler class when the
     * database is populated.
     * <p>
     * The method first creates 5 new rentals. Then it calls the getAllRentals method to retrieve all rentals from
     * the database.
     * <p>
     * It checks if the returned list of rentals is not null and if the number of retrieved rentals matches the number
     * of created rentals.
     * <p>
     * If the list is null or the sizes do not match, the test fails.
     * <p>
     * If an exception is thrown during the process (EntityNotFoundException, EntityNotFoundException,
     * RentalNotAllowedException, or InvalidIDException), the test also fails.
     */
    @Test
    @Order(12)
    void testGetAllRentals_PopulatedRentalsTable_OneRental()
    {
        System.out.println("\n12: Testing getAllRentals method with some rentals in the database...");

        try
        {
            //Create 1 rental
            RentalHandler.createNewRental(1, 1);

            //Retrieve all rentals
            List<Rental> rentals = RentalHandler.getAllRentals();

            //Check if the number of rentals retrieved matches the number of rentals created
            assertNotNull(rentals, "The retrieved rentals list should not be null.");
            assertEquals(1, rentals.size(), "The number of retrieved rentals does not match the " +
                    "number of created rentals.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test method validates the functionality of the getAllRentals method in the RentalHandler class when the
     * database is populated.
     * <p>
     * The method first creates 5 new rentals. Then it calls the getAllRentals method to retrieve all rentals from
     * the database.
     * <p>
     * It checks if the returned list of rentals is not null and if the number of retrieved rentals matches the number
     * of created rentals.
     * <p>
     * If the list is null or the sizes do not match, the test fails.
     * <p>
     * If an exception is thrown during the process (EntityNotFoundException, EntityNotFoundException,
     * RentalNotAllowedException, or InvalidIDException), the test also fails.
     */
    @Test
    @Order(13)
    void testGetAllRentals_PopulatedRentalsTable_MultipleRentals()
    {
        System.out.println("\n13: Testing getAllRentals method with some rentals in the database...");

        try
        {
            // Create 5 rentals, should get IDs 1-5
            for (int i = 1; i <= 5; i++)
                RentalHandler.createNewRental(i, i);

            //Retrieve all rentals
            List<Rental> rentals = RentalHandler.getAllRentals();

            //Check if the number of rentals retrieved matches the number of rentals created
            assertNotNull(rentals, "The retrieved rentals list should not be null.");
            assertEquals(5, rentals.size(), "The number of retrieved rentals does not match the " +
                    "number of created rentals.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}