package edu.groupeighteen.librarydbms.control.entities.rental;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetOverdueRentals class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetOverdueRentalsTest extends BaseRentalHandlerTest
{
    /**
     * Creates
     */
    @BeforeEach
    void setupTestRentals()
    {
        createAndSaveRentalsWithDifferentDateAndDueDates(8);
    }

    /**
     *
     */
    @Test
    @Order(1)
    void testGetOverdueRentals()
    {
        System.out.println("\n1: Testing GetOverdueRentals...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("\nTEST FINISHED.");
    }
}