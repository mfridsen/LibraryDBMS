package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetRentalsByRentalDay class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetRentalsByRentalDayTest extends BaseRentalHandlerTest
{
    /**
     * Tests the method getRentalsByRentalDay() when the rental day provided is null.
     * Expects an InvalidDateException.
     */
    @Test
    @Order(41)
    void testGetRentalsByRentalDay_NullRentalDay()
    {
        System.out.println("\n41: Testing getRentalsByRentalDay method with null rental day...");

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDay(null),
                "getRentalsByRentalDay should throw InvalidDateException when rentalDay is null");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when the rental day provided is in the future.
     * Expects an InvalidDateException.
     */
    @Test
    @Order(42)
    void testGetRentalsByRentalDay_FutureRentalDay()
    {
        System.out.println("\n42: Testing getRentalsByRentalDay method with future rental day...");

        LocalDate futureDate = LocalDate.now().plusDays(1);

        assertThrows(InvalidDateException.class, () -> RentalHandler.getRentalsByRentalDay(futureDate),
                "getRentalsByRentalDay should throw InvalidDateException when rentalDay is in the future");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are no existing rentals.
     * Expects the returned list to be empty.
     */
    @Test
    @Order(43)
    void testGetRentalsByRentalDay_NoExistingRentals()
    {
        System.out.println("\n43: Testing getRentalsByRentalDay method with no existing rentals...");

        LocalDate rentalDay = LocalDate.now();

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(0, rentals.size(),
                    "Returned rental list should be empty when there are no rentals on the rental day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are existing rentals, but none on the desired date.
     * Expects the returned list to be empty.
     */
    @Test
    @Order(44)
    void testGetRentalsByRentalDay_NoRentalsOnDesiredDate()
    {
        System.out.println(
                "\n44: Testing getRentalsByRentalDay method with rentals existing, but none on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(5, 5);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(0, rentals.size(),
                    "Returned rental list should be empty when there are no rentals on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are 5 existing rentals and 1 on the desired date.
     * Expects the returned list to contain one rental.
     */
    @Test
    @Order(45)
    void testGetRentalsByRentalDay_OneRentalOnDesiredDate()
    {
        System.out.println(
                "\n45: Testing getRentalsByRentalDay method with 5 existing rentals and 1 on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(5, 4);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(1, rentals.size(),
                    "Returned rental list should contain one rental when only one rental is on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are 5 existing rentals and 3 on the desired date.
     * Expects the returned list to contain three rentals.
     */
    @Test
    @Order(46)
    void testGetRentalsByRentalDay_ThreeRentalsOnDesiredDate()
    {
        System.out.println(
                "\n46: Testing getRentalsByRentalDay method with 5 existing rentals and 3 on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(6, 3);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(3, rentals.size(),
                    "Returned rental list should contain three rentals when three rentals are on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are 5 existing rentals and all are on the desired date.
     * Expects the returned list to contain all five rentals.
     */
    @Test
    @Order(47)
    void testGetRentalsByRentalDay_AllRentalsOnDesiredDate()
    {
        System.out.println(
                "\n47: Testing getRentalsByRentalDay method with 5 existing rentals and all on desired date...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different dates.
        createAndSaveRentalsWithDifferentDates(5, 0);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(5, rentals.size(),
                    "Returned rental list should contain five rentals when all rentals are on the desired day");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when there are rentals having same date but different times.
     * Expects the returned list to contain all rentals regardless of different times.
     */
    @Test
    @Order(48)
    void testGetRentalsByRentalDay_SameDateDifferentTimes()
    {
        System.out.println(
                "\n48: Testing getRentalsByRentalDay method with rentals having same date but different times...");

        LocalDate rentalDay = LocalDate.now();

        // Assuming method to create and save rentals with different times on the same date.
        createAndSaveRentalsWithDifferentTimes(5);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(5, rentals.size(),
                    "Returned rental list should contain five rentals when all rentals are on the desired day, regardless of different times");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the method getRentalsByRentalDay() when the rental day is before any existing rentals.
     * Expects the returned list to be empty.
     */
    @Test
    @Order(49)
    void testGetRentalsByRentalDay_BeforeExistingRentals()
    {
        System.out.println(
                "\n49: Testing getRentalsByRentalDay method with a rental day before any existing rentals...");

        LocalDate rentalDay = LocalDate.now().minusDays(5);

        // Assuming method to create and save rentals with dates after the rentalDay.
        createAndSaveRentalsWithDifferentDates(5, 0);

        try
        {
            List<Rental> rentals = RentalHandler.getRentalsByRentalDay(rentalDay);
            assertEquals(0, rentals.size(),
                    "Returned rental list should be empty when the rental day is before any existing rentals");
        }
        catch (InvalidDateException e)
        {
            fail("Unexpected exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

}