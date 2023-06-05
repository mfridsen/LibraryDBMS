package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetOverdueRentals method.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetOverdueRentalsTest extends BaseRentalHandlerTest
{
    /**
     * Test case for getOverdueRentals with some overdue rentals.
     */
    @Test
    @Order(1)
    void testGetOverdueRentals_SomeOverDueRentals()
    {
        System.out.println("\n1: Testing getOverdueRentals with some overdue rentals...");

        //Setup rentals, some overdue
        int numOfOverdueRentals = 8;
        createAndSaveRentalsWithDifferentDateAndDueDates(numOfOverdueRentals);
        assertEquals(numOfOverdueRentals, RentalHandler.getOverdueRentals().size());

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for getOverdueRentals with no overdue rentals.
     */
    @Test
    @Order(2)
    void testGetOverdueRentals_NoOverDueRentals()
    {
        System.out.println("\n2 Testing getOverdueRentals with no overdue rentals...");

        //Setup rentals, none overdue
        createAndSaveRentalsWithDifferentDateAndDueDates(0);
        assertEquals(0, RentalHandler.getOverdueRentals().size());

        System.out.println("\nTEST FINISHED.");
    }
}